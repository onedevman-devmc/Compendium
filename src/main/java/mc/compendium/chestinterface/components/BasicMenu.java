package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.components.configurations.MenuConfig;
import mc.compendium.chestinterface.events.BasicMenuEvent;
import mc.compendium.chestinterface.events.InterfaceEventListener;
import mc.compendium.chestinterface.events.MenuClickEvent;
import mc.compendium.chestinterface.events.MenuCloseEvent;
import mc.compendium.events.EventHandler;
import mc.compendium.events.EventHandlerPriority;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class BasicMenu<
    ConfigType extends MenuConfig,
    IconType extends AbstractChestIcon<?>,
    EventType extends BasicMenuEvent<?, ?>
> extends ChestInterface<ConfigType, EventType> implements InterfaceEventListener {

    @EventHandler(priority = EventHandlerPriority.HIGHEST)
    public void onClose(MenuCloseEvent<?> event) {
        if(event.getPlayer() instanceof Player && !this.config().silent())
            ChestInterface.playInteractionSound((Player) event.getPlayer());
    }

    @EventHandler(priority = EventHandlerPriority.HIGHEST)
    public void onClick(MenuClickEvent<?> event) {
        if(event.getPlayer() instanceof Player && !this.config().silent() && this.getIcon(event.getSlot()) != null)
            ChestInterface.playInteractionSound((Player) event.getPlayer());
    }

    //

    private final Map<Integer, IconType> icons = new HashMap<>();

    //

    public BasicMenu(ConfigType config, Class<EventType> eventType) {
        super(config, eventType);

        //

        this.addListener(this);
    }

    //

    public Map<Integer, IconType> icons() { return Collections.unmodifiableMap(this.icons); }

    //

    public boolean isSlotEmpty(int slot) {
        if(this.icons.containsKey(slot))
            return false;

        return this.icons.get(slot) == null;
    }

    //

    public IconType getIcon(int row, int col) {
        return this.getIcon((9 * row) + col);
    }

    public IconType getIcon(int slot) {
        if(this.isSlotEmpty(slot))
            return null;

        return this.icons.get(slot);
    }

    //

    public void setIcon(int row, int col, IconType icon) {
        this.setIcon((9 * row) + col, icon);
    }

    public void setIcon(int slot, IconType icon) {
        if(!this.isSlotEmpty(slot))
            this.icons.remove(slot);

        this.icons.put(slot, icon);
    }

    //

    public void removeIcon(int slot) {
        this.icons.remove(slot);
    }

    //

    public abstract Inventory toBukkit();

}
