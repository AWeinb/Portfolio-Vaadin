package de.axp.portfolio.framework.internal.commands;

import de.axp.portfolio.framework.FrameworkMessage;
import de.axp.portfolio.framework.FrameworkSessionInterface;
import de.axp.portfolio.framework.internal.MessageHandlerInterface;
import de.axp.portfolio.framework.internal.commands.CommandBuffer.CommandPacket;

class CommandHandlerNotifier {

	private final MessageHandlerInterface messageHandlerInterface;
	private final ResponseBuffer responseBuffer;

	CommandHandlerNotifier(MessageHandlerInterface messageHandlerInterface, ResponseBuffer responseBuffer) {
		this.messageHandlerInterface = messageHandlerInterface;
		this.responseBuffer = responseBuffer;
	}

	void notify(CommandPacket commandPacket) {
		messageHandlerInterface.handleMessage(commandPacket.getFrameworkSession(), commandPacket.getCommand(),
				new MessageHandlerInterface.ResponsePromise() {
					private FrameworkMessage.Message responseMessage;

					@Override
					public void setFuture(FrameworkMessage.Message responseMessage) {
						this.responseMessage = responseMessage;
					}

					@Override
					public void resolve() {
						try {
							responseBuffer.putResponse(new ResponseBuffer.ResponsePacket() {
								@Override
								public FrameworkSessionInterface.FrameworkSession getFrameworkSession() {
									return commandPacket.getFrameworkSession();
								}

								@Override
								public FrameworkMessage.Message getCommand() {
									return commandPacket.getCommand();
								}

								@Override
								public FrameworkMessage.Message getResponse() {
									return responseMessage;
								}

								@Override
								public boolean wasRejected() {
									return false;
								}
							});
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void reject() {
						try {
							responseBuffer.putResponse(new ResponseBuffer.ResponsePacket() {
								@Override
								public FrameworkSessionInterface.FrameworkSession getFrameworkSession() {
									return commandPacket.getFrameworkSession();
								}

								@Override
								public FrameworkMessage.Message getCommand() {
									return commandPacket.getCommand();
								}

								@Override
								public FrameworkMessage.Message getResponse() {
									return responseMessage;
								}

								@Override
								public boolean wasRejected() {
									return true;
								}
							});
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				});
	}
}
