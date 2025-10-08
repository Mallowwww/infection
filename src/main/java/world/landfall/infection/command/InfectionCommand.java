package world.landfall.infection.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.ArgumentUtils;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import world.landfall.infection.InfectionMod;
import world.landfall.infection.ModAttachments;
import world.landfall.infection.ModInfections;
import world.landfall.infection.api.Infection;
import world.landfall.infection.api.InfectionRegistry;

@EventBusSubscriber(modid = InfectionMod.MODID)
public class InfectionCommand {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        var dispatcher = event.getDispatcher();
        dispatcher.register(Commands.literal("infection")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("infect")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("infection", ResourceArgument.resource(event.getBuildContext(), InfectionRegistry.INFECTION_REGISTRY_KEY))
                                        .executes(InfectionCommand::infect)
                                )

                        )
                ).then(Commands.literal("get")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(InfectionCommand::get)
                        )
                ).then(Commands.literal("mutate")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(InfectionCommand::mutate)
                        )
                )
        );
    }
    private static int mutate(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var player = ctx.getArgument("player", EntitySelector.class).findSinglePlayer(ctx.getSource());
        if (!player.hasData(ModAttachments.ACTIVE_INFECTION)) {
            ctx.getSource().sendSystemMessage(Component.translatable("command.infection.mutate.fail"));
            return -1;
        }
        var infection = player.getData(ModAttachments.ACTIVE_INFECTION);
        var newInfection = infection.getType().createMutated(infection);
        newInfection.activate();
        player.setData(ModAttachments.ACTIVE_INFECTION, newInfection);
        ctx.getSource().sendSystemMessage(Component.translatable("command.infection.mutate.success",newInfection.type.toString()));
        return 1;
    }
    private static int get(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var player = ctx.getArgument("player", EntitySelector.class).findSinglePlayer(ctx.getSource());
        if (!player.hasData(ModAttachments.ACTIVE_INFECTION)) {
            ctx.getSource().sendSystemMessage(Component.translatable("command.infection.get.fail"));
            return -1;
        }
        var infection = player.getData(ModAttachments.ACTIVE_INFECTION);
        ctx.getSource().sendSystemMessage(Component.translatable("command.infection.get.success",infection.type.toString()));
        return 1;
    }

    private static int infect(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        var player = ctx.getArgument("player", EntitySelector.class).findSinglePlayer(ctx.getSource());
        Holder.Reference<Infection> infectionReference = ctx.getArgument("infection", Holder.Reference.class);
        var infection = InfectionRegistry.INFECTION_REGISTRY.get(infectionReference.key()).create();
        if (infection == null)
            return -1;
        player.setData(ModAttachments.ACTIVE_INFECTION, infection);
        infection.activate();
        ctx.getSource().sendSystemMessage(Component.translatable("command.infection.infect.success",infection.type.toString(), player.getName()));
        return 1;
    }

}
