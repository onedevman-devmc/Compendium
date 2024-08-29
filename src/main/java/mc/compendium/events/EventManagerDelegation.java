package mc.compendium.events;

import java.util.List;

public interface EventManagerDelegation<
    EventType extends Event,
    EventListenerType extends EventListener
> extends EventManagerInterface<EventType, EventListenerType> {

    EventManagerDelegation<EventType, EventListenerType> delegate();

    //

    default List<EventListenerType> getListeners() { return this.delegate().getListeners(); }

    default void addListener(EventListenerType listener) { this.delegate().addListener(listener); }

    default void removeListener(EventListenerType listener) { this.delegate().removeListener(listener); }

    default void removeAllListeners() { this.delegate().removeAllListeners(); }

    //

    default boolean handle(EventType event) { return this.delegate().handle(event); }

    default <E> boolean handle(E event) { return this.delegate().handle(event); }

}
