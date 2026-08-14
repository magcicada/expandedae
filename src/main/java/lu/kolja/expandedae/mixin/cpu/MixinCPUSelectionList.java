package lu.kolja.expandedae.mixin.cpu;

import appeng.client.Point;
import appeng.client.gui.widgets.CPUSelectionList;
import appeng.core.localization.ButtonToolTips;
import appeng.core.localization.Tooltips;
import appeng.menu.me.crafting.CraftingStatusMenu;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lu.kolja.expandedae.api.cpu.ISearchScreen;
import lu.kolja.expandedae.api.misc.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CPUSelectionList.class, remap = false)
public class MixinCPUSelectionList {

    /**
     * Truncates CPU Crafting Storages with Formatting
     */
    @ModifyReturnValue(
            method = "formatStorage",
            at = @At("RETURN")
    )
    private String eae$formatStorage(String original, @Local(argsOnly = true, name = "arg1") CraftingStatusMenu.CraftingCpuListEntry cpu) {
        return NumberUtil.formatNum(cpu.storage());
    }

    @Redirect(
            method = "drawBackgroundLayer",
            at = @At(value = "INVOKE", target = "Ljava/lang/String;valueOf(I)Ljava/lang/String;")
    )
    private String formatProcessorCount(int coProcessorCount) {
        return NumberUtil.formatNum(coProcessorCount);
    }

    @Inject(
            method = "onMouseUp",
            at = @At("RETURN")
    )
    private void eae$onMouseUp(Point mousePos, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        assert Minecraft.getInstance().screen != null;
        ((ISearchScreen) Minecraft.getInstance().screen).eae$clearSearch();
    }

    @WrapOperation(
            method = "getTooltip",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/core/localization/Tooltips;ofBytes(J)Lnet/minecraft/network/chat/MutableComponent;",
                    ordinal = 0
            )
    )
    private MutableComponent eae$getTooltip(long number, Operation<MutableComponent> original, @Local(name = "cpu") CraftingStatusMenu.CraftingCpuListEntry cpu) {
        var storage = cpu.storage();
        if (storage < 1024 * 1024 * 1024L) {
            return original.call(number);
        }
        return Component.literal(NumberUtil.formatNum(storage) + "B").withStyle(Tooltips.NUMBER_TEXT);
    }
}
