package mc.compendium.events;

import java.lang.reflect.Type;

public abstract class Event {

    private final boolean cancellable;
    private final Class<?> rawType = null;

    private boolean cancelled = false;

    //

    protected Event() {
        this(false);
    }

    protected Event(boolean cancellable) {
        this.cancellable = cancellable;
    }

    //

    public boolean cancellable() { return this.cancellable; }

    public boolean cancelled() { return this.cancelled; }

    public void setCancelled(boolean cancel) { this.cancelled = cancel; }

    //

    public boolean isCompatible(Type eventType) {
        return this.getClass().equals(eventType);
    }

}
