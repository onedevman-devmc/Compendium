package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.AbstractChestIcon;
import mc.compendium.chestinterface.components.HopperMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class HopperMenuClickEvent extends MenuClickEvent<HopperMenu<?>> {

    public HopperMenuClickEvent(
        InventoryClickEvent originalEvent, HumanEntity entity, Inventory inventory, HopperMenu<?> menu,
        ItemStack cursorItem, ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction action,
        AbstractChestIcon<?> icon
    ) {
        this(originalEvent, entity, inventory, menu, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action, icon, true);
    }

    public HopperMenuClickEvent(
        InventoryClickEvent originalEvent, HumanEntity entity, Inventory inventory, HopperMenu<?> menu,
        ItemStack cursorItem, ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction action,
        AbstractChestIcon<?> icon,
        boolean cancellable
    ) {
        super(originalEvent, entity, inventory, menu, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action, icon, cancellable);
    }

}
