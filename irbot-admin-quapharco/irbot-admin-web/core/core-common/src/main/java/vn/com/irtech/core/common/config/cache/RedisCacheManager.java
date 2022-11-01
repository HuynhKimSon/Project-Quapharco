package vn.com.irtech.core.common.config.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;

import vn.com.irtech.core.common.utils.spring.SpringUtils;

// @Service
@SuppressWarnings("rawtypes")
public class RedisCacheManager implements CacheManager {
	@Value("${spring.redis..cacheExpireTime}")
	private final long cacheExpireTime = 3600;

	private final ConcurrentHashMap<String, Cache> caches = new ConcurrentHashMap<>();

	private final RedisTemplate redisTemplate = SpringUtils.getBean("redisTemplate");

	@SuppressWarnings("unchecked")
	@Override
	public <K, V> Cache<K, V> getCache(String name) throws CacheException {
		Cache cache = caches.get(name);
		if (cache == null) {
			synchronized (this) {
				cache = new RedisCache<K, V>(name, cacheExpireTime, redisTemplate);
				caches.put(name, cache);
			}
		}
		return cache;
	}
}
