//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.linzn.jSocket.core;

import java.util.UUID;

public interface ConnectionListener {
    void onConnectEvent(UUID var1);

    void onDisconnectEvent(UUID var1);
}
