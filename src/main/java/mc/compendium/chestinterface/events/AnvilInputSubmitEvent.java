package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.Inventory;

public class AnvilInputSubmitEvent extends AnvilInputEvent {

    private final String text;

    //

    public AnvilInputSubmitEvent(Event bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, String text) {
        super(bukkitEvent, entity, inventory, anvilInput);

        this.text = text;
    }

    //

    public String text() { return this.text; }

}
