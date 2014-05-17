package com.googlecode.openbox.testu;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import org.apache.http.Header;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import com.googlecode.openbox.http.Response;

public class TestValidator {

	private static final Logger logger = LogManager.getLogger();

	private TestValidator() {

	}

	public static void checkResponseHeader(Response response,
			String headerName, String headerValue) {
		Header[] headers = response.getHeaders();
		boolean isSupport = false;
		for (Header header : headers) {
			if (header.getName().trim().equals(headerName)) {
				isSupport = true;
				String actual = header.getValue();
				String expected = headerValue;
				Assert.assertEquals(actual, expected,
						"header check failed as  header[" + headerName
								+ "] value not expected");
			}
		}
		if (headerValue != null) {
			Assert.assertTrue(isSupport, "header check failed as  header["
					+ headerName + "] doesn't find from response !");
		} else {
			if (isSupport) {
				Assert.fail("we expected headerName=[" + headerName
						+ "] shouldn't be find , but find it ");
			}
		}

	}

	public static void checkStatusCode(Response response, int statusCode) {
		Assert.assertEquals(response.getStatusCode(), statusCode,
				"header response status code  failed !");
	}

	public static <T> void assertResponse(T actual, T expected) {
		TestResult rb = compareObject(actual, expected);
		if (null == rb) {
			return;
		}
		if (logger.isInfoEnabled())
			logger.info(rb.getText());

		Assert.assertTrue(rb.isSuccess(), rb.getText());
	}

	public static <T> TestResult compareObject(T actual, T expected) {
		Set<Field> fields = getAllFields(actual.getClass());
		if (null == fields) {
			logger.warn("test validate compare object with nothings");
			return null;
		}
		TestResult testResult = TestResult.create();
		testResult
				.addMessage("\n\n------------------------------check point list----------------------------------\n");
		for (Field field : fields) {
			boolean needReset = false;
			if (!field.isAccessible()) {
				field.setAccessible(true);
				needReset = true;
			}
			Class<?> type = field.getType();

			try {
				Object actualItem = field.get(actual);
				Object expectedItem = field.get(expected);
				if (null == expectedItem) {
					// if null is expected , seen as * , any things are ok
					testResult.setItem(field.getName(), actualItem,
							expectedItem, true);
					continue;
				}
				if (type.isPrimitive() || type.isEnum()) {
					testResult.setItem(field.getName(), actualItem,
							expectedItem, actualItem == expectedItem);
				} else if (type == String.class) {
					testResult.setItem(field.getName(), actualItem,
							expectedItem, actualItem.equals(expectedItem));
				} else {
					testResult
							.merge(compareObject(actualItem, expectedItem));
				}
			} catch (Exception e) {
				String message = "get field [" + field + "] error as ";
				testResult.setItem(field.getName(), message,
						"should not be null", false);
			}

			if (needReset) {
				field.setAccessible(false);
			}

		}
		testResult.addMessage("\noverall check result based above list is ["
				+ testResult.isSuccess() + "]\n");
		testResult
				.addMessage("--------------------------------------------------------------------------------\n");

		return testResult;
	}

	private static Set<Field> getAllFields(final Class<?> type) {
		Set<Field> fields = new HashSet<Field>();
		if (null != type.getSuperclass()) {
			fields.addAll(getAllFields(type.getSuperclass()));
		}
		Field[] localFields = type.getDeclaredFields();
		for (Field field : localFields) {
			fields.add(field);
		}
		return fields;
	}

}
