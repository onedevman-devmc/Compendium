package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

public class AnvilInputEvent extends ChestInterfaceEvent {

    private final AnvilInput _anvil_input;

    //

    public AnvilInputEvent(Event bukkit_event, HumanEntity entity, Inventory inventory, AnvilInput anvil_input) {
        super(bukkit_event, entity, inventory, anvil_input);

        this._anvil_input = anvil_input;
    }

    //

    public AnvilInput anvilInput() { return this._anvil_input; }

}
