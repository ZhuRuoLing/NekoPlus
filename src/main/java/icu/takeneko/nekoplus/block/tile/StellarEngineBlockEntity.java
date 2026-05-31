package icu.takeneko.nekoplus.block.tile;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animatable.stateless.StatelessAnimationController;
import com.geckolib.util.GeckoLibUtil;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import dev.dubhe.anvilcraft.api.power.IPowerProducer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.nekoplus.client.renderer.animation.StellarEngineAnimationController;
import icu.takeneko.nekoplus.foundation.grid.OffCenterPowerComponent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class StellarEngineBlockEntity extends BlockEntity implements IPowerProducer, OffCenterPowerComponent, GeoAnimatable {

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    private final AABB shape;
    @Getter
    @Setter
    private PowerGrid grid;

    public StellarEngineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        AABB inflated = new AABB(pos).inflate(3.5, 0, 3.5);
        this.shape = inflated.setMaxY(inflated.maxY + 8);
    }

    @Getter
    @Setter
    @Persisted
    @DescSynced
    private EngineAnimationState engineAnimationState = EngineAnimationState.CLOSED;

    public void setOpen(boolean open) {
        if (engineAnimationState == EngineAnimationState.CLOSED) {
            engineAnimationState = EngineAnimationState.OPENING;
        }
    }

    @Override
    public AABB getShape() {
        return shape;
    }

    @Override
    public int getOutputPower() {
        return 1024 * 1000;
    }

    @Override
    public @Nullable Level getCurrentLevel() {
        return getLevel();
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
//        controllers.add(StellarEngineAnimationController.openCloseController());
//        controllers.add(StellarEngineAnimationController.ringAnimation());
//        controllers.add(StellarEngineAnimationController.sunAnimation());
        controllers.add(StellarEngineAnimationController.createStateless(
            "ring",
            StellarEngineAnimationController.ANIM_RING
        ));
        controllers.add(StellarEngineAnimationController.createStateless(
            "sun",
            StellarEngineAnimationController.ANIM_SUN
        ));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    public enum EngineAnimationState {
        CLOSED,
        OPENING,
        OPENED
    }
}
