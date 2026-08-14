package lu.kolja.expandedae.mixin.storage;

import appeng.api.networking.IGridNode;
import appeng.api.stacks.AEKey;
import appeng.api.stacks.KeyCounter;
import appeng.api.storage.MEStorage;
import appeng.me.service.StorageService;
import appeng.parts.AEBasePart;
import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.api.misc.IStorageLocations;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(value = StorageService.class, remap = false)
public class MixinStorageService implements IStorageLocations {
    @Shadow @Final private Map<IGridNode, AccessorProviderState> nodeProviders;

    @Override
    public Set<BlockPos> eae$getStorageLocations(AEKey what) {
        var locations = new HashSet<BlockPos>();
        var counter = new KeyCounter();
        for (var entry : nodeProviders.entrySet()) {
            var node = entry.getKey();
            var state = entry.getValue();
            var owner = node.getOwner();
            BlockPos pos = null;
            if (owner instanceof BlockEntity be) {
                pos = be.getBlockPos();
            } else if (owner instanceof AEBasePart part) {
                pos = part.getBlockEntity().getBlockPos();
            } else {
                Expandedae.LOGGER.warn("Could not get position for owner {} on node {}, please report this issue.", owner, node);
            }
            if (pos == null) continue;

            for (var inv : state.getInventories()) {
                counter.clear();
                inv.getAvailableStacks(counter);
                if (counter.get(what) > 0) {
                    locations.add(pos);
                    break;
                }
            }
        }
        return locations;
    }

    @Mixin(targets = "appeng.me.service.StorageService$ProviderState", remap = false)
    public interface AccessorProviderState {
        @Accessor("inventories")
        Set<MEStorage> getInventories();
    }
}
