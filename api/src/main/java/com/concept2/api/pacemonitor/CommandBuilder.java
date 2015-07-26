package com.concept2.api.pacemonitor;

import android.content.ContentValues;

import com.concept2.api.ResultCallback;
import com.concept2.api.internal.DataHolder;
import com.concept2.api.pacemonitor.PaceMonitor.GetCaloriesResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetDistanceResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetDragFactorResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetErrorValueResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetForcePlotResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetHeartRatePlotResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetHeartRateResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetHighResWorkDistanceResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetHighResWorkTimeResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetIntervalTypeResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetPaceResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetPowerResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetRestTimeResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetStrokeRateResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetStrokeStateResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetTimeResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetUserInfoResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutIntervalCountResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutNumberResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutStateResult;
import com.concept2.api.pacemonitor.PaceMonitor.GetWorkoutTypeResult;
import com.concept2.api.pacemonitor.internal.CommandImpl;
import com.concept2.api.pacemonitor.internal.GetCaloriesResultRef;
import com.concept2.api.pacemonitor.internal.GetDistanceResultRef;
import com.concept2.api.pacemonitor.internal.GetDragFactorResultRef;
import com.concept2.api.pacemonitor.internal.GetErrorValueResultRef;
import com.concept2.api.pacemonitor.internal.GetForcePlotResultRef;
import com.concept2.api.pacemonitor.internal.GetHeartRatePlotResultRef;
import com.concept2.api.pacemonitor.internal.GetHeartRateResultRef;
import com.concept2.api.pacemonitor.internal.GetHighResWorkDistanceResultRef;
import com.concept2.api.pacemonitor.internal.GetHighResWorkTimeResultRef;
import com.concept2.api.pacemonitor.internal.GetIntervalTypeResultRef;
import com.concept2.api.pacemonitor.internal.GetPaceResultRef;
import com.concept2.api.pacemonitor.internal.GetPowerResultRef;
import com.concept2.api.pacemonitor.internal.GetRestTimeResultRef;
import com.concept2.api.pacemonitor.internal.GetStrokeRateResultRef;
import com.concept2.api.pacemonitor.internal.GetStrokeStateResultRef;
import com.concept2.api.pacemonitor.internal.GetTimeResultRef;
import com.concept2.api.pacemonitor.internal.GetUserInfoResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutIntervalCountResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutNumberResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutStateResultRef;
import com.concept2.api.pacemonitor.internal.GetWorkoutTypeResultRef;
import com.concept2.api.pacemonitor.internal.PaceMonitorImpl;
import com.concept2.api.pacemonitor.internal.PaceMonitorResultRef;
import com.concept2.api.pacemonitor.internal.contracts.CommandContract;
import com.concept2.api.pacemonitor.internal.contracts.PaceMonitorColumnContract;
import com.concept2.api.service.broker.pacemonitor.util.CsafeUnitUtil;
import com.concept2.api.utils.Objects;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

public class CommandBuilder {

    public interface Command<R extends PaceMonitorResult> {

        ResultCallback<R> getResultCallback();
    }

    /**
     * Gets the status of the pace monitor.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> getStatusCmd(
            ResultCallback<PaceMonitorResult> callback) {
        return new CommandImpl(CommandContract.CMD_GET_STATUS, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Gets the odometer of the pace monitor.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetDistanceResult> getOdometerCmd(
            ResultCallback<GetDistanceResult> callback) {
        return new CommandImpl<GetDistanceResult>(CommandContract.CMD_GET_ODOMETER, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 7 || buffer.get() != CommandContract.CMD_GET_ODOMETER
                        || buffer.get() != 0x05) {
                    return null;
                }
                byte[] bytes = new byte[5];
                buffer.get(bytes);

                int meters = ((bytes[3] & 0xFF) << 24)
                        | ((bytes[2] & 0xFF) << 16)
                        | ((bytes[1] & 0xFF) << 8)
                        | (bytes[0] & 0xFF);
                int units = bytes[4] & 0xFF;
                meters = CsafeUnitUtil.normalizeInt(meters, units);

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.METERS, meters);
                return values;
            }

            @Override
            public GetDistanceResult getResult(DataHolder data, int row) {
                return new GetDistanceResultRef(data, row);
            }
        };
    }

    /**
     * Gets the currently displayed work time from the pace monitor.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetTimeResult> getWorkTimeCmd(
            ResultCallback<GetTimeResult> callback) {
        return new CommandImpl<GetTimeResult>(CommandContract.CMD_GET_WORK_TIME, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 5 || buffer.get() != CommandContract.CMD_GET_WORK_TIME
                        || buffer.get() != 0x03) {
                    return null;
                }
                byte[] time = new byte[3];
                buffer.get(time);

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.SECONDS,
                        TimeUnit.HOURS.toSeconds(time[0] & 0xFF)
                        + TimeUnit.MINUTES.toSeconds(time[1] & 0xFF)
                        + (time[2] & 0xFF));
                return values;
            }

            @Override
            public GetTimeResult getResult(DataHolder data, int row) {
                return new GetTimeResultRef(data, row);
            }
        };
    }

    /**
     * Gets the currently displayed work distance from the pace monitor.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetDistanceResult> getWorkDistanceCmd(
            ResultCallback<GetDistanceResult> callback) {
        return new CommandImpl<GetDistanceResult>(CommandContract.CMD_GET_WORK_DISTANCE, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 5 || buffer.get() != CommandContract.CMD_GET_WORK_DISTANCE
                        || buffer.get() != 0x03) {
                    return null;
                }
                byte[] bytes = new byte[3];
                buffer.get(bytes);

                int meters = ((bytes[1] & 0xFF) << 8)
                        | (bytes[0] & 0xFF);
                int units = bytes[2] & 0xFF;
                meters = CsafeUnitUtil.normalizeInt(meters, units);

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.METERS, meters);
                return values;
            }

            @Override
            public GetDistanceResult getResult(DataHolder data, int row) {
                return new GetDistanceResultRef(data, row);
            }
        };
    }

    /**
     * Gets the currently displayed work calories from the pace monitor.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetCaloriesResult> getWorkCaloriesCmd(
            ResultCallback<GetCaloriesResult> callback) {
        return new CommandImpl<GetCaloriesResult>(CommandContract.CMD_GET_WORK_CALORIES, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 4 || buffer.get() != CommandContract.CMD_GET_WORK_CALORIES
                        || buffer.get() != 0x02) {
                    return null;
                }
                byte[] calories = new byte[2];
                buffer.get(calories);

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.CALORIES,
                        ((calories[1] & 0xFF) << 8) | (calories[0] & 0xFF));
                return values;
            }

            @Override
            public GetCaloriesResult getResult(DataHolder data, int row) {
                return new GetCaloriesResultRef(data, row);
            }
        };
    }

    /**
     * Gets the currently running programmed/pre-stored workout number from the pace monitor.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetWorkoutNumberResult> getStoredWorkoutNumberCmd(
            ResultCallback<GetWorkoutNumberResult> callback) {
        return new CommandImpl<GetWorkoutNumberResult>(CommandContract.CMD_GET_STORED_WORKOUT_NUMBER, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 3 || buffer.get() != CommandContract.CMD_GET_STORED_WORKOUT_NUMBER
                        || buffer.get() != 0x01) {
                    return null;
                }
                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.WORKOUT_NUMBER, buffer.get() & 0xFF);
                return values;
            }

            @Override
            public GetWorkoutNumberResult getResult(DataHolder data, int row) {
                return new GetWorkoutNumberResultRef(data, row);
            }
        };
    }

    /**
     * Gets the current pace from the pace monitor. Pace is reported in seconds / km.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetPaceResult> getPaceCmd(
            ResultCallback<GetPaceResult> callback) {
        return new CommandImpl<GetPaceResult>(CommandContract.CMD_GET_PACE, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 5 || buffer.get() != CommandContract.CMD_GET_PACE
                        || buffer.get() != 0x03) {
                    return null;
                }
                byte[] bytes = new byte[3];
                buffer.get(bytes);

                int pace = ((bytes[1] & 0xFF) << 8)
                        | (bytes[0] & 0xFF);
                int units = bytes[2] & 0xFF;
                pace = CsafeUnitUtil.normalizeInt(pace, units);

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.PACE, pace);
                return values;
            }

            @Override
            public GetPaceResult getResult(DataHolder data, int row) {
                return new GetPaceResultRef(data, row);
            }
        };
    }

    /**
     * Gets the current stroke rate from the pace monitor. Stroke rate is reported in
     * strokes / minute.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetStrokeRateResult> getStrokeRateCmd(
            ResultCallback<GetStrokeRateResult> callback) {
        return new CommandImpl<GetStrokeRateResult>(CommandContract.CMD_GET_STROKE_RATE, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 5 || buffer.get() != CommandContract.CMD_GET_STROKE_RATE
                        || buffer.get() != 0x03) {
                    return null;
                }
                byte[] bytes = new byte[3];
                buffer.get(bytes);

                int strokeRate = ((bytes[1] & 0xFF) << 8)
                        | (bytes[0] & 0xFF);
                int units = bytes[2] & 0xFF;
                strokeRate = CsafeUnitUtil.normalizeInt(strokeRate, units);

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.STROKE_RATE, strokeRate);
                return values;
            }

            @Override
            public GetStrokeRateResult getResult(DataHolder data, int row) {
                return new GetStrokeRateResultRef(data, row);
            }
        };
    }

    /**
     * Gets the user info saved to the pace monitor.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetUserInfoResult> getUserInfoCmd(
            ResultCallback<GetUserInfoResult> callback) {
        return new CommandImpl<GetUserInfoResult>(CommandContract.CMD_GET_USER_INFO, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 7 || buffer.get() != CommandContract.CMD_GET_USER_INFO
                        || buffer.get() != 0x05) {
                    return null;
                }
                byte[] bytes = new byte[5];
                buffer.get(bytes);

                int weight = ((bytes[1] & 0xFF) << 8)
                        | (bytes[0] & 0xFF);
                int units = bytes[2] & 0xFF;
                weight = CsafeUnitUtil.normalizeInt(weight, units);
                int age = bytes[3] & 0xFF;
                int gender = bytes[4] & 0xFF;

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.WEIGHT, weight);
                values.put(PaceMonitorColumnContract.AGE, age);
                values.put(PaceMonitorColumnContract.GENDER, gender);
                return values;
            }

            @Override
            public GetUserInfoResult getResult(DataHolder data, int row) {
                return new GetUserInfoResultRef(data, row);
            }
        };
    }

    /**
     * Gets the current heart rate from the pace monitor. Heart rate is reported in beats / minute.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetHeartRateResult> getHeartRateCmd(
            ResultCallback<GetHeartRateResult> callback) {
        return new CommandImpl<GetHeartRateResult>(CommandContract.CMD_GET_HEART_RATE, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 3 || buffer.get() != CommandContract.CMD_GET_HEART_RATE
                        || buffer.get() != 0x01) {
                    return null;
                }
                int heartRate = buffer.get() & 0xFF;

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.HEART_RATE, heartRate);
                return values;
            }

            @Override
            public GetHeartRateResult getResult(DataHolder data, int row) {
                return new GetHeartRateResultRef(data, row);
            }
        };
    }

    /**
     * Get the current power from the pace monitor. Power is reported in Watts.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetPowerResult> getPowerCmd(
            ResultCallback<GetPowerResult> callback) {
        return new CommandImpl<GetPowerResult>(CommandContract.CMD_GET_POWER, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 5 || buffer.get() != CommandContract.CMD_GET_POWER
                        || buffer.get() != 0x03) {
                    return null;
                }
                byte[] bytes = new byte[3];
                buffer.get(bytes);
                int power = ((bytes[1] & 0xFF) << 8)
                        | (bytes[0] & 0xFF);
                int units = bytes[2] & 0xFF;
                power = CsafeUnitUtil.normalizeInt(power, units);

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.POWER, power);
                return values;
            }

            @Override
            public GetPowerResult getResult(DataHolder data, int row) {
                return new GetPowerResultRef(data, row);
            }
        };
    }

    /**
     * Gets the current workout type as one of {@link PaceMonitor.WorkoutType}.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetWorkoutTypeResult> getWorkoutTypeCmd(
            ResultCallback<GetWorkoutTypeResult> callback) {
        return new CommandImpl<GetWorkoutTypeResult>(CommandContract.CMD_GET_WORKOUT_TYPE,
                true /* isCustomCommand */, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 5 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x03 || buffer.get() != CommandContract.CMD_GET_WORKOUT_TYPE
                        || buffer.get() != 0x01) {
                    return null;
                }
                int workoutType = buffer.get() & 0xFF;

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.WORKOUT_TYPE, workoutType);
                return values;
            }

            @Override
            public GetWorkoutTypeResult getResult(DataHolder data, int row) {
                return new GetWorkoutTypeResultRef(data, row);
            }
        };
    }

    /**
     * Gets the current drag factor.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetDragFactorResult> getDragFactorCmd(
            ResultCallback<GetDragFactorResult> callback) {
        return new CommandImpl<GetDragFactorResult>(CommandContract.CMD_GET_DRAG_FACTOR, true /* isCustomCommand */,
                callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 5 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x03 || buffer.get() != CommandContract.CMD_GET_DRAG_FACTOR
                        || buffer.get() != 0x01) {
                    return null;
                }
                int dragFactor = buffer.get() & 0xFF;

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.DRAG_FACTOR, dragFactor);
                return values;
            }

            @Override
            public GetDragFactorResult getResult(DataHolder data, int row) {
                return new GetDragFactorResultRef(data, row);
            }
        };
    }

    /**
     * Gets the current stroke state as one of {@link PaceMonitor.StrokeState}.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetStrokeStateResult> getStrokeStateCmd(
            ResultCallback<GetStrokeStateResult> callback) {
        return new CommandImpl<GetStrokeStateResult>(CommandContract.CMD_GET_STROKE_STATE,
                true /* isCustomCommand */, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 5 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x03 || buffer.get() != CommandContract.CMD_GET_STROKE_STATE
                        || buffer.get() != 0x01) {
                    return null;
                }
                int strokeState = buffer.get() & 0xFF;

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.STROKE_STATE, strokeState);
                return values;
            }

            @Override
            public GetStrokeStateResult getResult(DataHolder data, int row) {
                return new GetStrokeStateResultRef(data, row);
            }
        };
    }

    /**
     * Gets the high resolution work time accurate to the 1/100th of a second.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetHighResWorkTimeResult> getHighResolutionWorkTimeCmd(
            ResultCallback<GetHighResWorkTimeResult> callback) {
        return new CommandImpl<GetHighResWorkTimeResult>(CommandContract.CMD_GET_HIGH_RES_WORK_TIME,
                true /* isCustomCommand */, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 8 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x07 || buffer.get() != CommandContract.CMD_GET_HIGH_RES_WORK_TIME
                        || buffer.get() != 0x05) {
                    return null;
                }
                double time = 0.01 * (read4ByteInt(buffer) + buffer.get());

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.HIGH_RES_SECONDS, time);
                return values;
            }

            @Override
            public GetHighResWorkTimeResult getResult(DataHolder data, int row) {
                return new GetHighResWorkTimeResultRef(data, row);
            }
        };
    }

    /**
     * Gets the high resolution work distance accurate to the 1/10th of a meter.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetHighResWorkDistanceResult> getHighResolutionWorkDistanceCmd(
            ResultCallback<GetHighResWorkDistanceResult> callback) {
        return new CommandImpl<GetHighResWorkDistanceResult>(
                CommandContract.CMD_GET_HIGH_RES_WORK_DISTANCE,
                true /* isCustomCommand */, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 8 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x07 || buffer.get() != CommandContract.CMD_GET_HIGH_RES_WORK_DISTANCE
                        || buffer.get() != 0x05) {
                    return null;
                }
                double time = 0.1 * (read4ByteInt(buffer) + buffer.get());

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.HIGH_RES_DISTANCE, time);
                return values;
            }

            @Override
            public GetHighResWorkDistanceResult getResult(DataHolder data, int row) {
                return new GetHighResWorkDistanceResultRef(data, row);
            }
        };
    }

    /**
     * Gets and clears the latched error value in the pace monitor when the screen error display
     * mode is disabled.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    // TODO enumerate error values.
    public static Command<GetErrorValueResult> getErrorValueCmd(
            ResultCallback<GetErrorValueResult> callback) {
        return new CommandImpl<GetErrorValueResult>(CommandContract.CMD_GET_ERROR_VALUE, true /* isCustomCommand */,
                callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 5 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x04 || buffer.get() != CommandContract.CMD_GET_ERROR_VALUE
                        || buffer.get() != 0x02) {
                    return null;
                }
                int errorValue = read2ByteInt(buffer);

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.ERROR_VALUE, errorValue);
                return values;
            }

            @Override
            public GetErrorValueResult getResult(DataHolder data, int row) {
                return new GetErrorValueResultRef(data, row);
            }
        };
    }

    /**
     * Gets the workout state as one of {@link PaceMonitor.WorkoutState}.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetWorkoutStateResult> getWorkoutStateCmd(
            ResultCallback<GetWorkoutStateResult> callback) {
        return new CommandImpl<GetWorkoutStateResult>(CommandContract.CMD_GET_WORKOUT_STATE,
                true /* isCustomCommand */, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 4 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x03 || buffer.get() != CommandContract.CMD_GET_WORKOUT_STATE
                        || buffer.get() != 0x01) {
                    return null;
                }
                int workoutState = buffer.get() & 0xFF;

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.WORKOUT_STATE, workoutState);
                return values;
            }

            @Override
            public GetWorkoutStateResult getResult(DataHolder data, int row) {
                return new GetWorkoutStateResultRef(data, row);
            }
        };
    }

    /**
     * Gets the current workout interval count.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetWorkoutIntervalCountResult> getWorkoutIntervalCountCmd(
            ResultCallback<GetWorkoutIntervalCountResult> callback) {
        return new CommandImpl<GetWorkoutIntervalCountResult>(
                CommandContract.CMD_GET_WORKOUT_INTERVAL_COUNT,
                true /* isCustomCommand */, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 4 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x03 || buffer.get() != CommandContract.CMD_GET_WORKOUT_INTERVAL_COUNT
                        || buffer.get() != 0x01) {
                    return null;
                }
                int intervalCount = buffer.get() & 0xFF;

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.INTERVAL_COUNT, intervalCount);
                return values;
            }

            @Override
            public GetWorkoutIntervalCountResult getResult(DataHolder data, int row) {
                return new GetWorkoutIntervalCountResultRef(data, row);
            }
        };
    }

    /**
     * Gets the current interval type as one of {@link PaceMonitor.IntervalType}.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetIntervalTypeResult> getIntervalTypeCmd(
            ResultCallback<GetIntervalTypeResult> callback) {
        return new CommandImpl<GetIntervalTypeResult>(CommandContract.CMD_GET_INTERVAL_TYPE,
                true /* isCustomCommand */, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 4 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x03 || buffer.get() != CommandContract.CMD_GET_INTERVAL_TYPE
                        || buffer.get() != 0x01) {
                    return null;
                }
                int intervalType = buffer.get() & 0xFF;

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.INTERVAL_TYPE, intervalType);
                return values;
            }

            @Override
            public GetIntervalTypeResult getResult(DataHolder data, int row) {
                return new GetIntervalTypeResultRef(data, row);
            }
        };
    }

    /**
     * Gets the current rest time in seconds.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetRestTimeResult> getRestTimeCmd(
            ResultCallback<GetRestTimeResult> callback) {
        return new CommandImpl<GetRestTimeResult>(CommandContract.CMD_GET_REST_TIME, true /* isCustomCommand */,
                callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 5 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x04 || buffer.get() != CommandContract.CMD_GET_REST_TIME
                        || buffer.get() != 0x02) {
                    return null;
                }
                int restTime = read2ByteInt(buffer);

                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.REST_TIME, restTime);
                return values;
            }

            @Override
            public GetRestTimeResult getResult(DataHolder data, int row) {
                return new GetRestTimeResultRef(data, row);
            }
        };
    }

    /**
     * Gets the force curve plot with a given resolution. Use resolution 0 to get the maximum of 16.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param numSamples The number of sample points [0-16] where 0 can be used to represent max.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetForcePlotResult> getForcePlotCmd(
            ResultCallback<GetForcePlotResult> callback, int numSamples) {
        PaceMonitorImpl.validateNumSamples(numSamples);
        if (numSamples == 0) {
            numSamples = 16;
        }
        byte[] data = new byte[] { (byte) (numSamples * 2) };
        return new CommandImpl<GetForcePlotResult>(CommandContract.CMD_GET_FORCE_PLOT, true /* isCustomCommand */,
                data, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 36 || buffer.get() != CommandContract.USR_CONFIG1
                        || buffer.get() != 0x23 || buffer.get() != CommandContract.CMD_GET_FORCE_PLOT
                        || buffer.get() != 0x21) {
                    return null;
                }
                int[] forcePlot = new int[16];
                for (int i = 0; i < forcePlot.length; ++i) {
                    forcePlot[i] = read2ByteInt(buffer);
                }
                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.FORCE_PLOT, Objects.toByteArray(forcePlot));
                return values;
            }

            @Override
            public GetForcePlotResult getResult(DataHolder data, int row) {
                return new GetForcePlotResultRef(data, row);
            }
        };
    }

    /**
     * Gets the heart rate plot with a given resolution. Use resolution 0 to get the maximum of 16.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param numSamples The number of sample points [0-16] where 0 can be used to represent max.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<GetHeartRatePlotResult> getHeartRatePlotCmd(
            ResultCallback<GetHeartRatePlotResult> callback, int numSamples) {
        PaceMonitorImpl.validateNumSamples(numSamples);
        if (numSamples == 0) {
            numSamples = 16;
        }
        byte[] data = new byte[] { (byte) (numSamples * 2) };
        return new CommandImpl<GetHeartRatePlotResult>(CommandContract.CMD_GET_HEART_RATE_PLOT,
                true /* isCustomCommand */, data, callback) {
            @Override
            public ContentValues handleResult(PaceMonitorStatus status, ByteBuffer buffer) {
                if (buffer.remaining() < 2 || buffer.get() != CommandContract.USR_CONFIG1) {
                    return null;
                }
                int responseLength = buffer.get();
                if (responseLength == 0) {
                    return super.handleResult(status, buffer);
                }
                if (responseLength != 0x23 || buffer.get() != CommandContract.CMD_GET_HEART_RATE_PLOT
                        || buffer.get() != 0x21) {
                    return null;
                }
                int[] heartRatePlot = new int[16];
                for (int i = 0; i < heartRatePlot.length; ++i) {
                    heartRatePlot[i] = read2ByteInt(buffer);
                }
                ContentValues values = super.handleResult(status, buffer);
                values.put(PaceMonitorColumnContract.HEART_RATE_PLOT,
                        Objects.toByteArray(heartRatePlot));
                return values;
            }

            @Override
            public GetHeartRatePlotResult getResult(DataHolder data, int row) {
                return new GetHeartRatePlotResultRef(data, row);
            }
        };
    }

    /**
     * Set the current time HH:MM:SS using a 24 hour clock.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param hours The hours to set [0-23].
     * @param minutes The minutes to set [0-59].
     * @param seconds The seconds to set [0-59].
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setTimeCmd(ResultCallback<PaceMonitorResult> callback,
            int hours, int minutes, int seconds) {
        PaceMonitorImpl.validateTime(hours, minutes, seconds);
        byte[] data = new byte[] { toByte(hours), toByte(minutes), toByte(seconds) };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_TIME, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Set the current date MM/DD/YYYY. This method only accepts years between [1900, 2155].
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param year The year to set [1900-2155].
     * @param month The month to set [1-12].
     * @param day The day of the month (range depends on the year and month).
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setDateCmd(ResultCallback<PaceMonitorResult> callback,
            int year, int month, int day) {
        PaceMonitorImpl.validateDate(year, month, day);
        year -= 1900;
        byte[] data = new byte[] { toByte(year), toByte(month), toByte(day) };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_DATE, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Set the timeout for the current pace monitor state.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param seconds The number of seconds to timeout.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setTimeoutCmd(
            ResultCallback<PaceMonitorResult> callback, int seconds) {
        PaceMonitorImpl.validateTimeout(seconds);
        byte[] data = new byte[] { toByte(seconds) };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_TIMEOUT, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Sets the goal time for the workout in seconds. Value must be at least 20 seconds and under 10
     * hours.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param seconds The number of seconds the user should row.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setGoalTimeCmd(
            ResultCallback<PaceMonitorResult> callback, int seconds) {
        PaceMonitorImpl.validateGoalTime(seconds);
        byte hoursByte = toByte(TimeUnit.SECONDS.toHours(seconds));
        seconds -= TimeUnit.HOURS.toSeconds(hoursByte);
        byte minutesByte = toByte(TimeUnit.SECONDS.toMinutes(seconds));
        seconds -= TimeUnit.MINUTES.toSeconds(minutesByte);
        byte secondsByte = toByte(seconds);
        byte[] data = new byte[] { hoursByte, minutesByte, secondsByte };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_GOAL_TIME, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Sets the goal distance for the workout in meters. Value must be in the range of
     * [100, 50000] meters.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param meters The number of meters the user should row.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setGoalDistanceCmd(
            ResultCallback<PaceMonitorResult> callback, int meters) {
        PaceMonitorImpl.validateGoalDistance(meters);
        byte[] data = new byte[] { getByte(meters, 0), getByte(meters, 1), CsafeUnitUtil.METER };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_GOAL_DISTANCE, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Sets the goal calories for the workout. Value must be in the range of [0, 65535] calories.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param calories The number of calories the user should burn.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setGoalCaloriesCmd(
            ResultCallback<PaceMonitorResult> callback, int calories) {
        PaceMonitorImpl.validateGoalCalories(calories);
        byte[] data = new byte[] { getByte(calories, 0), getByte(calories, 1) };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_GOAL_CALORIES, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Sets the goal power for the workout. Value must be in the range of [0, 65535] watts.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param watts The power the user should produce.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setGoalPowerCmd(
            ResultCallback<PaceMonitorResult> callback, int watts) {
        PaceMonitorImpl.validateGoalPower(watts);
        byte[] data = new byte[] { getByte(watts, 0), getByte(watts, 1), CsafeUnitUtil.WATTS };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_GOAL_POWER, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Sets the pace monitor to a programmed or pre-stored workout using the following definitions:
     * <ul>
     *     <li>0: programmed workout.</li>
     *     <li>1-5: Standard list.</li>
     *     <li>6-10: Custom list.</li>
     *     <li>11-15: Favorites list (only available if logcard is present).</li>
     * </ul>
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param workoutNumber The workout number.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setStoredWorkoutNumberCmd(
            ResultCallback<PaceMonitorResult> callback, int workoutNumber) {
        PaceMonitorImpl.validateWorkoutNumber(workoutNumber);
        byte[] data = new byte[] { toByte(workoutNumber), 0x00 };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_STORED_WORKOUT_NUMBER, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Sets the split to use time units with a given seconds value accurate to 1/100th of a second
     * and must be at least 20 seconds.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param seconds The split duration in seconds.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setSplitTimeCmd(
            ResultCallback<PaceMonitorResult> callback, double seconds) {
        PaceMonitorImpl.validateSplitTime(seconds);
        int duration = (int) (seconds * 100);
        byte[] data = new byte[] { 0x00 /* time */, getByte(duration, 0),
                getByte(duration, 1), getByte(duration, 2), getByte(duration, 3) };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_SPLIT_DURATION,
                true /* isCustomCommand */, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Sets the split to use distance units with a given meter value accurate to the meter
     * and must be at least 100m.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param meters The split duration in meters.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setSplitDistanceCmd(
            ResultCallback<PaceMonitorResult> callback, int meters) {
        PaceMonitorImpl.validateSplitDistance(meters);
        byte[] data = new byte[] { (byte) 0x80 /* distance */, getByte(meters, 0),
                getByte(meters, 1), getByte(meters, 2), getByte(meters, 3) };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_SPLIT_DURATION,
                true /* isCustomCommand */, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Enable/disable the screen error mode.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param enabled True if the screen error mode should be enabled.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setScreenErrorModeCmd(
            ResultCallback<PaceMonitorResult> callback, boolean enabled) {
        byte[] data = new byte[] { (byte) (enabled ? 0x01 : 0x00) };
        return new CommandImpl<PaceMonitorResult>(CommandContract.CMD_SET_SCREEN_ERROR_MODE,
                true /* isCustomCommand */, data, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    /**
     * Sets the state of the pace monitor. State must be one of
     * {@link PaceMonitor.PaceMonitorState}.
     *
     * @param callback An optional {@link ResultCallback} to call when the result is ready.
     * @param state The new state of the pace monitor.
     * @return {@link Command} That can be executed or batched with others.
     */
    public static Command<PaceMonitorResult> setPaceMonitorState(
            ResultCallback<PaceMonitorResult> callback, int state) {
        PaceMonitorImpl.validatePaceMonitorState(state);
        byte command;
        switch (state) {
            case PaceMonitor.PaceMonitorState.RESET:
                command = CommandContract.CMD_GO_RESET;
                break;
            case PaceMonitor.PaceMonitorState.IDLE:
                command = CommandContract.CMD_GO_IDLE;
                break;
            case PaceMonitor.PaceMonitorState.HAVE_ID:
                command = CommandContract.CMD_GO_HAVE_ID;
                break;
            case PaceMonitor.PaceMonitorState.IN_USE:
                command = CommandContract.CMD_GO_IN_USE;
                break;
            case PaceMonitor.PaceMonitorState.FINISHED:
                command = CommandContract.CMD_GO_FINISHED;
                break;
            case PaceMonitor.PaceMonitorState.READY:
                command = CommandContract.CMD_GO_READY;
                break;
            case PaceMonitor.PaceMonitorState.BAD_ID:
                command = CommandContract.CMD_GO_BAD_ID;
                break;
            default:
                throw new IllegalArgumentException("Invalid pace monitor state " + state);
        }
        return new CommandImpl<PaceMonitorResult>(command, callback) {
            @Override
            public PaceMonitorResult getResult(DataHolder data, int row) {
                return new PaceMonitorResultRef(data, row);
            }
        };
    }

    private static byte toByte(int val) {
        return (byte) (val & 0xFF);
    }

    private static byte toByte(long val) {
        return (byte) (val & 0xFF);
    }

    private static byte getByte(int val, int idx) {
        return (byte) ((val >> (idx * 8)) & 0xFF);
    }

    private static int read4ByteInt(ByteBuffer buffer) {
        return (buffer.get() & 0xFF)
                | ((buffer.get() & 0xFF) << 8)
                | ((buffer.get() & 0xFF) << 16)
                | ((buffer.get() & 0xFF) << 24);
    }

    private static int read2ByteInt(ByteBuffer buffer) {
        return (buffer.get() & 0xFF)
                | ((buffer.get() & 0xFF) << 8);
    }
}
