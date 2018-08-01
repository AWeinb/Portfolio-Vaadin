package de.axp.framework.internal.service.task;

import de.axp.framework.api.serviceinterfaces.TaskServiceInterface;
import de.axp.framework.api.serviceinterfaces.TaskServiceInterface.TaskHandler;
import de.axp.framework.internal.mainloop.MainLoop;
import de.axp.framework.internal.mainloop.MainLoopPackage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class TaskHandlerNotifier implements MainLoop.MainLoopListener {

	private final MainLoop.MainLoopAccessor outputBufferAccessor;
	private final Map<String, Map<String, TaskHandler>> handlers = Collections.synchronizedMap(new HashMap<>());

	TaskHandlerNotifier(MainLoop.MainLoopAccessor outputBufferAccessor) {
		this.outputBufferAccessor = outputBufferAccessor;
	}

	void addHandler(String sessionId, String context, TaskHandler handler) {
		if (!handlers.containsKey(sessionId)) {
			handlers.put(sessionId, Collections.synchronizedMap(new HashMap<>()));
		}
		handlers.get(sessionId).put(context, handler);
	}

	@Override
	public void notify(MainLoopPackage aPackage) {
		TaskServiceInterface.Task task = (TaskServiceInterface.Task) aPackage.getPayload();
		String sessionId = aPackage.getSessionId();
		String contextId = task.getContextId();
		String taskId = task.getTaskId();

		TaskHandler handler = getTaskHandler(sessionId, contextId);
		TaskServiceInterface.TaskPromise callback = createAnswerPromise(sessionId, taskId);

		if (handler != null) {
			handler.handle(task, callback);
		} else {
			callback.respond(TaskServiceInterface.TaskResolution.UNHANDLED, task);
		}
	}

	private TaskHandler getTaskHandler(String sessionId, String contextId) {
		Map<String, TaskHandler> handlerMap = handlers.get(sessionId);
		return handlerMap.get(contextId);
	}

	private TaskServiceInterface.TaskPromise createAnswerPromise(String sessionId, String taskId) {
		return (resolution, result) -> {
			TaskResult response = TaskResult.build(taskId, resolution, result);
			MainLoopPackage packedResponse = new MainLoopPackage(sessionId, response);
			outputBufferAccessor.put(packedResponse);
		};
	}
}
