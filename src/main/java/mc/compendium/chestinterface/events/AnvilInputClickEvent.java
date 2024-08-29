package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AnvilInput;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class AnvilInputClickEvent extends AnvilInputEvent {

    private final ItemStack clickedItem;
    private final int slot;
    private final ClickType mouseClick;

    //

    public AnvilInputClickEvent(Event bukkitEvent, HumanEntity entity, Inventory inventory, AnvilInput anvilInput, ItemStack clicked_item, int slot, ClickType mouseClick) {
        super(bukkitEvent, entity, inventory, anvilInput);

        this.clickedItem = clicked_item;
        this.slot = slot;
        this.mouseClick = mouseClick;
    }

    //

    public ItemStack clickedItem() { return this.clickedItem; }

    public int slot() { return this.slot; }

    public ClickType mouseClick() { return this.mouseClick; }

}
