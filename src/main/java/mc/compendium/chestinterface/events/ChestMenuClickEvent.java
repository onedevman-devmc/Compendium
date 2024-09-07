package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AbstractChestIcon;
import mc.compendium.chestinterface.components.ChestMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestMenuClickEvent extends MenuClickEvent<ChestMenu<?>> {

    public ChestMenuClickEvent(
        InventoryClickEvent bukkitEvent, HumanEntity entity, Inventory inventory, ChestMenu<?> menu,
        ItemStack cursorItem, ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType,
        AbstractChestIcon<?> icon
    ) {
        this(bukkitEvent, entity, inventory, menu, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, icon, true);
    }

    public ChestMenuClickEvent(
        InventoryClickEvent bukkitEvent, HumanEntity entity, Inventory inventory, ChestMenu<?> menu,
        ItemStack cursorItem, ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType,
        AbstractChestIcon<?> icon,
        boolean cancellable
    ) {
        super(bukkitEvent, entity, inventory, menu, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, icon, cancellable);
    }

}