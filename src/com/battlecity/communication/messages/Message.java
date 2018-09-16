package com.battlecity.communication.messages;

import com.battlecity.communication.exceptions.MissingParamException;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {

    private byte[] messageType;

    private Map<byte[], byte[]> paramsMap;

    public Message(byte[] messageType) {
        this.messageType = messageType;
        paramsMap = new HashMap<>();
    }

    public byte[] getMessageType() {
        return messageType;
    }

    public void setProperty(byte[] key, byte[] value) {
        if (key != null && value != null) {
            paramsMap.put(key, value);
        }
    }

    public byte[] getProperty(byte[] key) {
        return getParam(key);
    }

    public byte[] getRequiredProperty(byte[] key) throws MissingParamException {
        byte[] value = getParam(key);
        if (value == null) {
            throw new MissingParamException("Missing param for key " + new String(key, Charset.defaultCharset()));
        }
        return value;
    }

    private byte[] getParam(byte[] key) {
        if (key == null) {
            throw new NullPointerException("Key can't be null");
        }
        return paramsMap.get(key);
    }
}
