package lu.kolja.expandedae.xmod.advancedae;

import appeng.api.crafting.IPatternDetails;
import appeng.api.networking.crafting.ICraftingCPU;
import appeng.api.upgrades.Upgrades;
import lu.kolja.expandedae.mixin.compat.advancedae.AAEAccessorAdvCraftingCPULogic;
import lu.kolja.expandedae.mixin.compat.advancedae.AAEAccessorExecutingCraftingJob;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.pedroksl.advanced_ae.common.cluster.AdvCraftingCPU;
import net.pedroksl.advanced_ae.common.definitions.AAEBlocks;
import net.pedroksl.advanced_ae.common.definitions.AAEItems;

import static lu.kolja.expandedae.definition.ExpItems.AUTO_COMPLETE_CARD;

public class AdvancedAE {
    public AdvancedAE() {
        Upgrades.add(
                AUTO_COMPLETE_CARD,
                getAdvancedAEItem("small_adv_pattern_provider_part"),
                1,
                "group.adv_pattern_provider.name"
        );

        Upgrades.add(
                AUTO_COMPLETE_CARD,
                getAdvancedAEItem("adv_pattern_provider_part"),
                1,
                "group.advanced_pattern_provider.name"
        );

        Upgrades.add(
                AUTO_COMPLETE_CARD,
                getAdvancedAEBlock("small_adv_pattern_provider"),
                1,
                "group.adv_pattern_provider.name"
        );

        Upgrades.add(
                AUTO_COMPLETE_CARD,
                getAdvancedAEBlock("adv_pattern_provider"),
                1,
                "group.advanced_pattern_provider.name"
        );
    }

    private static Item getAdvancedAEItem(String id) {
        return BuiltInRegistries.ITEM.get(
                new ResourceLocation("advanced_ae", id)
        );
    }

    private static Block getAdvancedAEBlock(String id) {
        return BuiltInRegistries.BLOCK.get(
                new ResourceLocation("advanced_ae", id)
        );
    }

    public static void handleCpu(ICraftingCPU cpu, IPatternDetails details) {
        if (cpu instanceof AdvCraftingCPU advCpu) {
            var task = ((AAEAccessorExecutingCraftingJob) ((AAEAccessorAdvCraftingCPULogic) advCpu.craftingLogic).getJob()).getTasks().get(details);
            if (task != null && task.getValue() <= 1) {
                advCpu.cancelJob();
            }
        }
    }
}
