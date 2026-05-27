package icu.takeneko.nekoplus.all;

import com.mojang.brigadier.CommandDispatcher;
import icu.takeneko.nekoplus.foundation.block.tile.NPInspectionSupported;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

@EventBusSubscriber
public class NPCommands {
    @SubscribeEvent
    public static void on(RegisterClientCommandsEvent e) {
        CommandDispatcher<CommandSourceStack> dispatcher = e.getDispatcher();
        dispatcher.register(
            Commands.literal("npClientInspection").
                then(
                    Commands.argument("pos", BlockPosArgument.blockPos()).
                        executes(context -> {
                            BlockPos pos = context.getArgument("pos", WorldCoordinates.class).getBlockPos(context.getSource());
                            Level level = context.getSource().getUnsidedLevel();
                            if (level.getBlockEntity(pos) instanceof NPInspectionSupported inspectionSupported) {
                                inspectionSupported.echo(context.getSource());
                            }
                            return 0;
                        })
                )
        );
    }
}
