package org.uvdev.rowbot.concept2api.pacemonitor.internal.contracts;

/**
 * Command definitions for the Concept2 Pace Monitor.
 */
public interface CommandContract {

    // Short commands.
    byte CMD_GET_STATUS = (byte) 0x80;
    byte CMD_GO_RESET = (byte) 0x81;
    byte CMD_GO_IDLE = (byte) 0x82;
    byte CMD_GO_HAVE_ID = (byte) 0x83;
    byte CMD_GO_IN_USE = (byte) 0x85;
    byte CMD_GO_FINISHED = (byte) 0x86;
    byte CMD_GO_READY = (byte) 0x87;
    byte CMD_GO_BAD_ID = (byte) 0x88;
    byte CMD_GET_ODOMETER = (byte) 0x9B;
    byte CMD_GET_WORK_TIME = (byte) 0xA0;
    byte CMD_GET_WORK_DISTANCE = (byte) 0xA1;
    byte CMD_GET_WORK_CALORIES = (byte) 0xA3;
    byte CMD_GET_STORED_WORKOUT_NUMBER = (byte) 0xA4;
    byte CMD_GET_PACE = (byte) 0xA6;
    byte CMD_GET_STROKE_RATE = (byte) 0xA7;
    byte CMD_GET_USER_INFO = (byte) 0xAB;
    byte CMD_GET_HEART_RATE = (byte) 0xB0;
    byte CMD_GET_POWER = (byte) 0xB4;

    // Long commands.
    byte CMD_SET_TIME = (byte) 0x11;
    byte CMD_SET_DATE = (byte) 0x12;
    byte CMD_SET_TIMEOUT = (byte) 0x13;
    byte CMD_SET_GOAL_TIME = (byte) 0x20;
    byte CMD_SET_GOAL_DISTANCE = (byte) 0x21;
    byte CMD_SET_GOAL_CALORIES = (byte) 0x23;
    byte CMD_SET_STORED_WORKOUT_NUMBER = (byte) 0x24;
    byte CMD_SET_GOAL_POWER = (byte) 0x34;

    // Custom commands.
    byte USR_CONFIG1 = (byte) 0x1A;
    byte CMD_GET_WORKOUT_TYPE = (byte) 0x89;
    byte CMD_GET_DRAG_FACTOR = (byte) 0xC1;
    byte CMD_GET_STROKE_STATE = (byte) 0xBF;
    byte CMD_GET_HIGH_RES_WORK_TIME = (byte) 0xA0;
    byte CMD_GET_HIGH_RES_WORK_DISTANCE = (byte) 0xA3;
    byte CMD_GET_ERROR_VALUE = (byte) 0xC9;
    byte CMD_GET_WORKOUT_STATE = (byte) 0x8D;
    byte CMD_GET_WORKOUT_INTERVAL_COUNT = (byte) 0x9F;
    byte CMD_GET_INTERVAL_TYPE = (byte) 0x8E;
    byte CMD_GET_REST_TIME = (byte) 0xCF;
    byte CMD_SET_SPLIT_DURATION = (byte) 0x05;
    byte CMD_GET_FORCE_PLOT = (byte) 0x6B;
    byte CMD_GET_HEART_RATE_PLOT = (byte) 0x6C;
    byte CMD_SET_SCREEN_ERROR_MODE = (byte) 0x27;
}
