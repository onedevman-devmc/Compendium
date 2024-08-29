package mc.compendium.cache;

public interface CacheInterface<Key, Value> {

    boolean contains(Key key);

    void store(Key key, Value value);

    void store(Key key, Value value, CacheDataPersistence persistence);

    void store(Key key, Value value, CacheDataPersistence persistence, long delay);

    Value get(Key key);

    Value withdraw(Key key);

    long delay();
    long delay(long ms);

    CachePersistence persistence();
    CachePersistence persistence(CachePersistence persistent);

    //

    CacheDataPersistence persistenceOf(Key key);

    long delayOf(Key key);

    boolean expired(Key key);

    boolean expired(Key key, long delay);

    //

    void cleanup();

    void cleanup(boolean force);

}
