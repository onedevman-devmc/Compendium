package mc.compendium.chestinterface.bukkit;

import mc.compendium.chestinterface.ChestInterfaceApi;
import mc.compendium.chestinterface.components.*;
import mc.compendium.chestinterface.events.TradeSelectEvent;
import mc.compendium.chestinterface.events.*;
import mc.compendium.utils.bukkit.Inventories;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record InterfaceEventBukkitListener(ChestInterfaceApi api) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onOpenInventory(InventoryOpenEvent event) {
        if(!this.api().enabled()) return;

        //

        Inventory inventory = event.getInventory();
        HumanEntity player = event.getPlayer();

        boolean accepted = false;

        //

        if (event.getInventory() instanceof MerchantInventory merchantInventory) {
            Merchant merchant = merchantInventory.getMerchant();

            TradeInterface<?> tradeInterface = TradeInterface.getAssociated(merchant);
            if (tradeInterface == null) return;

            accepted = tradeInterface.handle(new TradeInterfaceOpenEvent(event, player, merchantInventory, tradeInterface));
        } else {
            ChestInterface<?, ?> chestInterface = ChestInterface.getAssociated(inventory);
            if (chestInterface == null) return;

            if (chestInterface instanceof ChestMenu<?> menu)
                accepted = menu.handle(new ChestMenuOpenEvent(event, player, inventory, menu));
            else if (chestInterface instanceof HopperMenu<?> menu)
                accepted = menu.handle(new HopperMenuOpenEvent(event, player, inventory, menu));
            else if (chestInterface instanceof AnvilInput anvilInput)
                accepted = anvilInput.handle(new AnvilInputOpenEvent(event, player, inventory, anvilInput));
        }

        //

        event.setCancelled(!accepted);
    }

    //

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onCloseInventory(InventoryCloseEvent event) {
        if(!this.api().enabled()) return;

        //

        Inventory inventory = event.getInventory();
        HumanEntity player = event.getPlayer();

        BasicInterface<?, ?, ?> basicInterface = null;

        boolean accepted = false;

        //

        if (event.getInventory() instanceof MerchantInventory merchantInventory) {
            Merchant merchant = merchantInventory.getMerchant();

            TradeInterface<?> tradeInterface = TradeInterface.getAssociated(merchant);
            basicInterface = tradeInterface;

            if (tradeInterface == null) return;

            accepted = tradeInterface.handle(new TradeInterfaceCloseEvent(event, player, merchantInventory, tradeInterface));
        } else {
            ChestInterface<?, ?> chestInterface = ChestInterface.getAssociated(inventory);
            basicInterface = chestInterface;

            if (chestInterface == null) return;

            if (chestInterface instanceof ChestMenu<?> menu)
                accepted = menu.handle(new ChestMenuCloseEvent(event, player, inventory, menu));
            else if (chestInterface instanceof HopperMenu<?> menu)
                accepted = menu.handle(new HopperMenuCloseEvent(event, player, inventory, menu));
            else if (chestInterface instanceof AnvilInput anvilInput)
                accepted = anvilInput.handle(new AnvilInputCloseEvent(event, player, inventory, anvilInput));
        }

        //

        final BasicInterface<?, ?, ?> finalChestInterface = basicInterface;
        if (!accepted) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                this.api().getPlugin(), () -> {
                    if (!finalChestInterface.equals(ChestInterface.getAssociated(player.getOpenInventory().getTopInventory())))
                        player.openInventory(inventory);
                },
                1L
            );
        }
    }

    //

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onClickInventory(InventoryClickEvent event) {
        if(!this.api().enabled()) return;

        //

        Inventory inventory = event.getInventory();
        Inventory clickedInventory = event.getClickedInventory();

        boolean clickedInInterfaceInventory = inventory.equals(clickedInventory);

        HumanEntity player = event.getWhoClicked();

        boolean accepted = true;

        //

        ItemStack cursorItem = event.getCursor();
        ItemStack clickedItem = event.getCurrentItem();
        int slot = event.getSlot();
        InventoryType.SlotType slotType = event.getSlotType();
        ClickType clickType = event.getClick();
        InventoryAction action = event.getAction();
        TradeSlotType tradeSlotType = TradeSlotType.getBySlot(slot);

        //

        if (event.getInventory() instanceof MerchantInventory merchantInventory) {
            Merchant merchant = merchantInventory.getMerchant();

            TradeInterface<?> tradeInterface = TradeInterface.getAssociated(merchant);
            if (tradeInterface == null) return;

            TradeInterfaceClickEvent clickEvent = new TradeInterfaceClickEvent(
                event, player, merchantInventory, tradeInterface,
                cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action,
                tradeSlotType
            );
            accepted = tradeInterface.handle(clickEvent);

            if (InventoryType.SlotType.RESULT.equals(event.getSlotType())) {
                InventoryAction inventoryAction = event.getAction();

                int recipeIndex = merchantInventory.getSelectedRecipeIndex();
                Trade trade = tradeInterface.getTradeList().get(recipeIndex);

                synchronized (trade) {
                    int requestedUses = 0;

                    List<ItemStack> givenIngredients = new ArrayList<>();
                    for (int i = 0; i < 2; ++i) givenIngredients.add(merchantInventory.getItem(i));
                    givenIngredients = Collections.unmodifiableList(givenIngredients);

                    TradeAction tradeAction = TradeAction.UNKNOWN;

                    if (TradeEvent.SINGLE_TRADE_BUKKIT_ACTIONS.contains(inventoryAction)) {
                        tradeAction = TradeAction.SINGLE_TRADE;

                        requestedUses = 1;
                    } else if (TradeEvent.MULTIPLE_TRADE_BUKKIT_ACTIONS.contains(inventoryAction)) {
                        tradeAction = TradeAction.MULTIPLE_TRADE;
                        ItemStack tradeResultItem = trade.getResult();

                        requestedUses = TradeEvent.calculateMaxTheoricalRecipeUses(trade, givenIngredients.get(0), givenIngredients.get(1));

                        ItemStack theoricalResultItemStack = tradeResultItem.clone();
                        theoricalResultItemStack.setAmount(theoricalResultItemStack.getAmount() * requestedUses);

                        Inventory checkInventory = Bukkit.createInventory(null, 9 * 4);
                        checkInventory.setMaxStackSize(theoricalResultItemStack.getMaxStackSize());

                        requestedUses = Inventories.getContainableQuantityIn(
                            Inventories.copyContentInto(player.getInventory().getStorageContents(), checkInventory),
                            theoricalResultItemStack
                        ) / tradeResultItem.getAmount();
                    }

                    //

                    TradeEvent tradeEvent = new TradeEvent(
                        event, player, merchantInventory, tradeInterface,
                        cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action,
                        tradeSlotType, recipeIndex, trade, requestedUses, givenIngredients, tradeAction
                    );

                    if (requestedUses == 0) tradeEvent.setCancelled(true);
                    if (!tradeEvent.cancelled()) tradeEvent.setCancelled(clickEvent.cancelled());

                    accepted = tradeInterface.handle(tradeEvent);
                }
            }
        } else {
            ChestInterface<?, ?> chestInterface = ChestInterface.getAssociated(inventory);
            if (chestInterface == null) return;

            AbstractChestIcon<?> icon = null;
            ChestInterfaceEvent<?> chestInterfaceEvent = null;

            if (chestInterface instanceof ChestMenu<?> menu) {
                if(clickedInInterfaceInventory) icon = menu.getIcon(slot);
                chestInterfaceEvent = new ChestMenuClickEvent(event, player, inventory, menu, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action, icon);
            } else if (chestInterface instanceof HopperMenu<?> menu) {
                if(clickedInInterfaceInventory) icon = menu.getIcon(slot);
                chestInterfaceEvent = new HopperMenuClickEvent(event, player, inventory, menu, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action, icon);
            } else if (chestInterface instanceof AnvilInput anvilInput) {
                if(clickedInInterfaceInventory) icon = anvilInput.config().inputIcon();
                chestInterfaceEvent = new AnvilInputClickEvent(event, player, inventory, anvilInput, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action);
            }

            if (chestInterfaceEvent != null) {
                accepted = chestInterface.handle(chestInterfaceEvent);
            }

            if (icon != null) {
                ChestIconClickEvent iconClickEvent = new ChestIconClickEvent(event, player, inventory, chestInterface, icon, cursorItem, clickedItem, clickedInventory, slot, slotType, clickType, action);
                iconClickEvent.setCancelled(!accepted);
                accepted = icon.handle(iconClickEvent);
            }
        }

        //

        event.setCancelled(!accepted);
    }

    //

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onDragInInventory(InventoryDragEvent event) {
        if(Objects.equals(event.getInventorySlots(), event.getRawSlots()))
            this.cancelUnhandledInterfaceBukkitEvent(event);
    }

    //

    @EventHandler
    private void onSelectTrade(org.bukkit.event.inventory.TradeSelectEvent event) {
        if(!this.api().enabled()) return;

        //

        MerchantInventory inventory = event.getInventory();
        Merchant merchant = inventory.getMerchant();
        HumanEntity player = event.getWhoClicked();

        TradeInterface<?> tradeInterface = TradeInterface.getAssociated(merchant);
        if (tradeInterface == null) return;

        boolean accepted = true;

        //

        int tradeIndex = event.getIndex();
        Trade trade = tradeInterface.getTradeList().get(tradeIndex);

        TradeSelectEvent tradeSelectEvent = new TradeSelectEvent(event, player, inventory, tradeInterface, tradeIndex, trade);
        accepted = tradeInterface.handle(tradeSelectEvent);

        //

        event.setCancelled(!accepted);
    }

    //

    private void cancelUnhandledInterfaceBukkitEvent(Event event) {
        if(!(event instanceof InventoryEvent inventoryEvent)) return;
        if(!(inventoryEvent instanceof Cancellable)) return;

        if(!this.api().enabled()) return;

        //

        Inventory inventory = inventoryEvent.getInventory();
        BasicInterface<?, ?, ?> basicInterface = null;

        //

        if (inventoryEvent.getInventory() instanceof MerchantInventory merchantInventory) basicInterface = TradeInterface.getAssociated(merchantInventory.getMerchant());
        else basicInterface = ChestInterface.getAssociated(inventory);

        //

        ((Cancellable) inventoryEvent).setCancelled(basicInterface != null);
    }

}
