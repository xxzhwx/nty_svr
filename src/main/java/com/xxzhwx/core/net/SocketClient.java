package com.xxzhwx.core.net;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class SocketClient implements Runnable {
    private String hostname;
    private int port;
    private Channel channel;

    public SocketClient(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
    }

    @Override
    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new SocketServerInitializer());

            ChannelFuture f = b.connect(hostname, port).sync();
            channel = f.channel();
            if (f.isDone()) {
                System.out.println("Connect success.");

                /** Todo
                 * Commit a connected task to the main loop, so the application can handle connect success.
                 */

                // Wait until the channel is closed.
                channel.closeFuture().sync();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();

            // Reconnect
            //connect();
        }
    }

    public void sendCmd(short cmdCode, Object data) {
        channel.writeAndFlush(new Cmd(cmdCode, data)); // thread-safe
    }
}
