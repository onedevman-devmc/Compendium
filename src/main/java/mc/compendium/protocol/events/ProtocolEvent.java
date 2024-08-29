package mc.compendium.protocol.events;

import mc.compendium.events.WrapperEvent;

public class ProtocolEvent<T> extends WrapperEvent<T> {

    protected ProtocolEvent() {
        this(true);
    }

    protected ProtocolEvent(boolean cancellable) {
        this(null, cancellable);
    }

    protected ProtocolEvent(T wrapped) {
        this(wrapped, true);
    }

    protected ProtocolEvent(T wrapped, boolean cancellable) {
        super(wrapped, cancellable);
    }

}
