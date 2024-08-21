package mc.compendium.chestinterface.bukkit;

import mc.compendium.chestinterface.components.AnvilInput;
import mc.compendium.chestinterface.components.ChestIcon;
import mc.compendium.chestinterface.components.ChestInterface;
import mc.compendium.chestinterface.components.ChestMenu;
import mc.compendium.chestinterface.events.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public class BukkitChestInterfaceEventListener implements Listener {

    private final JavaPlugin plugin;

    //

    public BukkitChestInterfaceEventListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    //

    public JavaPlugin getPlugin() { return plugin; }

    //

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onOpen(InventoryOpenEvent event) { this.handle(event); }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent event) { this.handle(event); }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onIconClick(InventoryClickEvent event) { this.handle(event); }

    //

    public void handle(Event e) {
        if(!(e instanceof InventoryEvent inventoryEvent)) return;

        Inventory inventory = inventoryEvent.getInventory();

        ChestInterface<? extends ChestInterfaceEvent> chestInterface = ChestInterface.getAssociated(inventory);

        if(chestInterface == null) return;

        boolean cancelled = false;

        if(inventoryEvent instanceof InventoryOpenEvent event) {
            if(chestInterface instanceof ChestMenu menu)
                event.setCancelled(menu.call(new ChestMenuOpenEvent(event, event.getPlayer(), inventory, menu)));
            else if(chestInterface instanceof AnvilInput anvil_input)
                event.setCancelled(anvil_input.call(new AnvilInputOpenEvent(event, event.getPlayer(), inventory, anvil_input)));
        }

        //

        else if(inventoryEvent instanceof InventoryCloseEvent event) {
            if (chestInterface instanceof ChestMenu menu)
                cancelled = menu.call(new ChestMenuCloseEvent(event, event.getPlayer(), inventory, menu));
            else if (chestInterface instanceof AnvilInput anvil_input)
                cancelled = anvil_input.call(new AnvilInputCloseEvent(event, event.getPlayer(), inventory, anvil_input));

            if (cancelled) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(
                    this.getPlugin(), () -> {
                        if(!chestInterface.equals(ChestInterface.getAssociated(event.getPlayer().getOpenInventory().getTopInventory())))
                            event.getPlayer().openInventory(inventory);
                    },
                    1L
                );
            }
        }

        //

        else if(inventoryEvent instanceof InventoryClickEvent event) {
            cancelled = true;

            ChestIcon icon = null;
            ChestInterfaceEvent chestInterfaceEvent = null;

            int slot = event.getSlot();
            ClickType mouse_click = event.getClick();
            HumanEntity entity = event.getWhoClicked();

            if(chestInterface instanceof ChestMenu menu) {
                icon = menu.getIcon(slot);
                chestInterfaceEvent = new ChestMenuClickEvent(event, entity, inventory, menu, event.getCurrentItem(), slot, mouse_click);
            }
            else if(chestInterface instanceof AnvilInput anvil_input) {
                icon = anvil_input.config.inputIcon();
                chestInterfaceEvent = new AnvilInputClickEvent(event, entity, inventory, anvil_input, event.getCurrentItem(), slot, mouse_click);
            }

            if (icon != null)
                cancelled = !icon.call(new ChestIconClickEvent(event, entity, inventory, chestInterface, icon, event.getCurrentItem(), slot, mouse_click));

            if(chestInterfaceEvent != null) {
                chestInterfaceEvent.setCancelled(!cancelled);
                cancelled = !chestInterface.call(chestInterfaceEvent);
            }

            event.setCancelled(cancelled);
        }

//        else if(inventoryEvent instanceof PrepareAnvilEvent event) {
//
//        }
    }

}
