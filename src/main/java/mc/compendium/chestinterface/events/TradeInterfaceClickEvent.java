package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import mc.compendium.chestinterface.components.TradeSlotType;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.MerchantInventory;

public class TradeInterfaceClickEvent extends TradeInterfaceEvent<InventoryClickEvent> implements ItemClickEvent {

    private final ItemStack cursorItem;
    private final ItemStack clickedItem;
    private final Inventory clickedInventory;
    private final int slot;
    private final InventoryType.SlotType slotType;
    private final ClickType clickType;
    private final InventoryAction action;
    private final TradeSlotType tradeSlotType;

    //

    public TradeInterfaceClickEvent(
        InventoryClickEvent originalEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface,
        ItemStack cursorItem, ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction action,
        TradeSlotType tradeSlotType
    ) {
        this(originalEvent, entity, inventory, tradeInterface, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action, tradeSlotType, true);
    }

    public TradeInterfaceClickEvent(
        InventoryClickEvent originalEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface<?> tradeInterface,
        ItemStack cursorItem, ItemStack clickedItem, Inventory clickedInventory, int slot, InventoryType.SlotType slotType, ClickType clickType, InventoryAction action,
        TradeSlotType tradeSlotType,
        boolean cancellable
    ) {
        super(originalEvent, entity, inventory, tradeInterface, cancellable);

        //

        this.cursorItem = cursorItem;
        this.clickedItem = clickedItem;
        this.clickedInventory = clickedInventory;
        this.slot = slot;
        this.slotType = slotType;
        this.clickType = clickType;
        this.action = action;
        this.tradeSlotType = tradeSlotType;
    }

    //

    @Override
    public ItemStack getCursorItem() { return this.cursorItem; }

    @Override
    public ItemStack getClickedItem() { return this.clickedItem; }

    @Override
    public Inventory getClickedInventory() { return this.clickedInventory; }

    @Override
    public int getSlot() { return this.slot; }

    @Override
    public InventoryType.SlotType getSlotType() { return this.slotType; }

    @Override
    public ClickType getClickType() { return this.clickType; }

    @Override
    public InventoryAction getAction() { return this.action; }

    //

    public TradeSlotType getTradeSlotType() { return this.tradeSlotType; }

}
