package de.axp.framework.internal.service.task;

import de.axp.framework.api.services.TaskService;
import de.axp.framework.internal.mainloop.MainLoop;

public final class TaskServiceFactory {

	public static InternalTaskService createInternalTaskService(MainLoop mainLoop) {
		InternalTaskServiceImpl internalTaskService = new InternalTaskServiceImpl();
		mainLoop.addPlugin(internalTaskService);
		return internalTaskService;
	}

	public static TaskService.Task createTask(String contextId, String taskId, Object content) {
		return new TaskImpl(contextId, taskId, content);
	}
}
