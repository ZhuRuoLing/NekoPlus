package icu.takeneko.nekoplus.mixin.anvilcraft;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.dubhe.anvilcraft.api.behavior.SetTreeNode;
import dev.dubhe.anvilcraft.api.behavior.TreeNode;
import dev.dubhe.anvilcraft.event.giantanvil.shock.GiantAnvilShockEventListener;
import dev.dubhe.anvilcraft.event.giantanvil.shock.ShockContext;
import icu.takeneko.nekoplus.all.NPBlocks;
import icu.takeneko.nekoplus.internal.GiantAnvilShockInternals;
import org.apache.commons.lang3.ArrayUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GiantAnvilShockEventListener.class)
public class GiantAnvilShockEventListenerMixin {

    @Shadow
    @Final
    public static String DESTROY_TYPE;

    @WrapOperation(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Ldev/dubhe/anvilcraft/api/behavior/TreeNode;multiple([Ldev/dubhe/anvilcraft/api/behavior/TreeNode;)Ldev/dubhe/anvilcraft/api/behavior/SetTreeNode;",
            ordinal = 1
        )
    )
    private static <T extends ShockContext> SetTreeNode<T> addAttachment(TreeNode<T>[] children, Operation<SetTreeNode<T>> original){
        TreeNode<? extends ShockContext>[] treeNodes = ArrayUtils.add(
            children,
            TreeNode.<ShockContext>predicatedExecutable(it -> it.unwrap().testCorner(NPBlocks.TITANIUM_ALLOY_BLOCK))
                .executes(it -> it.putAttachment(DESTROY_TYPE, GiantAnvilShockInternals.LIGHTWEIGHT_BLOCKS))
        );
        return original.call((Object) treeNodes);
    }
}
