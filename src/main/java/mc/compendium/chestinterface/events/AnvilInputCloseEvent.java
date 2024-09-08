package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class AnvilInputCloseEvent extends AnvilInputEvent<InventoryCloseEvent> {

    public AnvilInputCloseEvent(InventoryCloseEvent originalEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput) {
        this(originalEvent, entity, inventory, anvilInput, true);
    }

    public AnvilInputCloseEvent(InventoryCloseEvent originalEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, boolean cancellable) {
        super(originalEvent, entity, inventory, anvilInput, cancellable);
    }

}
