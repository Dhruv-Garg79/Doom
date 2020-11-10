package doom.http.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is used to define path of controller and can only be used on class
 * and should not contain any queryParam or PathParam
 * */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Path {
    String value() default "/";
}
