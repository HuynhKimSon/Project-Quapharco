package vn.com.irtech.core.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vn.com.irtech.core.common.constant.Constants;
import vn.com.irtech.core.common.domain.CacheEntity;
import vn.com.irtech.core.common.utils.spring.SpringUtils;

/**
 * Cache tools
 * 
 * @author admin
 */
public class CacheUtils {
	private static Logger logger = LoggerFactory.getLogger(CacheUtils.class);

	private static CacheManager cacheManager = SpringUtils.getBean(CacheManager.class);

	private static final String SYS_CACHE = "sys-cache";

	/**
	 * Get SYS_CACHE cache
	 * 
	 * @param key
	 * @return
	 */
	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}

	/**
	 * Get SYS_CACHE cache
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Object get(String key, Object defaultValue) {
		Object value = get(key);
		return value != null ? value : defaultValue;
	}

	/**
	 * Write to SYS_CACHE cache
	 * 
	 * @param key
	 * @return
	 */
	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}

	/**
	 * Remove from SYS_CACHE cache
	 * 
	 * @param key
	 * @return
	 */
	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}

	/**
	 * Get cache
	 * 
	 * @param cacheName
	 * @param key
	 * @return
	 */
	public static Object get(String cacheName, String key) {
		return getCache(cacheName).get(getKey(key));
	}

	/**
	 * Get cache
	 * 
	 * @param cacheName
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static Object get(String cacheName, String key, Object defaultValue) {
		Object value = get(cacheName, getKey(key));
		return value != null ? value : defaultValue;
	}

	/**
	 * Write cache
	 * 
	 * @param cacheName
	 * @param key
	 * @param value
	 */
	public static void put(String cacheName, String key, Object value) {
		Cache<String, Object> cache = getCache(cacheName);
		if (cache != null) {
			cache.put(getKey(key), value);
		}
	}

	/**
	 * Remove from cache
	 * 
	 * @param cacheName
	 * @param key
	 */
	public static void remove(String cacheName, String key) {
		getCache(cacheName).remove(getKey(key));
	}

	/**
	 * Remove all from cache
	 * 
	 * @param cacheName
	 */
	public static void removeAll(String cacheName) {
		Cache<String, Object> cache = getCache(cacheName);
		Set<String> keys = cache.keys();
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			cache.remove(it.next());
		}
		logger.info("Clear cache： {} => {}", cacheName, keys);
	}

	/**
	 * Remove the specified key from the cache
	 * 
	 * @param keys
	 */
	public static void removeByKeys(Set<String> keys) {
		removeByKeys(SYS_CACHE, keys);
	}

	/**
	 * Remove the specified key from the cache
	 * 
	 * @param cacheName
	 * @param keys
	 */
	public static void removeByKeys(String cacheName, Set<String> keys) {
		for (Iterator<String> it = keys.iterator(); it.hasNext();) {
			remove(it.next());
		}
		logger.info("Clear cache： {} => {}", cacheName, keys);
	}

	/**
	 * Get the cache key name
	 * 
	 * @param key
	 * @return
	 */
	private static String getKey(String key) {
		return key;
	}

	/**
	 * Get a Cache, if not, display the log.
	 * 
	 * @param cacheName
	 * @return
	 */
	public static Cache<String, Object> getCache(String cacheName) {
		if (cacheManager == null) {
			return null;
		}
		Cache<String, Object> cache = cacheManager.getCache(cacheName);
		if (cache == null) {
			throw new RuntimeException("Cache “" + cacheName + "” not exist.");
		}
		return cache;
	}

	public static List<Map<String, String>> getAllCacheData(CacheEntity cacheEntity) {
		String keyParam = cacheEntity.getKey();
		String keyNameParam = cacheEntity.getKeyName();

		List<Map<String, String>> keyDataList = new ArrayList<>();
		Set<String> keys = null; // List keys from cache
		int count = 1;

		// Get sys cache
		if (StringUtils.isEmpty(keyNameParam) || SYS_CACHE.equalsIgnoreCase(keyNameParam)) {
			Cache<String, Object> sysCache = getCache(SYS_CACHE);
			if (StringUtils.isNotEmpty(keyParam)) {
				Object value = get(keyParam);
				if (value != null) {
					Map<String, String> map = new HashMap<>();
					map.put("id", count++ + "");
					map.put("key", keyParam);
					map.put("value", value.toString());
					map.put("keyName", SYS_CACHE);
					keyDataList.add(map);
				}
			} else {
				keys = sysCache.keys();
				for (Iterator<String> it = keys.iterator(); it.hasNext();) {
					String key = it.next();
					String[] keyArr = key.split(":");
					key = keyArr[keyArr.length - 1];
					String value = get(key).toString();
					if (StringUtils.isNotEmpty(value)) {
						Map<String, String> map = new HashMap<>();
						map.put("id", count++ + "");
						map.put("key", key);
						map.put("value", value);
						map.put("keyName", SYS_CACHE);
						keyDataList.add(map);
					}
				}
			}
		}

		// Get config cache
		if (StringUtils.isEmpty(keyNameParam) || Constants.SYS_CONFIG_CACHE.equalsIgnoreCase(keyNameParam)) {
			Cache<String, Object> configCache = getCache(Constants.SYS_CONFIG_CACHE);
			if (StringUtils.isNotEmpty(keyParam)) {
				Object value = get(Constants.SYS_CONFIG_CACHE, Constants.SYS_CONFIG_KEY + keyParam);
				if (value != null) {
					Map<String, String> map = new HashMap<>();
					map.put("id", count++ + "");
					map.put("key", keyParam);
					map.put("value", value.toString());
					map.put("keyName", Constants.SYS_CONFIG_CACHE);
					keyDataList.add(map);
				}
			} else {
				keys = configCache.keys();
				for (Iterator<String> it = keys.iterator(); it.hasNext();) {
					String key = it.next();
					String[] keyArr = key.split(":");
					key = Constants.SYS_CONFIG_KEY + keyArr[keyArr.length - 1];
					String value = get(Constants.SYS_CONFIG_CACHE, key).toString();
					if (StringUtils.isNotEmpty(value)) {
						Map<String, String> map = new HashMap<>();
						map.put("id", count++ + "");
						map.put("key", key);
						map.put("value", value);
						map.put("keyName", Constants.SYS_CONFIG_CACHE);
						keyDataList.add(map);
					}
				}
			}
		}

		// Get dictionary cache
		if (StringUtils.isEmpty(keyNameParam) || Constants.SYS_DICT_CACHE.equalsIgnoreCase(keyNameParam)) {
			Cache<String, Object> dictCache = getCache(Constants.SYS_DICT_CACHE);
			if (StringUtils.isNotEmpty(keyParam)) {
				Object value = get(Constants.SYS_DICT_CACHE, Constants.SYS_DICT_KEY + keyParam);
				if (value != null) {
					Map<String, String> map = new HashMap<>();
					map.put("id", count++ + "");
					map.put("key", keyParam);
					map.put("value", value.toString());
					map.put("keyName", Constants.SYS_DICT_CACHE);
					keyDataList.add(map);
				}
			} else {
				keys = dictCache.keys();
				for (Iterator<String> it = keys.iterator(); it.hasNext();) {
					String key = it.next();
					String[] keyArr = key.split(":");
					key = Constants.SYS_DICT_KEY + keyArr[keyArr.length - 1];
					String value = get(Constants.SYS_DICT_CACHE, key).toString();
					if (StringUtils.isNotEmpty(value)) {
						Map<String, String> map = new HashMap<>();
						map.put("id", count++ + "");
						map.put("key", key);
						map.put("value", value);
						map.put("keyName", Constants.SYS_DICT_CACHE);
						keyDataList.add(map);
					}
				}
			}
		}
		return keyDataList;
	}

	public static String[] getCacheNames() {
		return ((EhCacheManager) cacheManager).getCacheManager().getCacheNames();
	}
}
