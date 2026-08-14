package lu.kolja.expandedae.api.misc;

import lu.kolja.expandedae.definition.ExpLang;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Arrays;
import java.util.List;

public class TooltipHelper {
    @OnlyIn(Dist.CLIENT)
    public static void shiftInfo(List<Component> lines, Component... hints) {
        if (KeybindUtil.isShiftDown()) {
            lines.addAll(Arrays.asList(hints));
        } else {
            lines.add(ExpLang.SHIFT_INFO.text());
        }
    }
}
