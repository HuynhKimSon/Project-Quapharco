package vn.com.irtech.core.common.utils;

/**
 * 获取地址类
 * 
 * @author admin
 */
public class AddressUtils
{
//    private static final Logger log = LoggerFactory.getLogger(AddressUtils.class);

    public static final String IP_URL = "http://ip-api.com/json/";

    public static final String UNKNOWN = "Internet";

    public static String getRealAddressByIP(String ip)
    {
        String address = UNKNOWN;
        if (IpUtils.internalIp(ip))
        {
            return "Intranet IP";
        }
//        if (Global.isAddressEnabled())
//        {
//            try
//            {
//                String rspStr = HttpUtils.sendGet(IP_URL + "/" + ip, "", Constants.GBK);
//                if (StringUtils.isEmpty(rspStr))
//                {
//                    log.error("Get location error {}", ip);
//                    return UNKNOWN;
//                }
//                JSONObject obj = JSONObject.parseObject(rspStr);
//                String region = obj.getString("country");
//                String city = obj.getString("city");
//                return String.format("%s, %s", region, city);
//            }
//            catch (Exception e)
//            {
//                log.error("Get geolocation exception{}", ip);
//            }
//        }
        return address;
    }
}
