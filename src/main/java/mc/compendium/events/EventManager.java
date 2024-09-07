package mc.compendium.events;

import mc.compendium.reflection.MethodUtil;
import mc.compendium.types.Pair;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class EventManager<
    EventType extends Event,
    EventListenerType extends EventListener
> implements EventManagerDelegation<EventType, EventListenerType> {

    //

    private final List<EventListenerType> listeners = new ArrayList<>();
    private final Map<EventHandlerPriority, Map<Integer, Pair<EventListenerType, Method>>> prioritizedEventHandlerMap;

    private final Class<? extends Event> eventType;

    //

    public EventManager(Class<EventType> eventType) {
        this.eventType = eventType;

        //

        Map<EventHandlerPriority, Map<Integer, Pair<EventListenerType, Method>>> result = new HashMap<>();

        for(EventHandlerPriority priority : EventHandlerPriority.DECREASING_ORDER)
            result.put(priority, new HashMap<>());



        this.prioritizedEventHandlerMap = Collections.unmodifiableMap(result);
    }

    //

    private boolean isEventHandler(Method method) {
        if(!method.isAnnotationPresent(EventHandler.class) || method.getParameterCount() != 1) return false;

        Type parameterType = method.getParameters()[0].getParameterizedType();
        Class<?> rawParameterType = (Class<?>) (parameterType instanceof ParameterizedType parameterizedParameterType ? parameterizedParameterType.getRawType() : parameterType);

        return eventType.isAssignableFrom(rawParameterType);
    }

    private List<Method> getEventHandlers(EventListenerType listener) {
        return MethodUtil.getInstance().filter(listener, this::isEventHandler);
    }

    private void mapEventHandlers(EventListenerType listener) {
        for(Method eventHandler : getEventHandlers(listener))
            this.prioritizedEventHandlerMap.get(eventHandler.getAnnotation(EventHandler.class).priority()).put(eventHandler.hashCode(), Pair.of(listener, eventHandler));
    }

    private void unmapEventHandlers(EventListenerType listener) {
        for(Method eventHandler : getEventHandlers(listener))
            this.prioritizedEventHandlerMap.get(eventHandler.getAnnotation(EventHandler.class).priority()).remove(eventHandler.hashCode());
    }

    private void unmapAllEventHandlers() {
        for(EventHandlerPriority priority : EventHandlerPriority.DECREASING_ORDER)
            this.prioritizedEventHandlerMap.get(priority).clear();
    }

    //

    @Override
    public EventManager<EventType, EventListenerType> delegate() {
        return this;
    }

    //

    @Override
    public List<EventListenerType> getListeners() {
        return Collections.unmodifiableList(this.listeners);
    }

    @Override
    public void addListener(EventListenerType listener) {
        if(this.listeners.contains(listener)) return;

        this.mapEventHandlers(listener);
        this.listeners.add(listener);
    }

    @Override
    public void removeListener(EventListenerType listener) {
        if(!this.listeners.contains(listener)) return;

        this.mapEventHandlers(listener);
        this.listeners.remove(listener);
    }

    @Override
    public void removeAllListeners() {
        this.listeners.clear();
        this.unmapAllEventHandlers();
    }

    //

    /*
     * Call event among all registered listeners and returns if event is accepted (not cancelled).
     */
    @Override
    public boolean handle(EventType event) {
        for(EventHandlerPriority priority : EventHandlerPriority.DECREASING_ORDER) {
            Collection<Pair<EventListenerType, Method>> eventHandlerPairs = this.prioritizedEventHandlerMap.get(priority).values();

            for(Pair<EventListenerType, Method> eventHandlerPair : eventHandlerPairs) {
                EventListenerType listener = eventHandlerPair.first();
                Method eventHandler = eventHandlerPair.last();

                Class<?> eventType = event.getClass();
                Type eventHandlerParameterType = eventHandler.getParameters()[0].getParameterizedType();

                if(event.isCompatible(eventHandlerParameterType)) {
                    EventHandler eventHandlerProperties = eventHandler.getAnnotation(EventHandler.class);

                    if(!event.cancellable() || !event.cancelled() || eventHandlerProperties.ignoreCancelled()) {
                        try {
                            MethodUtil.getInstance().invoke(listener, eventHandler, event);
                        } catch(Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }

        return !event.cancelled();
    }

    @Override
    public <E> boolean handle(E event) {
        return this.handle((EventType) event);
    }

}
