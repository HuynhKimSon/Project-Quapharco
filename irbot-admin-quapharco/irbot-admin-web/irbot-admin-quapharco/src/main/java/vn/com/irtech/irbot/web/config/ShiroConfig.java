package vn.com.irtech.irbot.web.config;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.commons.io.IOUtils;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import vn.com.irtech.core.common.config.cache.RedisCacheManager;
import vn.com.irtech.core.common.constant.Constants;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.common.utils.spring.SpringUtils;
import vn.com.irtech.core.framework.shiro.realm.UserRealm;
import vn.com.irtech.core.framework.shiro.session.OnlineSessionDAO;
import vn.com.irtech.core.framework.shiro.session.OnlineSessionFactory;
import vn.com.irtech.core.framework.shiro.session.RedisSessionDAO;
import vn.com.irtech.core.framework.shiro.web.filter.LogoutFilter;
import vn.com.irtech.core.framework.shiro.web.filter.captcha.CaptchaValidateFilter;
import vn.com.irtech.core.framework.shiro.web.filter.kickout.KickoutSessionFilter;
import vn.com.irtech.core.framework.shiro.web.filter.online.OnlineSessionFilter;
import vn.com.irtech.core.framework.shiro.web.filter.sync.SyncOnlineSessionFilter;
import vn.com.irtech.core.framework.shiro.web.session.OnlineWebSessionManager;
import vn.com.irtech.core.framework.shiro.web.session.SpringSessionValidationScheduler;
import vn.com.irtech.irbot.web.shiro.realm.AdminUserRealm;

/**
 * Permission configuration loading
 * 
 * @author admin
 */
@Configuration
public class ShiroConfig {
	public static final String PREMISSION_STRING = "perms[\"{0}\"]";

	// Session timeout, in milliseconds (default 30 minutes)
	@Value("${shiro.session.expireTime}")
	private int expireTime;

	// How often to check the validity of the session, in milliseconds, the default
	// is 10 minutes
	@Value("${shiro.session.validationInterval}")
	private int validationInterval;

	// Maximum number of sessions for the same user
	@Value("${shiro.session.maxSession}")
	private int maxSession;

	// Kick out users who have logged in before/after logging in, and kick out users
	// who have logged in by default
	@Value("${shiro.session.kickoutAfter}")
	private boolean kickoutAfter;

	// Verification code switch
	@Value("${shiro.user.captchaEnabled}")
	private boolean captchaEnabled;

	// Verification code type
	@Value("${shiro.user.captchaType}")
	private String captchaType;

	// Set the cookie domain
	@Value("${shiro.cookie.domain}")
	private String domain;

	// Set the effective access path of the cookie
	@Value("${shiro.cookie.path}")
	private String path;

	// Set HttpOnly attribute
	@Value("${shiro.cookie.httpOnly}")
	private boolean httpOnly;

	// Set the cookie expiration time, in seconds
	@Value("${shiro.cookie.maxAge}")
	private int maxAge;

	/**
	 * Set cipherKey key
	 */
	@Value("${shiro.cookie.cipherKey}")
	private String cipherKey;

	/**
	 * Login address
	 */
	@Value("${shiro.user.loginUrl}")
	private String loginUrl;

	// Authorization failed address
	@Value("${shiro.user.unauthorizedUrl}")
	private String unauthorizedUrl;

	// redis cache switch
	@Value("${spring.redis.enabled}")
	private boolean redisEnabled = false;

	/**
	 * Cache manager implemented using Ehcache
	 */
//    @Bean
	public EhCacheManager getEhCacheManager() {
		// TODO Check cache key "irtech"
		net.sf.ehcache.CacheManager cacheManager = net.sf.ehcache.CacheManager.getCacheManager("digiup");
		EhCacheManager em = new EhCacheManager();
		if (StringUtils.isNull(cacheManager)) {
			em.setCacheManager(new net.sf.ehcache.CacheManager(getCacheManagerConfigFileInputStream()));
			return em;
		} else {
			em.setCacheManager(cacheManager);
			return em;
		}
	}

	/**
	 * Return to the configuration file Stream to prevent the ehcache configuration
	 * file from being occupied all the time and unable to completely destroy the
	 * project and redeploy
	 */
	protected InputStream getCacheManagerConfigFileInputStream() {
		String configFile = "classpath:ehcache/ehcache-shiro.xml";
		InputStream inputStream = null;
		try {
			inputStream = ResourceUtils.getInputStreamForPath(configFile);
			byte[] b = IOUtils.toByteArray(inputStream);
			InputStream in = new ByteArrayInputStream(b);
			return in;
		} catch (IOException e) {
			throw new ConfigurationException(
					"Unable to obtain input Stream for cacheManagerConfigFile [" + configFile + "]", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	/**
	 * Cache manager is implemented using redis
	 *
	 * @return
	 */
//    @Bean
//    @ConditionalOnMissingBean(name = "redisTemplate")
	public RedisCacheManager getRedisCacheManager() {
		RedisCacheManager redisCacheManager = new RedisCacheManager();
		return redisCacheManager;
	}

	@Bean
	public CacheManager cacheManager() {
		if (redisEnabled) {
			return getRedisCacheManager();
		} else {
			return getEhCacheManager();
		}
	}

	/**
	 * Custom Realm
	 */
	@Bean
	public UserRealm userRealm(CacheManager cacheManager) {
		// FIXME check this for realm
		// UserRealm userRealm = SpringUtils.getBean(UserRealm.class); //new
		// UserRealm();
		UserRealm userRealm = new AdminUserRealm();
		userRealm.setAuthorizationCacheName(Constants.SYS_AUTH_CACHE);
		userRealm.setCacheManager(cacheManager);
		return userRealm;
	}

	/**
	 * Custom sessionDAO session
	 */
	@Bean
	public OnlineSessionDAO sessionDAO() {
		OnlineSessionDAO sessionDAO = new OnlineSessionDAO(); // SpringUtils.getBean(OnlineSessionDAO.class);//
		return sessionDAO;
	}

	/**
	 * Customize RedisSessionDAO session
	 */
	@Bean
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisSessionDAO redisSessionDAO() {
		RedisSessionDAO redisSessionDAO = new RedisSessionDAO();// SpringUtils.getBean(RedisSessionDAO.class);
		redisSessionDAO.setExpireTime(expireTime * 60);
		return redisSessionDAO;
	}

	/**
	 * Custom sessionFactory session
	 */
	@Bean
	public OnlineSessionFactory sessionFactory() {
		OnlineSessionFactory sessionFactory = new OnlineSessionFactory();
		return sessionFactory;
	}

	/**
	 * Session manager
	 */
	@Bean
	public OnlineWebSessionManager sessionManager(CacheManager cacheManager) {
		OnlineWebSessionManager manager = new OnlineWebSessionManager();
		// Join the cache manager
		manager.setCacheManager(cacheManager);
//        manager.setCacheManager(redisEnabled ? getRedisCacheManager() : getEhCacheManager());
		// Delete expired session
		manager.setDeleteInvalidSessions(true);
		// Set the global session timeout
		manager.setGlobalSessionTimeout(expireTime * 60 * 1000);
		// Remove JSESSIONID
		manager.setSessionIdUrlRewritingEnabled(false);
		// Define invalid Session timing scheduler to be used
		manager.setSessionValidationScheduler(SpringUtils.getBean(SpringSessionValidationScheduler.class));
		// Whether to check session regularly
		manager.setSessionValidationSchedulerEnabled(true);
		// Custom SessionDao
		manager.setSessionDAO(redisEnabled ? redisSessionDAO() : sessionDAO());
		// manager.setSessionDAO(sessionDAO());
		// Custom sessionFactory
		manager.setSessionFactory(sessionFactory());
		return manager;
	}

	/**
	 * Security Manager
	 */
	@Bean
	public SecurityManager securityManager(UserRealm userRealm, CacheManager cacheManager) {
		DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
		// Set realm.
		securityManager.setRealm(userRealm);
		// remember me
		securityManager.setRememberMeManager(rememberMeManager());
		// Inject the cache manager;
		securityManager.setCacheManager(cacheManager);
		// session manager
		securityManager.setSessionManager(sessionManager(cacheManager));
		return securityManager;
	}

	/**
	 * Logout filter
	 */
	public LogoutFilter logoutFilter(CacheManager cacheManager) {
		LogoutFilter logoutFilter = new LogoutFilter();
		logoutFilter.setCacheManager(cacheManager);
		logoutFilter.setLoginUrl(loginUrl);
		return logoutFilter;
	}

	/**
	 * Shiro filter configuration
	 */
	@Bean
	public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager, CacheManager cacheManager) {
		ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
		// Shiro's core security interface, this attribute is required
		shiroFilterFactoryBean.setSecurityManager(securityManager);
		// If the identity authentication fails, jump to the configuration of the login
		// page
		shiroFilterFactoryBean.setLoginUrl(loginUrl);
		// If permission authentication fails, jump to the specified page
		shiroFilterFactoryBean.setUnauthorizedUrl(unauthorizedUrl);
		// Shiro connection constraint configuration, that is, the definition of the
		// filter chain
		LinkedHashMap<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
		// Set up anonymous access to static resources
		filterChainDefinitionMap.put("/favicon.ico**", "anon");
		filterChainDefinitionMap.put("/logo.png**", "anon");
		filterChainDefinitionMap.put("/html/**", "anon");
		filterChainDefinitionMap.put("/css/**", "anon");
		filterChainDefinitionMap.put("/docs/**", "anon");
		filterChainDefinitionMap.put("/fonts/**", "anon");
		filterChainDefinitionMap.put("/img/**", "anon");
		filterChainDefinitionMap.put("/ajax/**", "anon");
		filterChainDefinitionMap.put("/app/**", "anon");
		filterChainDefinitionMap.put("/js/**", "anon");
		filterChainDefinitionMap.put("/captcha/captchaImage**", "anon");
		// Exit the logout address, shiro to clear the session
		filterChainDefinitionMap.put("/logout", "logout");
		// Access that does not need to be intercepted
		filterChainDefinitionMap.put("/login", "anon,captchaValidate");
		// Registration related
		filterChainDefinitionMap.put("/register", "anon,captchaValidate");
		// System permission list
		// filterChainDefinitionMap.putAll(SpringUtils.getBean(IMenuService.class).selectPermsAll());
		filterChainDefinitionMap.put("/app/login", "anon");

		Map<String, Filter> filters = new LinkedHashMap<String, Filter>();
		filters.put("onlineSession", onlineSessionFilter());
		filters.put("syncOnlineSession", syncOnlineSessionFilter());
		filters.put("captchaValidate", captchaValidateFilter());
		filters.put("kickout", kickoutSessionFilter(cacheManager));
		// Logout is successful, then jump to the specified page
		filters.put("logout", logoutFilter(cacheManager));
		shiroFilterFactoryBean.setFilters(filters);

		// All requests require authentication
		filterChainDefinitionMap.put("/**", "user,kickout,onlineSession,syncOnlineSession");
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

		return shiroFilterFactoryBean;
	}

	/**
	 * Custom online user processing filter
	 */
	@Bean
	public OnlineSessionFilter onlineSessionFilter() {
		OnlineSessionFilter onlineSessionFilter = new OnlineSessionFilter();
		onlineSessionFilter.setLoginUrl(loginUrl);
		return onlineSessionFilter;
	}

	/**
	 * Customize online user synchronization filter
	 */
	@Bean
	public SyncOnlineSessionFilter syncOnlineSessionFilter() {
		SyncOnlineSessionFilter syncOnlineSessionFilter = new SyncOnlineSessionFilter();
		return syncOnlineSessionFilter;
	}

	/**
	 * Custom verification code filter
	 */
	@Bean
	public CaptchaValidateFilter captchaValidateFilter() {
		CaptchaValidateFilter captchaValidateFilter = new CaptchaValidateFilter();
		captchaValidateFilter.setCaptchaEnabled(captchaEnabled);
		captchaValidateFilter.setCaptchaType(captchaType);
		return captchaValidateFilter;
	}

	/**
	 * cookie Property setting
	 */
	public SimpleCookie rememberMeCookie() {
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		cookie.setDomain(domain);
		cookie.setPath(path);
		cookie.setHttpOnly(httpOnly);
		cookie.setMaxAge(maxAge * 24 * 60 * 60);
		return cookie;
	}

	/**
	 * remember me
	 */
	public CookieRememberMeManager rememberMeManager() {
		CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
		cookieRememberMeManager.setCookie(rememberMeCookie());
		cookieRememberMeManager.setCipherKey(Base64.decode(cipherKey));
		return cookieRememberMeManager;
	}

	/**
	 * Multi-device login restriction for the same user
	 */
	public KickoutSessionFilter kickoutSessionFilter(CacheManager cacheManager) {
		KickoutSessionFilter kickoutSessionFilter = new KickoutSessionFilter();
		kickoutSessionFilter.setCacheManager(cacheManager);
		kickoutSessionFilter.setSessionManager(sessionManager(cacheManager));
		// The maximum number of sessions for the same user, the default is -1 without
		// limit; for example, 2 means that the same user allows up to two people to log
		// in at the same time
		kickoutSessionFilter.setMaxSession(maxSession);
		// Whether to kick out those who log in later, the default is false; that is,
		// the user who logs in in the latter kicks out the user who logs in in the
		// former; the order of kicking out
		kickoutSessionFilter.setKickoutAfter(kickoutAfter);
		// The address to redirect to after being kicked out;
		kickoutSessionFilter.setKickoutUrl("/login?kickout=1");
		return kickoutSessionFilter;
	}

	/**
	 * Integration of thymeleaf template engine and shiro framework
	 */
	@Bean
	public ShiroDialect shiroDialect() {
		return new ShiroDialect();
	}

	/**
	 * Turn on Shiro annotation notifier
	 */
	@Bean
	public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(
			@Qualifier("securityManager") SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
		authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
		return authorizationAttributeSourceAdvisor;
	}
}
