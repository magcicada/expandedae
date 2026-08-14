package lu.kolja.expandedae.menu;

import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.guisync.GuiSync;
import appeng.menu.implementations.PatternProviderMenu;
import lu.kolja.expandedae.api.patternprovider.ISpecialSlots;
import lu.kolja.expandedae.definition.ExpMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class GigaPatternProviderMenu extends PatternProviderMenu {
    @GuiSync(20)
    public int currentPage = 0;

    public GigaPatternProviderMenu(int id, Inventory playerInventory, PatternProviderLogicHost host) {
        super(ExpMenus.GIGA_PATTERN_PROVIDER, id, playerInventory, host);
        registerClientAction("setPage", Integer.class, this::setPage);
    }

    @Override
    protected ItemStack transferStackToMenu(ItemStack input) {
        for (int i = currentPage * 72; i < currentPage * 72 + 72; i++) {
            var slot = ((ISpecialSlots) this).eae$getSpecialSlots()[i];
            if (slot.hasItem()) continue;
            if (slot.mayPlace(input)) {
                slot.set(input);
                return ItemStack.EMPTY;
            }
        }
        return super.transferStackToMenu(input);
    }

    public void setPage(int page) {
        if (this.isClientSide()) {
            sendClientAction("setPage", page);
            for (var slot : ((ISpecialSlots) this).eae$getSpecialSlots()) {
                slot.setActive(page == slot.getSlotIndex() / 72);
            }
            return;
        }
        this.currentPage = page;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
