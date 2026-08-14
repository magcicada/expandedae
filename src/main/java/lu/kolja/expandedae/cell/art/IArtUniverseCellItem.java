package lu.kolja.expandedae.cell.art;

import appeng.api.stacks.AEKeyType;
import com.google.common.base.Preconditions;
import lu.kolja.expandedae.cell.IExpandedCellItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Optional;

public interface IArtUniverseCellItem extends IExpandedCellItem {

    AEKeyType getKeyType();

    /**
     * Convenient helper to append useful tooltip information.
     */
    default void addCellInformationToTooltip(ItemStack is, List<Component> lines) {
        Preconditions.checkArgument(is.getItem() == this);
        ArtUniverseCellHandler.INSTANCE.addCellInformationToTooltip(is, lines);
    }

    /**
     * Helper to get the additional tooltip image line showing the content/filter/upgrades.
     */
    default Optional<TooltipComponent> getCellTooltipImage(ItemStack is) {
        Preconditions.checkArgument(is.getItem() == this);
        return ArtUniverseCellHandler.INSTANCE.getTooltipImage(is);
    }
}