package mc.compendium.types;

public class UniMap<K, V> extends AbstractUniMap<K, V, UniMap<K, V>> {

    public UniMap() {}

    //

    @Override
    protected UniMap<K, V> superThis() { return this; }

    //



}
