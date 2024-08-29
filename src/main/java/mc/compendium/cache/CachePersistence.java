package mc.compendium.cache;

public enum CachePersistence {
    PERSISTENT(CacheDataPersistence.PERSISTENT),
    VOLATILE(CacheDataPersistence.VOLATILE);

    private final CacheDataPersistence dataPersistence;

    CachePersistence(CacheDataPersistence persistence) {
        this.dataPersistence = persistence;
    }

    public CacheDataPersistence getDataPersistence() {
        return this.dataPersistence;
    }
}
