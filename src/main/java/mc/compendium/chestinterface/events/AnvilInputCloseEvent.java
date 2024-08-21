package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class AnvilInputCloseEvent extends AnvilInputEvent {

    public AnvilInputCloseEvent(Event bukkit_event, HumanEntity entity, Inventory inventory, AnvilInput anvil_input) {
        super(bukkit_event, entity, inventory, anvil_input);
    }

    //

    @Override
    public InventoryCloseEvent bukkitEvent() { return (InventoryCloseEvent) super.bukkitEvent(); }

}
