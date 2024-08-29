package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.ChestMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class MenuClickEvent extends BasicMenuEvent<InventoryClickEvent> {

    private final ItemStack clickedItem;
    private final int slot;
    private final ClickType mouseClick;

    //

    public MenuClickEvent(InventoryClickEvent bukkitEvent, HumanEntity entity, Inventory inventory, ChestMenu menu, ItemStack clickedItem, int slot, ClickType mouseClick) {
        super(bukkitEvent, entity, inventory, menu);

        this.clickedItem = clickedItem;
        this.slot = slot;
        this.mouseClick = mouseClick;
    }

    //

    public ItemStack clickedItem() { return this.clickedItem; }

    public int slot() { return this.slot; }

    public ClickType mouseClick() { return this.mouseClick; }

}
