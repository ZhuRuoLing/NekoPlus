package icu.takeneko.nekoplus.foundation.ui;

import com.lowdragmc.lowdraglib2.gui.holder.IModularUIHolder;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface NPBlockEntityUIHolder extends IModularUIHolder {
    default BlockEntity self() {
        return (BlockEntity) this;
    }
}
