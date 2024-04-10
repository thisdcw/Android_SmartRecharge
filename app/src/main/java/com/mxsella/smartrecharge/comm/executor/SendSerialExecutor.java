package com.mxsella.smartrecharge.comm.executor;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendSerialExecutor implements Executor {
    final Queue<Runnable> tasks = new ArrayDeque<>();
    final ExecutorService executors;
    Runnable active;

    public SendSerialExecutor(ExecutorService executor) {
        this.executors = executor;
    }

    public synchronized void execute(final Runnable r) {
        tasks.offer(() -> {
            try {
                r.run();
            } finally {
                scheduleNext();
            }
        });
        if (active == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            executors.execute(active);
        }
    }
}
