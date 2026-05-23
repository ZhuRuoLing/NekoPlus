package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.event.AnvilEvent;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import dev.dubhe.anvilcraft.util.ItemResourceHelper;
import icu.takeneko.nekoplus.all.NPSoundEvents;
import icu.takeneko.nekoplus.block.ParticleStabilizerBlock;
import icu.takeneko.nekoplus.content.tile.logic.stabilizer.ParticleStabilizerLogic;
import icu.takeneko.nekoplus.content.tile.logic.stabilizer.ParticleStabilizerLogicHost;
import icu.takeneko.nekoplus.client.sound.LoopingBlockSoundInstance;
import icu.takeneko.nekoplus.foundation.block.tile.BlockCollisionEventReceiver;
import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.foundation.block.tile.Overclockable;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.inventory.NPItemHandler;
import icu.takeneko.nekoplus.foundation.inventory.NPItemHandlerSlice;
import icu.takeneko.nekoplus.foundation.inventory.NPItemHandlerOwner;
import icu.takeneko.nekoplus.foundation.ui.NPUI;
import icu.takeneko.nekoplus.recipe.AirCondensingRecipe;
import icu.takeneko.nekoplus.ui.ParticleStabilizerUI;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;


@SuppressWarnings("DataFlowIssue")
public class ParticleStabilizerBlockEntity
    extends NPSynedBlockEntity
    implements NPOverclockablePowerConsumer, NPUIBlock.Provider, Tickable, BlockCollisionEventReceiver, NPItemHandlerOwner, Overclockable, ParticleStabilizerLogicHost {

    public static final int MACHINE_COOLDOWN = 30 * 20;

    private static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(ParticleStabilizerBlockEntity.class);

    @Persisted
    @Getter
    private final NPItemHandler itemHandler = new NPItemHandler(5, this);

    @Getter
    @Setter
    private PowerGrid grid;

    @Persisted
    @DescSynced
    @Getter
    @RequireRerender
    private State state = State.WORKING;

    @Persisted
    @Getter
    private int countdown = MACHINE_COOLDOWN;

    @DescSynced
    @RequireRerender
    @Getter
    @Setter
    private boolean isOverload = false;

    @Setter
    private int efficiency = 1;

    @Getter
    @Setter
    @Persisted
    private int progress;

    @Getter
    @Setter
    @DescSynced
    private int maxProgress;

    @Getter
    @Setter
    @Nullable
    private AirCondensingRecipe currentRecipe;

    @Getter
    @DescSynced
    @RequireRerender
    private boolean isWorking;

    @Getter
    @DescSynced
    private boolean isOverclockEnabled = false;

    // @OnlyIn(Dist.CLIENT)
    private LoopingBlockSoundInstance soundInstance;

    private final ParticleStabilizerLogic logic = new ParticleStabilizerLogic.Impl();

    public ParticleStabilizerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
    }

    @Override
    public void gridTick() {
    }

    @Override
    public void tick() {
        flushState();
        BlockState newState = getBlockState().setValue(ParticleStabilizerBlock.OVERLOAD, isOverload);
        newState = newState.setValue(ParticleStabilizerBlock.COOLING, state == State.COOLING);
        level.setBlock(getPos(), newState, Block.UPDATE_ALL);
        if (this.isOverload) {
            if (this.progress != 0) {
                BlockPos pos = this.getPos();
                level.playSound(null, pos.getX(), pos.getY(), pos.getZ(), NPSoundEvents.INTERRUPT.get(), SoundSource.BLOCKS, 0.5f, 1);
                this.progress = 0;
            }
            return;
        }
        if (countdown > 0) {
            this.countdown = Math.max(countdown - efficiency, 0);
            updateState(State.COOLING);
        } else {
            updateState(State.WORKING);
        }
        logic.tick(this);
        isWorking = this.currentRecipe != null;
    }


    @Override
    // @OnlyIn(Dist.CLIENT)
    public void scheduleRenderUpdate() {
        super.scheduleRenderUpdate();
        if ((isWorking || this.state == State.COOLING) && !isOverload) {
            if (soundInstance != null) {
                soundInstance.stopNow();
            }
            soundInstance = new LoopingBlockSoundInstance(
                NPSoundEvents.PARTICLE_STABILIZER_WORKING.get(),
                SoundSource.BLOCKS,
                this
            );

            Minecraft.getInstance().getSoundManager().play(soundInstance);
        } else {
            if (soundInstance != null) {
                soundInstance.stopNow();
                soundInstance = null;
            }
        }
    }

    private void updateState(State value) {
        if (this.state != value) {
            this.state = value;
        }
    }

    @Override
    public ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    @Override
    public @Nullable Level getCurrentLevel() {
        return level;
    }

    @Override
    public BlockPos getPos() {
        return getBlockPos();
    }

    @Override
    public boolean acceptCollision(FallingBlockEntity entity, double speed, AnvilEvent.CollisionBlock event) {
        return logic.handleCollision(this, entity, speed, event);
    }

    @Override
    public void onContentChanged() {
        setChanged();
    }

    @Override
    public boolean isItemValid(int slot, ItemResource stack) {
        if (slot == 0) {
            return logic.isValidTriggerItem(stack);
        }
        return true;
    }

    @Override
    public int getBaseOverclockCost() {
        return 32;
    }

    @Override
    public int maxOverclockRatio() {
        return 100;
    }

    @Override
    public int currentOverclockRatio() {
        return efficiency;
    }

    @Override
    public boolean isOverclockable() {
        return this.state == State.COOLING && isOverclockEnabled;
    }

    @Override
    public int getBaseInputPower() {
        return 16;
    }

    @Override
    public int getOverclockedInputPower() {
        return 32 * efficiency;
    }

    @Override
    public ItemResource getTriggerResource() {
        return itemHandler.getResource(0);
    }

    @Override
    public ItemStack tryConsumeTriggerItem() {
        try (Transaction transaction = Transaction.openRoot()){
            int extracted = itemHandler.slice(0, 1).extract(getTriggerResource(), 1, transaction);
            if (extracted > 0) {
                transaction.commit();
                return getTriggerResource().toStack(extracted);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NPItemHandlerSlice getOutputItemHandler() {
        return itemHandler.slice(1, 5);
    }

    @Override
    public boolean hasValidWorkingState() {
        return !this.isOverload && this.state == ParticleStabilizerBlockEntity.State.WORKING;
    }

    @Override
    public void resetCooldown() {
        this.countdown = ParticleStabilizerBlockEntity.MACHINE_COOLDOWN;
    }

    @Override
    public void resetState() {
        this.state = ParticleStabilizerBlockEntity.State.COOLING;
    }

    @Override
    public ModularUI getModularUI(BlockUIMenuType.BlockUIHolder holder) {
        return NPUI.of(new ParticleStabilizerUI(this), holder);
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        super.preRemoveSideEffects(pos, state);
        Containers.dropContents(level, pos, itemHandler.getStacks());
    }

    public void toggleOverclock() {
        isOverclockEnabled = !isOverclockEnabled;
    }

    public enum State {
        COOLING, WORKING
    }
}
