package com.googlecode.openbox.testu.tester;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE })
@Inherited
@Documented
public @interface CaseModule {

	public String name();
	public String parent() default "";
}
