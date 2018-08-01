package de.axp.framework.internal.service.task;

import de.axp.framework.api.MainThreadSynchronization;
import de.axp.framework.api.serviceinterfaces.TaskServiceInterface;
import de.axp.framework.api.serviceinterfaces.TaskServiceInterface.Task;
import de.axp.framework.api.serviceinterfaces.TaskServiceInterface.TaskHandler;
import de.axp.framework.internal.mainloop.MainLoop;
import de.axp.framework.internal.mainloop.MainLoopPackage;

class TaskServiceImpl implements MainLoop.MainLoopPlugin, TaskService {

	private MainLoop.MainLoopAccessor inputBufferAccessor;
	private TaskHandlerNotifier handlerNotifier;
	private TaskPromiseNotifier promiseNotifier;

	@Override
	public void initialize(MainLoop.MainLoopAccessor inputBufferAccessor,
	                       MainLoop.MainLoopAccessor outputBufferAccessor) {
		this.inputBufferAccessor = inputBufferAccessor;

		handlerNotifier = new TaskHandlerNotifier(outputBufferAccessor);
		promiseNotifier = new TaskPromiseNotifier();
	}

	@Override
	public void dispose() {

	}

	@Override
	public MainLoop.MainLoopListener getInputListener() {
		return handlerNotifier;
	}

	@Override
	public MainLoop.MainLoopListener getOutputListener() {
		return promiseNotifier;
	}

	@Override
	public void setMainThreadSynchronization(MainThreadSynchronization synchronization) {
		promiseNotifier.setMainThreadSynchronization(synchronization);
	}

	@Override
	public void register(String sessionId, String contextId, TaskHandler handler) {
		handlerNotifier.addHandler(sessionId, contextId, handler);
	}

	@Override
	public void trigger(String sessionId, Task task, TaskServiceInterface.TaskPromise promise) {
		promiseNotifier.registerPromise(sessionId, task.getTaskId(), promise);
		inputBufferAccessor.put(new MainLoopPackage(sessionId, task));
	}
}
