package eu.magkari.mc.stewdetective.mixin;

import eu.magkari.mc.stewdetective.pond.HIIConfigAccess;
import io.github.a5b84.helditeminfo.config.HeldItemInfoConfig;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = HeldItemInfoConfig.class, remap = false)
public class HIIConfigMixin implements HIIConfigAccess {
    @Override
    public boolean stewDetective$showStewEffects() {
        return true;
    }
}
