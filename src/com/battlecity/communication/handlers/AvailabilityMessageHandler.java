package com.battlecity.communication.handlers;

import com.battlecity.communication.MessageTypes;
import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;

public class AvailabilityMessageHandler implements MessageHandler {

    @Override
    public byte[] getType() {
        return MessageTypes.TYPE_AVAILABILITY;
    }

    @Override
    public void processMessage(Message message) throws MissingParamException {

    }

}
