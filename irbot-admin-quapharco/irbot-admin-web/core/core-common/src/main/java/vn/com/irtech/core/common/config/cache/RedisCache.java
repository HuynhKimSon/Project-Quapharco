package vn.com.irtech.core.common.config.cache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SuppressWarnings("unchecked")
public class RedisCache<K, V> implements Cache<K, V> {
	private final String cacheName;
	private final long cacheExpireTime;
	private final RedisTemplate<K, V> redisTemplate;

	public RedisCache(String cacheName, long cacheExpireTime, RedisTemplate<K, V> redisTemplate) {
		super();
		this.cacheName = cacheName;
		this.cacheExpireTime = cacheExpireTime;
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		this.redisTemplate = redisTemplate;
	}

	public String cacheKey(String cacheName, K key) {
		return keyPrefix(cacheName) + key;
	}

	public String keyPrefix(String cacheName) {
		return cacheName + ":";
	}

	@Override
	public V get(K key) throws CacheException {
		return redisTemplate.opsForValue().get(cacheKey(cacheName, key));
	}

	@Override
	public V put(K key, V value) throws CacheException {
		redisTemplate.opsForValue().set((K) cacheKey(cacheName, key), value, cacheExpireTime, TimeUnit.SECONDS);
		return value;
	}

	@Override
	public V remove(K key) throws CacheException {
		V value = redisTemplate.opsForValue().get(cacheKey(cacheName, key));
		redisTemplate.opsForValue().getOperations().delete((K) cacheKey(cacheName, key));
		return value;
	}

	@Override
	public void clear() throws CacheException {
		redisTemplate.delete(keys());
	}

	@Override
	public int size() {
		return keys() == null ? 0 : keys().size();
	}

	@Override
	public Set<K> keys() {
		return redisTemplate.keys((K) ("*" + cacheName + "*"));
	}

	@Override
	public Collection<V> values() {
		return keys().stream().map(key -> redisTemplate.opsForValue().get(key)).collect(Collectors.toList());
	}
}
