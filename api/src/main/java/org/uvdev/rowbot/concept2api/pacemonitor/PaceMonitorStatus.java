package org.uvdev.rowbot.concept2api.pacemonitor;

/**
 * The pace monitor's status. This value is returned with each command that was executed.
 */
public interface PaceMonitorStatus {

    /**
     * @return The pace monitor frame count. This value should alternate between 0 and 1 for each
     *         command that executes.
     */
    int getFrameCount();

    /**
     * @return The previous frame status detailing the result of the previous frame as one of
     *         {@link PrevFrameStatus}.
     */
    int getPrevFrameStatus();

    /**
     * @return The pace monitor's status as one of {@link SlaveStatus}.
     */
    int getSlaveStatus();

    /**
     * The previous frame's status.
     */
    interface PrevFrameStatus {
        /**
         * Invalid status, something went wrong.
         */
        int INVALID = Integer.MIN_VALUE;

        /**
         * Frame was processed without error.
         */
        int OK = 0;

        /**
         * Frame was rejected because it was recognized as a legal frame (i.e. not prevBad) but
         * contained a command that was illegal for the current state of the Slave or had
         * illegal syntax. This also includes commands considered illegal by the Slave - that
         * is, recognized but not permitted. An unrecognized command with legal syntax is
         * skipped - not rejected.
         */
        int REJECTED = 1;

        /**
         * Frame had bad checksum or overran buffer. If the Slave can detect it, this status may
         * also be used to indicate missing Start or Stop bytes.
         */
        int BAD = 2;

        /**
         * Still processing last frame and cannot receive new frame at this time. NOTE: Many
         * implementations will not be able to respond at all until the previous frame is
         * processed and will simple discard incoming commands until the processing is complete.
         */
        int NOT_READY = 3;
    }

    /**
     * The slave's status after executing a command.
     */
    interface SlaveStatus {
        /**
         * Invalid status, something went wrong.
         */
        int INVALID = Integer.MIN_VALUE;

        /**
         * Serious internal error of type that suggests unit not be used, e.g. unit has lost its
         * calibration parameters. Slave should remain in this State until the problem has been
         * fixed.
         */
        int ERROR = 0;

        /**
         * The initial state is entered when the slave is turned on or is reset. The Slave
         * remains in this state until either
         * <ol>
         *     <li>A user begins a manual workout causing a jump to OffLine State</li>
         *     <li>The Slave receives configuration commands from the Master and is promoted to
         *     Idle State.</li>
         * </ol>
         * Once the Idle State is entered, the only way to get back to the Ready State is
         * through a GoReady command.
         */
        int READY = 1;

        /**
         * Slave has been configured by the Master and is now part of the Network environment.
         * The Slave is waiting for a user to enter an ID or a Start event (such as pressing a
         * Start key without entering an ID). This is where a user chooses between
         * <ol>
         *     <li>A Manual workout that is not monitored by the Master</li>
         *     <li>Entering a valid ID to begin a Master sponsored workout.</li>
         * </ol>
         */
        int IDLE = 2;

        /**
         * A user ID or a Start event has been entered. The Master can request the ID and decide
         * based on what was entered whether to issue a command to go the InUse State or back to
         * Idle State.
         */
        int HAVE_ID = 3;

        /**
         * The user's workout program is running. If sufficient time elapses without activity or
         * the user presses a pause button, the Pause State is entered. Finishing the workout
         * jumps to the Finished state.
         */
        int IN_USE = 4;

        /**
         * The workout program is halted by the user. If sufficient time passes without the user
         * restarting the program and returning to the InUse state, the Finished state will be
         * entered.
         */
        int PAUSED = 5;

        /**
         * The workout program is completed. The Master solicits the results of the workout and
         * then issues the cmdGoIdle command to return to the Idle State.
         */
        int FINISHED = 7;

        /**
         * The user has elected a workout program that is not supervised by the Master. When he
         * finishes the Slave will automatically return to the Idle state.
         */
        int MANUAL = 8;

        /**
         * The user has begun a workout program and the Master has not configured the Slave. On
         * finishing, the Slave will return to the Ready state.
         */
        int OFFLINE = 9;
    }
}
