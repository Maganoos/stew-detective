package eu.magkari.mc.stewdetective.config;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import eu.magkari.mc.stewdetective.StewDetective;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class StewDetectiveCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess access) {
        dispatcher.register(
                ClientCommandManager.literal("stewdetective")
                        .then(ClientCommandManager.literal("heldItemIntegration")
                                .executes(context -> {
                                    boolean enabled = StewDetective.config.isEnabled();
                                    context.getSource().sendFeedback(Text.translatable("stew-detective.command.hiii", enabled ? Text.translatable("options.on").formatted(Formatting.GREEN) : Text.translatable("options.off").formatted(Formatting.RED)));
                                    return 1;
                                })
                                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                                        .executes(context -> {
                                            boolean enabled = BoolArgumentType.getBool(context, "enabled");
                                            StewDetective.config.setEnabled(enabled);
                                            context.getSource().sendFeedback(Text.translatable("stew-detective.command.hiii.set", enabled ? Text.translatable("options.on").formatted(Formatting.GREEN) : Text.translatable("options.off").formatted(Formatting.RED)));
                                            return 1;
                                        })
                                )
                        )
        );
    }
}
