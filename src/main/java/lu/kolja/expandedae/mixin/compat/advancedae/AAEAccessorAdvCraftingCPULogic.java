package lu.kolja.expandedae.mixin.compat.advancedae;

import lu.kolja.mixinloadconditions.LoadCondition;
import net.pedroksl.advanced_ae.common.logic.AdvCraftingCPULogic;
import net.pedroksl.advanced_ae.common.logic.ExecutingCraftingJob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@LoadCondition(loadIf = "advanced_ae")
@Mixin(value = AdvCraftingCPULogic.class, remap = false)
public interface AAEAccessorAdvCraftingCPULogic {
    @Accessor("job")
    ExecutingCraftingJob getJob();
}