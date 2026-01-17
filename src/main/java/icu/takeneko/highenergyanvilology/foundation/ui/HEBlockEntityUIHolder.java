package icu.takeneko.highenergyanvilology.foundation.ui;

import com.lowdragmc.lowdraglib2.gui.holder.IModularUIHolder;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface HEBlockEntityUIHolder extends IModularUIHolder {
    default BlockEntity self() {
        return (BlockEntity) this;
    }
}
