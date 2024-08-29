package mc.compendium.cache;

import java.util.HashMap;
import java.util.Map;

public class Cache<Key, Value> implements CacheInterface<Key, Value> {

    public static long TIMESTAMP() { return System.currentTimeMillis(); }

    public static class DELAY {

        public static long MILLISECONDS(long count) { return count; }
        public static long SECONDS(long count) { return count * 1000; }
        public static long MINUTES(long count) { return count * 60 * 1000; }
        public static long HOURS(long count) { return count * 60 * 60 * 1000; }

    }

    //

    private record CacheData<Value>(long timestamp, CacheDataPersistence persistence, long delay, Value value) {

        public static <Value> CacheData<Value> of(long timestamp, CacheDataPersistence persistence, long delay, Value value) {
            return new CacheData<>(timestamp, persistence, delay, value);
        }

    }

    //

    private final Map<Key, CacheData<Value>> cache = new HashMap<>();

    private long delay = 0;

    private CachePersistence persistence = CachePersistence.VOLATILE;

    //

    public Cache() {

    }

    public Cache(CachePersistence persistence) {
        this.persistence(persistence);
    }

    public Cache(long delay) {
        this.delay(delay);
    }

    //

    public boolean contains(Key key) {
        return this.contains(key, true);
    }

    private boolean contains(Key key, boolean cleanup) {
        if(cleanup)
            this.cleanup();

        return this.cache.containsKey(key);
    }

    //

    public void store(Key key, Value value) {
        this.store(key, value, true);
    }

    private void store(Key key, Value value, boolean cleanup) {
        this.store(key, value, this.persistence().getDataPersistence(), cleanup);
    }

    public void store(Key key, Value value, CacheDataPersistence persistence) {
        this.store(key, value, persistence, true);
    }

    private void store(Key key, Value value, CacheDataPersistence persistence, boolean cleanup) {
        this.store(key, value, persistence, -1, cleanup);
    }

    public void store(Key key, Value value, CacheDataPersistence persistence, long delay) {
        this.store(key, value, persistence, delay, true);
    }

    private void store(Key key, Value value, CacheDataPersistence persistence, long delay, boolean cleanup) {
        this.withdraw(key, cleanup);
        this.cache.put(key, CacheData.of(Cache.TIMESTAMP(), persistence, delay, value));
    }

    //

    public Value get(Key key) {
        return this.get(key, true);
    }

    private Value get(Key key, boolean cleanup) {
        return this.contains(key, cleanup) ? this.cache.get(key).value() : null;
    }

    //

    public Value withdraw(Key key) {
        return this.withdraw(key, true);
    }

    private Value withdraw(Key key, boolean cleanup) {
        return this.contains(key, cleanup) ? this.cache.remove(key).value() : null;
    }

    //

    public long delay() { return this.delay; }
    public long delay(long ms) { this.delay = ms; return this.delay(); }

    public CachePersistence persistence() { return this.persistence; }
    public CachePersistence persistence(CachePersistence persistence) { this.persistence = persistence; return this.persistence(); }

    //

    public CacheDataPersistence persistenceOf(Key key) {
        return this.persistenceOf(key, true);
    }

    private CacheDataPersistence persistenceOf(Key key, boolean cleanup) {
        if(!this.contains(key, cleanup))
            return null;

        return this.cache.get(key).persistence().get(this);
    }

    //

    public long delayOf(Key key) {
        return this.delayOf(key, true);
    }

    private long delayOf(Key key, boolean cleanup) {
        if(!this.contains(key, cleanup))
            return 0;

        long delay = this.cache.get(key).delay();

        if(delay < 0)
            return this.delay();

        return delay;
    }

    //

    public boolean expired(Key key) {
        return this.expired(key, true);
    }

    private boolean expired(Key key, boolean cleanup) {
        return this.expired(key, this.delayOf(key, cleanup), cleanup);
    }

    //

    public boolean expired(Key key, long delay) {
        return this.expired(key, delay, true);
    }

    private boolean expired(Key key, long delay, boolean cleanup) {
        if(!this.contains(key, cleanup))
            return true;

        return (Cache.TIMESTAMP() - this.cache.get(key).timestamp()) > delay;
    }

    //

    public void cleanup() {
        cleanup(false);
    }

    public void cleanup(boolean force) {
        for(Key key : this.cache.keySet())
            if(this.expired(key, false) && (this.persistenceOf(key, false).equals(CacheDataPersistence.VOLATILE) || force))
                this.withdraw(key, false);
    }

}