package com.battlecity.communication;

import com.battlecity.communication.exceptions.MissingParamException;
import com.battlecity.communication.handlers.MessageHandler;
import com.battlecity.communication.messages.Message;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MessageRouter {

    private static final Logger LOG = Logger.getLogger(MessageRouter.class.getName());
    private final ConcurrentHashMap<byte[], MessageHandler> handlerMap = new ConcurrentHashMap();

    public void processMessage(Message message) {
        byte[] messageType = message.getMessageType();

        MessageHandler handler = handlerMap.get(messageType);
        if (handler == null) {
            LOG.log(Level.SEVERE, "Handler not found for message " + message);
            return;
        }
        try {
            handler.processMessage(message);
        } catch (MissingParamException e) {
            LOG.log(Level.SEVERE, "Missing param for message " + message, e);
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Unexpected error for message " + message, e);
        }
    }

    public void addMessageHandler(MessageHandler messageHandler){
        handlerMap.put(messageHandler.getType(), messageHandler);
    }

}
