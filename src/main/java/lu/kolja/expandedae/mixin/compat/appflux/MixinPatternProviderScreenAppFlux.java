package lu.kolja.expandedae.mixin.compat.appflux;

import appeng.client.gui.AEBaseScreen;
import appeng.client.gui.implementations.PatternProviderScreen;
import appeng.client.gui.style.ScreenStyle;
import appeng.client.gui.widgets.ServerSettingToggleButton;
import appeng.menu.implementations.PatternProviderMenu;
import lu.kolja.expandedae.api.misc.KeybindUtil;
import lu.kolja.expandedae.api.patternprovider.IPatternProvider;
import lu.kolja.expandedae.client.gui.widgets.ExpActionButton;
import lu.kolja.expandedae.client.gui.widgets.ExpActionItems;
import lu.kolja.expandedae.definition.ExpSettings;
import lu.kolja.expandedae.enums.BlockingMode;
import lu.kolja.mixinloadconditions.LoadCondition;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@LoadCondition(loadIfAny = {"appflux", "pccard"})
@Mixin(value = PatternProviderScreen.class, remap = false)
public abstract class MixinPatternProviderScreenAppFlux<C extends PatternProviderMenu> extends AEBaseScreen<C> {

    @Unique
    private ServerSettingToggleButton<BlockingMode> eae$blockingMode;

    private MixinPatternProviderScreenAppFlux(C menu, Inventory playerInventory, Component title, ScreenStyle style) {
        super(menu, playerInventory, title, style);
    }

    @Inject(
            method = "<init>(Lappeng/menu/implementations/PatternProviderMenu;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/chat/Component;Lappeng/client/gui/style/ScreenStyle;)V",
            at = @At("TAIL"),
            remap = false
    )
    private void init(PatternProviderMenu menu, Inventory playerInventory, Component title, ScreenStyle style, CallbackInfo ci) {
        ExpActionButton modifyPatterns = new ExpActionButton(ExpActionItems.MODIFY_PATTERNS,
                act -> ((IPatternProvider) menu).expandedae$modifyPatterns(
                KeybindUtil.shiftMultiplier() * KeybindUtil.ctrlMultiplier() * (this.isHandlingRightClick() ? -1 : 1)
        ));
        this.addToLeftToolbar(modifyPatterns);
        this.eae$blockingMode = new ServerSettingToggleButton<>(
                ExpSettings.BLOCKING_MODE,
                BlockingMode.DEFAULT
        );
        this.addToLeftToolbar(this.eae$blockingMode);
    }


    @Inject(method = "updateBeforeRender", at = @At("TAIL"), remap = false)
    private void updateBeforeRender(CallbackInfo ci) {
        this.eae$blockingMode.set(((IPatternProvider) menu).expandedae$getBlockingMode());
    }
}
