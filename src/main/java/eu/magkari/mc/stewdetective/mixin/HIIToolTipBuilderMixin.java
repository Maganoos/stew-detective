package eu.magkari.mc.stewdetective.mixin;

import com.moulberry.mixinconstraints.annotations.IfModLoaded;
import eu.magkari.mc.stewdetective.StewDetective;
import io.github.a5b84.helditeminfo.TooltipBuilder;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.SuspiciousStewEffectsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@IfModLoaded("held-item-info")
@Mixin(value = TooltipBuilder.class, remap = false)
public abstract class HIIToolTipBuilderMixin {

    @Final
    @Shadow private ItemStack stack;

    @Shadow
    public abstract void appendAll(List<? extends Text> newLines);

    @Inject(method="build", at=@At("HEAD"))
    private void addStew(CallbackInfoReturnable<List<Text>> cir) {
        if (!StewDetective.config.isEnabled()) return;
        SuspiciousStewEffectsComponent component = this.stack.get(DataComponentTypes.SUSPICIOUS_STEW_EFFECTS);
        if (component == null || component.effects().isEmpty()) return;

        this.appendAll(StewDetective.extractEffects(component));
    }
}
