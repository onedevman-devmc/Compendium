package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.TradeInterface;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.inventory.MerchantInventory;

import java.util.function.Consumer;

public class TradeInterfaceEvent<
    BukkitEventType extends org.bukkit.event.Event
> extends InterfaceEvent<BukkitEventType, MerchantInventory, TradeInterface> {

    public TradeInterfaceEvent(BukkitEventType bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface menu) {
        this(bukkitEvent, entity, inventory, menu, true);
    }

    public TradeInterfaceEvent(BukkitEventType bukkitEvent, HumanEntity entity, MerchantInventory inventory, TradeInterface menu, boolean cancellable) {
        super(bukkitEvent, entity, inventory, menu, cancellable);
    }

}
