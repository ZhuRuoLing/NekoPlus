package icu.takeneko.nekoplus.block.tile;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import icu.takeneko.nekoplus.client.renderer.animation.TardisAnimationController;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;

public class TardisBlockEntity extends NPSynedBlockEntity implements GeoAnimatable {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public TardisBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @DescSynced
    @Persisted
    @Getter
    private boolean animating = false;

    public void onClick() {
        animating = !animating;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(TardisAnimationController.createForAnimation("rotating", TardisAnimationController.ROTATING));
        controllers.add(TardisAnimationController.createForAnimation("waving", TardisAnimationController.WAVING));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }
}
