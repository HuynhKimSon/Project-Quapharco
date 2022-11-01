package vn.com.irtech.core.framework.util;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;

import vn.com.irtech.core.common.domain.LoginUser;
import vn.com.irtech.core.common.utils.StringUtils;
import vn.com.irtech.core.common.utils.bean.BeanUtils;
import vn.com.irtech.core.framework.shiro.realm.UserRealm;

/**
 * shiro utils
 * 
 * @author admin
 */
public class ShiroUtils
{
    public static Subject getSubject()
    {
        return SecurityUtils.getSubject();
    }

    public static Session getSession()
    {
        return SecurityUtils.getSubject().getSession();
    }

    public static void logout()
    {
        getSubject().logout();
    }

    public static LoginUser getLoginUser()
    {
        LoginUser user = null;
        Object obj = getSubject().getPrincipal();
        if(obj == null) {
        	return null;
        }
        // convert if class is not instance of LoginUser class
        // FIXME Fixbug
        if (StringUtils.isNotNull(obj) && !(obj instanceof LoginUser)) {
            user = new LoginUser();
            BeanUtils.copyBeanProp(user, obj);
            return user;
        } else {
        	return (LoginUser) obj;
        }

    }
    

    public static void setLoginUser(LoginUser user)
    {
        Subject subject = getSubject();
        PrincipalCollection principalCollection = subject.getPrincipals();
        String realmName = principalCollection.getRealmNames().iterator().next();
        PrincipalCollection newPrincipalCollection = new SimplePrincipalCollection(user, realmName);
        subject.runAs(newPrincipalCollection);
    }

    public static void clearCachedAuthorizationInfo()
    {
        RealmSecurityManager rsm = (RealmSecurityManager) SecurityUtils.getSecurityManager();
        UserRealm realm = (UserRealm) rsm.getRealms().iterator().next();
        realm.clearCachedAuthorizationInfo();
    }

    public static Long getUserId()
    {
        return getLoginUser().getUserId().longValue();
    }

    public static String getLoginName()
    {
        return getLoginUser().getLoginName();
    }

    public static String getIp()
    {
        return getSubject().getSession().getHost();
    }

    public static String getSessionId()
    {
        return String.valueOf(getSubject().getSession().getId());
    }

    /**
     * Generate random salt
     */
    public static String randomSalt()
    {
        // One Byte occupies two bytes, the 3 bytes generated here, the string length is 6
        SecureRandomNumberGenerator secureRandom = new SecureRandomNumberGenerator();
        String hex = secureRandom.nextBytes(3).toHex();
        return hex;
    }
}
