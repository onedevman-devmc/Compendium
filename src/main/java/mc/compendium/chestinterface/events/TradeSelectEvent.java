package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.MerchantInventory;

public class TradeSelectEvent extends TradeInterfaceEvent<org.bukkit.event.inventory.TradeSelectEvent> {
    public TradeSelectEvent(org.bukkit.event.inventory.TradeSelectEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public TradeSelectEvent(org.bukkit.event.inventory.TradeSelectEvent bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);
    }
}
