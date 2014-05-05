package com.googlecode.openbox.server;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.common.ExecuteResult;

public class ConcurrentServerGroup extends AbstractServerGroup {
	private static final Logger logger = LogManager.getLogger();

	public ConcurrentServerGroup() {
		super();
	}

	public static ConcurrentServerGroup newInstance() {
		return new ConcurrentServerGroup();
	}

	@Override
	public ExecuteResult executeShell(final String shell) {
		Server[] servers = listServers();
		int num = servers.length;
		if (num == 0) {
			throw new RuntimeException(
					"execute shell failed as server group is empty , can't found any server in it");
		}
		ExecutorService executorService = Executors.newFixedThreadPool(num);
		@SuppressWarnings("unchecked")
		final Future<ExecuteResult>[] results = new Future[num];
		for (int i = 0; i < num; i++) {
			final Server server = servers[i];
			Callable<ExecuteResult> task = new Callable<ExecuteResult>() {
				@Override
				public ExecuteResult call() throws Exception {
					final ExecuteResult executeResult = ExecuteResult
							.newInstance(true);
					try {
						server.getSshClient().executeShellByFTP(System.out,
								shell);
						executeResult.appendMessage("execute shell on server :"
								+ server + " success !");
					} catch (Exception e) {
						executeResult.setResult(false);
						executeResult.appendMessage("execute shell on server :"
								+ server + " failed !");
					}
					return executeResult;
				}
			};
			results[i] = executorService.submit(task);
		}
		ExecuteResult[] executeResults = getExecuteResults(results);
		executorService.shutdown();
		return ExecuteResult.mergeExecuteResult(executeResults);
	}

	@Override
	public ExecuteResult executeCommands(final String command) {
		Server[] servers = listServers();
		int num = servers.length;
		if (num == 0) {
			throw new RuntimeException(
					"execute shell failed as server group is empty , can't found any server in it");
		}
		ExecutorService executorService = Executors.newFixedThreadPool(num);
		@SuppressWarnings("unchecked")
		final Future<ExecuteResult>[] results = new Future[num];
		for (int i = 0; i < num; i++) {
			final Server server = servers[i];
			Callable<ExecuteResult> task = new Callable<ExecuteResult>() {
				@Override
				public ExecuteResult call() throws Exception {
					final ExecuteResult executeResult = ExecuteResult
							.newInstance(true);
					try {
						server.getSshClient().executeCommand(System.out,
								command);
						executeResult
								.appendMessage("execute command on server :"
										+ server + " success !");
					} catch (Exception e) {
						executeResult.setResult(false);
						executeResult
								.appendMessage("execute command on server :"
										+ server + " failed !");
					}
					return executeResult;
				}
			};
			results[i] = executorService.submit(task);
		}
		ExecuteResult[] executeResults = getExecuteResults(results);
		executorService.shutdown();
		return ExecuteResult.mergeExecuteResult(executeResults);
	}

	@Override
	public String[] executeSingleCommandGetResponse(final String command) {
		Server[] servers = listServers();
		int num = servers.length;
		if (num == 0) {
			throw new RuntimeException(
					"execute shell failed as server group is empty , can't found any server in it");
		}
		ExecutorService executorService = Executors.newFixedThreadPool(num);
		@SuppressWarnings("unchecked")
		final Future<String>[] results = new Future[num];
		for (int i = 0; i < num; i++) {
			final Server server = servers[i];
			Callable<String> task = new Callable<String>() {
				@Override
				public String call() throws Exception {
					try {
						return server.getSshClient().executeSingleCommand(
								System.out, command);
					} catch (Exception e) {
						logger.error("execute single command error on server: "
								+ server, e);
						return null;
					}
				}
			};
			results[i] = executorService.submit(task);
		}
		try {
			return getExecuteStringResults(results);
		} finally {
			executorService.shutdown();
		}
	}

	private ExecuteResult[] getExecuteResults(Future<ExecuteResult>[] results) {
		int num = results.length;
		ExecuteResult[] executeResults = new ExecuteResult[num];
		for (int i = 0; i < num; i++) {
			Future<ExecuteResult> result = results[i];
			try {
				executeResults[i] = result.get();
			} catch (Exception e) {
				throw new RuntimeException(
						"wait and get future result failed !", e);
			}
		}
		return executeResults;
	}

	private String[] getExecuteStringResults(Future<String>[] results) {
		int num = results.length;
		String[] executeResults = new String[num];
		for (int i = 0; i < num; i++) {
			Future<String> result = results[i];
			try {
				executeResults[i] = result.get();
			} catch (Exception e) {
				throw new RuntimeException(
						"wait and get future result failed !", e);
			}
		}
		return executeResults;
	}
}
