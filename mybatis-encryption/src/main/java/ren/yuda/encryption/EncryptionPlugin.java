package ren.yuda.encryption;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
})
public class EncryptionPlugin implements Interceptor {
    private final static Logger log = LoggerFactory.getLogger(EncryptionPlugin.class);
    private Properties properties;


    @Override
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        String methodName = method.getName();
        if (methodName.equals("update") || methodName.equals("query")) {
            Object parameter = invocation.getArgs()[1];
            doEncrypt(parameter);
        }
        Object proceed = invocation.proceed();
        if (methodName.equals("handleResultSets")) {

            doDecrypt(proceed);
        }
        return proceed;
    }

    private void doDecrypt(Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof ArrayList<?>) {
            Map<Field, PropertyDescriptor> encryptFields = null;
            for (Object o : ((ArrayList<?>) object)) {
                if (encryptFields == null) {
                    encryptFields = EncryptFieldsCache.getEncryptFields(o.getClass());
                    if (encryptFields.isEmpty()) {
                        break;
                    }
                }
                decryptField(encryptFields, o);
            }
        }

    }

    private void decryptField(Map<Field, PropertyDescriptor> encryptFields, Object item) {
        if (encryptFields.isEmpty()) {
            return;
        }
        for (Map.Entry<Field, PropertyDescriptor> entry : encryptFields.entrySet()) {
            Field field = entry.getKey();
            PropertyDescriptor descriptor = entry.getValue();
            Object source = getValue(item, descriptor);
            if (source == null) {
                continue;
            }
            Encrypt annotation = field.getAnnotation(Encrypt.class);
            EncryptAlgorithm algorithm = AlgorithmRegister.getInstance().getAlgorithm(annotation.algorithm());
            String data = source.toString();
            String encryptedFlag = algorithm.getEncryptedFlag();
            if (!data.startsWith(encryptedFlag)) {
                log.warn("field ={} encrypted warn,dont have prefix={}", field.getName(), encryptedFlag);
                continue;
            }
            data = data.substring(encryptedFlag.length());

            String decrypt = algorithm.decrypt(data);
            setValue(item, descriptor, decrypt);
        }
    }

    private void doEncrypt(Object object) {
        if (object == null) {
            return;
        }
        if (object instanceof MapperMethod.ParamMap<?>) {
            encryptParamMap((MapperMethod.ParamMap<?>) object);
        } else {
            Map<Field, PropertyDescriptor> encryptFields = EncryptFieldsCache.getEncryptFields(object.getClass());
            if (encryptFields.isEmpty()) {
                return;
            }
            encryptField(encryptFields, object);
        }
    }

    private void encryptParamMap(MapperMethod.ParamMap<?> map) {
        map.forEach((k, v) -> {
            if (v instanceof Collection<?>) {
                encryptCollection((Collection<?>) v);
            } else {
                encryptField(EncryptFieldsCache.getEncryptFields(v.getClass()), v);
            }
        });
    }

    private void encryptCollection(Collection<?> c) {
        if (c.isEmpty()) {
            return;
        }
        Map<Field, PropertyDescriptor> needToEncryptFields = null;
        for (Object o : c) {
            if (needToEncryptFields == null) {
                needToEncryptFields = EncryptFieldsCache.getEncryptFields(o.getClass());
                if (needToEncryptFields.isEmpty()) {
                    break;
                }
            }
            encryptField(needToEncryptFields, o);
        }
    }

    private void encryptField(Map<Field, PropertyDescriptor> needToEncryptFields, Object item) {
        if (needToEncryptFields.isEmpty()) {
            return;
        }


        for (Map.Entry<Field, PropertyDescriptor> entry : needToEncryptFields.entrySet()) {
            Field field = entry.getKey();
            PropertyDescriptor descriptor = entry.getValue();
            Object source = getValue(item, descriptor);
            if (source == null) {
                continue;
            }
            Encrypt annotation = field.getAnnotation(Encrypt.class);
            EncryptAlgorithm algorithm = AlgorithmRegister.getInstance().getAlgorithm(annotation.algorithm());
            String data = source.toString();
            if (data.startsWith(algorithm.getEncryptedFlag())) {
                log.debug("field ={} has encrypted", field.getName());
                continue;
            }
            String encrypt = algorithm.encrypt(data);
            setValue(item, descriptor, algorithm.getEncryptedFlag() + encrypt);
        }

    }

    private Object setValue(Object item, PropertyDescriptor descriptor, String encrypt) {
        try {
            return descriptor.getWriteMethod().invoke(item, encrypt);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("set value error {}", descriptor.getName(), e);
            throw new RuntimeException(e);
        }
    }

    private Object getValue(Object item, PropertyDescriptor descriptor) {
        try {
            return descriptor.getReadMethod().invoke(item);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error("get value error {}", descriptor.getName(), e);
            throw new RuntimeException(e);
        }
    }


}
