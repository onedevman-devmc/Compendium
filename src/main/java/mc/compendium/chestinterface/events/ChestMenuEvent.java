package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;

public class ChestMenuEvent extends ChestInterfaceEvent {

    private final ChestMenu _menu;

    //

    public ChestMenuEvent(Event bukkit_event, HumanEntity entity, Inventory inventory, ChestMenu menu) {
        super(bukkit_event, entity, inventory, menu);

        this._menu = menu;
    }

    //

    public ChestMenu menu() { return this._menu; }

    //

    @Override
    public InventoryEvent bukkitEvent() { return (InventoryEvent) super.bukkitEvent(); }

}
