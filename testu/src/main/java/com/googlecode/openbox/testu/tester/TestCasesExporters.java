package com.googlecode.openbox.testu.tester;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target({ TYPE })
public @interface TestCasesExporters {
	Class<? extends TestCasesExporter>[] value() default {};
}
