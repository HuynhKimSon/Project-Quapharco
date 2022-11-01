package vn.com.irtech.core.framework.config;

import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import static com.google.code.kaptcha.Constants.*;

/**
 * Verification code configuration
 * 
 * @author admin
 */
@Configuration
public class CaptchaConfig
{
    @Bean(name = "captchaProducer")
    public DefaultKaptcha getKaptchaBean()
    {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // Whether there is a border, the default is true, we can set yes, no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // The color of the captcha text character is Color.BLACK by default
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
        // The width of the captcha image is 200 by default
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        // The height of the captcha image is 50 by default
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        // 验The text size of the certificate code defaults to 40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "38");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCode");
        // The length of the verification code text is 5 by default
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");
        // Verification code text font style The default is new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // Picture style Water grain com.google.code.kaptcha.impl.WaterRipple Fisheye com.google.code.kaptcha.impl.FishEyeGimpy Shadow com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }

    @Bean(name = "captchaProducerMath")
    public DefaultKaptcha getKaptchaBeanMath()
    {
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        Properties properties = new Properties();
        // Whether there is a border, the default is true, we can set yes, no
        properties.setProperty(KAPTCHA_BORDER, "yes");
        // The border color defaults to Color.BLACK
        properties.setProperty(KAPTCHA_BORDER_COLOR, "105,179,90");
        // The color of the captcha text character is Color.BLACK by default
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_COLOR, "blue");
        // The width of the captcha image is 200 by default
        properties.setProperty(KAPTCHA_IMAGE_WIDTH, "160");
        // The height of the captcha image is 50 by default
        properties.setProperty(KAPTCHA_IMAGE_HEIGHT, "60");
        // The text size of the verification code defaults to 40
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_SIZE, "35");
        // KAPTCHA_SESSION_KEY
        properties.setProperty(KAPTCHA_SESSION_CONFIG_KEY, "kaptchaCodeMath");
        // Verification code text generator
        properties.setProperty(KAPTCHA_TEXTPRODUCER_IMPL, "vn.com.irtech.core.framework.config.KaptchaTextCreator");
        // Captcha text character spacing default is 2
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "3");
        // The length of the verification code text is 5 by default
        properties.setProperty(KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "6");
        // The captcha text font style defaults to new Font("Arial", 1, fontSize), new Font("Courier", 1, fontSize)
        properties.setProperty(KAPTCHA_TEXTPRODUCER_FONT_NAMES, "Arial,Courier");
        // Verification code noise color The default is Color.BLACK
        properties.setProperty(KAPTCHA_NOISE_COLOR, "white");
        // Interference implementation class
        properties.setProperty(KAPTCHA_NOISE_IMPL, "com.google.code.kaptcha.impl.NoNoise");
        // Picture style Water grain com.google.code.kaptcha.iVerification code noise color The default is Color.BLACKmpl.WaterRipple Fisheye com.google.code.kaptcha.impl.FishEyeGimpy Shadow com.google.code.kaptcha.impl.ShadowGimpy
        properties.setProperty(KAPTCHA_OBSCURIFICATOR_IMPL, "com.google.code.kaptcha.impl.ShadowGimpy");
        Config config = new Config(properties);
        defaultKaptcha.setConfig(config);
        return defaultKaptcha;
    }
}
