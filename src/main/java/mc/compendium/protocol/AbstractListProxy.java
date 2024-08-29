package mc.compendium.protocol;

import mc.compendium.events.EventListener;
import mc.compendium.events.EventManager;
import mc.compendium.protocol.events.AbstractListProxyEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class AbstractListProxy<
    T,
    E extends AbstractListProxyEvent<T>,
    L extends EventListener
> extends EventManager<E, L> implements List<T> {

    private final List<T> proxied;

    //

    private final Class<? extends AbstractListProxyEvent<T>> addItemEventClass;
    private final Class<? extends AbstractListProxyEvent<T>> removeItemEventClass;

    //

    protected AbstractListProxy(Class<E> eventType, Class<? extends AbstractListProxyEvent<T>> addItemEventClass, Class<? extends AbstractListProxyEvent<T>> removeItemEventClass) {
        this(eventType, new ArrayList<>(), addItemEventClass, removeItemEventClass);
    }

    protected AbstractListProxy(Class<E> eventType, List<T> proxied, Class<? extends AbstractListProxyEvent<T>> addItemEventClass, Class<? extends AbstractListProxyEvent<T>> removeItemEventClass) {
        super(eventType);

        this.proxied = proxied;

        this.addItemEventClass = addItemEventClass;
        this.removeItemEventClass = removeItemEventClass;
    }

    //

    public List<T> getProxiedList() { return this.proxied; }

    //

    @Override
    public int size() { return proxied.size(); }

    @Override
    public boolean isEmpty() { return this.proxied.isEmpty(); }

    @Override
    public boolean contains(Object o) { return this.proxied.contains(o); }

    @Override
    public Iterator<T> iterator() { return this.proxied.iterator(); }

    @Override
    public Object[] toArray() { return this.proxied.toArray(); }

    @Override
    public <T> T[] toArray(T[] ts) { return this.proxied.toArray(ts); }

    @Override
    public boolean add(T item) {
        try {
            return this.handle(addItemEventClass.getConstructor(item.getClass()).newInstance(item)) && this.proxied.add(item);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(Object o) {
        try {
            if(!this.handle(removeItemEventClass.getConstructor(o.getClass()).newInstance(o))) return false;
            return this.proxied.remove(o);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean containsAll(Collection<?> collection) { return this.proxied.containsAll(collection); }

    @Override
    public boolean addAll(Collection<? extends T> collection) { throw new UnsupportedOperationException(); }

    @Override
    public boolean addAll(int i, Collection<? extends T> collection) { throw new UnsupportedOperationException(); }

    @Override
    public boolean removeAll(Collection<?> collection) { throw new UnsupportedOperationException(); }

    @Override
    public boolean retainAll(Collection<?> collection) { throw new UnsupportedOperationException(); }

    @Override
    public void clear() { throw new UnsupportedOperationException(); }

    @Override
    public T get(int i) { return this.proxied.get(i); }

    @Override
    public T set(int i, T item) {
        try {
            if(this.handle(addItemEventClass.getConstructor(item.getClass()).newInstance(item))) {
                T previous = this.proxied.set(i, item);
                this.handle(removeItemEventClass.getConstructor(previous.getClass(), boolean.class).newInstance(previous, false));
                return previous;
            }
            else throw new RejectedByEventException();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(int i, T item) {
        try {
            if(this.handle(addItemEventClass.getConstructor(item.getClass()).newInstance(item))) this.proxied.add(i, item);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T remove(int i) {
        try {
            T item = this.proxied.get(i);

            if(this.handle(removeItemEventClass.getConstructor(item.getClass()).newInstance(item))) return this.proxied.remove(i);
            else throw new RejectedByEventException();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int indexOf(Object o) { return this.proxied.indexOf(o); }

    @Override
    public int lastIndexOf(Object o) { return this.proxied.lastIndexOf(o); }

    @Override
    public ListIterator<T> listIterator() { return this.proxied.listIterator(); }

    @Override
    public ListIterator<T> listIterator(int i) { return this.proxied.listIterator(i); }

    @Override
    public List<T> subList(int i, int i1) { return this.proxied.subList(i, i1); }

}
