package lu.kolja.expandedae.enums;

import appeng.block.crafting.ICraftingUnitType;
import appeng.core.definitions.BlockDefinition;
import lombok.Getter;
import lu.kolja.expandedae.definition.ExpBlocks;
import net.minecraft.world.item.Item;

import java.io.Serializable;

import static lu.kolja.expandedae.definition.ExpBlocks.*;

public enum ExpTiers implements ICraftingUnitType, Serializable {
    SINGULARITY_STORAGE(0, "singularity_crafting_storage", false, Long.MAX_VALUE),
    UNIT(0, "exp_crafting_unit", false),
    TIER_2(2, "2"),
    TIER_4(4, "4"),
    TIER_8(8, "8"),
    TIER_16(16, "16"),
    TIER_32(32, "32"),
    TIER_64(64, "64"),
    TIER_128(128, "128"),
    TIER_256(256, "256"),
    TIER_512(512, "512"),
    TIER_1K(1024, "1k"),
    TIER_2K(2048, "2k"),
    TIER_4K(4096, "4k"),
    TIER_8K(8192, "8k"),
    TIER_16K(16384, "16k"),
    TIER_32K(32768, "32k"),
    TIER_64K(65536, "64k"),
    TIER_128K(131072, "128k"),
    TIER_256K(262144, "256k"),
    TIER_512K(524288, "512k"),
    TIER_1M(1048576, "1m");


    @Getter
    private final int threads;
    @Getter
    private final long storage;
    @Getter
    private final String affix;
    @Getter
    private final String cpuAffix;
    @Getter
    private final boolean isCPU;

    ExpTiers(int threads, String affix) {
        this(threads, affix, true);
    }

    ExpTiers(int threads, String affix, boolean isCPU) {
        this(threads, affix, isCPU, 0);
    }

    ExpTiers(int threads, String affix, boolean isCPU, long storage) {
        this.threads = threads;
        this.affix = affix;
        this.cpuAffix = "exp_crafting_accelerator_" + affix;
        this.isCPU = isCPU;
        this.storage = storage;
    }

    @Override
    public long getStorageBytes() {
        return storage;
    }

    @Override
    public int getAcceleratorThreads() {
        return threads;
    }

    public BlockDefinition<?> getDefinition() {
        return switch (this) {
            case SINGULARITY_STORAGE -> SINGULARITY_CRAFTING_STORAGE;
            case UNIT -> EXP_CRAFTING_UNIT;
            case TIER_2 -> CPU_2;
            case TIER_4 -> CPU_4;
            case TIER_8 -> CPU_8;
            case TIER_16 -> CPU_16;
            case TIER_32 -> CPU_32;
            case TIER_64 -> CPU_64;
            case TIER_128 -> CPU_128;
            case TIER_256 -> CPU_256;
            case TIER_512 -> CPU_512;
            case TIER_1K -> CPU_1K;
            case TIER_2K -> CPU_2K;
            case TIER_4K -> CPU_4K;
            case TIER_8K -> CPU_8K;
            case TIER_16K -> CPU_16K;
            case TIER_32K -> CPU_32K;
            case TIER_64K -> CPU_64K;
            case TIER_128K -> CPU_128K;
            case TIER_256K -> CPU_256K;
            case TIER_512K -> CPU_512K;
            case TIER_1M -> CPU_1M;
        };
    }

    @Override
    public Item getItemFromType() {
        return getDefinition().asItem();
    }
}
