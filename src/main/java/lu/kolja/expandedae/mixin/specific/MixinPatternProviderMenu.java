package lu.kolja.expandedae.mixin.specific;

import appeng.api.inventories.InternalInventory;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.AEBaseMenu;
import appeng.menu.SlotSemantic;
import appeng.menu.implementations.PatternProviderMenu;
import appeng.menu.slot.RestrictedInputSlot;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lu.kolja.expandedae.api.patternprovider.ISpecialSlots;
import lu.kolja.expandedae.definition.ExpSemantics;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = PatternProviderMenu.class, remap = false)
public class MixinPatternProviderMenu extends AEBaseMenu implements ISpecialSlots {
    @Unique
    private static final SlotSemantic[] eae$semantics = {
            ExpSemantics.PAGE_1,
            ExpSemantics.PAGE_2,
            ExpSemantics.PAGE_3,
            ExpSemantics.PAGE_4
    };

    @Unique
    private final RestrictedInputSlot[] eae$slots = new RestrictedInputSlot[72 * 4];

    public MixinPatternProviderMenu(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @WrapOperation(
            method = "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lappeng/helpers/patternprovider/PatternProviderLogicHost;)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lappeng/menu/implementations/PatternProviderMenu;addSlot(Lnet/minecraft/world/inventory/Slot;Lappeng/menu/SlotSemantic;)Lnet/minecraft/world/inventory/Slot;",
                    ordinal = 0
            )
    )
    private Slot init(PatternProviderMenu instance, Slot slot, SlotSemantic slotSemantic, Operation<Slot> original, @Local(name = "patternInv") InternalInventory patternInv, @Local(name = "x") int x) {
        if (patternInv.size() == 288) {
            this.eae$slots[x] = (RestrictedInputSlot) slot;
            return this.addSlot(slot, eae$semantics[x / 72]);
        }
        return original.call(instance, slot, slotSemantic);
    }

    @Inject(
            method = "<init>(Lnet/minecraft/world/inventory/MenuType;ILnet/minecraft/world/entity/player/Inventory;Lappeng/helpers/patternprovider/PatternProviderLogicHost;)V",
            at = @At("TAIL")
    )
    private void init(MenuType<?> menuType, int id, Inventory playerInventory, PatternProviderLogicHost host, CallbackInfo ci, @Local(name = "patternInv") InternalInventory patternInv) {
        if (patternInv.size() == 288) {
            for (int i = 72; i < 72 * 4; i++) {
                eae$slots[i].setActive(false);
            }
        }
    }

    @Override
    public RestrictedInputSlot[] eae$getSpecialSlots() {
        return eae$slots;
    }
}
