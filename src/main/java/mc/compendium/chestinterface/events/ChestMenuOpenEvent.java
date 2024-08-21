package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class ChestMenuOpenEvent extends ChestMenuEvent {

    public ChestMenuOpenEvent(Event bukkit_event, HumanEntity entity, Inventory inventory, ChestMenu menu) {
        super(bukkit_event, entity, inventory, menu);
    }

    //

    @Override
    public InventoryOpenEvent bukkitEvent() { return (InventoryOpenEvent) super.bukkitEvent(); }

}
