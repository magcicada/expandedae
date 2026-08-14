package lu.kolja.expandedae.mixin.compat.appflux;

import appeng.helpers.patternprovider.PatternProviderLogic;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantics;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.PatternProviderMenu;
import lu.kolja.expandedae.api.base.IUpgradableMenu;
import lu.kolja.expandedae.api.misc.PatternHelper;
import lu.kolja.expandedae.api.patternprovider.IPatternProvider;
import lu.kolja.expandedae.definition.ExpSemantics;
import lu.kolja.expandedae.definition.ExpSettings;
import lu.kolja.expandedae.enums.BlockingMode;
import lu.kolja.mixinloadconditions.LoadCondition;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@LoadCondition(loadIfAny = {"appflux", "pccard"})
@Mixin(value = PatternProviderMenu.class, remap = false)
public abstract class MixinPatternProviderMenuAppFlux extends AEBaseMenu implements IUpgradableMenu, IPatternProvider {
    @Shadow(remap = false) @Final protected PatternProviderLogic logic;

    @Unique
    @GuiSync(8)
    private BlockingMode eae$blockingMode = BlockingMode.DEFAULT;

    public MixinPatternProviderMenuAppFlux(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lappeng/helpers/patternprovider/PatternProviderLogicHost;)V",
            at = @At("TAIL")
    )
    private void initToolbox(MenuType<?> menuType, int id, Inventory playerInventory, PatternProviderLogicHost host, CallbackInfo ci) {
        this.registerClientAction("modifyPatterns", Integer.class, this::expandedae$modifyPatterns);
    }

    @Override
    public void expandedae$modifyPatterns(Integer mult) {
        if (this.isClientSide()) this.sendClientAction("modifyPatterns", mult);
        else {
            for (var slot : this.getSlots(SlotSemantics.ENCODED_PATTERN)) {
                slot.set(PatternHelper.modifyPatterns(slot.getItem(), mult, this.getPlayer().level()));
            }
            for (var semantic : ExpSemantics.ALL) {
                for (var slot : this.getSlots(semantic)) {
                    slot.set(PatternHelper.modifyPatterns(slot.getItem(), mult, this.getPlayer().level()));
                }
            }
        }
    }

    @Inject(method = "broadcastChanges", at = @At("HEAD"), remap = true)
    public void broadcastChanges(CallbackInfo ci) {
        if (this.isServerSide()) eae$blockingMode = logic.getConfigManager().getSetting(ExpSettings.BLOCKING_MODE);
    }

    @Override
    public BlockingMode expandedae$getBlockingMode() {
        return eae$blockingMode;
    }
}
