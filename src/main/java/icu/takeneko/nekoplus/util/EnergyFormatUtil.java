package icu.takeneko.nekoplus.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class EnergyFormatUtil {
    private static final long KILOWATT_HOUR_IN_KILOWATT_SECONDS = 3600L;
    private static final long MEGAWATT_HOUR_IN_KILOWATT_SECONDS = 3_600_000L;
    private static final BigDecimal KILOWATT_HOUR_DIVISOR = BigDecimal.valueOf(KILOWATT_HOUR_IN_KILOWATT_SECONDS);
    private static final BigDecimal MEGAWATT_HOUR_DIVISOR = BigDecimal.valueOf(MEGAWATT_HOUR_IN_KILOWATT_SECONDS);

    private EnergyFormatUtil() {
    }

    public static String formatStoredEnergyNumber(long kilowattSeconds) {
        BigDecimal divisor = Math.abs(kilowattSeconds) >= MEGAWATT_HOUR_IN_KILOWATT_SECONDS
            ? MEGAWATT_HOUR_DIVISOR
            : KILOWATT_HOUR_DIVISOR;
        return BigDecimal.valueOf(kilowattSeconds)
            .divide(divisor, 2, RoundingMode.DOWN)
            .stripTrailingZeros()
            .toPlainString();
    }

    public static String formatStoredEnergyUnit(long kilowattSeconds) {
        return Math.abs(kilowattSeconds) >= MEGAWATT_HOUR_IN_KILOWATT_SECONDS ? "MWh" : "kWh";
    }
}
