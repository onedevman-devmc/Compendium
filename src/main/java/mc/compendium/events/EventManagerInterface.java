package mc.compendium.events;

import java.util.List;

public interface EventManagerInterface<
    EventType extends Event,
    EventListenerType extends EventListener
> {

    List<EventListenerType> getListeners();

    void addListener(EventListenerType listener);

    void removeListener(EventListenerType listener);

    void removeAllListeners();

    //

    boolean handle(EventType event);

    <E> boolean handle(E event);

}
