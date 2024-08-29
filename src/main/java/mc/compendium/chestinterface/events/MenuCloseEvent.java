package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class MenuCloseEvent extends BasicMenuEvent<InventoryCloseEvent> {

    public MenuCloseEvent(InventoryCloseEvent bukkitEvent, HumanEntity entity, Inventory inventory, ChestMenu menu) {
        super(bukkitEvent, entity, inventory, menu);
    }

}
