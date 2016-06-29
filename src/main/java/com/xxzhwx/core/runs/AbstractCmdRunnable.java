package com.xxzhwx.core.runs;

import com.xxzhwx.core.net.Cmd;
import com.xxzhwx.core.queue.BaseQueue;

import java.util.concurrent.TimeUnit;

public abstract class AbstractCmdRunnable implements Runnable {
    private BaseQueue<Runnable> taskQueue;
    private BaseQueue<Cmd> cmdQueue;

    public AbstractCmdRunnable(BaseQueue<Cmd> cmdQueue) {
        this.taskQueue = new BaseQueue<>();
        this.cmdQueue = cmdQueue;
    }

    public void submitTask(Runnable task) {
        taskQueue.put(task);
    }

    public void run() {
        for (;;) {
            Runnable task;
            while (taskQueue.size() > 0) {
                task = taskQueue.poll();
                if (task != null) {
                    task.run();
                }
            }

            Cmd cmd;
            while (cmdQueue.size() > 0) {
                cmd = cmdQueue.poll();
                if (cmd != null) {
                    handleCmd(cmd);
                }
            }

            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract  void handleCmd(Cmd cmd);
}
