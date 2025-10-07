package world.landfall.infection.command;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.commands.arguments.coordinates.WorldCoordinates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import world.landfall.infection.InfectionMod;

import java.util.UUID;

@EventBusSubscriber(modid = InfectionMod.MODID)
public class FakePlayerCommand {

    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        // This didn't work for a few reasons, mainly that FakePlayers aren't really meant to act like a real entity, just to simulate actions
//        var dispatcher = event.getDispatcher();
//        dispatcher.register(Commands.literal("fakeplayer")
//                .then(Commands.argument("pos", BlockPosArgument.blockPos())
//                        .executes(FakePlayerCommand::fakeplayer)
//                )
//        );
    }

    private static int fakeplayer(CommandContext<CommandSourceStack> ctx) {
        var source = ctx.getSource();
        var position = ctx.getArgument("pos", WorldCoordinates.class).getPosition(source);

        //source.getLevel().addNewPlayer(FakePlayerFactory.get(source.getLevel(), UUIDUtil.createOfflineProfile("Gtrainer23")));
        var fakePlayer = FakePlayerFactory.get(source.getLevel(),new GameProfile(UUID.randomUUID(), "Gtrainer23"));
        source.getLevel().addFreshEntity(fakePlayer);
        fakePlayer.teleportTo(position.x, position.y, position.z);

        //source.getLevel().addNewPlayer(new FakePlayer(source.getLevel(),UUIDUtil.createOfflineProfile("Gtrainer23")));
        return 0;
    }
}
