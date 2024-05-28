package com.mxsella.smartrecharge.utils;

import java.util.Timer;
import java.util.TimerTask;

public class TimerUtils {
    private Timer timer;

    public TimerUtils() {
        timer = new Timer();
    }

    public void scheduleTask(long delayMillis, Runnable task) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                task.run();
            }
        };
        timer.schedule(timerTask, delayMillis);
    }

    public void cancel() {
        timer.cancel();
    }
}
