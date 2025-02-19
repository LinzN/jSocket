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

public interface ThreadTaskler {
    void runThreadPoolExecutor(Runnable runnable);

    void runThreadExecutor(Thread thread);

    void runSingleThreadExecutor(Runnable runnable);

    boolean isDebugging();
}
