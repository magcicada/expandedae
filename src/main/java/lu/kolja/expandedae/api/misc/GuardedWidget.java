package lu.kolja.expandedae.api.misc;

import appeng.client.gui.style.WidgetStyle;
import net.minecraft.client.gui.components.AbstractWidget;
import org.jetbrains.annotations.UnknownNullability;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public record GuardedWidget<W extends AbstractWidget>(@UnknownNullability W widget) {
    /**
     * Serves as a guard against adding a widget that doesn't exist in the JSON,
     * so we don't crash the client or fail to open the screen
     * @param widgetId The id of the widget, to be checked against the currently present ones in the JSON
     * @param widgets The ScreenStyle, containing all currently present widgets in the screen JSON
     * @param widgetSupplier The supplier of the widget, which will be added to the screen if it does exist in the JSON
     * @return The widget, if it does exist in the JSON, or null if it doesn't
     * @param <T> The type of the widget
     */
    public static <T extends AbstractWidget> GuardedWidget<T> guardedWidget(String widgetId, Map<String, WidgetStyle> widgets, Function<String, T> widgetSupplier) {
        if (widgets.get(widgetId) == null) return new GuardedWidget<>(null);
        return new GuardedWidget<>(widgetSupplier.apply(widgetId));
    }

    /**
     * Runs the given consumer if the widget is not null
     * @param consumer The consumer to run if the widget is not null
     * @return This object
     */
    public GuardedWidget<W> runIfPresent(Consumer<W> consumer) {
        if (widget != null)
            consumer.accept(this.widget);
        return this;
    }

    public boolean isPresent() {
        return widget != null;
    }
}
