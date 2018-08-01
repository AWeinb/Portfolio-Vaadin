package de.axp.framework.internal;

import de.axp.framework.api.AuthenticatedPortfolioFramework;
import de.axp.framework.api.FrameworkSession;
import de.axp.framework.api.MainThreadSynchronization;
import de.axp.framework.api.services.SessionService;
import de.axp.framework.api.services.TaskService;
import de.axp.framework.internal.service.ServiceRegistry;
import de.axp.framework.internal.service.session.InternalSessionService;
import de.axp.framework.internal.service.task.InternalTaskService;

class AuthenticatedPortfolioFrameworkImpl implements AuthenticatedPortfolioFramework {

	private final ServiceRegistry serviceRegistry;
	private final FrameworkSession session;
	private final SessionService sessionService;
	private final TaskService taskService;

	AuthenticatedPortfolioFrameworkImpl(ServiceRegistry serviceRegistry, FrameworkSession session,
	                                    SessionService sessionService, TaskService taskService) {
		this.serviceRegistry = serviceRegistry;
		this.session = session;
		this.sessionService = sessionService;
		this.taskService = taskService;
	}

	@Override
	public void setMainThreadSynchronization(MainThreadSynchronization synchronization) {
		InternalSessionService internalSessionService = (InternalSessionService) serviceRegistry.get(
				InternalSessionService.class);
		internalSessionService.checkSession(session);

		InternalTaskService internalTaskService = (InternalTaskService) serviceRegistry.get(InternalTaskService.class);
		internalTaskService.setMainThreadSynchronization(synchronization);
	}

	@Override
	public SessionService getFrameworkSessionService() {
		return sessionService;
	}

	@Override
	public TaskService getFrameworkTaskService() {
		return taskService;
	}
}
