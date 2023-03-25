package ren.yuda.encryption;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface Encrypt {
    String algorithm() default "des";
}
