package de.axp.framework.internal;

import de.axp.framework.api.PortfolioFramework;
import de.axp.framework.api.services.SessionService;
import de.axp.framework.api.services.TaskService;
import de.axp.framework.internal.services.BaseServiceRegistry;

class PortfolioFrameworkImpl implements PortfolioFramework {

	private final SessionService.FrameworkSession session;
	private final BaseServiceRegistry.AuthenticatedServiceRegistry serviceRegistry;

	PortfolioFrameworkImpl(SessionService.FrameworkSession session,
	                       BaseServiceRegistry.AuthenticatedServiceRegistry serviceRegistry) {
		this.session = session;
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	public SessionService getFrameworkSessionService() {
		return serviceRegistry.getService(SessionService.class);
	}

	@Override
	public TaskService getFrameworkTaskService() {
		return serviceRegistry.getService(TaskService.class);
	}
}
