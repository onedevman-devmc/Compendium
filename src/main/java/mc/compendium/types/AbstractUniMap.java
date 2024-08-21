package mc.compendium.types;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractUniMap<K, V, T extends AbstractUniMap<K, V, T>> {

    private final Map<K, V> map = new HashMap<>();
    public T superThis;

    //

    public AbstractUniMap() {
        this.superThis = this.superThis();
    }

    //

    protected abstract T superThis();

    //

    public T set(K key, V value) {
        this.remove(key);
        this.map.put(key, value);
        return superThis;
    }

    public T remove(K key) {
        this.map.remove(key);
        return superThis;
    }

    public V get(K key) {
        return this.map.get(key);
    }

    public boolean has(K key) {
        return this.map.containsKey(key);
    }

    //

    public Map<K, V> asMap() {
        return new HashMap<>(map);
    }

}
