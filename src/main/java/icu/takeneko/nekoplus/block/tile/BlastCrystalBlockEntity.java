package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import dev.dubhe.anvilcraft.api.chargecollector.ChargeCollectorManager;
import icu.takeneko.nekoplus.block.BlastCrystalBlock;
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
    private static final double MIN_CHARGE_BEFORE_DECAY = 10.0;
    private static final double ACCUMULATED_CHARGE_DECAY_MULTIPLIER = 0.95;

    private static final double CHARGE_PER_RADIUS_AT_FALLOFF_EDGE = 18.0;
    private static final double DISTANCE_FALLOFF_EXPONENT_BASE = 4.0;
    private static final double BLOCK_CENTER_DISTANCE_OFFSET = 1.0;
    private static final double MIN_EFFECTIVE_RADIUS_AFTER_OFFSET = 1.0;
    private static final double MAX_NORMALIZED_DISTANCE = 1.0;
    private static final double MAX_PROXIMITY = 1.0;

    private static final int DETONATION_DELAY_TICKS = 15;
    private static final int DETONATION_TRIGGER_COUNTDOWN = 1;

    private static final double NORMAL_DEGRADE_CHANCE = 0.5;
    private static final double DAMAGED_DEGRADE_CHANCE = 0.7;
    private static final double CRACKED_DEGRADE_CHANCE = 0.9;

    private static final double NORMAL_BASE_DETONATION_CHARGE = 128.0;
    private static final double NORMAL_ACCUMULATED_CHARGE_DISCOUNT = 0.5;
    private static final double DAMAGED_BASE_DETONATION_CHARGE = 96.0;
    private static final double DAMAGED_ACCUMULATED_CHARGE_DISCOUNT = 0.1;
    private static final double CRACKED_DETONATION_CHARGE = 32.0;

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
            this.detonateTickCountdown = DETONATION_DELAY_TICKS - level.getRandom().nextInt(1, 7);
        }
    }

    private static double calculateCharge(double radius, double distance) {
        if (radius <= 0.0) {
            return 0.0;
        }
        double effectiveRadius = Math.max(radius - BLOCK_CENTER_DISTANCE_OFFSET, MIN_EFFECTIVE_RADIUS_AFTER_OFFSET);
        double effectiveDistance = Math.max(distance - BLOCK_CENTER_DISTANCE_OFFSET, 0.0);
        double normalizedDistance = Math.clamp(
            effectiveDistance / effectiveRadius,
            0.0,
            MAX_NORMALIZED_DISTANCE
        );
        double proximity = MAX_PROXIMITY - normalizedDistance;
        return CHARGE_PER_RADIUS_AT_FALLOFF_EDGE
            * radius
            * Math.pow(DISTANCE_FALLOFF_EXPONENT_BASE, proximity);
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
            6,
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
            case NORMAL -> NORMAL_DEGRADE_CHANCE * (1 + (level.getRandom().nextDouble() - 0.5) / 2);
            case DAMAGED -> DAMAGED_DEGRADE_CHANCE;
            case CRACKED -> CRACKED_DEGRADE_CHANCE;
        };
    }

    private double getDegradeChargeAmount() {
        if (!(getBlockState().getBlock() instanceof BlastCrystalBlock block)) {
            return 0.0;
        }
        return switch (block.getStage()) {
            case NORMAL -> NORMAL_BASE_DETONATION_CHARGE - accumulatedCharge * NORMAL_ACCUMULATED_CHARGE_DISCOUNT;
            case DAMAGED -> DAMAGED_BASE_DETONATION_CHARGE - accumulatedCharge * DAMAGED_ACCUMULATED_CHARGE_DISCOUNT;
            case CRACKED -> CRACKED_DETONATION_CHARGE;
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
            detonateTickCountdown - DETONATION_TRIGGER_COUNTDOWN,
            0,
            Integer.MAX_VALUE
        );
        if (detonateTickCountdown == DETONATION_TRIGGER_COUNTDOWN) {
            detonate(getServerLevel(), getBlockPos());
        }
        if (this.accumulatedCharge < MIN_CHARGE_BEFORE_DECAY) {
            if (this.accumulatedCharge != 0.0) {
                this.accumulatedCharge = 0.0;
            }
            return;
        }
        this.accumulatedCharge *= ACCUMULATED_CHARGE_DECAY_MULTIPLIER;
        if (this.accumulatedCharge < MIN_CHARGE_BEFORE_DECAY) {
            this.accumulatedCharge = 0.0;
        }
    }
}
