
package test.playo.com.playotest.utils;

import com.squareup.moshi.JsonQualifier;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Documented
@JsonQualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface NullPrimitive {

    /** Fallback value for {@code boolean} primitives. Default: {@code false}. */
    boolean fallbackBoolean() default false;

    /** Fallback value for {@code byte} primitives. Default: {@code Byte.MIN_VALUE}. */
    byte fallbackByte() default Byte.MIN_VALUE;

    /** Fallback value for {@code char} primitives. Default: {@code Character.MIN_VALUE}. */
    char fallbackChar() default Character.MIN_VALUE;

    /** Fallback value for {@code double} primitives. Default: {@code Double.MIN_VALUE}. */
    double fallbackDouble() default Double.MIN_VALUE;

    /** Fallback value for {@code float} primitives. Default: {@code Float.MIN_VALUE}. */
    float fallbackFloat() default Float.MIN_VALUE;

    /** Fallback value for {@code int} primitives. Default: {@code Integer.MIN_VALUE}. */
    int fallbackInt() default Integer.MIN_VALUE;

    /** Fallback value for {@code long} primitives. Default: {@code Long.MIN_VALUE}. */
    long fallbackLong() default Long.MIN_VALUE;

    /** Fallback value for {@code short} primitives. Default: {@code Short.MIN_VALUE}. */
    short fallbackShort() default Short.MIN_VALUE;
}
