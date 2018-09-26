package com.battlecity.server.model;

import com.battlecity.utils.IDGeneratorUtil;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnection implements Comparable {

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private long id;

    public ClientConnection(Socket socket) throws IOException {
        this.id = IDGeneratorUtil.generate();
        this.socket = socket;
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    public long getId() {
        return id;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!obj.getClass().equals(this.getClass())) return false;
        ClientConnection that = (ClientConnection) obj;
        return that.id == id;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        ClientConnection clientConnection = (ClientConnection) o;
        return Long.compare(id, clientConnection.id);
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

}
