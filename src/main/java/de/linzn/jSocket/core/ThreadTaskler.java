//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package de.linzn.jSocket.core;

public interface ThreadTaskler {
    void runThreadPoolExecutor(Runnable var1);

    void runThreadExecutor(Thread var1);

    void runSingleThreadExecutor(Runnable var1);

    boolean isDebugging();
}
