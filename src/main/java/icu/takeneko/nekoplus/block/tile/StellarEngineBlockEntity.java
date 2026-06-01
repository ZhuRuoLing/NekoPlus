package icu.takeneko.nekoplus.block.tile;

import com.geckolib.animatable.GeoAnimatable;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.util.GeckoLibUtil;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.power.IPowerProducer;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.nekoplus.client.renderer.animation.StellarEngineAnimationController;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import icu.takeneko.nekoplus.foundation.grid.OffCenterPowerComponent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

public class StellarEngineBlockEntity extends NPSynedBlockEntity implements IPowerProducer, OffCenterPowerComponent, GeoAnimatable, Tickable {
    private static final int OPEN_ANIMATION_TICKS = 160;
    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(StellarEngineBlockEntity.class);

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
    private EngineAnimationState engineAnimationState = EngineAnimationState.OPENING;

    @Persisted
    private int openingTicks = 0;

    public void setOpen(boolean open) {
        if (!open || engineAnimationState != EngineAnimationState.CLOSED) return;
        setEngineAnimationState(EngineAnimationState.OPENING);
        openingTicks = 0;
        setChanged();
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide()) return;
        if (engineAnimationState != EngineAnimationState.OPENING) return;
        openingTicks++;
        if (openingTicks >= OPEN_ANIMATION_TICKS) {
            setEngineAnimationState(EngineAnimationState.OPENED);
            setChanged();
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
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
        controllers.add(StellarEngineAnimationController.openCloseController());
        controllers.add(StellarEngineAnimationController.ringAnimation());
        controllers.add(StellarEngineAnimationController.sunAnimation());
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
