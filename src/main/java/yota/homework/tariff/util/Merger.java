package yota.homework.tariff.util;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.stereotype.Component;

import javax.persistence.Embedded;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class Merger {
    private final EntityManager entityManager;

    /**
     * Merges source with non null fields to target.
     *
     * @param from             - entity from which data will be copied
     * @param to               - entity to which data will copied
     * @param classesToInclude - complex types of source entity fields which to be copied
     * @return merged entity
     */
    public <T> T merge(final T from, final T to, final Class... classesToInclude) {
        if (to == null) return from;
        if (from == null) return to;

        Class fromClass = unproxy(from).getClass();
        Class toClass = unproxy(to).getClass();
        if (fromClass != toClass)
            throw new IllegalArgumentException(String.format("Can't merge from [{%s}] to [{%s}].", fromClass, toClass));
        return merge(fromClass, from, to, classesToInclude);
    }

    @SneakyThrows
    private  <T> T merge(final Class targetClass, final T from, final T to, final Class... classesToInclude) {
        if (targetClass != null) {
            processFields(from, to, getDeclaredFields(targetClass), classesToInclude);
            merge(targetClass.getSuperclass(), from, to, classesToInclude);
        }
        return to;
    }

    private <T> void processFields(final T from, final T to, final List<Field> declaredFields,
            final Class[] classesToInclude) throws IllegalAccessException {
        // Merge works with fields directly via reflection.
        // It works fine with normal entities, but doesn't work
        // with Hibernate proxies as we need to set fields values
        // in the real object, not in the proxy.
        // So, we need to unproxy such objects and work directly with them.
        final T unproxiedTo = unproxy(to);

        // Unproxying of the "from" object is done for the sake of completeness.
        // A real use case when "from" object is a proxy doesn't seem to be possible.
        final T unproxiedFrom = unproxy(from);

        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Object fieldValue = declaredField.get(unproxiedFrom);
            if (fieldValue != null) {
                Class<?> type = declaredField.getType();
                if (!Modifier.isStatic(declaredField.getModifiers())) {
                    if (isSimpleType(type) || isArrayOfSimpleType(type) || isAssignableFrom(type, classesToInclude)) {
                        if (declaredField.isAnnotationPresent(Embedded.class)) {
                            //noinspection UnnecessaryLocalVariable
                            Object nestedFrom = fieldValue;
                            Object nestedTo = declaredField.get(unproxiedTo);
                            if (nestedTo == null) setValue(declaredField, nestedFrom, unproxiedTo);
                            else processFields(nestedFrom, nestedTo, getDeclaredFields(nestedFrom.getClass()), classesToInclude);
                        } else setValue(declaredField, fieldValue, unproxiedTo);
                    }
                }
            }
        }
    }

    private List<Field> getDeclaredFields(Class targetClass) {
        return Arrays.asList(targetClass.getDeclaredFields());
    }

    private <T> void setValue(Field declaredField, Object value, T to) {
        try {
            declaredField.set(to, findLinkIfEntity(value));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            log.warn("Can't set [{}] field with [{}] value to [{}]. Cause [{}]",
                    declaredField, value, to, e.getClass() + " : " + e.getMessage());
        }
    }

    private Object findLinkIfEntity(Object value) throws IllegalAccessException {
        Class<?> clazz = value.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                // may return proxy, but we don't care
                // since we're not setting to this object - only read it
                return entityManager.find(clazz, field.get(value));
            }
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public static <T> T unproxy(T possiblyProxiedEntity) {
        if (possiblyProxiedEntity instanceof HibernateProxy) {
            Hibernate.initialize(possiblyProxiedEntity);
            return (T) ((HibernateProxy) possiblyProxiedEntity)
                    .getHibernateLazyInitializer()
                    .getImplementation();
        } else {
            return possiblyProxiedEntity;
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean isAssignableFrom(final Class<?> type, final Class[] classesToInclude) {
        for (Class aClass : classesToInclude) {
            if (aClass.isAssignableFrom(type)) {
                return true;
            }
        }
        return false;
    }

    private static boolean isArrayOfSimpleType(Class<?> type) {
        return type.isArray() && isSimpleType(type.getComponentType());
    }
    private static boolean isSimpleType(final Class<?> type) {
        return type.isEnum() || type.isPrimitive()
                || Number.class.isAssignableFrom(type) || String.class.isAssignableFrom(type)
                || LocalDateTime.class.isAssignableFrom(type) || Boolean.class.isAssignableFrom(type);
    }
}
