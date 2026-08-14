package lu.kolja.expandedae.mixin.cpu;

import appeng.blockentity.crafting.CraftingBlockEntity;
import appeng.me.cluster.implementations.CraftingCPUCluster;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = CraftingCPUCluster.class, priority = 2000, remap = false)
public abstract class MixinCraftingCPUCluster {
    @Shadow
    private long storage;

    @Shadow
    @Final
    private List<CraftingBlockEntity> blockEntities;

    @Redirect(
            method = "addBlockEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/blockentity/crafting/CraftingBlockEntity;getAcceleratorThreads()I",
                    ordinal = 1
            ),
            remap = false
    )
    private int modifyThreadLimit(CraftingBlockEntity instance) {
        return 1; //so any number of threads is allowed
    }

    /// If storage would overflow, set it to max so any subsequent storage won't add anything
    @WrapOperation(
            method = "addBlockEntity",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/blockentity/crafting/CraftingBlockEntity;getStorageBytes()J",
                    ordinal = 1
            )
    )
    private long modifyStorage(CraftingBlockEntity instance, Operation<Long> original, @Local(name = "te") CraftingBlockEntity te) {
        var additionalStorage = original.call(instance);
        if (Long.MAX_VALUE - additionalStorage >= this.storage) {
            return additionalStorage;
        } else {
            this.storage = Long.MAX_VALUE;
            return 0;
        }
    }
}
