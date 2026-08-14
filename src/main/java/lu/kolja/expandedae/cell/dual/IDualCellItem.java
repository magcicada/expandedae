package lu.kolja.expandedae.cell.dual;

import appeng.api.stacks.AEKeyType;
import com.google.common.base.Preconditions;
import lu.kolja.expandedae.cell.IExpandedCellItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

/**
 * Implement this on any item to register a "dual cell", which is a cell that works similarly to AE2's own item and
 * fluid cells, only that this can store any arbitrary combination of {@link AEKeyType}'s
 */
public interface IDualCellItem extends IExpandedCellItem {
    /**
     * Basic cell items are limited to a single {@link AEKeyType}.
     */
    AEKeyTypes<?> getKeyTypes();

    /**
     * Convenient helper to append useful tooltip information.
     */
    default void addCellInformationToTooltip(ItemStack is, List<Component> lines) {
        Preconditions.checkArgument(is.getItem() == this);
        DualCellHandler.INSTANCE.addCellInformationToTooltip(is, lines);
    }

    /**
     * Helper to get the additional tooltip image line showing the content/filter/upgrades.
     */
    default Optional<TooltipComponent> getCellTooltipImage(ItemStack is) {
        Preconditions.checkArgument(is.getItem() == this);
        return DualCellHandler.INSTANCE.getTooltipImage(is);
    }
}
