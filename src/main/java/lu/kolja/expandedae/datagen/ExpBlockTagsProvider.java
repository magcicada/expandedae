package lu.kolja.expandedae.datagen;

import lu.kolja.expandedae.Expandedae;
import lu.kolja.expandedae.definition.ExpBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ExpBlockTagsProvider extends BlockTagsProvider {

    public ExpBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Expandedae.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var pickaxe = tag(BlockTags.MINEABLE_WITH_PICKAXE);
        ExpBlocks.getBlocks().forEach(blockDef -> pickaxe.add(blockDef.block()));
    }
}