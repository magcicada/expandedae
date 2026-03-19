package lu.kolja.expandedae.mixin.terminal;

import appeng.client.gui.WidgetContainer;
import appeng.client.gui.me.items.EncodingModePanel;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.me.items.ProcessingEncodingPanel;
import lu.kolja.expandedae.client.button.ModifyIconButton;
import lu.kolja.expandedae.definition.ExpLang;
import lu.kolja.expandedae.helper.misc.GuardedWidget;
import lu.kolja.expandedae.helper.patternprovider.IPatternEncodingTerminalMenu;
import lu.kolja.expandedae.mixin.accessor.AccessorScreenStyle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static lu.kolja.expandedae.client.gui.widgets.ExpIcon.DIVISION_2;
import static lu.kolja.expandedae.client.gui.widgets.ExpIcon.DIVISION_3;
import static lu.kolja.expandedae.client.gui.widgets.ExpIcon.DIVISION_8;
import static lu.kolja.expandedae.client.gui.widgets.ExpIcon.MULTIPLY_2;
import static lu.kolja.expandedae.client.gui.widgets.ExpIcon.MULTIPLY_3;
import static lu.kolja.expandedae.client.gui.widgets.ExpIcon.MULTIPLY_8;

@Mixin(value = ProcessingEncodingPanel.class, remap = false)
public abstract class MixinProcessingEncodingPanel extends EncodingModePanel {
    @Unique
    private GuardedWidget<ModifyIconButton> eae$x2;
    @Unique
    private GuardedWidget<ModifyIconButton> eae$x3;
    @Unique
    private GuardedWidget<ModifyIconButton> eae$x8;
    @Unique
    private GuardedWidget<ModifyIconButton> eae$div2;
    @Unique
    private GuardedWidget<ModifyIconButton> eae$div3;
    @Unique
    private GuardedWidget<ModifyIconButton> eae$div8;

    protected MixinProcessingEncodingPanel(PatternEncodingTermScreen<?> screen, WidgetContainer widgets) {
        super(screen, widgets);
    }

    @Inject(method = "<init>", at = @At("RETURN"), remap = false)
    private void init(PatternEncodingTermScreen<?> screen, WidgetContainer widgets, CallbackInfo ci) {
        eae$x2 = GuardedWidget.guardedWidget("mult2", ((AccessorScreenStyle) screen.getStyle()).getWidgets(),
                        id -> new ModifyIconButton(b -> ((IPatternEncodingTerminalMenu) menu).eae$ModifyPattern(2),
                                MULTIPLY_2,
                                ExpLang.GUI_BUTTONS_PATTERN_MULT.text(2),
                                ExpLang.GUI_BUTTONS_TOOLTIPS_PATTERN_MULT.text(2)))
                .runIfPresent(w -> widgets.add("mult2", w));

        eae$x3 = GuardedWidget.guardedWidget("mult3", ((AccessorScreenStyle) screen.getStyle()).getWidgets(),
                        id -> new ModifyIconButton(b -> ((IPatternEncodingTerminalMenu) menu).eae$ModifyPattern(3),
                                MULTIPLY_3,
                                ExpLang.GUI_BUTTONS_PATTERN_MULT.text(3),
                                ExpLang.GUI_BUTTONS_TOOLTIPS_PATTERN_MULT.text(3)))
                .runIfPresent(w -> widgets.add("mult3", w));

        eae$x8 = GuardedWidget.guardedWidget("mult8", ((AccessorScreenStyle) screen.getStyle()).getWidgets(),
                        id -> new ModifyIconButton(b -> ((IPatternEncodingTerminalMenu) menu).eae$ModifyPattern(8),
                                MULTIPLY_8,
                                ExpLang.GUI_BUTTONS_PATTERN_MULT.text(8),
                                ExpLang.GUI_BUTTONS_TOOLTIPS_PATTERN_MULT.text(8)))
                .runIfPresent(w -> widgets.add("mult8", w));

        eae$div2 = GuardedWidget.guardedWidget("div2", ((AccessorScreenStyle) screen.getStyle()).getWidgets(),
                        id -> new ModifyIconButton(b -> ((IPatternEncodingTerminalMenu) menu).eae$ModifyPattern(-2),
                                DIVISION_2,
                                ExpLang.GUI_BUTTONS_PATTERN_DIV.text(2),
                                ExpLang.GUI_BUTTONS_TOOLTIPS_PATTERN_DIV.text(2)))
                .runIfPresent(w -> widgets.add("div2", w));

        eae$div3 = GuardedWidget.guardedWidget("div3", ((AccessorScreenStyle) screen.getStyle()).getWidgets(),
                        id -> new ModifyIconButton(b -> ((IPatternEncodingTerminalMenu) menu).eae$ModifyPattern(-3),
                                DIVISION_3,
                                ExpLang.GUI_BUTTONS_PATTERN_DIV.text(3),
                                ExpLang.GUI_BUTTONS_TOOLTIPS_PATTERN_DIV.text(3)))
                .runIfPresent(w -> widgets.add("div3", w));

        eae$div8 = GuardedWidget.guardedWidget("div8", ((AccessorScreenStyle) screen.getStyle()).getWidgets(),
                        id -> new ModifyIconButton(b -> ((IPatternEncodingTerminalMenu) menu).eae$ModifyPattern(-8),
                                DIVISION_8,
                                ExpLang.GUI_BUTTONS_PATTERN_DIV.text(8),
                                ExpLang.GUI_BUTTONS_TOOLTIPS_PATTERN_DIV.text(8)))
                .runIfPresent(w -> widgets.add("div8", w));
    }

    @Inject(method = "setVisible", at = @At("TAIL"), remap = false)
    private void setVisibleHooks(boolean visible, CallbackInfo ci) {
        eae$x2.runIfPresent(w -> w.setVisibility(visible));
        eae$x3.runIfPresent(w -> w.setVisibility(visible));
        eae$x8.runIfPresent(w -> w.setVisibility(visible));
        eae$div2.runIfPresent(w -> w.setVisibility(visible));
        eae$div3.runIfPresent(w -> w.setVisibility(visible));
        eae$div8.runIfPresent(w -> w.setVisibility(visible));
    }
}
