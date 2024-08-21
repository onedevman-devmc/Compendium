package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class AnvilInputOpenEvent extends AnvilInputEvent {

    public AnvilInputOpenEvent(Event bukkit_event, HumanEntity entity, Inventory inventory, AnvilInput anvil_input) {
        super(bukkit_event, entity, inventory, anvil_input);
    }

    //

    @Override
    public InventoryOpenEvent bukkitEvent() { return (InventoryOpenEvent) super.bukkitEvent(); }

}
