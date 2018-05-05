package de.axp.portfolio.framework.internal.service.command;

import de.axp.portfolio.framework.api.interaction.FrameworkHandler;
import de.axp.portfolio.framework.api.interaction.FrameworkPackage;
import de.axp.portfolio.framework.api.interaction.FrameworkPromise;
import de.axp.portfolio.framework.internal.service.InternalFrameworkService;

public interface CommandService extends InternalFrameworkService {

	void dispatchCommand(String sessionID, String packageID, Command commandPackage) throws InterruptedException;

	interface CommandHandler extends FrameworkHandler {

		void execute(String sessionID, String commandID, Object content, FrameworkPromise promiseToResolveOrReject);
	}

	class Command implements FrameworkPackage {

		private String sessionID;
		private String packageID;
		private Object content;
		private FrameworkPromise promise;

		public Command(String sessionID, String packageID, Object content, FrameworkPromise promise) {
			this.sessionID = sessionID;
			this.packageID = packageID;
			this.content = content;
			this.promise = promise;
		}

		@Override
		public String getSessionID() {
			return sessionID;
		}

		@Override
		public String getPackageID() {
			return packageID;
		}

		@Override
		public Object getContent() {
			return content;
		}

		public FrameworkPromise getPromise() {
			return promise;
		}
	}

	class Response implements FrameworkPackage {

		private String sessionID;
		private String packageID;
		private Object content;

		public Response(String sessionID, String packageID, Object content) {
			this.sessionID = sessionID;
			this.packageID = packageID;
			this.content = content;
		}

		@Override
		public String getSessionID() {
			return sessionID;
		}

		@Override
		public String getPackageID() {
			return packageID;
		}

		@Override
		public Object getContent() {
			return content;
		}
	}
}
