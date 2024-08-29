package mc.compendium.cache;

import mc.compendium.types.Formatter;

public class AliasCache<Key, AliasKey, Value> implements CacheInterface<Key, Value> {

    private final Formatter<Key, AliasKey> aliasformatter;

    public final Cache<AliasKey, Value> cache = new Cache<>();

    //

    public AliasCache(Formatter<Key, AliasKey> aliasformatter) {
        this.aliasformatter = aliasformatter;
    }

    public AliasCache(Formatter<Key, AliasKey> aliasformatter, CachePersistence persistence) {
        this(aliasformatter);
        cache.persistence(persistence);
    }

    public AliasCache(Formatter<Key, AliasKey> aliasformatter, long delay) {
        this(aliasformatter);
        this.delay(delay);
    }

    //

    @Override
    public boolean contains(Key key) {
        return this.cache.contains(aliasformatter.format(key));
    }

    @Override
    public void store(Key key, Value value) {
        this.cache.store(aliasformatter.format(key), value);
    }

    @Override
    public void store(Key key, Value value, CacheDataPersistence persistence) {
        this.cache.store(aliasformatter.format(key), value, persistence);
    }

    @Override
    public void store(Key key, Value value, CacheDataPersistence persistence, long delay) {
        this.cache.store(aliasformatter.format(key), value, persistence, delay);
    }

    @Override
    public Value get(Key key) {
        return this.cache.get(aliasformatter.format(key));
    }

    @Override
    public Value withdraw(Key key) {
        return this.cache.withdraw(aliasformatter.format(key));
    }

    @Override
    public long delay() {
        return this.cache.delay();
    }

    @Override
    public long delay(long ms) {
        return this.cache.delay(ms);
    }

    @Override
    public CachePersistence persistence() {
        return this.cache.persistence();
    }

    @Override
    public CachePersistence persistence(CachePersistence persistence) {
        return this.cache.persistence(persistence);
    }

    @Override
    public CacheDataPersistence persistenceOf(Key key) {
        return this.cache.persistenceOf(aliasformatter.format(key));
    }

    @Override
    public long delayOf(Key key) {
        return this.cache.delayOf(aliasformatter.format(key));
    }

    @Override
    public boolean expired(Key key) {
        return this.cache.expired(aliasformatter.format(key));
    }

    @Override
    public boolean expired(Key key, long delay) {
        return this.cache.expired(aliasformatter.format(key), delay);
    }

    @Override
    public void cleanup() {
        this.cache.cleanup();
    }

    @Override
    public void cleanup(boolean force) {
        this.cache.cleanup(force);
    }

}
