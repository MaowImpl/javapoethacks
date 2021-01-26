package maow.javapoethacks;

import com.squareup.javapoet.TypeName;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains hacky methods to bypass weird JavaPoet restrictions.
 *
 * @since 1.0.0
 * @author Maow
 */
public final class JavaPoetUtils {
    private static final Constructor<?> CTOR;

    private static final Map<String, TypeName> TYPES = new HashMap<>();

    /**
     * Returns a new instance of {@link TypeName} based on the supplied String.<br><br>
     *
     * In some code bases, it's not possible to get a {@link java.lang.reflect.Type} or {@link javax.lang.model.type.TypeMirror},
     * in this situation, the other solution is to convert a {@link com.squareup.javapoet.ClassName} to a TypeName,
     * but this also requires having a canonical name, and in very specific situations, it can be very intensive trying to find a canonical name.<br><br>
     *
     * This method caches all of its results in a {@link HashMap} to help rectify some performance issues related to reflection.
     *
     * @param name The underlying keyword of the TypeName
     * @return An instance of TypeName with a keyword specified from the name parameter of this method
     * @since 1.0.0
     */
    public static TypeName from(String name) {
        return TYPES.computeIfAbsent(name, JavaPoetUtils::create);
    }

    private static TypeName create(String name) {
        try {
            return (TypeName) CTOR.newInstance(name);
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return null;
    }

    static {
        Constructor<?> ctor = null;
        try {
            ctor = TypeName.class.getDeclaredConstructor(String.class);
            ctor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        CTOR = ctor;
    }
}
