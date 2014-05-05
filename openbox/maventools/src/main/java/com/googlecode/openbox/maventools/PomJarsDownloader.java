package com.googlecode.openbox.maventools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.dom4j.Document;
import org.dom4j.Element;

import com.googlecode.openbox.common.IOUtils;
import com.googlecode.openbox.common.UtilsAPI;
import com.googlecode.openbox.common.XmlUtils;

public class PomJarsDownloader {

	public static final String XPATH_DEPENDANCES = "/project/dependencies/dependency";
	public static final String XPATH_DEPENDANCE_GROUPID = "/groupId";
	public static final String XPATH_DEPENDANCE_ARTIFACTID = "/artifactId";
	public static final String XPATH_DEPENDANCE_VERSION = "/version";

	public static final String XPATH_REPOSITORIES = "/project/repositories/repository";
	public static final String XPATH_REPOSITORIE_RUL = "/url";

	public static final String PATH_SPLIT = "/";
	public static final String JAR_SPLIT = "-";
	public static final String JAR_EXTENTATION = ".jar";

	private String pomFilePath;
	private String downloadLocation;
	private Document pom;
	private String[] hosts;

	public PomJarsDownloader(String pomFilePath, String downloadLocation) {
		this.pomFilePath = pomFilePath;
		this.downloadLocation = downloadLocation;
		init();
	}

	private void init() {
		initPom();
		initRepositoryHosts();
	}

	private void initPom() {
		pom = XmlUtils.buildXML(this.pomFilePath);
		System.out.println(pom.asXML());
		XmlUtils.printXml(pom);
	}

	private void initRepositoryHosts() {
		List<Element> repositories = listRepositories();
		int num = repositories.size();
		hosts = new String[num];
		for (int i = 0; i < num; i++) {
			hosts[i] = this.parseRepositoryHostUrl(repositories.get(i));
		}
	}

	@SuppressWarnings("unchecked")
	private List<Element> listDependances() {
		return XmlUtils.queryXPath(pom, XPATH_DEPENDANCES);
	}

	@SuppressWarnings("unchecked")
	private List<Element> listRepositories() {
		return XmlUtils.queryXPath(pom, XPATH_REPOSITORIES);
	}

	private String parseDependanceSubPath(Element dependance) {
		String groupId = XmlUtils.querySingleXPath(dependance,
				XPATH_DEPENDANCE_GROUPID).replaceAll("\\.", PATH_SPLIT);
		String artifactId = XmlUtils.querySingleXPath(dependance,
				XPATH_DEPENDANCE_ARTIFACTID);
		String version = XmlUtils.querySingleXPath(dependance,
				XPATH_DEPENDANCE_VERSION);

		StringBuilder sb = new StringBuilder(PATH_SPLIT);
		sb.append(groupId).append(PATH_SPLIT).append(artifactId)
				.append(PATH_SPLIT).append(version).append(PATH_SPLIT)
				.append(artifactId).append(JAR_SPLIT).append(version)
				.append(JAR_EXTENTATION);

		return sb.toString();
	}

	private String parseRepositoryHostUrl(Element repository) {
		return XmlUtils.querySingleXPath(repository, XPATH_REPOSITORIE_RUL);
	}

	public void downloadAllJars() {

		List<Element> dependances = listDependances();
		CloseableHttpClient httpClient = HttpClients.createDefault();

		try {
			for (Element dependance : dependances) {
				String subPath = parseDependanceSubPath(dependance);
				for (int i = 0; i < hosts.length; i++) {
					String path = hosts[i];
					if(path.endsWith("/")){
						path = path.substring(0, path.length()-1);
					}
					path = path + subPath;
					HttpGet httpGet = new HttpGet(path);
					CloseableHttpResponse response = httpClient
							.execute(httpGet);

					if (200 != response.getStatusLine().getStatusCode()) {
						response.close();
						System.out.println(httpGet.getURI()+"-->["+response.getStatusLine().getStatusCode()+"]");
						continue;
					}
					String jarName = UtilsAPI.getLastPath(subPath);
					String downloadJarPath = downloadLocation + "\\" + jarName;
					InputStream content = response.getEntity().getContent();
					IOUtils.createFile(downloadJarPath, content);
//					installPomJar(dependance, downloadJarPath);
					response.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("downlaod pom jar file failed as :", e);
		} finally {
			try {
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public void installJarsBaseOnPomOnly() {

		List<Element> dependances = listDependances();

		try {
			for (Element dependance : dependances) {
				String subPath = parseDependanceSubPath(dependance);
				String jarName = UtilsAPI.getLastPath(subPath);
				String downloadJarPath = downloadLocation + "\\" + jarName;
				installPomJar(dependance, downloadJarPath);
			}
		} catch (Exception e) {
			throw new RuntimeException("downlaod pom jar file failed as :", e);
		}
	}

	private void installPomJar(Element dependance, String downloadedJarPath) {
		String cmd = getInstallMvnCmd(dependance, downloadedJarPath);
		exec(cmd);
	}

	private String getInstallMvnCmd(Element dependance, String downloadedJarPath) {
		String groupId = XmlUtils.querySingleXPath(dependance,
				XPATH_DEPENDANCE_GROUPID);
		String artifactId = XmlUtils.querySingleXPath(dependance,
				XPATH_DEPENDANCE_ARTIFACTID);
		String version = XmlUtils.querySingleXPath(dependance,
				XPATH_DEPENDANCE_VERSION);

		return getMvnInstallCommand(groupId, artifactId, version,
				downloadedJarPath);
	}

	public static String getMvnInstallCommand(String groupId,
			String artifactId, String version, String jarLocation) {
		StringBuilder sb = new StringBuilder(
				"mvn.bat install:install-file -DgroupId=");
		sb.append(groupId);
		sb.append(" -DartifactId=").append(artifactId);
		sb.append(" -Dversion=").append(version);
		sb.append(" -Dfile=").append(jarLocation);
		sb.append(" -Dpackaging=jar");
		return sb.toString();
	}

	public void exec(String javaExcute) {
		Process p = null;
		InputStream is = null;
		try {
			System.out.println("java_executor-->cmd=[" + javaExcute + "]");
			p = Runtime.getRuntime().exec(javaExcute);
			is = p.getInputStream();
			InputStreamReader bi = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(bi);
			String message;
			message = br.readLine();
			while (message != null && !"".equals(message)) {
				System.out.println(message);
				message = br.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} finally {
			try {
				if (null != is) {
					is.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			p.destroy();
		}

	}

	public static void main(String... args) {
		String dmsTestPlatformPomFilePath = "D:\\EclipseEE\\workspace\\test_platform\\pom.xml";
		String dmsTestPlatformDownloadLocation = "D:\\pom_downloads\\test_platform";

		// String dmsqaPomFilePath = "C:\\EclipseEE\\workspace\\dmsqa\\pom.xml";
		// String dmsqaDownloadLocation = "C:\\pom_downloads\\dmsqa";

		// String pomFilePath = dmsqaPomFilePath;
		// String downloadLocation = dmsqaDownloadLocation;

		String pomFilePath = dmsTestPlatformPomFilePath;
		String downloadLocation = dmsTestPlatformDownloadLocation;

		PomJarsDownloader pomDownloader = new PomJarsDownloader(pomFilePath,
				downloadLocation);
		pomDownloader.downloadAllJars();
	}

}
