package mc.compendium.chestinterface.events;

import mc.compendium.chestinterface.components.BasicInterface;
import mc.compendium.events.EventManager;

public abstract class InterfaceEventManager<
    EventType extends InterfaceEvent<?, ?, ? extends BasicInterface<?, ?, ?>>
> extends EventManager<EventType, InterfaceEventListener> {

    protected InterfaceEventManager(Class<EventType> eventType) {
        super(eventType);
    }

}
