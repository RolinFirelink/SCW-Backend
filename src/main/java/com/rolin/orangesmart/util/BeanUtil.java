package com.rolin.orangesmart.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ObjectUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;

@Slf4j
public class BeanUtil {

    private BeanUtil() {
    }

    public static <T> T copyProperties(Object source, Class<T> tClass) {
        if (null == source) {
            return null;
        }
        try {
            Constructor<T> constructor = tClass.getDeclaredConstructor();
            T target = constructor.newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            log.error("实例化Class对象出现异常: " + e.getMessage());
            return null;
        }
    }


    public static <T> T copyProperties(Object source, T target) {
        if (null == source) {
            target = null;
        } else {
            BeanUtils.copyProperties(source, target);
        }
        return target;
    }

    public static <T> T mergeProperties(T... ts) {
        if (ts == null || ts.length == 0) {
            log.error("传入参数不能为null");
            return null;
        }
        T result = ts[0];
        for (int i = 1; i < ts.length; i++) {
            result = mergeProperties(result, ts[i]);
        }
        return result;
    }

    private static <T> T mergeProperties(T t1, T t2) {
        if (t1 == null) {
            return t2;
        }
        if (t2 == null) {
            return t1;
        }

        // 创建一个新的目标对象来存储合并后的结果
        T t = (T) BeanUtils.instantiateClass(t1.getClass());

        // 复制第一个对象的属性到目标对象
        BeanUtils.copyProperties(t1, t);

        // 获取目标对象的属性描述符
        PropertyDescriptor[] propertyDescriptors;
        try {
            propertyDescriptors = Introspector.getBeanInfo(t1.getClass()).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            log.error("合并过程中出现错误:" + e);
            return null;
        }

        for (PropertyDescriptor pd : propertyDescriptors) {
            try {
                Object mergedValue = pd.getReadMethod().invoke(t);
                if (mergedValue == null) {
                    Object valueFromObj2 = pd.getReadMethod().invoke(t2);
                    if (valueFromObj2 != null) {
                        pd.getWriteMethod().invoke(t, valueFromObj2);
                    }
                }
            } catch (Exception e) {
                log.error("合并过程中出现错误:" + e);
                return null;
            }
        }

        return t;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null)
                emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    public static <T> T copyPropertiesIgnoreNull(Object src, T target) {
        if (null != src) {
            BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
            return target;
        }
        return null;
    }

    /**
     * 将一个 JavaBean 对象转化为一个  Map
     */
    public static Map<String, Object> toMap(Object bean) {
        Map<String, Object> returnMap = new HashMap<>();
        try {
            Class<?> type = bean.getClass();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, (Object[]) new Object[0]);
                    returnMap.put(propertyName, result);
                }
            }
        } catch (Exception e) {
            log.error("toMap error");
        }
        return returnMap;
    }

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     */
    public static Object toBean(Class<?> type, Map<String, Object> map) {
        Object obj = null;
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(type); // 获取类属性
            obj = type.newInstance(); // 创建 JavaBean 对象
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
        // 给 JavaBean 对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = Optional.ofNullable(beanInfo).map(BeanInfo::getPropertyDescriptors)
                .orElse(new PropertyDescriptor[0]);
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (map.containsKey(propertyName)) {
                // 下面一句可以 try 起来，这样当一个属性赋值失败的时候就不会影响其他属性赋值。
                try {
                    Object value = map.get(propertyName);
                    Object[] args = new Object[1];
                    args[0] = ObjectUtils.isEmpty(value) ? null : value;
                    descriptor.getWriteMethod().invoke(obj, args);
                } catch (Exception e) {
                    log.info(propertyName + " toMap error");
                }
            }
        }
        return obj;
    }
}
