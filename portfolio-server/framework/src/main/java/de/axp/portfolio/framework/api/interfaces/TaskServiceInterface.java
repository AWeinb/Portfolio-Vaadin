package de.axp.portfolio.framework.api.interfaces;

import de.axp.portfolio.framework.api.FrameworkExternalHandler;
import de.axp.portfolio.framework.internal.service.event.Task;

public interface TaskServiceInterface {

	void addTaskHandler(TaskHandler taskHandler);

	void addTaskHandler(String contextId, TaskHandler taskHandler);

	void triggerTask(String taskId, Object content, TaskPromise promise);

	void triggerTask(String contextId, String taskId, Object content, TaskPromise promise);

	enum TaskResolution {
		RESOLVED, REJECTED
	}

	@FunctionalInterface
	interface TaskHandler extends FrameworkExternalHandler {

		void handle(Task event, TaskPromise answer);

	}

	@FunctionalInterface
	interface TaskPromise {

		void on(TaskResolution resolution, Object content);

	}
}
