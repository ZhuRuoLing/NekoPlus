package icu.takeneko.nekoplus.mixin.anvilcraft;

import dev.dubhe.anvilcraft.block.entity.BaseLaserBlockEntity;
import dev.dubhe.anvilcraft.block.entity.StampingPlatformBlockEntity;
import icu.takeneko.nekoplus.internal.LaserRendererInternals;
import icu.takeneko.nekoplus.internal.StampingPlatformsInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashSet;
import java.util.Set;

@Mixin(StampingPlatformBlockEntity.class)
public abstract class StampingPlatformBlockEntityMixin extends BlockEntity implements StampingPlatformsInternals.LaserTarget {
    @Unique
    private final Set<BlockPos> nekoplus$emitters = new HashSet<>();

    protected StampingPlatformBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void nekoplus$setEmitter(BlockPos emitter, boolean active) {
        if (active) nekoplus$emitters.add(emitter.immutable());
        else nekoplus$emitters.remove(emitter);
        nekoplus$tick();
    }

    @Override
    public void nekoplus$tick() {
        Level world = getLevel();
        if (world == null || world.isClientSide()) return;
        nekoplus$emitters.removeIf(pos -> !(world.getBlockEntity(pos) instanceof BaseLaserBlockEntity laser)
            || laser.getLaserLevel() < 64
            || !(laser instanceof LaserRendererInternals.Extension extension)
            || !extension.targets(getBlockPos()));
        BlockState state = getBlockState();
        boolean active = !nekoplus$emitters.isEmpty();
        if (state.hasProperty(StampingPlatformsInternals.LASER_TARGETED)
            && state.getValue(StampingPlatformsInternals.LASER_TARGETED) != active) {
            world.setBlockAndUpdate(getBlockPos(), state.setValue(StampingPlatformsInternals.LASER_TARGETED, active));
        }
    }
}
