package lu.kolja.expandedae.mixin.storage;

import appeng.api.networking.IGridNode;
import appeng.api.stacks.AEKey;
import appeng.me.service.StorageService;
import appeng.menu.AEBaseMenu;
import appeng.menu.me.common.MEStorageMenu;
import lu.kolja.expandedae.api.cpu.IHighlightMenu;
import lu.kolja.expandedae.api.misc.IStorageLocations;
import lu.kolja.expandedae.definition.ExpLang;
import lu.kolja.expandedae.highlight.BlockHighlightHandler;
import lu.kolja.expandedae.network.ExpNetworkHandler;
import lu.kolja.expandedae.network.implementations.HighlightDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MEStorageMenu.class, remap = false)
public abstract class MixinMEStorageMenu extends AEBaseMenu implements IHighlightMenu {
    @Shadow protected abstract boolean canInteractWithGrid();

    @Shadow private @Nullable IGridNode networkNode;

    public MixinMEStorageMenu(MenuType<?> menuType, int id, Inventory playerInventory, Object host) {
        super(menuType, id, playerInventory, host);
    }

    @Override
    public void eae$highlight(AEKey what) {
        if (!canInteractWithGrid()) return;
        if (what == null) return;
        if (networkNode == null) return;

        var grid = networkNode.getGrid();
        if (grid == null) return;

        var storageService = grid.getStorageService();
        if (!(storageService instanceof StorageService service)) return;

        var positions = ((IStorageLocations) service).eae$getStorageLocations(what);
        var level = networkNode.getLevel();
        var dim = level.dimension();

        if (positions.isEmpty()) {
            getPlayer().sendSystemMessage(ExpLang.NO_HIGHLIGHTED_BLOCKS.text());
            return;
        }

        this.getPlayer().sendSystemMessage(ExpLang.HIGHLIGHTING_STORAGE.text(what.getDisplayName()));
        for (var pos : positions) {
            var packet = new HighlightDataPacket(pos, dim, BlockHighlightHandler.getTime(pos, this.getPlayer().getOnPos()));
            ExpNetworkHandler.HANDLER.sendToClient(packet, getPlayer());
        }
    }
}
