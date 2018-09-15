package de.axp.framework.internal;

import de.axp.framework.api.services.DataService;
import de.axp.framework.api.services.PluginService;
import de.axp.framework.api.services.ServiceService;
import de.axp.framework.api.services.TaskService;
import de.axp.framework.internal.services.data.DataServiceFactory;
import de.axp.framework.internal.services.plugin.PluginServiceFactory;
import de.axp.framework.internal.services.service.ServiceServiceFactory;
import de.axp.framework.internal.services.task.TaskServiceFactory;

public final class InternalFactory {

	private InternalFactory() {
	}

	public static PortfolioFrameworkImpl createFramework() {
		ServiceService serviceService = ServiceServiceFactory.createServiceService();
		PluginService pluginService = PluginServiceFactory.createPluginService();
		TaskService taskService = TaskServiceFactory.createTaskService(serviceService);
		DataService dataService = DataServiceFactory.createDataService(serviceService);

		serviceService.registerNewService(ServiceService.class, serviceService);
		serviceService.registerNewService(PluginService.class, pluginService);
		serviceService.registerNewService(TaskService.class, taskService);
		serviceService.registerNewService(DataService.class, dataService);

		return new PortfolioFrameworkImpl(serviceService);
	}
}
