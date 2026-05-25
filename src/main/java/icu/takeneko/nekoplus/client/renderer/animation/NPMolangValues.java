package icu.takeneko.nekoplus.client.renderer.animation;

import com.geckolib.loading.math.MolangQueries;
import icu.takeneko.nekoplus.block.tile.StellarEngineBlockEntity;
import icu.takeneko.nekoplus.block.tile.TardisBlockEntity;

public class NPMolangValues {
    public static void register(){
        MolangQueries.<StellarEngineBlockEntity>setActorVariable("variable.rotating_speed", _ -> 1);

        MolangQueries.<TardisBlockEntity>setActorVariable("tardis.rotating_speed", _ -> 1);
        MolangQueries.<TardisBlockEntity>setActorVariable("tardis.floating.delta_height", _ -> 4);
        MolangQueries.<TardisBlockEntity>setActorVariable("tardis.floating.min_height", _ -> 4);
        MolangQueries.<TardisBlockEntity>setActorVariable("tardis.floating.speed", _ -> 0.3f);

        MolangQueries.<TardisBlockEntity>setActorVariable("tardis.waving.delta_deg", _ -> 10);
        MolangQueries.<TardisBlockEntity>setActorVariable("tardis.waving.speed", _ -> 0.25f);

        MolangQueries.<TardisBlockEntity>setActorVariable("tardis.flashing.speed", _ -> 0.75f);
        MolangQueries.<TardisBlockEntity>setActorVariable("tardis.flashing.size", _ -> 1.5f);

        MolangQueries.<TardisBlockEntity>setActorVariable("action.floating", a -> a.animatable().isAnimating() ? 1 : 0);
        MolangQueries.<TardisBlockEntity>setActorVariable("action.rotating", a -> a.animatable().isAnimating() ? 1 : 0);
        MolangQueries.<TardisBlockEntity>setActorVariable("action.waving", a -> a.animatable().isAnimating() ? 1 : 0);

    }
}
