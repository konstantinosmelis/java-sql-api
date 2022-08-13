package com.melissaratos.database.api.annotation;

import com.melissaratos.database.api.Engine;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Table {

    String name() default "";
    Engine engine() default Engine.INNODB;
}
