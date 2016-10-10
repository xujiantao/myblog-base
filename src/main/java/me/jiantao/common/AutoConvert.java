package me.jiantao.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于标识自动转换需要转换的属性，标识在类上，标识
 * 所有的字段都自动转换，否则指定属性进行转换
 * BeanUtil.BeanToMap()会用到
 * @author xujiantao
 *
 */
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoConvert {

}
