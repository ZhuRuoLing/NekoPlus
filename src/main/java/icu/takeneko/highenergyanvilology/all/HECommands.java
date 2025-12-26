package icu.takeneko.highenergyanvilology.all;

import com.mojang.brigadier.CommandDispatcher;
import icu.takeneko.highenergyanvilology.foundation.block.entity.HEInspectionSupported;
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
public class HECommands {
    @SubscribeEvent
    public static void on(RegisterClientCommandsEvent e) {
        CommandDispatcher<CommandSourceStack> dispatcher = e.getDispatcher();
        dispatcher.register(
            Commands.literal("heClientInspection").
                then(
                    Commands.argument("pos", BlockPosArgument.blockPos()).
                        executes(context -> {
                            BlockPos pos = context.getArgument("pos", WorldCoordinates.class).getBlockPos(context.getSource());
                            Level level = context.getSource().getUnsidedLevel();
                            if (level.getBlockEntity(pos) instanceof HEInspectionSupported inspectionSupported) {
                                inspectionSupported.echo(context.getSource());
                            }
                            return 0;
                        })
                )
        );
    }
}
