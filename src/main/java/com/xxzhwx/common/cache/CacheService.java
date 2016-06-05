package com.xxzhwx.common.cache;

import com.google.common.cache.*;
import com.google.common.util.concurrent.UncheckedExecutionException;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public abstract class CacheService<K, V> extends CacheLoader<K, V>
        implements RemovalListener<K, V> {
    private String name;
    private LoadingCache<K, V> cache;

    public CacheService(String name) {
        this.name = name;
        this.cache = CacheBuilder.newBuilder()
                .initialCapacity(10)
                .maximumSize(200000)
                .concurrencyLevel(1)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .removalListener(this)
                .build(this);
    }

    public V get(K key) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            System.out.println("<<CacheService>> " + name + " fail to get: " + key);
            throw new UncheckedExecutionException(e);
        }
    }

    public void remove(K key) {
        cache.invalidate(key);
    }

    public abstract void save(K key, V value) throws DataSaveException;

    @Override
    public void onRemoval(RemovalNotification<K, V> notification) {
        K key = notification.getKey();
        V value = notification.getValue();

        try {
            save(key, value);
        } catch (DataSaveException e) {
            // log and swallow
            e.printStackTrace();
        }
    }
}
