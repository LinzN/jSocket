/*
 * Copyright (C) 2025. Niklas Linz - All Rights Reserved
 * You may use, distribute and modify this code under the
 * terms of the LGPLv3 license, which unfortunately won't be
 * written for another century.
 *
 * You should have received a copy of the LGPLv3 license with
 * this file. If not, please write to: niklas.linz@enigmar.de
 *
 */

package de.linzn.jSocket.core;

public class ChannelDataEventPacket {
    public IncomingDataListener incomingDataListener;
    public String channel;

    public ChannelDataEventPacket(String channel, IncomingDataListener incomingDataListener) {
        this.channel = channel;
        this.incomingDataListener = incomingDataListener;
    }
}
