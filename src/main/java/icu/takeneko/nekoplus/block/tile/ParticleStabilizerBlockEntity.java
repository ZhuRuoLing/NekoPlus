package icu.takeneko.nekoplus.block.tile;

import com.lowdragmc.lowdraglib2.gui.factory.BlockUIMenuType;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib2.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib2.syncdata.annotation.RequireRerender;
import com.lowdragmc.lowdraglib2.syncdata.field.ManagedFieldHolder;
import dev.dubhe.anvilcraft.api.power.PowerGrid;
import icu.takeneko.nekoplus.all.NPSoundEvents;
import icu.takeneko.nekoplus.block.ParticleStabilizerBlock;
import icu.takeneko.nekoplus.content.tile.logic.stabilizer.ParticleStabilizerLogic;
import icu.takeneko.nekoplus.content.tile.logic.stabilizer.ParticleStabilizerLogicHost;
import icu.takeneko.nekoplus.client.sound.LoopingBlockSoundInstance;
import icu.takeneko.nekoplus.config.NPConfig;
import icu.takeneko.nekoplus.foundation.block.tile.NPOverclockablePowerConsumer;
import icu.takeneko.nekoplus.foundation.block.tile.NPSynedBlockEntity;
import icu.takeneko.nekoplus.foundation.block.tile.NPUIBlock;
import icu.takeneko.nekoplus.foundation.block.tile.Overclockable;
import icu.takeneko.nekoplus.foundation.Tickable;
import icu.takeneko.nekoplus.foundation.client.sound.LoopingSoundController;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import org.jspecify.annotations.Nullable;


@SuppressWarnings("DataFlowIssue")
public class ParticleStabilizerBlockEntity
    extends NPSynedBlockEntity
    implements NPOverclockablePowerConsumer, NPUIBlock.Provider, Tickable, NPItemHandlerOwner, Overclockable, ParticleStabilizerLogicHost {

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

    @Getter
    @Setter
    private LoopingSoundController soundController;

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
                level.playSound(
                    null,
                    pos.getX(),
                    pos.getY(),
                    pos.getZ(),
                    NPSoundEvents.INTERRUPT.get(),
                    SoundSource.BLOCKS,
                    0.5f,
                    1
                );
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
        ClientSupport.INSTANCE.handleSoundUpdates(this);
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
        return NPConfig.PARTICLE_STABILIZER_BASE_OVERCLOCK_COST.getAsInt();
    }

    @Override
    public int maxOverclockRatio() {
        return NPConfig.PARTICLE_STABILIZER_MAX_OVERCLOCK_RATIO.getAsInt();
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
        try (Transaction transaction = Transaction.openRoot()) {
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

    private static class ClientSupport {
        public static final ClientSupport INSTANCE = new ClientSupport();

        public void handleSoundUpdates(ParticleStabilizerBlockEntity blockEntity) {
            if ((blockEntity.isWorking || blockEntity.state == State.COOLING) && !blockEntity.isOverload) {
                if (blockEntity.soundController != null) {
                    blockEntity.soundController.stopNow();
                }

                blockEntity.soundController = new LoopingSoundController() {
                    private boolean shouldPlay = true;

                    @Override
                    public boolean shouldSoundStop() {
                        return !shouldPlay;
                    }

                    @Override
                    public void stopNow() {
                        shouldPlay = false;
                    }
                };

                LoopingBlockSoundInstance<?> soundInstance = new LoopingBlockSoundInstance<>(
                    NPSoundEvents.PARTICLE_STABILIZER_WORKING.get(),
                    SoundSource.BLOCKS,
                    blockEntity,
                    blockEntity.soundController
                );

                Minecraft.getInstance().getSoundManager().play(soundInstance);
            } else {
                if (blockEntity.soundController != null) {
                    blockEntity.soundController.stopNow();
                    blockEntity.soundController = null;
                }
            }
        }
    }
}
