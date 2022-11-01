package vn.com.irtech.core.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import vn.com.irtech.core.common.enums.BusinessType;
import vn.com.irtech.core.common.enums.OperatorType;

/**
 * Custom operation log record annotation
 * 
 * @author admin
 */
@Target({ ElementType.PARAMETER, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log
{
    /**
     * Module 
     */
    public String title() default "";

    /**
     * Features
     */
    public BusinessType businessType() default BusinessType.OTHER;

    /**
     * Operator category
     */
    public OperatorType operatorType() default OperatorType.MANAGE;

    /**
     * Whether to save the requested parameters
     */
    public boolean isSaveRequestData() default true;
}
