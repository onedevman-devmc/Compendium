package mc.compendium.chestinterface.events;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public interface ItemClickEvent {

    ItemStack getCursorItem();

    ItemStack getClickedItem();

    Inventory getClickedInventory();

    int getSlot();

    InventoryType.SlotType getSlotType();

    ClickType getClickType();

}
