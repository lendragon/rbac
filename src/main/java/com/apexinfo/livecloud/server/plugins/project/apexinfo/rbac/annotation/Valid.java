package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.annotation;

import java.lang.annotation.*;

/**
 * @ClassName: Valid
 * @Description: 数据校验注解
 * @Author linlongyue
 * @Date 2023/12/25
 * @Version 1.0
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Valid {
    /**
     * @return
     * @description 是否必要(不能为null)
     */
    boolean required() default true;

    /**
     * @return
     * @description 是否允许为空字符串/空集合
     */
    boolean empty() default false;

    /**
     * @return
     * @description 特殊规则号码(相同的特殊规则可以允许不判断)
     */
    int skipCode() default -1;

}
