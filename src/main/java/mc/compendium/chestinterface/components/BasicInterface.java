package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.events.InterfaceEvent;
import mc.compendium.chestinterface.events.InterfaceEventManager;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public abstract class BasicInterface<
    BukkitType,
    InventoryType extends Inventory,
    EventType extends InterfaceEvent<
        ?,
        InventoryType,
        ? extends BasicInterface<?, InventoryType, ?>
    >
> extends InterfaceEventManager<EventType> {

    public static void playInteractionSound(Player player) {
        player.playSound(player.getLocation(), Sound.BLOCK_DISPENSER_DISPENSE, 1, 1);
    }

    //

    protected BasicInterface(Class<EventType> eventType) {
        super(eventType);
    }

    //

    public abstract BukkitType toBukkit();

}