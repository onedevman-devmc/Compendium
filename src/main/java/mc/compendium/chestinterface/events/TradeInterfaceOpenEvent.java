package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.MerchantInventory;

public class TradeInterfaceOpenEvent extends TradeInterfaceEvent<InventoryOpenEvent> {

    public TradeInterfaceOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public TradeInterfaceOpenEvent(InventoryOpenEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);
    }

}
