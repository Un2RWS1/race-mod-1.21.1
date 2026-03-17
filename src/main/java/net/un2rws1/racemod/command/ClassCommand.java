package net.un2rws1.racemod.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.un2rws1.racemod.classsystem.ClassManager;
import net.un2rws1.racemod.classsystem.PlayerClass;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;
import static net.minecraft.command.argument.EntityArgumentType.getPlayer;
import static net.minecraft.command.argument.EntityArgumentType.player;

public final class ClassCommand {
    private ClassCommand() {
    }

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(literal("race")
                .requires(source -> source.hasPermissionLevel(2))
                .then(literal("reset")
                        .then(argument("target", player())
                                .executes(context -> {
                                    ServerPlayerEntity target = getPlayer(context, "target");
                                    ClassManager.resetAndOpenSelection(target);
                                    context.getSource().sendFeedback(
                                            () -> Text.literal("Reset race for " + target.getName().getString()),
                                            true
                                    );
                                    return 1;
                                })))
                .then(literal("open")
                        .then(argument("target", player())
                                .executes(context -> {
                                    ServerPlayerEntity target = getPlayer(context, "target");
                                    ClassManager.openSelection(target);
                                    context.getSource().sendFeedback(
                                            () -> Text.literal("Opened race selection for " + target.getName().getString()),
                                            true
                                    );
                                    return 1;
                                })))
                .then(literal("get")
                        .then(argument("target", player())
                                .executes(context -> {
                                    ServerPlayerEntity target = getPlayer(context, "target");
                                    PlayerClass playerClass = ClassManager.getPlayerClass(target);

                                    if (playerClass == null) {
                                        context.getSource().sendFeedback(
                                                () -> Text.literal(target.getName().getString() + " has no race selected."),
                                                false
                                        );
                                    } else {
                                        context.getSource().sendFeedback(
                                                () -> Text.literal(target.getName().getString() + " is " + playerClass.getDisplayName()),
                                                false
                                        );
                                    }

                                    return 1;
                                })))
                .then(literal("set")
                        .then(argument("target", player())
                                .then(argument("class_id", StringArgumentType.word())
                                        .suggests((context, builder) -> {
                                            for (PlayerClass playerClass : PlayerClass.values()) {
                                                builder.suggest(playerClass.getId());
                                            }
                                            return builder.buildFuture();
                                        })
                                        .executes(context -> {
                                            ServerPlayerEntity target = getPlayer(context, "target");
                                            String classId = StringArgumentType.getString(context, "class_id");

                                            PlayerClass playerClass = PlayerClass.byId(classId).orElse(null);
                                            if (playerClass == null) {
                                                context.getSource().sendError(Text.literal("Irrelevant Race: " + classId));
                                                return 0;
                                            }

                                            ClassManager.forceSetClass(target, playerClass);
                                            context.getSource().sendFeedback(
                                                    () -> Text.literal("Set " + target.getName().getString() + " to " + playerClass.getDisplayName()),
                                                    true
                                            );
                                            return 1;
                                        })))));
    }
}