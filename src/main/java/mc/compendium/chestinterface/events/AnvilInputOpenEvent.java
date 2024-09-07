package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class AnvilInputOpenEvent extends AnvilInputEvent<InventoryOpenEvent> {

    public AnvilInputOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput) {
        this(bukkitEvent, entity, inventory, anvilInput, true);
    }

    public AnvilInputOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, boolean cancellable) {
        super(bukkitEvent, entity, inventory, anvilInput, cancellable);
    }

}
