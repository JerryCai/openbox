package com.googlecode.openbox.testu.tester;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.googlecode.openbox.testu.tester.TestCaseResults.Result;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Inherited
@Documented
public @interface TempForceAllTestResult {
	public Result value();
	public String comments() default "This is temp force all test results , not actual executed results!";
}
