//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.linzn.jSocket.server;

import de.linzn.jSocket.core.ChannelDataEventPacket;
import de.linzn.jSocket.core.ConnectionListener;
import de.linzn.jSocket.core.IncomingDataListener;
import de.linzn.jSocket.core.ThreadTaskler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;

public class JServer implements Runnable {
    ServerSocket server;
    ArrayList<ChannelDataEventPacket> dataInputListener;
    ArrayList<ConnectionListener> connectionListeners;
    HashMap<UUID, JServerConnection> jServerConnections;
    ThreadTaskler threadTaskler;
    private String host;
    private int port;

    public JServer(String host, int port, ThreadTaskler threadTaskler) {
        this.host = host;
        this.port = port;
        this.threadTaskler = threadTaskler;
        this.jServerConnections = new HashMap();
        this.dataInputListener = new ArrayList();
        this.connectionListeners = new ArrayList();
        if (threadTaskler.isDebugging()) {
            System.out.println("[" + Thread.currentThread().getName() + "] Create JServer");
        }

    }

    public void openServer() {
        try {
            this.server = new ServerSocket();
            this.server.bind(new InetSocketAddress(this.host, this.port));
            this.threadTaskler.runSingleThreadExecutor(this);
        } catch (IOException var2) {
            IOException e = var2;
            e.printStackTrace();
        }

    }

    public void closeServer() {
        try {
            this.server.close();
            ArrayList<UUID> uuidList = new ArrayList(this.jServerConnections.keySet());
            Iterator var2 = uuidList.iterator();

            while (var2.hasNext()) {
                UUID uuid = (UUID) var2.next();
                ((JServerConnection) this.jServerConnections.get(uuid)).setDisable();
            }

            this.jServerConnections.clear();
        } catch (IOException var4) {
            IOException e = var4;
            e.printStackTrace();
        }

    }

    public void run() {
        Thread.currentThread().setName("jServer");

        do {
            try {
                Socket socket = this.server.accept();
                socket.setTcpNoDelay(true);
                JServerConnection jServerConnection = new JServerConnection(socket, this);
                jServerConnection.setEnable();
                this.jServerConnections.put(jServerConnection.getUUID(), jServerConnection);
            } catch (IOException var3) {
                if (this.threadTaskler.isDebugging()) {
                    System.out.println("[" + Thread.currentThread().getName() + "] Connection already closed!");
                }
            }
        } while (!this.server.isClosed());

    }

    public void registerIncomingDataListener(String channel, IncomingDataListener dataInputListener) {
        this.dataInputListener.add(new ChannelDataEventPacket(channel, dataInputListener));
    }

    public void registerConnectionListener(ConnectionListener connectionListener) {
        this.connectionListeners.add(connectionListener);
    }

    public JServerConnection getClient(UUID uuid) {
        return (JServerConnection) this.jServerConnections.get(uuid);
    }

    public HashMap<UUID, JServerConnection> getClients() {
        return this.jServerConnections;
    }
}
