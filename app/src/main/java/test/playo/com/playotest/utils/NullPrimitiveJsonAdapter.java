package test.playo.com.myapplication.datamanager;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedHashSet;
import java.util.Set;




public final class NullPrimitiveJsonAdapter<T> extends JsonAdapter<T> {

    /** Set of primitives classes that are supported by <strong>this</strong> adapter. */
    static final Set<Class<?>> PRIMITIVE_CLASSES = new LinkedHashSet<>();

    static {
        PRIMITIVE_CLASSES.add(boolean.class);
        PRIMITIVE_CLASSES.add(byte.class);
        PRIMITIVE_CLASSES.add(char.class);
        PRIMITIVE_CLASSES.add(double.class);
        PRIMITIVE_CLASSES.add(float.class);
        PRIMITIVE_CLASSES.add(int.class);
        PRIMITIVE_CLASSES.add(long.class);
        PRIMITIVE_CLASSES.add(short.class);
    }

    public static final JsonAdapter.Factory FACTORY = new JsonAdapter.Factory() {
        @Override public JsonAdapter<?> create(Type type, Set<? extends Annotation> annotations,
                                               Moshi moshi) {
            Annotation annotation = findAnnotation(annotations, NullPrimitive.class);
            if (annotation == null) return null;

            Class<?> rawType = Types.getRawType(type);
            if (!PRIMITIVE_CLASSES.contains(rawType)) return null;

            // Clone the set and remove the annotation so that we can pass the remaining set to moshi.
            Set<? extends Annotation> reducedAnnotations = new LinkedHashSet<>(annotations);
            reducedAnnotations.remove(annotation);

            String fallbackType = fallbackType(rawType);
            Object fallback = retrieveFallback((NullPrimitive) annotation, fallbackType);

            return new NullPrimitiveJsonAdapter<>(moshi.adapter(type, reducedAnnotations),
                    fallback, fallbackType);
        }

        /** Invokes the appropriate fallback method based on the {@code fallbackType}. */
        private Object retrieveFallback(NullPrimitive annotation, String fallbackType) {
            try {
                Method fallbackMethod = NullPrimitive.class.getMethod(fallbackType);
                return fallbackMethod.invoke(annotation);
            } catch (Exception e) {
                throw new AssertionError(e);
            }
        }

        /** Constructs the appropriate fallback method name based on the {@code rawType}. */
        private String fallbackType(Class<?> rawType) {
            String typeName = rawType.getSimpleName();
            String methodSuffix = typeName.substring(0, 1).toUpperCase() + typeName.substring(1);
            return "fallback" + methodSuffix;
        }
    };

    final JsonAdapter<T> adapter;
    final T fallback;
    final String fallbackType;

    NullPrimitiveJsonAdapter(JsonAdapter<T> adapter, T fallback, String fallbackType) {
        this.adapter = adapter;
        this.fallback = fallback;
        this.fallbackType = fallbackType;
    }

    @Override public T fromJson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            reader.nextNull(); // We need to consume the value.
            return fallback;
        }
        return adapter.fromJson(reader);
    }

    @Override public void toJson(JsonWriter writer, T value) throws IOException {
        adapter.toJson(writer, value);
    }

    @Override public String toString() {
        return adapter + ".fallbackOnNull(" + fallbackType + '=' + fallback + ')';
    }
    public static Annotation findAnnotation(Set<? extends Annotation> annotations,
                                            Class<? extends Annotation> annotationClass) {
        if (annotations.isEmpty()) return null; // Save an iterator in the common case.
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == annotationClass) {
                return annotation;
            }
        }
        return null;
    }
}
