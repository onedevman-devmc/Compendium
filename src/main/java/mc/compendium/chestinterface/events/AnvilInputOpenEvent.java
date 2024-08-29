package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class AnvilInputOpenEvent extends AnvilInputEvent {

    public AnvilInputOpenEvent(Event bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput) {
        super(bukkitEvent, entity, inventory, anvilInput);
    }

    //

    @Override
    public InventoryOpenEvent bukkitEvent() { return (InventoryOpenEvent) super.bukkitEvent(); }

}
