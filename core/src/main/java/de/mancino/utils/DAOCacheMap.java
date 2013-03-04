package de.mancino.utils;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public abstract class DAOCacheMap<K,V,D> {
    public static final long DEFAULT_CACHE_TIMEOUT = 15L * 60L * 1000L;

    private final ConcurrentMap<K, V> cacheMap;
    private final ConcurrentMap<K, Long> timeoutMap;
    private final long cacheTimeout;
    private D dataStore;

    public DAOCacheMap() {
        this(DEFAULT_CACHE_TIMEOUT, null);
    }

    public DAOCacheMap(long cacheTimeout, D dataStore) {
        this.dataStore = dataStore;
        this.cacheTimeout = cacheTimeout;
        this.cacheMap = new ConcurrentHashMap<K, V>();
        this.timeoutMap = new ConcurrentHashMap<K, Long>();
    }

    protected abstract V reload(final K key, final D dataStore);

    public V get(final K key) {
        synchronized(cacheMap) {
            if(!isCached(key) || isTimedOut(key)) {
                cacheMap.put(key, reload(key,getDataStore()));
                timeoutMap.put(key, System.currentTimeMillis());
            }
            return cacheMap.get(key);
        }
    }

    protected D getDataStore() {
        return dataStore;
    }

    public void setDataStore(final D dataStore) {
        synchronized(cacheMap) {
            this.dataStore = dataStore;
        }
    }

    public void invalidate(final K key) {
        synchronized(cacheMap) {
            cacheMap.remove(key);
            timeoutMap.remove(key);
        }
    }

    public void invalidateAll() {
        synchronized(cacheMap) {
            cacheMap.clear();
            timeoutMap.clear();
        }
    }

    public boolean isCached(final K key) {
        return cacheMap.get(key) != null;
    }

    public boolean isTimedOut(final K key) {
        return System.currentTimeMillis() - timeoutMap.get(key) > cacheTimeout;
    }
}
