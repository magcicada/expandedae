package lu.kolja.expandedae.mixin.patternprovider;

import appeng.api.upgrades.IUpgradeInventory;
import appeng.api.upgrades.IUpgradeableObject;
import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import lu.kolja.mixinloadconditions.LoadCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@LoadCondition(ignoreIfAny = {"appflux", "pccard"})
@Mixin(value = PatternProviderLogicHost.class, remap = false)
public interface MixinPatternProviderLogicHost extends IUpgradeableObject {

    @Shadow
    PatternProviderLogic getLogic();

    @Override
    default IUpgradeInventory getUpgrades() {
        return ((IUpgradeableObject) this.getLogic()).getUpgrades();
    }

}