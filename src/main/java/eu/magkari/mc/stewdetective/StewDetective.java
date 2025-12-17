package eu.magkari.mc.stewdetective;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.SuspiciousStewEffects;

import java.util.ArrayList;
import java.util.List;

public class StewDetective implements ClientModInitializer {
    @Override
	public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register((ItemStack stack, Item.TooltipContext context, TooltipFlag type, List<Component> lines) -> {
            if (type.isCreative()) return;

            SuspiciousStewEffects component = stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
            if (component == null || component.effects().isEmpty()) return;
            int appendIndex = 1;

            lines.add(appendIndex, Component.translatable("stew-detective.tooltip").withStyle(ChatFormatting.GRAY));

            lines.addAll(++appendIndex, extractEffects(component));
        });
    }

    public static List<Component> extractEffects(SuspiciousStewEffects component) {
        List<Component> lines = new ArrayList<>();
        for (SuspiciousStewEffects.Entry entry : component.effects()) {
            var effect = entry.effect().value();
            int duration = entry.duration();

            int c = effect.getColor(), r = (c>>16)&255, g = (c>>8)&255, b = c&255, m = Math.max(r,Math.max(g,b));
            if(m<128){double s=128.0/(m+1); r=Math.min(255,(int)(r*s)); g=Math.min(255,(int)(g*s)); b=Math.min(255,(int)(b*s));}
            Component name = Component.translatable(effect.getDescriptionId()).setStyle(Style.EMPTY.withColor((r<<16)|(g<<8)|b));
            Component dur = Component.literal(" (" + duration / 20 + "s)").withStyle(ChatFormatting.GRAY);

            lines.add(Component.literal("  ").append(name).append(dur));
        }
        return lines;
    }
}