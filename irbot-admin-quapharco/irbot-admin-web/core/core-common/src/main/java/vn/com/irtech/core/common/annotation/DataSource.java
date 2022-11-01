package vn.com.irtech.core.common.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import vn.com.irtech.core.common.enums.DataSourceType;

/**
 * Customize multiple data source switch annotations
 * 
 * @author admin
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DataSource
{
    /**
     * Switch data source name
     */
    public DataSourceType value() default DataSourceType.MASTER;
}
