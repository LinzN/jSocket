//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.linzn.jSocket.server;

import de.linzn.jSocket.core.ChannelDataEventPacket;
import de.linzn.jSocket.core.ConnectionListener;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;
import java.util.UUID;

public class JServerConnection implements Runnable {
    private Socket socket;
    private JServer jServer;
    private UUID uuid;

    public JServerConnection(Socket socket, JServer jServer) {
        this.socket = socket;
        this.jServer = jServer;
        this.uuid = UUID.randomUUID();
        if (jServer.threadTaskler.isDebugging()) {
            System.out.println("[" + Thread.currentThread().getName() + "] Create JServerConnection");
        }

    }

    public synchronized void setEnable() {
        this.jServer.threadTaskler.runSingleThreadExecutor(this);
    }

    public synchronized void setDisable() {
        this.closeConnection();
    }

    public void run() {
        this.onConnect();

        try {
            while (!this.jServer.server.isClosed() && this.isValidConnection()) {
                this.readInput();
            }
        } catch (IOException var2) {
            this.closeConnection();
        }

    }

    public boolean isValidConnection() {
        return this.socket.isConnected() && !this.socket.isClosed();
    }

    public boolean readInput() throws IOException {
        BufferedInputStream bInStream = new BufferedInputStream(this.socket.getInputStream());
        DataInputStream dataInput = new DataInputStream(bInStream);
        String headerChannel = dataInput.readUTF();
        int dataSize = dataInput.readInt();
        byte[] fullData = new byte[dataSize];

        for (int i = 0; i < dataSize; ++i) {
            fullData[i] = dataInput.readByte();
        }

        if (headerChannel != null && !headerChannel.isEmpty()) {
            if (this.jServer.threadTaskler.isDebugging()) {
                System.out.println("[" + Thread.currentThread().getName() + "] Data amount: " + fullData.length);
            }

            this.onDataInput(headerChannel, fullData);
            return true;
        } else {
            if (this.jServer.threadTaskler.isDebugging()) {
                System.out.println("[" + Thread.currentThread().getName() + "] No channel in header");
            }

            return false;
        }
    }

    public synchronized void writeOutput(String headerChannel, byte[] bytes) {
        if (this.isValidConnection()) {
            try {
                byte[] fullData = bytes;
                int dataSize = bytes.length;
                BufferedOutputStream bOutSream = new BufferedOutputStream(this.socket.getOutputStream());
                DataOutputStream dataOut = new DataOutputStream(bOutSream);
                dataOut.writeUTF(headerChannel);
                dataOut.writeInt(dataSize);

                for (int i = 0; i < dataSize; ++i) {
                    dataOut.writeByte(fullData[i]);
                }

                bOutSream.flush();
            } catch (IOException var8) {
                IOException e = var8;
                e.printStackTrace();
            }
        } else if (this.jServer.threadTaskler.isDebugging()) {
            System.out.println("[" + Thread.currentThread().getName() + "] The JConnection is closed. No output possible!");
        }

    }

    public synchronized void closeConnection() {
        if (!this.socket.isClosed()) {
            try {
                this.socket.close();
            } catch (IOException var2) {
            }

            this.onDisconnect();
            this.jServer.jServerConnections.remove(this.uuid);
        }

    }

    private void onConnect() {
        if (this.jServer.threadTaskler.isDebugging()) {
            System.out.println("[" + Thread.currentThread().getName() + "] Connected to Socket");
        }

        this.jServer.threadTaskler.runSingleThreadExecutor(() -> {
            Iterator var1 = this.jServer.connectionListeners.iterator();

            while (var1.hasNext()) {
                ConnectionListener socketConnectionListener = (ConnectionListener) var1.next();
                socketConnectionListener.onConnectEvent(this.uuid);
            }

        });
    }

    private void onDisconnect() {
        if (this.jServer.threadTaskler.isDebugging()) {
            System.out.println("[" + Thread.currentThread().getName() + "] Disconnected from Socket");
        }

        this.jServer.threadTaskler.runSingleThreadExecutor(() -> {
            Iterator var1 = this.jServer.connectionListeners.iterator();

            while (var1.hasNext()) {
                ConnectionListener socketConnectionListener = (ConnectionListener) var1.next();
                socketConnectionListener.onDisconnectEvent(this.uuid);
            }

        });
    }

    private void onDataInput(String channel, byte[] bytes) {
        if (this.jServer.threadTaskler.isDebugging()) {
            System.out.println("[" + Thread.currentThread().getName() + "] IncomingData from Socket");
        }

        this.jServer.threadTaskler.runSingleThreadExecutor(() -> {
            Iterator var3 = this.jServer.dataInputListener.iterator();

            while (var3.hasNext()) {
                ChannelDataEventPacket dataInputListenerObject = (ChannelDataEventPacket) var3.next();
                if (dataInputListenerObject.channel.equalsIgnoreCase(channel)) {
                    dataInputListenerObject.incomingDataListener.onEvent(channel, this.uuid, bytes);
                }
            }

        });
    }

    public UUID getUUID() {
        return this.uuid;
    }
}
