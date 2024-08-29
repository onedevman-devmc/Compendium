package mc.compendium.cache;

public enum CacheDataPersistence {
    CACHE_RELATIVE(cache -> cache.persistence().getDataPersistence()),
    VOLATILE(null),
    PERSISTENT(null);

    private interface PersistenceGetter {
        CacheDataPersistence get(Cache<?, ?> cache);
    }

    private final PersistenceGetter getter;

    CacheDataPersistence(PersistenceGetter getter) {
        this.getter = getter;
    }

    public CacheDataPersistence get(Cache<?, ?> cache) {
        if(this.getter == null)
            return this;

        return this.getter.get(cache);
    }
}
