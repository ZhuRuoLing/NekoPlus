package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import dev.dubhe.anvilcraft.api.chargecollector.ChargeCollectorManager;
import icu.takeneko.nekoplus.block.BlastCrystalBlock;
import icu.takeneko.nekoplus.config.NPConfig;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BlastCrystalBlockEntity extends NPSynedBlockEntity implements Tickable {

    private static final ConfigAccess config = new ConfigAccess();

    @Persisted
    private double accumulatedCharge = 0.0;
    private boolean detonating = false;
    @Persisted
    private int detonateTickCountdown = 0;

    public BlastCrystalBlockEntity(
        BlockEntityType<?> type,
        BlockPos pos,
        BlockState blockState
    ) {
        super(type, pos, blockState);
    }

    public void handleExplosionHit(ServerLevel level, BlockPos pos, Explosion explosion) {
        if (this.detonating) {
            return;
        }
        float radius = explosion.radius();
        Vec3 center = explosion.center();
        double distance = pos.getCenter().distanceTo(center);
        double charge = calculateCharge(radius, distance);
        ChargeCollectorManager.charge(
            charge,
            level,
            pos
        );
        this.accumulatedCharge += charge;
        setChanged();
        if (this.accumulatedCharge >= this.getDegradeChargeAmount()) {
            this.detonateTickCountdown = config.detonationDelayTicks - level.getRandom().nextInt(1, 7);
        }
    }

    private static double calculateCharge(double radius, double distance) {
        if (radius <= 0.0) {
            return 0.0;
        }
        double effectiveRadius = Math.max(radius - 1.0, 1.0);
        double effectiveDistance = Math.max(distance - 1.0, 0.0);
        double normalizedDistance = Math.clamp(
            effectiveDistance / effectiveRadius,
            0.0,
            1.0
        );
        double proximity = 1.0 - normalizedDistance;
        return config.chargePerRadiusAtFalloffEdge
            * radius
            * Math.pow(config.distanceFalloffExponentBase, proximity);
    }

    private void detonate(ServerLevel level, BlockPos pos) {
        this.accumulatedCharge = 0.0;
        setChanged();

        this.detonating = true;
        Vec3 center = pos.getCenter();
        level.explode(
            null,
            center.x(),
            center.y(),
            center.z(),
            config.explosionPower,
            Level.ExplosionInteraction.TNT
        );
        this.detonating = false;

        double degradeRoll = level.getRandom().nextDouble();
        if (degradeRoll < this.getDegradeChance()) {
            this.degrade(level, pos);
        }
    }

    private double getDegradeChance() {
        if (!(getBlockState().getBlock() instanceof BlastCrystalBlock block)) {
            return 0.0;
        }
        return switch (block.getStage()) {
            case NORMAL -> config.normalDegradeChance * (1 + (level.getRandom().nextDouble() - 0.5) / 2);
            case DAMAGED -> config.damagedDegradeChance;
            case CRACKED -> config.crackedDegradeChance;
        };
    }

    private double getDegradeChargeAmount() {
        if (!(getBlockState().getBlock() instanceof BlastCrystalBlock block)) {
            return 0.0;
        }
        return switch (block.getStage()) {
            case NORMAL -> config.normalBaseDetonationCharge - accumulatedCharge * config.normalAccumulatedChargeSensitivity;
            case DAMAGED -> config.damagedBaseDetonationCharge - accumulatedCharge * config.damagedAccumulatedChargeSensitivity;
            case CRACKED -> config.crackedDetonationCharge;
        };
    }

    private void degrade(ServerLevel level, BlockPos pos) {
        if (getBlockState().getBlock() instanceof BlastCrystalBlock block) {
            BlockState blockState = block.getStage().nextState();
            level.setBlockAndUpdate(pos, blockState);
        }
    }

    @Override
    public void tick() {
        if (!(level instanceof ServerLevel)) {
            return;
        }
        this.detonateTickCountdown = Math.clamp(
            detonateTickCountdown - 1,
            0,
            Integer.MAX_VALUE
        );
        if (detonateTickCountdown == 1) {
            detonate(getServerLevel(), getBlockPos());
        }
        if (this.accumulatedCharge < config.minChargeBeforeDecay) {
            if (this.accumulatedCharge != 0.0) {
                this.accumulatedCharge = 0.0;
            }
            return;
        }
        this.accumulatedCharge *= config.accumulatedChargeDecayMultiplier;
        if (this.accumulatedCharge < config.minChargeBeforeDecay) {
            this.accumulatedCharge = 0.0;
        }
    }

    public static class ConfigAccess {
        public volatile double minChargeBeforeDecay = 10.0;
        public volatile double accumulatedChargeDecayMultiplier = 0.95;
        public volatile double chargePerRadiusAtFalloffEdge = 18.0;
        public volatile double distanceFalloffExponentBase = 4.0;
        public volatile int detonationDelayTicks = 15;
        public volatile double normalDegradeChance = 0.5;
        public volatile double damagedDegradeChance = 0.7;
        public volatile double crackedDegradeChance = 0.9;
        public volatile double normalBaseDetonationCharge = 128.0;
        public volatile double normalAccumulatedChargeSensitivity = 0.5;
        public volatile double damagedBaseDetonationCharge = 96.0;
        public volatile double damagedAccumulatedChargeSensitivity = 0.1;
        public volatile double crackedDetonationCharge = 32.0;
        public volatile float explosionPower = 6.0f;

        public void reload() {
            this.minChargeBeforeDecay = NPConfig.BLAST_CRYSTAL_MIN_CHARGE_BEFORE_DECAY.getAsDouble();
            this.accumulatedChargeDecayMultiplier = NPConfig.BLAST_CRYSTAL_ACCUMULATED_CHARGE_DECAY_MULTIPLIER.getAsDouble();
            this.chargePerRadiusAtFalloffEdge = NPConfig.BLAST_CRYSTAL_CHARGE_PER_RADIUS_AT_FALLOFF_EDGE.getAsDouble();
            this.distanceFalloffExponentBase = NPConfig.BLAST_CRYSTAL_DISTANCE_FALLOFF_EXPONENT_BASE.getAsDouble();
            this.detonationDelayTicks = NPConfig.BLAST_CRYSTAL_DETONATION_DELAY_TICKS.getAsInt();
            this.normalDegradeChance = NPConfig.BLAST_CRYSTAL_NORMAL_DEGRADE_CHANCE.getAsDouble();
            this.damagedDegradeChance = NPConfig.BLAST_CRYSTAL_DAMAGED_DEGRADE_CHANCE.getAsDouble();
            this.crackedDegradeChance = NPConfig.BLAST_CRYSTAL_CRACKED_DEGRADE_CHANCE.getAsDouble();
            this.normalBaseDetonationCharge = NPConfig.BLAST_CRYSTAL_NORMAL_BASE_DETONATION_CHARGE.getAsDouble();
            this.normalAccumulatedChargeSensitivity = NPConfig.BLAST_CRYSTAL_NORMAL_ACCUMULATED_CHARGE_SENSITIVITY.getAsDouble();
            this.damagedBaseDetonationCharge = NPConfig.BLAST_CRYSTAL_DAMAGED_BASE_DETONATION_CHARGE.getAsDouble();
            this.damagedAccumulatedChargeSensitivity = NPConfig.BLAST_CRYSTAL_DAMAGED_ACCUMULATED_CHARGE_SENSITIVITY.getAsDouble();
            this.crackedDetonationCharge = NPConfig.BLAST_CRYSTAL_CRACKED_DETONATION_CHARGE.getAsDouble();
            this.explosionPower = NPConfig.BLAST_CRYSTAL_EXPLOSION_POWER.get().floatValue();
        }
    }

    public static ConfigAccess config() {
        return config;
    }
}
