package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class AnvilInputCloseEvent extends AnvilInputEvent {

    public AnvilInputCloseEvent(Event bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput) {
        super(bukkitEvent, entity, inventory, anvilInput);
    }

    //

    @Override
    public InventoryCloseEvent bukkitEvent() { return (InventoryCloseEvent) super.bukkitEvent(); }

}
