package mc.compendium.chestinterface.bukkit;

import mc.compendium.chestinterface.components.*;
import mc.compendium.chestinterface.events.TradeSelectEvent;
import mc.compendium.chestinterface.events.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.MerchantInventory;
import org.bukkit.plugin.java.JavaPlugin;

public record InterfaceEventBukkitListener(JavaPlugin plugin) implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onOpenInventory(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        HumanEntity player = event.getPlayer();

        boolean accepted = false;

        //

        if (event.getInventory() instanceof MerchantInventory merchantInventory) {
            Merchant merchant = merchantInventory.getMerchant();

            TradeInterface tradeInterface = TradeInterface.getAssociated(merchant);
            if (tradeInterface == null) return;

            accepted = tradeInterface.handle(new TradeInterfaceOpenEvent(event, player, merchantInventory, tradeInterface));
        } else {
            ChestInterface<?, ?> chestInterface = ChestInterface.getAssociated(inventory);
            if (chestInterface == null) return;

            if (chestInterface instanceof ChestMenu menu)
                accepted = menu.handle(new MenuOpenEvent(event, player, inventory, menu));
            else if (chestInterface instanceof AnvilInput anvilInput)
                accepted = anvilInput.handle(new AnvilInputOpenEvent(event, player, inventory, anvilInput));
        }

        //

        event.setCancelled(!accepted);
    }

    //

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onCloseInventory(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        HumanEntity player = event.getPlayer();

        ChestInterface<?, ?> chestInterface = null;

        boolean accepted = false;

        //

        if (event.getInventory() instanceof MerchantInventory merchantInventory) {
            Merchant merchant = merchantInventory.getMerchant();

            TradeInterface tradeInterface = TradeInterface.getAssociated(merchant);
            if (tradeInterface == null) return;

            accepted = tradeInterface.handle(new TradeInterfaceCloseEvent(event, player, merchantInventory, tradeInterface));
        } else {
            chestInterface = ChestInterface.getAssociated(inventory);
            if (chestInterface == null) return;

            if (chestInterface instanceof ChestMenu menu)
                accepted = menu.handle(new MenuCloseEvent(event, player, inventory, menu));
            else if (chestInterface instanceof AnvilInput anvilInput)
                accepted = anvilInput.handle(new AnvilInputCloseEvent(event, player, inventory, anvilInput));
        }

        //

        final ChestInterface<?, ?> finalChestInterface = chestInterface;
        if (!accepted) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(
                    this.plugin(), () -> {
                        if (finalChestInterface != null && !finalChestInterface.equals(ChestInterface.getAssociated(player.getOpenInventory().getTopInventory())))
                            player.openInventory(inventory);
                    },
                    1L
            );
        }
    }

    //

    @EventHandler(priority = EventPriority.HIGHEST)
    private void onClickInventory(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        HumanEntity entity = event.getWhoClicked();

        boolean accepted = true;

        //

        if (event.getInventory() instanceof MerchantInventory merchantInventory) {
            Merchant merchant = merchantInventory.getMerchant();

            TradeInterface tradeInterface = TradeInterface.getAssociated(merchant);
            if (tradeInterface == null) return;

            if (InventoryType.SlotType.RESULT.equals(event.getSlotType())) {
                TradeEvent tradeEvent = new TradeEvent(event, entity, merchantInventory, tradeInterface);
                accepted = tradeInterface.handle(tradeEvent);
            }
        } else {
            ChestInterface<?, ?> chestInterface = ChestInterface.getAssociated(inventory);
            if (chestInterface == null) return;

            ChestIcon icon = null;
            ChestInterfaceEvent<?> chestInterfaceEvent = null;

            int slot = event.getSlot();
            ClickType mouseClick = event.getClick();

            if (chestInterface instanceof ChestMenu menu) {
                icon = menu.getIcon(slot);
                chestInterfaceEvent = new MenuClickEvent(event, entity, inventory, menu, event.getCurrentItem(), slot, mouseClick);
            } else if (chestInterface instanceof AnvilInput anvilInput) {
                icon = anvilInput.config().inputIcon();
                chestInterfaceEvent = new AnvilInputClickEvent(event, entity, inventory, anvilInput, event.getCurrentItem(), slot, mouseClick);
            }

            if (icon != null)
                accepted = icon.handle(new ChestIconClickEvent(event, entity, inventory, chestInterface, icon, event.getCurrentItem(), slot, mouseClick));

            if (chestInterfaceEvent != null) {
                chestInterfaceEvent.setCancelled(accepted);
                accepted = chestInterface.handle(chestInterfaceEvent);
            }
        }

        //

        event.setCancelled(!accepted);
    }

    @EventHandler
    private void onSelectTrade(org.bukkit.event.inventory.TradeSelectEvent event) {
        MerchantInventory inventory = event.getInventory();
        Merchant merchant = inventory.getMerchant();
        HumanEntity player = event.getWhoClicked();

        TradeInterface tradeInterface = TradeInterface.getAssociated(merchant);
        if (tradeInterface == null) return;

        boolean accepted = true;

        //

        TradeSelectEvent tradeSelectEvent = new TradeSelectEvent(event, player, inventory, tradeInterface);
        accepted = tradeInterface.handle(tradeSelectEvent);

        //

        event.setCancelled(!accepted);
    }

}
