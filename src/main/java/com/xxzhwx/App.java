package com.xxzhwx;

import com.xxzhwx.core.handler.HandlerManager;
import com.xxzhwx.core.handler.TestRequestHandler;
import com.xxzhwx.core.net.SocketServer;
import com.xxzhwx.core.queue.CmdQueue;
import com.xxzhwx.core.runs.CmdRunnable;
import com.xxzhwx.core.runs.PreloadRunnable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
    private final static int PORT = 9090;

    public static void main(String[] args) {
        HandlerManager.getInstance().register(new TestRequestHandler());

        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new PreloadRunnable());
        exec.execute(new CmdRunnable(CmdQueue.getInstance()));
        exec.execute(new SocketServer(PORT));
        System.out.println("Server started.");
    }
}
