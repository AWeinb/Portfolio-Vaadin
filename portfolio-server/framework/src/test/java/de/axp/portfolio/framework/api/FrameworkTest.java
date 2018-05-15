package de.axp.portfolio.framework.api;

import de.axp.portfolio.framework.api.interfaces.FrameworkEventInterface;
import de.axp.portfolio.framework.internal.service.event.EventService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FrameworkTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testAll() throws Exception {
		BaseFramework framework = BaseFramework.create();

		AuthenticatedFramework sessionFramework = framework.authenticate("123");
		sessionFramework.getFrameworkSessionInterface().initializeSession();
		FrameworkEventInterface frameworkEventInterface = sessionFramework.getFrameworkEventInterface();
		frameworkEventInterface.addEventConsumer(getCommandHandler());

		frameworkEventInterface.dispatchEvent("FutureCallback", "A",
				FrameworkPromise.whenRejected(f -> Assert.assertEquals("A", f)));

		frameworkEventInterface.dispatchEvent("FutureCallback", "B",
				FrameworkPromise.whenResolved(f -> Assert.assertEquals("B", f)));

		framework.dispose();
	}

	private EventService.EventConsumer getCommandHandler() {
		return event -> {
			if (event.getData().equals("A")) {
				event.getPromise().ifPresent(e -> e.reject(event.getData()));
			} else {
				event.getPromise().ifPresent(e -> e.resolve(event.getData()));
			}
		};
	}
}