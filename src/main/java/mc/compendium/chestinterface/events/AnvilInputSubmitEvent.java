package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class AnvilInputSubmitEvent extends AnvilInputEvent<InventoryClickEvent> {

    private final String text;

    //

    public AnvilInputSubmitEvent(InventoryClickEvent originalEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, String text) {
        this(originalEvent, entity, inventory, anvilInput, text, true);
    }

    public AnvilInputSubmitEvent(InventoryClickEvent originalEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, String text, boolean cancellable) {
        super(originalEvent, entity, inventory, anvilInput, cancellable);

        //

        this.text = text;
    }

    //

    public String getText() { return this.text; }

}
