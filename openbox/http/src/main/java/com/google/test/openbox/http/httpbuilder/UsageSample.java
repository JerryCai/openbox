package com.google.test.openbox.http.httpbuilder;

import java.io.IOException;

import org.dom4j.Document;

import com.google.test.openbox.common.XmlUtils;
import com.google.test.openbox.http.httpbuilder.HttpBuilder.HttpBuilderXmlResponseHanlder;



public class UsageSample {

	public static final void main(String... args) throws IllegalStateException,
			IOException {

		HttpBuilder
				.create()
				.setMethod("GET")
				.setUrl("http://qfwebex03.qa.webex.com/meetings/v1/meeting/M46RQFDO4KOQTFXVJU0OMRN2TM-2KG9/invites")
				.addParameter("token", token).addHeader("password", "P@ss99")
				.execute();

		HttpBuilder.create().setMethod("POST").setScheme("https")
				.setHost("login-qf1-qz1.qa.webex.com").setPath("/cas/auth.do")
				.addForm("cmd", "login")
				.addForm("email", "xiaolong_qf_0@qfwebex03.qa.webex.com")
				.addForm("password", "P@ss123")
				.addForm("clientid", "5da5ab16460a9f1616d229ebee131535")
				.execute();

		Document xml = HttpBuilder.create().setMethod("POST").setScheme("https")
				.setHost("login-qf1-qz1.qa.webex.com").setPath("/cas/auth.do")
				.addForm("cmd", "login")
				.addForm("email", "xiaolong_qf_0@qfwebex03.qa.webex.com")
				.addForm("password", "P@ss123")
				.addForm("clientid", "5da5ab16460a9f1616d229ebee131535")
				.execute(HttpBuilderXmlResponseHanlder.create());
		System.out.println(XmlUtils.querySingleXPath(xml, "//LoginResponse/response/result"));

		HttpBuilder httpBuilder = HttpBuilder.create();
		httpBuilder.setMethod("POST");
		httpBuilder.setScheme("http");
		httpBuilder.setHost("login-qf1-qz1.qa.webex.com");
		httpBuilder.setPath("/cas/auth.do");
		httpBuilder.addForm("cmd", "login");
		httpBuilder.addForm("email", "xiaolong_qf_0@qfwebex03.qa.webex.com");
		httpBuilder.addForm("password", "P@ss123");
		httpBuilder.addForm("clientid", "5da5ab16460a9f1616d229ebee131535");

		httpBuilder.execute();

	}

	public static final String token = "AAABQjHxbdUAAHCACEfa12jDr6WizJdwrIWRcnOROZtWhHqC2F3CURYqmUwFAABJADJppaZXq7tl0"
			+ "4fMAp20Y%2B5bB6FVxAa1R%2FScfsM10BDOIeYjbKR6jX782Ru6WZos9tE8TB0QG%2F3eoznp3NJgwEL%2FkztKLlTUsMelJHkvXjB0qU"
			+ "CgDcjttN6po6R3oxYk8%2BVBt1TlvpfatZctWPSVHvUh3F5UwCCFaEiBr3eLrMkQAloViDZByhmxbl%2B7dTfjwhmxf%2BD%2BtVL6O"
			+ "cnJNv%2Blrog6QhnQq1cq3DdyV5X9hqiEieSY6R4HfDFMsDyYD9BiJLkMbExOo8V8ZOLaffOdWGV2Pg79tSpDAKjVAvf7tyF4wfBXdK%2F"
			+ "tOt85feqe9OroAs8SZ3nWtDEGlC81vhG3hcXYdCMsnwJhcKwsGZiL%2BvrY69g1JSlhs6EzxnG72HswOEs2bvgFktHOJunJrDFwJL5k";

}
