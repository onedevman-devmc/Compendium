package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.MerchantInventory;

public class TradeSelectEvent extends TradeInterfaceEvent {
    public TradeSelectEvent(Event bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public TradeSelectEvent(Event bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);
    }
}
