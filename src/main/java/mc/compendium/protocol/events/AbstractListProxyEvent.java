package mc.compendium.protocol.events;

import mc.compendium.events.Event;

public abstract class AbstractListProxyEvent<T> extends Event {

    private final T item;

    //

    protected AbstractListProxyEvent(T item) {
        this(item, true);
    }

    protected AbstractListProxyEvent(T item, boolean cancellable) {
        super(cancellable);
        this.item = item;
    }

    //

    public T getItem() { return this.item; }

}
