package com.battlecity.communication.handlers;

import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.messages.Message;

public interface MessageHandler {

    byte[] getType();

    void processMessage(Message message) throws MissingParamException;

}
