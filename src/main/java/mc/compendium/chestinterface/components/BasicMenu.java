package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.components.configurations.MenuConfig;
import mc.compendium.chestinterface.events.MenuClickEvent;
import mc.compendium.chestinterface.events.MenuCloseEvent;
import mc.compendium.chestinterface.events.BasicMenuEvent;
import mc.compendium.chestinterface.events.InterfaceEventListener;
import mc.compendium.events.EventHandler;
import mc.compendium.events.EventHandlerPriority;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class BasicMenu<
    ConfigType extends MenuConfig,
    EventType extends BasicMenuEvent<?>
> extends ChestInterface<ConfigType, EventType> {

    private final Map<Integer, ChestIcon> icons = new HashMap<>();

    //

    public BasicMenu(ConfigType config, Class<EventType> eventType) {
        super(config, eventType);

        //

        BasicMenu<ConfigType, EventType> _this = this;

        this.addListener(new InterfaceEventListener() {
            @EventHandler(priority = EventHandlerPriority.HIGHEST)
            public void onClose(MenuCloseEvent event) {
                if(event.entity() instanceof Player && !_this.config().silent())
                    ChestInterface.playInteractionSound((Player) event.entity());
            }

            @EventHandler(priority = EventHandlerPriority.HIGHEST)
            public void onClick(MenuClickEvent event) {
                if(event.entity() instanceof Player && !_this.config().silent() && _this.getIcon(event.slot()) != null)
                    ChestInterface.playInteractionSound((Player) event.entity());
            }
        });
    }

    //

    public Map<Integer, ChestIcon> icons() { return Collections.unmodifiableMap(this.icons); }

    //

    public boolean isSlotEmpty(int slot) {
        if(this.icons.containsKey(slot))
            return false;

        return this.icons.get(slot) == null;
    }

    //

    public ChestIcon getIcon(int slot) {
        if(this.isSlotEmpty(slot))
            return null;

        return this.icons.get(slot);
    }

    //

    public void setIcon(int row, int col, ChestIcon icon) {
        this.setIcon(9*row+col, icon);
    }

    public void setIcon(int slot, ChestIcon icon) {
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
