package ren.yuda.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EncryptFieldsCache {
    private static final Logger log = LoggerFactory.getLogger(EncryptFieldsCache.class);


    private static final Map<Class<?>, SoftReference<Map<Field, PropertyDescriptor>>> cache = new HashMap<>();

    public static Map<Field, PropertyDescriptor> getEncryptFields(Class<?> aClass) {
        SoftReference<Map<Field, PropertyDescriptor>> reference = cache.get(aClass);
        if (reference == null) {
            Map<Field, PropertyDescriptor> fields = getFields(aClass);
            cache.put(aClass, new SoftReference<>(fields));
            return fields;
        }
        return reference.get();

    }

    private static Map<Field, PropertyDescriptor> getFields(Class<?> aClass) {
        return Arrays.stream(aClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Encrypt.class) && field.getType().equals(String.class))
                .distinct()
                .collect(Collectors.toMap(Function.identity(), value -> getPropertyDescriptor(value, aClass)));
    }

    private static PropertyDescriptor getPropertyDescriptor(Field value, Class<?> aClass) {
        try {
            return new PropertyDescriptor(value.getName(), aClass);
        } catch (IntrospectionException e) {
            log.error("get property descriptor error{}", value.getName(), e);
            throw new RuntimeException(e);
        }
    }
}
