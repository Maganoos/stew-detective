package eu.magkari.mc.stewdetective;

import eu.magkari.mc.stewdetective.config.Config;
import eu.magkari.mc.stewdetective.config.StewDetectiveCommand;
import net.fabricmc.api.ClientModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class StewDetective implements ClientModInitializer {
    public static final String MOD_ID = "stewdetective";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static Config config;

    @Override
	public void onInitializeClient() {
        if (FabricLoader.getInstance().isModLoaded("held-item-info")) {
            config = new Config(FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json"));
            config.load();
            ClientCommandRegistrationCallback.EVENT.register(StewDetectiveCommand::register);
        }


        ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext context, TooltipType type, List<Text> lines) -> {
            if (type.isCreative()) return;

            SuspiciousStewEffectsComponent component = stack.get(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS);
            if (component == null || component.effects().isEmpty()) return;
            int appendIndex = 1;

            lines.add(appendIndex, Text.translatable("stew-detective.tooltip").formatted(Formatting.GRAY));

            lines.addAll(++appendIndex, extractEffects(component));
        });
    }

    public static List<Text> extractEffects(SuspiciousStewEffectsComponent component) {
        List<Text> lines = new ArrayList<>();
        for (SuspiciousStewEffectsComponent.StewEffect entry : component.effects()) {
            var effect = entry.effect().value();
            int duration = entry.duration();

            int c = effect.getColor(), r = (c>>16)&255, g = (c>>8)&255, b = c&255, m = Math.max(r,Math.max(g,b));
            if(m<128){double s=128.0/(m+1); r=Math.min(255,(int)(r*s)); g=Math.min(255,(int)(g*s)); b=Math.min(255,(int)(b*s));}
            Text name = Text.translatable(effect.getTranslationKey()).setStyle(Style.EMPTY.withColor((r<<16)|(g<<8)|b));
            Text dur = Text.literal(" (" + duration / 20 + "s)").formatted(Formatting.GRAY);

            lines.add(Text.literal("  ").append(name).append(dur));
        }
        return lines;
    }
}