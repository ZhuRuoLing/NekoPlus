package icu.takeneko.highenergyanvilology.foundation.ui.client;

import com.lowdragmc.lowdraglib2.gui.ui.ModularUIContainerMenu;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUIContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class HEModularUIContainerScreen extends ModularUIContainerScreen {
    public HEModularUIContainerScreen(ModularUIContainerMenu container, Inventory inventory, Component title) {
        super(container, inventory, title);
    }

    @Override
    protected void renderSlot(GuiGraphics guiGraphics, Slot slot) {
    }
}
