package eu.magkari.mc.stewdetective;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.Component;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.registry.Registries;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Formatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class StewDetective implements ClientModInitializer {
    @Override
	public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext context, TooltipType type, List<Text> lines) -> {
            if (type.isCreative()) return;

            SuspiciousStewEffectsComponent component = stack.get(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS);
            if (component == null || component.effects().isEmpty()) return;
            int appendIndex = 1;

            lines.add(appendIndex, Text.translatable("stew-detective.tooltip").formatted(Formatting.GRAY));

            for (SuspiciousStewEffectsComponent.StewEffect entry : component.effects()) {
                var effect = entry.effect().value();
                int duration = entry.duration();

                int c = effect.getColor(), r = (c>>16)&255, g = (c>>8)&255, b = c&255, m = Math.max(r,Math.max(g,b));
                if(m<128){double s=128.0/(m+1); r=Math.min(255,(int)(r*s)); g=Math.min(255,(int)(g*s)); b=Math.min(255,(int)(b*s));}
                Text name = Text.translatable(effect.getTranslationKey()).setStyle(Style.EMPTY.withColor((r<<16)|(g<<8)|b));
                Text dur = Text.literal(" (" + duration / 20 + "s)").formatted(Formatting.GRAY);

                lines.add(++appendIndex, Text.literal("  ").append(name).append(dur));
            }
        });
    }
}