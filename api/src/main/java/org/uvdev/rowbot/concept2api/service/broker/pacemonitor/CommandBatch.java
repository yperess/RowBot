package org.uvdev.rowbot.concept2api.service.broker.pacemonitor;

import android.util.Log;

import org.uvdev.rowbot.concept2api.pacemonitor.CommandBuilder;
import org.uvdev.rowbot.concept2api.pacemonitor.internal.CommandImpl;
import org.uvdev.rowbot.concept2api.utils.Objects;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandBatch {

    private final byte[][] mCommands;
    private final ArrayList<ArrayList<CommandImpl>> mCommandArray;

    private CommandBatch(ArrayList<byte[]> commands,
            ArrayList<ArrayList<CommandImpl>> commandArray) {
        mCommands = new byte[commands.size()][];
        for (int i = 0; i < mCommands.length; ++i) {
            mCommands[i] = commands.get(i);
        }
        mCommandArray = commandArray;
    }

    public int size() {
        return mCommands.length;
    }

    public byte[] getCommand(int index) {
        return mCommands[index];
    }

    public ArrayList<CommandImpl> getCommandArray(int index) {
        return mCommandArray.get(index);
    }

    @Override
    public int hashCode() {
        return Objects.hash((Object[]) mCommands);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CommandBatch)) {
            return false;
        }
        if (obj == this) {
            return true;
        }

        CommandBatch other = (CommandBatch) obj;
        if (mCommands.length != other.mCommands.length) {
            return false;
        }
        for (int i = 0; i < mCommands.length; ++i) {
            if (!Arrays.equals(mCommands[i], other.mCommands[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        Objects.ObjectsStringBuilder builder = Objects.stringBuilder();
        for (int i = 0; i < mCommands.length; ++i) {
            builder.addVal("Part"+i, Objects.toString(mCommands[i]));
        }
        return builder.toString();
    }

    public static CommandBatch build(List<CommandBuilder.Command> commandList) {
        return new Builder(commandList).build();
    }

    private static final class Builder {
        private static final String TAG = "Batch";
        private static final boolean DBG = true;

        private static final byte USR_CONFIG1 = (byte) 0x1A;
        private static final int BUFFER_SIZE = 128;

        private final List<CommandBuilder.Command> mCommandList;
        private final ArrayList<byte[]> mBatchCommands = new ArrayList<>();
        private final ArrayList<ArrayList<CommandImpl>> mCommandImplArray = new ArrayList<>();
        private final ByteBuffer mBuffer = ByteBuffer.allocate(BUFFER_SIZE);
        private CommandImpl mLastCommand;
        private int mBufferStuffedLength = 0;
        private int mCustomCommandLengthPos = 0;

        public Builder(List<CommandBuilder.Command> commandList) {
            mCommandList = commandList;
        }

        public CommandBatch build() {
            mCommandImplArray.add(new ArrayList<CommandImpl>());
            for (int i = 0, size = mCommandList.size(); i < size; ++i) {
                CommandImpl command = (CommandImpl) mCommandList.get(i);
                byte[] commandBytes = command.getCommandBytes();
                int stuffedLength = Csafe.getStuffedLength(command.getCommandBytes());
                if (DBG) {
                    Log.d(TAG, "Parsing command " + i + ": " + command);
                    Log.d(TAG, "    bytes: " + Objects.toString(commandBytes));
                    Log.d(TAG, "    stuffedLength: " + stuffedLength);
                    Log.d(TAG, "    bufferStuffedLength: " + mBufferStuffedLength);
                    Log.d(TAG, "    customCommandLengthPos: " + mCustomCommandLengthPos);
                }
                if (!command.isCustomCommand()) {
                    if (DBG) Log.d(TAG, "Command is standard");
                    // 1. regular command - check if there's room, add it.
                    if (willOverflowFrame(mBufferStuffedLength + stuffedLength)) {
                        flushBuffer();
                    }
                } else {
                    // 2. custom command
                    if (DBG) Log.d(TAG, "Custom command, lastCommand = " + mLastCommand);
                    if (mLastCommand == null || !mLastCommand.isCustomCommand()) {
                        // 2.1. w/ regular previous command or none
                        int len = commandBytes.length;
                        commandBytes = new byte[len];
                        System.arraycopy(command.getCommandBytes(), 0, commandBytes, 0, len);
                        stuffedLength = Csafe.getStuffedLength(commandBytes);
                        if (DBG) {
                            Log.d(TAG, "CommandBytes: " + Objects.toString(commandBytes));
                            Log.d(TAG, "StuffedLength: " + stuffedLength);
                        }
                        if (willOverflowFrame(mBufferStuffedLength + stuffedLength)) {
                            flushBuffer();
                        }
                        mCustomCommandLengthPos = mBuffer.position() + 1;
                        if (DBG) Log.d(TAG, "CustomCommandLengthPos: " + mCustomCommandLengthPos);
                    } else {
                        // 2.2. previous command is custom.
                        byte prevCustomLen = mBuffer.get(mCustomCommandLengthPos);
                        if (DBG) Log.d(TAG, "Previous command is also custom, length = "
                                + prevCustomLen);
                        // Check if command will overflow buffer.
                        if (willOverflowFrame(mBufferStuffedLength + stuffedLength)) {
                            flushBuffer();
                            // Create user config 1 prefix.
                            int len = commandBytes.length;
                            commandBytes = new byte[len];
                            System.arraycopy(command.getCommandBytes(), 0, commandBytes, 0, len);
                            stuffedLength = Csafe.getStuffedLength(commandBytes);
                            mCustomCommandLengthPos = 1;
                        } else {
                            // Combine commands.
                            if (DBG) Log.d(TAG, "Custom command length changed from "
                                    + prevCustomLen + " to "
                                    + (prevCustomLen + commandBytes.length - 2));
                            byte len = (byte) (prevCustomLen + commandBytes.length - 2);
                            mBuffer.put(mCustomCommandLengthPos, len);
                            // Remove custom command flag and length from current command.
                            commandBytes = Arrays.copyOfRange(commandBytes, 2, commandBytes.length);
                        }
                    }
                }
                mBuffer.put(commandBytes);
                mBufferStuffedLength += stuffedLength;
                mLastCommand = command;
                mCommandImplArray.get(mCommandImplArray.size()-1).add(command);
            }
            flushBuffer();

            for (int i = 0, size = mBatchCommands.size(); i < size; ++i) {
                byte[] bytes = mBatchCommands.get(i);
                Log.d("Batch", i + ". " + Objects.toString(bytes));
            }
            return new CommandBatch(mBatchCommands, mCommandImplArray);
        }

        private boolean willOverflowFrame(int size) {
            // Accounts for start, checksum, and stop bytes.
            return size + 3 >= 96;
        }

        private void flushBuffer() {
            int curLen = mBuffer.position();
            if (curLen == 0) {
                return;
            }
            mBuffer.rewind();
            byte[] bytes = new byte[curLen];
            mBuffer.get(bytes);
            mBatchCommands.add(bytes);
            mBuffer.rewind();
            mBufferStuffedLength = 0;
            mCommandImplArray.add(new ArrayList<CommandImpl>());
            Log.d(TAG, "Saving new buffer line: " + Objects.toString(bytes));
        }
    }
}
