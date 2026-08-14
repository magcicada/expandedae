package lu.kolja.expandedae.screen;

import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.client.gui.AESubScreen;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.widgets.TabButton;
import appeng.core.localization.GuiText;
import appeng.menu.me.items.PatternEncodingTermMenu;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;

public class SetProcessingPatternNameScreen<M extends PatternEncodingTermMenu, S extends PatternEncodingTermScreen<M>> extends AESubScreen<M, S> {
    private final GenericStack stack;
    private final Consumer<GenericStack> setter;
    private final EditBox name;

    public SetProcessingPatternNameScreen(S parent, GenericStack stack, Consumer<GenericStack> setter) {
        super(parent, "/screens/set_processing_pattern_name.json");

        this.stack = stack;
        this.setter = setter;

        this.name = widgets.addTextField("name");
        var name = this.stack.what().wrapForDisplayOrFilter().getHoverName().getString();
        this.name.setValue(name);
        this.name.setMaxLength(32);

        widgets.addButton("confirm", GuiText.Set.text(), this::setName);

        var icon = menu.getHost().getMainMenuIcon();
        var button = new TabButton(icon, icon.getHoverName(), b -> returnToParent());
        widgets.add("back", button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            setName();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void setName() {
        var amount = stack.amount();
        var itemStack = ((AEItemKey) stack.what()).toStack();
        itemStack.setHoverName(Component.literal(name.getValue()));
        var key = AEItemKey.of(itemStack);
        setter.accept(new GenericStack(key, amount));
        returnToParent();
    }
}
