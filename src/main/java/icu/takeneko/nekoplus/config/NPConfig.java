package icu.takeneko.nekoplus.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class NPConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
        .comment("A magic number")
        .translation("nekoplus.configuration.magicNumber")
        .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    static {
        BUILDER.push("overclocking");
    }

    public static final ModConfigSpec.IntValue PARTICLE_STABILIZER_MAX_OVERCLOCK_RATIO = BUILDER
        .comment("Maximum overclock ratio for the particle stabilizer.")
        .translation("nekoplus.configuration.overclocking.particleStabilizer.maxOverclockRatio")
        .defineInRange("particleStabilizer.maxOverclockRatio", 100, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue PARTICLE_STABILIZER_BASE_OVERCLOCK_COST = BUILDER
        .comment("Base overclock cost for the particle stabilizer.")
        .translation("nekoplus.configuration.overclocking.particleStabilizer.baseOverclockCost")
        .defineInRange("particleStabilizer.baseOverclockCost", 32, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue CHARGER_MAX_OVERCLOCK_RATIO = BUILDER
        .comment("Maximum overclock ratio for the charger.")
        .translation("nekoplus.configuration.overclocking.charger.maxOverclockRatio")
        .defineInRange("charger.maxOverclockRatio", 100, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue SMART_BLOCK_PLACER_MAX_OVERCLOCK_RATIO = BUILDER
        .comment("Maximum overclock ratio for the smart block placer.")
        .translation("nekoplus.configuration.overclocking.smartBlockPlacer.maxOverclockRatio")
        .defineInRange("smartBlockPlacer.maxOverclockRatio", 20, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue SMART_BLOCK_PLACER_BASE_OVERCLOCK_COST = BUILDER
        .comment("Base overclock cost for the smart block placer.")
        .translation("nekoplus.configuration.overclocking.smartBlockPlacer.baseOverclockCost")
        .defineInRange("smartBlockPlacer.baseOverclockCost", 4, 0, Integer.MAX_VALUE);

    static {
        BUILDER.pop();
    }

    static {
        BUILDER.push("battery");
    }

    public static final ModConfigSpec.IntValue BATTERY_CAPACITY = BUILDER
        .comment("Battery capacity, in watt-seconds.")
        .translation("nekoplus.configuration.battery.capacity")
        .defineInRange("capacity", 10800000, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue BATTERY_MAX_CHARGING_RATE = BUILDER
        .comment("Maximum charging rate for the battery.")
        .translation("nekoplus.configuration.battery.maxChargingRate")
        .defineInRange("maxChargingRate", 6000, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue BATTERY_MAX_DISCHARGING_RATE_DEFAULT = BUILDER
        .comment("Default maximum discharging rate for the battery.")
        .translation("nekoplus.configuration.battery.maxDischargingRateDefault")
        .defineInRange("maxDischargingRateDefault", 1000, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.IntValue BATTERY_MAX_DISCHARGING_RATE_MAX = BUILDER
        .comment("Maximum allowed maximum discharging rate for the battery.")
        .translation("nekoplus.configuration.battery.maxDischargingRateMax")
        .defineInRange("maxDischargingRateMax", 65536, 0, Integer.MAX_VALUE);

    static {
        BUILDER.pop();
    }

    static {
        BUILDER.push("crystal");
    }

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_MIN_CHARGE_BEFORE_DECAY = BUILDER
        .comment("Minimum accumulated charge before decay is applied.")
        .translation("nekoplus.configuration.crystal.minChargeBeforeDecay")
        .defineInRange("minChargeBeforeDecay", 10.0, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_ACCUMULATED_CHARGE_DECAY_MULTIPLIER = BUILDER
        .comment("Multiplier applied to accumulated charge each tick while decaying.")
        .translation("nekoplus.configuration.crystal.accumulatedChargeDecayMultiplier")
        .defineInRange("accumulatedChargeDecayMultiplier", 0.95, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_CHARGE_PER_RADIUS_AT_FALLOFF_EDGE = BUILDER
        .comment("Base generated charge per explosion radius at the falloff edge.")
        .translation("nekoplus.configuration.crystal.chargePerRadiusAtFalloffEdge")
        .defineInRange("chargePerRadiusAtFalloffEdge", 18.0, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_DISTANCE_FALLOFF_EXPONENT_BASE = BUILDER
        .comment("Exponential base used for blast crystal distance falloff.")
        .translation("nekoplus.configuration.crystal.distanceFalloffExponentBase")
        .defineInRange("distanceFalloffExponentBase", 4.0, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.IntValue BLAST_CRYSTAL_DETONATION_DELAY_TICKS = BUILDER
        .comment("Base detonation delay in ticks after the charge threshold is reached.")
        .translation("nekoplus.configuration.crystal.detonationDelayTicks")
        .defineInRange("detonationDelayTicks", 15, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_NORMAL_DEGRADE_CHANCE = BUILDER
        .comment("Degrade chance for normal blast crystals after detonation.")
        .translation("nekoplus.configuration.crystal.normalDegradeChance")
        .defineInRange("normalDegradeChance", 0.5, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_DAMAGED_DEGRADE_CHANCE = BUILDER
        .comment("Degrade chance for damaged blast crystals after detonation.")
        .translation("nekoplus.configuration.crystal.damagedDegradeChance")
        .defineInRange("damagedDegradeChance", 0.7, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_CRACKED_DEGRADE_CHANCE = BUILDER
        .comment("Degrade chance for cracked blast crystals after detonation.")
        .translation("nekoplus.configuration.crystal.crackedDegradeChance")
        .defineInRange("crackedDegradeChance", 0.9, 0.0, 1.0);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_NORMAL_BASE_DETONATION_CHARGE = BUILDER
        .comment("Base detonation charge for normal blast crystals.")
        .translation("nekoplus.configuration.crystal.normalBaseDetonationCharge")
        .defineInRange("normalBaseDetonationCharge", 128.0, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_NORMAL_ACCUMULATED_CHARGE_SENSITIVITY = BUILDER
        .comment("Accumulated charge sensitivity for normal blast crystal detonation thresholds.")
        .translation("nekoplus.configuration.crystal.normalAccumulatedChargeSensitivity")
        .defineInRange("normalAccumulatedChargeSensitivity", 0.5, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_DAMAGED_BASE_DETONATION_CHARGE = BUILDER
        .comment("Base detonation charge for damaged blast crystals.")
        .translation("nekoplus.configuration.crystal.damagedBaseDetonationCharge")
        .defineInRange("damagedBaseDetonationCharge", 96.0, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_DAMAGED_ACCUMULATED_CHARGE_SENSITIVITY = BUILDER
        .comment("Accumulated charge sensitivity for damaged blast crystal detonation thresholds.")
        .translation("nekoplus.configuration.crystal.damagedAccumulatedChargeSensitivity")
        .defineInRange("damagedAccumulatedChargeSensitivity", 0.1, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_CRACKED_DETONATION_CHARGE = BUILDER
        .comment("Detonation charge for cracked blast crystals.")
        .translation("nekoplus.configuration.crystal.crackedDetonationCharge")
        .defineInRange("crackedDetonationCharge", 32.0, 0.0, Double.MAX_VALUE);

    public static final ModConfigSpec.DoubleValue BLAST_CRYSTAL_EXPLOSION_POWER = BUILDER
        .comment("Explosion power produced by a blast crystal detonation.")
        .translation("nekoplus.configuration.crystal.explosionPower")
        .defineInRange("explosionPower", 6.0, 0.0, Float.MAX_VALUE);


    static {
        BUILDER.pop();
    }

    public static final ModConfigSpec SPEC = BUILDER.build();
}
