package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

public class AnvilInputEvent<
    BukkitEventType extends org.bukkit.event.Event
> extends ChestInterfaceEvent<BukkitEventType> {

    private final AnvilInput anvilInput;

    //

    public AnvilInputEvent(BukkitEventType bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput) {
        super(bukkitEvent, entity, inventory, anvilInput);

        this.anvilInput = anvilInput;
    }

    //

    public AnvilInput anvilInput() { return this.anvilInput; }

}
