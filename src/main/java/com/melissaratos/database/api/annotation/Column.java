package com.melissaratos.database.api.annotation;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {

    String name() default "";
    String type() default "varchar(255)";
    Key key() default Key.NONE;
    boolean nullable() default true;
    boolean autoincrement() default false;

    enum Key {
        PRIMARY,
        UNIQUE,
        INDEX,
        FULLTEXT,
        SPATIAL,
        NONE;
    }
}
