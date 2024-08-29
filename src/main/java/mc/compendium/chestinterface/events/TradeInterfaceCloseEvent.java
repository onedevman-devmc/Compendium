package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.MerchantInventory;

public class TradeInterfaceCloseEvent extends TradeInterfaceEvent<InventoryCloseEvent> {

    public TradeInterfaceCloseEvent(InventoryCloseEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public TradeInterfaceCloseEvent(InventoryCloseEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);
    }

}
