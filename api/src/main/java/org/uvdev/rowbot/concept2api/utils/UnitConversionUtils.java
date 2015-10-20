package org.uvdev.rowbot.concept2api.utils;

public final class UnitConversionUtils {
    /**
     * Convert a given split to watts.
     *
     * @param split The split to convert in sec / 500m.
     * @return The equivalent power in watts.
     */
    public static double splitToWatts(double split) {
        return 2.8 / Math.pow(split / 500.0, 3);
    }

    /**
     * Convert a given power in watts to split.
     *
     * @param watts The power in watts to convert.
     * @return The equivalent split in sec / 500m.
     */
    public static double wattsToSplit(double watts) {
        return Math.cbrt(2.8 / watts) * 500.0 ;
    }

    /**
     * Converts a given split to calorie burn rate.
     *
     * @param split The split to convert in sec / 500m.
     * @return The equivalent calorie burn rate.
     */
    public static double splitToCalPerHour(double split) {
        return (splitToWatts(split) * (4.0 * 0.8604)) + 300;
    }

    /**
     * Convert a calorie burn rate to split.
     *
     * @param calPerHour The calorie burn rate in calories / hour.
     * @return The equivalent split in sec / 500m.
     */
    public static double calPerHourToSplit(double calPerHour) {
        return wattsToSplit((calPerHour - 300) / (4.0 * 0.8604));
    }
}
