package com.concept2.api.service.broker.pacemonitor.util;

public class CsafeUnitUtil {
    public static final class ConversionException extends Exception {}

//    public static final int MILE = 1;
//    public static final int ONE_TENTH_MILE = 2;
//    public static final int ONE_HUNDREDTH_MILE = 3;
//    public static final int ONE_THOUSANDTH_MILE = 4;
//    public static final int FOOT = 5;
//    public static final int INCH = 6;
//    public static final int POUND = 7;
//    public static final int ONE_TENTH_POUND = 8;
//    public static final int TEN_FEET = 10;
//    public static final int MILES_PER_HOUR = 16;
//    public static final int ONE_TENTH_MILES_PER_HOUR = 17;
//    public static final int ONE_HUNDREDTH_MILES_PER_HOUR = 18;
//    public static final int FEET_PER_MINUTE = 19;
    public static final int KILOMETER = 33;
    public static final int ONE_TENTH_KILOMETER = 34;
    public static final int ONE_HUNDREDTH_KILOMETER = 35;
    public static final int METER = 36;
    public static final int ONE_TENTH_METER = 37;
    public static final int CENTIMETER = 38;
    public static final int KILOGRAM = 39;
    public static final int ONE_TENTH_KILOGRAM = 40;
    public static final int KILOMETERS_PER_HOUR = 48;
    public static final int ONE_TENTH_KILOMETER_PER_HOUR = 49;
    public static final int ONE_HUNDREDTH_KILOMETER_PER_HOUR = 50;
    public static final int METERS_PER_MINUTE = 51;
//    public static final int MINUTES_PER_MILE = 55;
    public static final int MINUTES_PER_KILOMETER = 56;
    public static final int SECONDS_PER_KILOMETER = 57;
//    public static final int SECONDS_PER_MILE = 58;
//    public static final int FLOORS = 65;
//    public static final int ONE_TENTH_FLOOR = 66;
//    public static final int STEPS = 67;
//    public static final int REVOLUTIONS = 68;
//    public static final int STRIDES = 69;
    public static final int STROKES = 70;
    public static final int BEATS = 71;
    public static final int CALORIES = 72;
    public static final int KILOPOND = 73; // Listes as Kp so might be kilopascals.
//    public static final int PERCENT_GRADE = 74;
//    public static final int ONE_HUNDREDTH_PERCENT_GRADE = 75;
//    public static final int ONE_TENTH_PERCENT_GRADE = 76;
//    public static final int ONE_TENTH_FLOORS_PER_MINUTE = 79;
//    public static final int FLOORS_PER_MINUTE = 80;
//    public static final int STEPS_PER_MINUTE = 81;
//    public static final int REVS_PER_MINUTE = 82;
//    public static final int STRIDES_PER_MINUTE = 83;
    public static final int STROKES_PER_MINUTE = 84;
    public static final int BEATS_PER_MINUTE = 85;
    public static final int CALORIES_PER_MINUTE = 86;
    public static final int CALORIES_PER_HOUR = 87;
    public static final int WATTS = 88;
    public static final int KILOPOND_METERS = 89;
//    public static final int INCH_POUNDS = 90;
//    public static final int FOOT_POUNDS = 91;
    public static final int NEUTON_METERS = 92;
    public static final int AMPERES = 97;
    public static final int ONE_THOUSANDTH_AMPERES = 98;
    public static final int VOLTS = 99;
    public static final int ONE_THOUSANDTH_VOLTS = 100;

    /**
     * Normalize the values.
     * <ul>
     *     <li>Distance - all distance units will be converted to meters.</li>
     *     <li>Mass - all mass units will be converted to kilograms.</li>
     *     <li>Speed - all speed units will be converted to seconds / kilometer.</li>
     *     <li>Energy / time - all energy per time units will be converted to calories / hour.</li>
     *     <li>Torque - all torque units will be converted to N-m.</li>
     *     <li>Current - all current units will be converted to A.</li>
     *     <li>Volts - Volts will be the measurement of electrical potential.</li>
     * </ul>
     *
     * @param value The value to normalize.
     * @param units The value's units.
     * @return The normalized value.
     */
    public static double normalize(int value, int units) {
        switch (units) {
            // Distance to m.
            case KILOMETER: return value * 1000.0;
            case ONE_TENTH_KILOMETER: return value * 100.0;
            case ONE_HUNDREDTH_KILOMETER: return value * 10.0;
            case ONE_TENTH_METER: return value / 10.0;
            case CENTIMETER: return value / 100.0;
            // Mass to kg.
            case ONE_TENTH_KILOGRAM: return value / 10.0;
            // Speed to sec / km.
            case KILOMETERS_PER_HOUR: return 3600.0 / value;
            case ONE_TENTH_KILOMETER_PER_HOUR: return 360.0 / value;
            case ONE_HUNDREDTH_KILOMETER_PER_HOUR: return 36.0 / value;
            case METERS_PER_MINUTE: return 60000.0 / value;
            case MINUTES_PER_KILOMETER: return 60.0 * value;
            // Energy / time to calories / hour.
            case CALORIES_PER_MINUTE: return 60.0 * value;
            // Torque to N-m.
            case KILOPOND_METERS: return 9.80665 * value;
            // Current to A.
            case ONE_THOUSANDTH_AMPERES: return 1000.0 * value;
            // mV to V.
            case ONE_THOUSANDTH_VOLTS: return 1000.0 * value;
            // Self: m, kg, sec / km, strokes, beats, calories, kg-force, spm, bpm, cal/h, W, N-m,
            // A, V
            default:
                return (double) value;
        }
    }

    /**
     * Normalize the value and return it as a rounded integer.
     *
     * @param value The value to normalize.
     * @param units The value's units.
     * @return The normalized value.
     */
    public static int normalizeInt(int value, int units) {
        return (int) Math.round(normalize(value, units));
    }
}
