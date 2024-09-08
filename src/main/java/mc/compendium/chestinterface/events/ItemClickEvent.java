package mc.compendium.chestinterface.events;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public interface ItemClickEvent {

    ItemStack getCursorItem();

    ItemStack getClickedItem();

    Inventory getInventory();

    Inventory getClickedInventory();

    int getSlot();

    InventoryType.SlotType getSlotType();

    ClickType getClickType();

    InventoryAction getAction();

    //

    default boolean isInterfaceClick() {
        return Objects.equals(this.getInventory(), this.getClickedInventory());
    }

    default boolean isClickedItemEmpty() {
        ItemStack clickedItem = this.getClickedItem();
        return clickedItem == null || clickedItem.getType().equals(Material.AIR);
    }

}
