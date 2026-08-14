package lu.kolja.expandedae.mixin.compat.emi;

import lu.kolja.mixinloadconditions.LoadCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@LoadCondition(loadIf = "emi")
@Mixin(targets = "appeng.integration.modules.emi.EmiItemStackConverter", remap = false)
public abstract class MixinEmiItemStackConverter {

    /**
     * The {@link appeng.api.stacks.GenericStack} taken from the craft confirm/status screens,
     * always returns a stack size of 0 for items making the stack unable to be favorited
     */
    @ModifyArg(
            method = "toEmiStack",
            at = @At(
                    value = "INVOKE",
                    target = "Ldev/emi/emi/api/stack/EmiStack;setAmount(J)Ldev/emi/emi/api/stack/EmiStack;"
            )
    )
    private long modifyAmount(long amount) {
        return amount == 0 ? 1 : amount;
    }
}
