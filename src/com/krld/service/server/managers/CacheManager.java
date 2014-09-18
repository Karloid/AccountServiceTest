package com.krld.service.server.managers;

import com.krld.service.server.contracts.PropertiesContract;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import static com.krld.service.server.contracts.PropertiesContract.*;

public class CacheManager {

    private Map<Integer, Long> cacheMap;


    public void init(Properties prop) {
        int maxEntries = Integer.valueOf(prop.getProperty(CACHE_MAX_ENTRIES));
        cacheMap = Collections.synchronizedMap(new LinkedHashMap<Integer, Long>(maxEntries + 1, 1.0f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Long> eldest) {
                return super.size() > maxEntries;
            }
        });
    }

    public Long getAmount(Integer id) {
        return cacheMap.get(id);
    }

    public void addAmount(Integer id, Long value) {
        cacheMap.put(id, value);
    }
}
