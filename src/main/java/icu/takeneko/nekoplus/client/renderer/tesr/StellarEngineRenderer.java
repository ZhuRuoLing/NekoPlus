package icu.takeneko.nekoplus.client.renderer.tesr;

import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.GeoBlockRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import icu.takeneko.nekoplus.all.NPBlockEntities;
import icu.takeneko.nekoplus.block.tile.StellarEngineBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.core.Direction;

import java.util.Map;

public class StellarEngineRenderer extends GeoBlockRenderer<StellarEngineBlockEntity, StellarEngineRenderer.StellarEngineRenderState> {

    public StellarEngineRenderer(
        BlockEntityRendererProvider.Context context
    ) {
        super(context, NPBlockEntities.STELLAR_ENGINE.get());
    }

    @Override
    protected Direction getBlockStateDirection(StellarEngineBlockEntity blockEntity) {
        return super.getBlockStateDirection(blockEntity).getOpposite();
    }

    public static class StellarEngineRenderState extends BlockEntityRenderState implements GeoRenderState {

        @Override
        public Map<DataTicket<?>, Object> getDataMap() {
            return Map.of();
        }
    }
}
