package icu.takeneko.nekoplus.mixin.anvilcraft;

import dev.dubhe.anvilcraft.block.entity.heatable.HeatableBlockEntity;
import icu.takeneko.nekoplus.internal.HeatableBlockEntityInternals;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeatableBlockEntity.class)
public abstract class HeatableBlockEntityMixin extends BlockEntity implements HeatableBlockEntityInternals.Extension {

    @Unique
    private int np$coolingInterval = 1;
    @Unique
    private int np$coolingCooldown = 1;
    @Unique
    private boolean np$insulationInitialized;

    protected HeatableBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void setInsulationCount(int count) {
        int interval = 1 << Math.clamp(count, 0, 4);
        if (!this.np$insulationInitialized || this.np$coolingInterval != interval) {
            this.np$coolingInterval = interval;
            this.np$coolingCooldown = interval;
        }
        this.np$insulationInitialized = true;
    }

    @Inject(method = "onLoad", at = @At("TAIL"))
    private void initializeInsulation(CallbackInfo ci) {
        Level level = this.getLevel();
        if (level != null && !level.isClientSide()) {
            this.updateInsulation(level, this.getBlockPos());
        }
    }

    @Inject(method = "addDuration(I)V", at = @At("HEAD"), cancellable = true)
    private void slowCooling(int second, CallbackInfo ci) {
        if (second != -1) return;
        Level level = this.getLevel();
        if (level == null || level.isClientSide()) return;
        if (!this.np$insulationInitialized) {
            this.updateInsulation(level, this.getBlockPos());
        }
        if (--this.np$coolingCooldown > 0) {
            ci.cancel();
            return;
        }
        this.np$coolingCooldown = this.np$coolingInterval;
    }
}
