package com.googlecode.openbox.testu.tester;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE,ElementType.PACKAGE})
@Inherited
@Documented
public @interface QA {
	public String name();
	public String email() default"";
	public String id() default "";
}
