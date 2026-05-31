package icu.takeneko.nekoplus.util;

import com.lowdragmc.lowdraglib2.gui.texture.IGuiTexture;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvent;
import com.lowdragmc.lowdraglib2.gui.ui.event.UIEvents;
import com.lowdragmc.lowdraglib2.gui.ui.rendering.GUIContext;
import com.lowdragmc.lowdraglib2.gui.util.WindowDragHelper;
import dev.anvilcraft.lib.v2.util.DistExecutor;
import icu.takeneko.nekoplus.foundation.ui.widgets.ResizeAwareUIElement;
import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import org.jspecify.annotations.Nullable;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class NPUIUtils {

    private static long CURSOR_NORMAL;
    private static long CURSOR_RESIZE_ALL;
    private static long CURSOR_RESIZE_H;
    private static long CURSOR_RESIZE_V;
    private static long CURSOR_RESIZE_TLBR;
    private static long CURSOR_RESIZE_TRBL;

    private static final MethodHandle MT_MUI_CSAL;

    static {
        MT_MUI_CSAL = getMT_ModularUI_calculateStyleAndLayout();
    }

    @SneakyThrows
    private static MethodHandle getMT_ModularUI_calculateStyleAndLayout() {
        Method method = ModularUI.class.getDeclaredMethod("calculateStyleAndLayout");
        method.setAccessible(true);
        return MethodHandles.lookup().unreflect(method);
    }

    public static void clientSetup() {
        CURSOR_NORMAL = GLFW.glfwCreateStandardCursor(GLFW.GLFW_ARROW_CURSOR);
        CURSOR_RESIZE_ALL = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_ALL_CURSOR);
        CURSOR_RESIZE_H = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_EW_CURSOR);
        CURSOR_RESIZE_V = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NS_CURSOR);

        CURSOR_RESIZE_TLBR = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NWSE_CURSOR);
        CURSOR_RESIZE_TRBL = GLFW.glfwCreateStandardCursor(GLFW.GLFW_RESIZE_NESW_CURSOR);

        if (CURSOR_RESIZE_TLBR == 0) {
            CURSOR_RESIZE_TLBR = CURSOR_NORMAL;
        }
        if (CURSOR_RESIZE_TRBL == 0) {
            CURSOR_RESIZE_TRBL = CURSOR_NORMAL;
        }
    }

    public static void setDragMove(UIElement element, UIElement target, @Nullable Predicate<UIEvent> movePredicate, @Nullable Consumer<UIEvent> onFinish) {
        element.addEventListener(UIEvents.MOUSE_DOWN, e -> {
            if ((movePredicate == null || movePredicate.test(e))) {
                setCursor(CURSOR_RESIZE_ALL);
                element.startDrag(new WindowDragHelper.DragMove(target.getLayoutX(), target.getLayoutY()), IGuiTexture.EMPTY);
                e.stopPropagation();
            }
        });
        element.addEventListener(UIEvents.DRAG_SOURCE_UPDATE, e -> {
            if (e.dragHandler.draggingObject instanceof WindowDragHelper.DragMove(var sx, var sy)) {
                var normalPosOffset = element.getLocalMouseNormal(e.x - e.dragStartX, e.y - e.dragStartY);
                target.getLayout()
                    .left(sx + normalPosOffset.x)
                    .top(sy + normalPosOffset.y);
            }
        });
        element.addEventListener(UIEvents.DRAG_END, it -> {
            setCursor(CURSOR_NORMAL);
            if (onFinish != null) {
                onFinish.accept(it);
            }
        });
    }

    public static void setBorderResize(
        UIElement element,
        UIElement target,
        float border,
        Vector2f minSize,
        Vector2f maxSize,
        @Nullable Predicate<UIEvent> resizePredicate,
        @Nullable BiPredicate<UIEvent, WindowDragHelper.DragResize> dragResizePredicate,
        @Nullable Consumer<UIEvent> onFinish
    ) {
        element.addEventListener(UIEvents.MOUSE_DOWN, e -> {
            if (resizePredicate != null && !resizePredicate.test(e)) return;
            var handle = detectResizeHandle(element, e.x, e.y, border);
            if (handle != null) {
                setCursor(mapResizeHandle(handle));
                element.startDrag(new WindowDragHelper.DragResize(
                    target.getLayoutX(), target.getLayoutY(),
                    target.getSizeWidth(), target.getSizeHeight(), handle), IGuiTexture.EMPTY);
                e.stopPropagation();
            }
        });

        element.addEventListener(UIEvents.DRAG_SOURCE_UPDATE, e -> {
            if (!(e.dragHandler.draggingObject instanceof WindowDragHelper.DragResize(
                float startX, float startY, float startW, float startH, WindowDragHelper.ResizeHandle handle
            ))) return;

            if (dragResizePredicate != null && !dragResizePredicate.test(e, (WindowDragHelper.DragResize) e.dragHandler.draggingObject))
                return;
            var d = element.getLocalMouseNormal(e.x - e.dragStartX, e.y - e.dragStartY);
            float dx = d.x;
            float dy = d.y;
            float x = startX;
            float y = startY;
            float w = startW;
            float h = startH;

            float minW = minSize.x, maxW = maxSize.x;
            float minH = minSize.y, maxH = maxSize.y;

            switch (handle) {
                case LEFT -> {
                    float clampedNewW = Math.min(maxW, Math.max(minW, startW - dx));
                    float dxApplied = startW - clampedNewW;     // 实际生效的 dx
                    x = startX + dxApplied;
                    w = clampedNewW;
                }
                case RIGHT -> {
                    w = Math.min(maxW, Math.max(minW, startW + dx));
                    x = startX;
                }
                case TOP -> {
                    float clampedNewH = Math.min(maxH, Math.max(minH, startH - dy));
                    float dyApplied = startH - clampedNewH;
                    y = startY + dyApplied;
                    h = clampedNewH;
                }
                case BOTTOM -> {
                    h = Math.min(maxH, Math.max(minH, startH + dy));
                    y = startY;
                }
                case TOP_LEFT -> {
                    float clampedNewW = Math.min(maxW, Math.max(minW, startW - dx));
                    float dxApplied = startW - clampedNewW;
                    x = startX + dxApplied;
                    w = clampedNewW;

                    float clampedNewH = Math.min(maxH, Math.max(minH, startH - dy));
                    float dyApplied = startH - clampedNewH;
                    y = startY + dyApplied;
                    h = clampedNewH;
                }
                case TOP_RIGHT -> {
                    w = Math.min(maxW, Math.max(minW, startW + dx));
                    x = startX;

                    float clampedNewH = Math.min(maxH, Math.max(minH, startH - dy));
                    float dyApplied = startH - clampedNewH;
                    y = startY + dyApplied;
                    h = clampedNewH;
                }
                case BOTTOM_LEFT -> {
                    float clampedNewW = Math.min(maxW, Math.max(minW, startW - dx));
                    float dxApplied = startW - clampedNewW;
                    x = startX + dxApplied;
                    w = clampedNewW;

                    h = Math.min(maxH, Math.max(minH, startH + dy));
                    y = startY;
                }
                case BOTTOM_RIGHT -> {
                    w = Math.min(maxW, Math.max(minW, startW + dx));
                    h = Math.min(maxH, Math.max(minH, startH + dy));
                    x = startX;
                    y = startY;
                }
            }

            target.getLayout()
                .left(x)
                .top(y)
                .width(w)
                .height(h);
        });
        element.addEventListener(UIEvents.DRAG_END, e -> {
            setCursor(CURSOR_NORMAL);
            if (onFinish != null) {
                onFinish.accept(e);
            }
        });
    }

    private static void setCursor(long cursor) {
        DistExecutor.run(Dist.CLIENT, () -> () -> GLFW.glfwSetCursor(Minecraft.getInstance().getWindow().handle(), cursor));
    }

    public static long mapResizeHandle(WindowDragHelper.ResizeHandle handle) {
        return switch (handle) {
            case LEFT, RIGHT -> CURSOR_RESIZE_H;
            case TOP, BOTTOM -> CURSOR_RESIZE_V;
            case TOP_LEFT, BOTTOM_RIGHT -> CURSOR_RESIZE_TLBR;
            case TOP_RIGHT, BOTTOM_LEFT -> CURSOR_RESIZE_TRBL;
        };
    }


    public static WindowDragHelper.ResizeHandle detectResizeHandle(UIElement element, float mouseWorldX, float mouseWorldY, float padding) {
        var local = element.getLocalMouse(mouseWorldX, mouseWorldY).sub(element.getPositionX(), element.getPositionY());
        var mx = local.x;
        var my = local.y;

        float w = element.getSizeWidth();
        float h = element.getSizeHeight();

        boolean left = mx >= 0 && mx <= padding;
        boolean right = mx >= (w - padding) && mx <= w;
        boolean top = my >= 0 && my <= padding;
        boolean bottom = my >= (h - padding) && my <= h;

        if (left && top) return WindowDragHelper.ResizeHandle.TOP_LEFT;
        if (right && top) return WindowDragHelper.ResizeHandle.TOP_RIGHT;
        if (left && bottom) return WindowDragHelper.ResizeHandle.BOTTOM_LEFT;
        if (right && bottom) return WindowDragHelper.ResizeHandle.BOTTOM_RIGHT;

        if (left) return WindowDragHelper.ResizeHandle.LEFT;
        if (right) return WindowDragHelper.ResizeHandle.RIGHT;
        if (top) return WindowDragHelper.ResizeHandle.TOP;
        if (bottom) return WindowDragHelper.ResizeHandle.BOTTOM;

        return null;
    }

    @SneakyThrows
    public static void forceRelayout(ModularUI ui) {
        MT_MUI_CSAL.invoke(ui);
    }

    @SneakyThrows
    public static void forceRelayout(UIElement element) {
        MT_MUI_CSAL.invoke(element.getModularUI());
    }

    public static void drawResizeIcon(GUIContext guiContext, float padding, ResizeAwareUIElement... elements) {
        WindowDragHelper.ResizeHandle handle = null;
        for (ResizeAwareUIElement element : elements) {
            if (handle == null && (element.isSelfOrChildHover() || element.isResizing())) {
                handle = detectResizeHandle(element, guiContext.mouseX, guiContext.mouseY, padding);
            }
        }

        if (handle == null) {
            setCursor(CURSOR_NORMAL);
            return;
        }
        setCursor(mapResizeHandle(handle));
    }
}
