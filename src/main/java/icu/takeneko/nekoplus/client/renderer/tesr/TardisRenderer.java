package icu.takeneko.nekoplus.client.renderer.tesr;

import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.GeoBlockRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.TardisBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;

import java.util.Map;

public class TardisRenderer extends GeoBlockRenderer<TardisBlockEntity, TardisRenderer.TardisEngineRenderState> {

    public TardisRenderer(
        BlockEntityRendererProvider.Context context
    ) {
        super(context, NPBlockEntities.TARDIS.get());
    }

    @Override
    protected Direction getBlockStateDirection(TardisBlockEntity blockEntity) {
        return super.getBlockStateDirection(blockEntity).getOpposite();
    }

    @Override
    public AABB getRenderBoundingBox(TardisBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos().above()).inflate(1.5);
    }

    public static class TardisEngineRenderState extends BlockEntityRenderState implements GeoRenderState {

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return Map.of();
        }
    }
}

