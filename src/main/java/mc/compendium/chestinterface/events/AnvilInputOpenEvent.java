package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

public class AnvilInputOpenEvent extends AnvilInputEvent<InventoryOpenEvent> {

    public AnvilInputOpenEvent(InventoryOpenEvent originalEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput) {
        this(originalEvent, entity, inventory, anvilInput, true);
    }

    public AnvilInputOpenEvent(InventoryOpenEvent originalEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, boolean cancellable) {
        super(originalEvent, entity, inventory, anvilInput, cancellable);
    }

}
