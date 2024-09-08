package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;

public class AnvilInputEvent<
    OriginalEventType
> extends ChestInterfaceEvent<OriginalEventType> {

    public AnvilInputEvent(OriginalEventType originalEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput) {
        this(originalEvent, entity, inventory, anvilInput, true);
    }

    public AnvilInputEvent(OriginalEventType originalEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, boolean cancellable) {
        super(originalEvent, entity, inventory, anvilInput, cancellable);
    }

}
