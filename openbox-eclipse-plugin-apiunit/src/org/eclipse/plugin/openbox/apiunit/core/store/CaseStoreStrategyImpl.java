package org.eclipse.plugin.openbox.apiunit.core.store;

public class CaseStoreStrategyImpl implements CaseStoreStrategy {

	public static final String CASE_STORGE_ROOT_PATH = "api_cases";
	public static final String CASE_FILE_TYPE=".xml";

	private static final CaseStoreStrategyImpl instance = new CaseStoreStrategyImpl();

	private CaseStoreStrategyImpl() {

	}

	public static CaseStoreStrategyImpl getInstance() {
		return instance;
	}

	@Override
	public String getRootPath() {
		return CASE_STORGE_ROOT_PATH;
	}

	@Override
	public String getStoreFileExtention() {
		return CASE_FILE_TYPE;
	}

}
