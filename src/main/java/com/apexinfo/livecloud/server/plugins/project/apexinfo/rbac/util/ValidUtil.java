package com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.util;

import com.apex.livebos.console.common.util.Util;
import com.apexinfo.livecloud.server.plugins.project.apexinfo.rbac.annotation.Valid;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @ClassName: ValidUtil
 * @Description: 数据校验工具类
 * @Author linlongyue
 * @Date 2023/12/25
 * @Version 1.0
 */
public class ValidUtil {
    /**
     * 异常信息
     */
    private static final String ERROR_TYPE = "类型异常";

    /**
     * @param zClass 要校验的实体类类型
     * @param object 要校验的实体类对象
     * @description 校验对象字段
     */
    public static void valid(Class<?> zClass, Object object) throws Exception {
        valid(zClass, object, -1);
    }

    /**
     * @param zClass   要校验的实体类类型
     * @param object   要校验的实体类对象
     * @param skipCode 跳过校验码, 如果开启了, 即skipCode != -1 会不判断
     * @description 校验对象字段
     */
    public static void valid(Class<?> zClass, Object object, int skipCode) throws Exception {
        if (object == null) {
            throw new NullPointerException("必须存在类型为" + zClass.getName() + "的参数");
        }

        if (object.getClass() != zClass) {
            throw new RuntimeException(ERROR_TYPE);
        }

        for (Field field : zClass.getDeclaredFields()) {
            Valid fieldValid = field.getDeclaredAnnotation(Valid.class);
            if (fieldValid == null) {
                continue;
            }

            // 如果是跳过校验码, 则允许
            if (skipCode != -1 && skipCode == fieldValid.skipCode()) {
                continue;
            }

            field.setAccessible(true);
            Object o = field.get(object);
            // 非null判断
            if (fieldValid.required() && o == null) {
                throw new RuntimeException("必须存在参数" + field.getName());
            }

            // 非empty判断
            if (!fieldValid.empty()) {
                if (o instanceof String && Util.isEmpty(o)) {
                    throw new RuntimeException("参数" + field.getName() + "不能为空字符串");
                }
                if (o instanceof List && Util.isEmpty((List) o)) {
                    throw new RuntimeException("参数" + field.getName() + "不能为空集合");
                }
            }
        }
    }
}
