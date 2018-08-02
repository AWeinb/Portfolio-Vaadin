package de.axp.framework.internal.services.task;

import de.axp.framework.api.services.TaskService;
import de.axp.framework.api.services.TaskService.TaskHandler;
import de.axp.framework.internal.infrastructure.mainloop.MainLoop;
import de.axp.framework.internal.infrastructure.mainloop.MainLoopPackage;

class TaskHandlerNotifier implements MainLoop.MainLoopListener {

	private final TaskHandlerRegistry handlerRegistry;
	private final MainLoop.MainLoopAccessor outputBufferAccessor;

	TaskHandlerNotifier(TaskHandlerRegistry handlerRegistry, MainLoop.MainLoopAccessor outputBufferAccessor) {
		this.handlerRegistry = handlerRegistry;
		this.outputBufferAccessor = outputBufferAccessor;
	}

	@Override
	public void notify(MainLoopPackage aPackage) {
		TaskService.Task task = (TaskService.Task) aPackage.getPayload();
		String sessionId = aPackage.getSessionId();
		String contextId = task.getContextId();
		String taskId = task.getTaskId();

		TaskHandler handler = handlerRegistry.getTaskHandler(sessionId, contextId);
		TaskService.TaskPromise callback = createAnswerPromise(sessionId, taskId);

		if (handler != null) {
			handler.handle(task, callback);
		} else {
			callback.respond(TaskService.TaskResolution.UNHANDLED, task);
		}
	}

	private TaskService.TaskPromise createAnswerPromise(String sessionId, String taskId) {
		return (resolution, result) -> {
			TaskResult response = TaskResult.build(taskId, resolution, result);
			MainLoopPackage packedResponse = new MainLoopPackage(sessionId, response);
			outputBufferAccessor.put(packedResponse);
		};
	}
}
