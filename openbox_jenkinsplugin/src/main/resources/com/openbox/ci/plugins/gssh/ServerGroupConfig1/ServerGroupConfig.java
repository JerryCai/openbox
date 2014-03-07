package com.cisco.ci.plugins.gssh;

import hudson.Extension;
import hudson.model.ManagementLink;
import hudson.model.Descriptor.FormException;
import hudson.model.Hudson;
import hudson.tasks.BuildWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import com.cisco.ci.plugins.gssh.GsshBuilderWrapper.GsshDescriptorImpl;


@Extension
public class ServerGroupConfig extends ManagementLink {

	@Extension
	public static final DescriptorImpl DESCRIPTOR = new DescriptorImpl();
	
	public String getDisplayName() {
		return "Cloud Server Management";
	}

	@Override
	public String getIconFileName() {
		return "cloud_server_logo.png";
	}

	@Override
	public String getUrlName() {
		return "server-pool";
	}
	
    public String getDescription() {
        return "Cisco cloud server management for more easily to access your remote servers ";
    }

	public static final String ERROR_TIP = "Current Cisco cloud Jenkins plugin encoutner error , please contact Jerry Cai : xiacai@cisco.com to check it , Thanks for your support";
	
	public void doServerGroupSubmit(StaplerRequest req, StaplerResponse rsp)
			throws IOException, UnsupportedEncodingException, ServletException,
			FormException {
		Hudson.getInstance().checkPermission(Hudson.ADMINISTER);
		if (DESCRIPTOR.doServerGroupSubmit(req, rsp)) {
			rsp.sendRedirect(".");
		} else {
			rsp.sendError(500, ERROR_TIP);
		}
	}

	public void doServerSubmit(StaplerRequest req, StaplerResponse rsp)
			throws IOException, UnsupportedEncodingException, ServletException,
			FormException {
		Hudson.getInstance().checkPermission(Hudson.ADMINISTER);
		if (DESCRIPTOR.doServerSubmit(req, rsp)) {
			rsp.sendRedirect(".");
		} else {
			rsp.sendError(500, ERROR_TIP);
		}
	}
	
	
	public static class DescriptorImpl extends GsshDescriptorImpl{
		public DescriptorImpl() {
			super();
		}

		public DescriptorImpl(Class<? extends BuildWrapper> clazz) {
			super(clazz);
		}
	}

}
