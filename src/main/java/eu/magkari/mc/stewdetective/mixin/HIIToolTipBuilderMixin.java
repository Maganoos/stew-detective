package eu.magkari.mc.stewdetective.mixin;

import eu.magkari.mc.stewdetective.StewDetective;
import eu.magkari.mc.stewdetective.pond.HIIConfigAccess;
import io.github.a5b84.helditeminfo.HeldItemInfo;
import io.github.a5b84.helditeminfo.TooltipBuilder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.SuspiciousStewEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = TooltipBuilder.class, remap = false)
public abstract class HIIToolTipBuilderMixin {

    @Final
    @Shadow private ItemStack stack;

    @Shadow
    public abstract void appendAll(List<? extends Component> newLines);

    @Inject(method="build", at=@At("HEAD"))
    private void addStew(CallbackInfoReturnable<List<Component>> cir) {
        if (!((HIIConfigAccess) HeldItemInfo.config).stewDetective$showStewEffects()) return;
        SuspiciousStewEffects component = this.stack.get(DataComponents.SUSPICIOUS_STEW_EFFECTS);
        if (component == null || component.effects().isEmpty()) return;

        this.appendAll(StewDetective.extractEffects(component));
    }
}
