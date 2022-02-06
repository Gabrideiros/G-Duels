package com.github.gabrideiros.duels.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TemporaryHashMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 1L;

    private final Map<K, Long> timeMap = new ConcurrentHashMap<>();
    private final EntryCallback<K, V> callback;
    private final long expiryInMillis;
    private boolean isAlive = true;

    public TemporaryHashMap(Long expiryInMillis, EntryCallback<K, V> callback) {
        this.expiryInMillis = expiryInMillis;
        this.callback = callback;
        initialize();
    }

    void initialize() {
        new CleanerThread().start();
    }

    public Long getRemainingTime(K key) {
        if (!timeMap.containsKey(key)) return 0L;

        long currentTime = new Date().getTime();
        return (timeMap.get(key) + expiryInMillis) - currentTime;
    }

    @Override
    public V put(K key, V value) {
        if (!isAlive) {
            initialize();
        }
        Date date = new Date();
        timeMap.put(key, date.getTime());

        V returnVal = super.put(key, value);

        callback.onAdd(key, value);
        return returnVal;
    }

    @Override
    public boolean containsKey(Object key) {
        long currentTime = new Date().getTime();
        if (timeMap.containsKey(key)) {
            if (((timeMap.get(key) + expiryInMillis) - currentTime) < 0L) {
                timeMap.remove(key);
                remove(key);
            }
        }

        return super.containsKey(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        if (!isAlive) {
            initialize();
        }
        for (K key : m.keySet()) {
            put(key, m.get(key));
        }
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if (!isAlive) {
            initialize();
        }
        if (!containsKey(key)) {
            return put(key, value);
        } else {
            return get(key);
        }
    }

    public V removeFromMap(K key) {
        if (!isAlive) {
            initialize();
        }

        if (containsKey(key)) {
            timeMap.remove(key);
            V value = remove(key);

            callback.onRemove(key, value);

            return value;
        }

        return null;
    }

    public void quitMap() {
        isAlive = false;
    }

    public interface EntryCallback<K, V> {
        void onAdd(K key, V value);
        void onRemove(K key, V value);
    }

    class CleanerThread extends Thread {

        @Override
        public void run() {
            while (isAlive) {
                cleanMap();
                try {
                    Thread.sleep(expiryInMillis / 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void cleanMap() {
            long currentTime = new Date().getTime();
            for (K key : timeMap.keySet()) {
                if (((timeMap.get(key) + expiryInMillis) - currentTime) < 0L) {
                    V value = removeFromMap(key);
                    timeMap.remove(key, value);
                }
            }
        }
    }
}
