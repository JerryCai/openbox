package com.googlecode.openbox.phone.managers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.googlecode.openbox.phone.Phone;

public class BatchPhonesImpl implements BatchPhones {
	private static final Logger logger = LogManager.getLogger();

	private Phone[] phones;

	private BatchPhonesImpl(Phone... phones) {
		this.phones = phones;
	}

	public static BatchPhonesImpl newInstance(Phone... phones) {
		return new BatchPhonesImpl(phones);
	}

	interface BatchAction {
		void execute(Phone phone);
	}

	private void batchExecute(BatchAction action) {
		for (Phone phone : phones) {
			try {
				action.execute(phone);
			} catch (Exception e) {
				logger.error(
						"batch mode execute encounter error , ingore error and continue to next",
						e);
			}
		}
	}

	@Override
	public void setOperationInterval(final int senconds) {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.setOperationInterval(senconds);
			}

		});

	}

	@Override
	public void register() {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.register();
			}

		});

	}

	@Override
	public void unregister() {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.unregister();
			}

		});

	}

	@Override
	public void dial(final String phoneNumber, final String callId) {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.call(phoneNumber, callId);
			}

		});

	}

	@Override
	public void dial(final String phoneNumber) {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.call(phoneNumber);
			}

		});

	}

	@Override
	public void invite(final String requestUri, final String callId) {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.invite(requestUri, callId);
			}

		});

	}

	@Override
	public void invite(final String requestUri) {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.invite(requestUri);
			}

		});

	}

	@Override
	public void reject() {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.reject();
			}

		});

	}

	@Override
	public void pickup() {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.pickup();
			}

		});

	}

	@Override
	public void sendDTMF(final String dtmf) {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.sendDTMF(dtmf);
			}

		});

	}

	@Override
	public void hangUp() {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.hangUp();
			}

		});

	}

	@Override
	public void close() {
		batchExecute(new BatchAction() {

			@Override
			public void execute(Phone phone) {
				phone.close();
			}

		});

	}

}
