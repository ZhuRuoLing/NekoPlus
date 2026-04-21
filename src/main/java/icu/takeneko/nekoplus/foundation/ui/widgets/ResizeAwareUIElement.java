package icu.takeneko.nekoplus.foundation.ui.widgets;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import lombok.Getter;
import lombok.Setter;

public class ResizeAwareUIElement extends UIElement {
    @Setter
    @Getter
    private boolean isResizing;

}
