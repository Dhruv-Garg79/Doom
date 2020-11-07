package doom.http.annotations;

import doom.http.HttpMethods;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@HttpMethod(HttpMethods.PUT)
public @interface PUT {
    String value();
}
