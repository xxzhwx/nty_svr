package com.xxzhwx.core.net;

import com.xxzhwx.common.CmdCode;
import com.xxzhwx.core.handler.HandlerManager;
import com.xxzhwx.core.handler.TestResponseHandler;
import com.xxzhwx.core.queue.CmdQueue;
import com.xxzhwx.core.runs.CmdRunnable;
import com.xxzhwx.core.runs.PreloadRunnable;
import com.xxzhwx.protobuf.request.TestRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.ConnectException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class SocketClient implements Runnable {
    private String hostname;
    private int port;
    private Channel channel;

    public SocketClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    public abstract void onConnectSuccess();
    public abstract void onConnectFailed();

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    //.option(ChannelOption.SO_TIMEOUT, 5000)
                    .handler(new SocketServerInitializer());

            ChannelFuture f = b.connect(hostname, port).sync();
            if (f.isSuccess()) {
                channel = f.channel();
                onConnectSuccess();
                // Wait until the channel is closed.
                channel.closeFuture().sync();
            }
        } catch (Exception e) {
            if (e instanceof ConnectException) {
                onConnectFailed();
            }
        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendCmd(short cmdCode, Object data) {
        channel.writeAndFlush(new Cmd(cmdCode, data)); // thread-safe
    }

    public static void main(String[] args) {
        HandlerManager.getInstance().register(new TestResponseHandler());

        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new PreloadRunnable());
        exec.execute(new CmdRunnable(CmdQueue.getInstance()));
        exec.execute(new SocketClient("127.0.0.1", 9090) {
            @Override
            public void onConnectSuccess() {
                System.out.println("Connect success.");
                sendCmd(CmdCode.REQ_TEST, new TestRequest(37));
            }

            @Override
            public void onConnectFailed() {
                System.out.println("Connect failed.");
            }
        });
    }
}
