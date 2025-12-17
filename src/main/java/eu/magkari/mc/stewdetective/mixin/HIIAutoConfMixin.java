package eu.magkari.mc.stewdetective.mixin;

import eu.magkari.mc.stewdetective.pond.HIIConfigAccess;
import io.github.a5b84.helditeminfo.config.HeldItemInfoConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = HeldItemInfoConfig.HeldItemInfoAutoConfig.class, remap = false)
public class HIIAutoConfMixin extends HIIConfigMixin implements HIIConfigAccess {
    @Unique
    public boolean showStewEffects = super.stewDetective$showStewEffects();
    @Override
    public boolean stewDetective$showStewEffects() {
        return this.showStewEffects;
    }
}
