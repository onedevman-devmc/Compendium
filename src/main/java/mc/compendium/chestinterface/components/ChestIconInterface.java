package mc.compendium.chestinterface.components;

import mc.compendium.chestinterface.events.ChestInterfaceEvent;
import mc.compendium.chestinterface.events.ChestInterfaceEventManager;
import org.bukkit.inventory.ItemStack;

public abstract class ChestIconInterface<EventType extends ChestInterfaceEvent> extends ChestInterfaceEventManager<EventType> {

    public abstract ItemStack toBukkit();

}
