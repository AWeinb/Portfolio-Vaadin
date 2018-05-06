package de.axp.portfolio.framework.internal.mainloop;

public interface MainLoop {

	void dispose();

	void addPlugin(MainLoopPlugin plugin);

	interface MainLoopListener {

		void notify(MainLoopPackage aPackage);
	}

	interface MainLoopAccessor {

		void put(MainLoopPackage aPackage);
	}

	interface MainLoopPlugin {

		void initialize(MainLoopAccessor input, MainLoopAccessor output);

		void dispose();

		MainLoopListener getInputListener();

		MainLoopListener getOutputListener();
	}
}
