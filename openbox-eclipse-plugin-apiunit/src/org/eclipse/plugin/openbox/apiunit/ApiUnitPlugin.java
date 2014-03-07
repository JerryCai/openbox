package org.eclipse.plugin.openbox.apiunit;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class ApiUnitPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.plugin.openbox.apiunit"; //$NON-NLS-1$
	public static final String ICON_PATH = "/icons/";

	// The shared instance
	private static ApiUnitPlugin plugin;

	/**
	 * The constructor
	 */
	public ApiUnitPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
	 * )
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		disposeImages();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static ApiUnitPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, ICON_PATH + path);
	}
	
	public static Image getImage(String path){
		return getImageDescriptor(path).createImage();
	}

	public static Image getCachedImage(String path) {
		if (!IMAGE_CACHE.containsKey(path)) {
			IMAGE_CACHE.put(path, getImageDescriptor(path).createImage());
		}
		return IMAGE_CACHE.get(path);
	}

	public static void disposeImages() {
		Collection<Image> images = IMAGE_CACHE.values();
		for (Image image : images) {
			image.dispose();
		}
		IMAGE_CACHE.clear();
	}

	private static final Map<String, Image> IMAGE_CACHE = new HashMap<String, Image>();
}
