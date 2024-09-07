package mc.compendium.events;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

public abstract class WrapperEvent<T> extends Event {

    private final T wrapped;

    //

    protected WrapperEvent(T wrapped) {
        this(wrapped, true);
    }

    protected WrapperEvent(T wrapped, boolean cancellable) {
        super(cancellable);
        this.wrapped = wrapped;
    }

    //

    public T getWrapped() { return wrapped; }

    //

    @Override
    public boolean isCompatible(Type eventType) {
        if(!(
            eventType instanceof ParameterizedType wrapperEventType
            && WrapperEvent.class.isAssignableFrom((Class<?>) wrapperEventType.getRawType())
            && ((Class<?>) wrapperEventType.getRawType()).isAssignableFrom(this.getClass())
        )) return false;

        if(wrapperEventType.getActualTypeArguments().length == 0) return true;
        Type typeArgument = wrapperEventType.getActualTypeArguments()[0];
        if(typeArgument instanceof WildcardType) return true;

        Class<?> wrappedType = (Class<?>) (typeArgument instanceof ParameterizedType parameterized ? parameterized.getRawType() : typeArgument);
        return wrappedType.isAssignableFrom(this.getWrapped().getClass());
    }

}
