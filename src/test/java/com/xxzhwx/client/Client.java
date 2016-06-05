package com.xxzhwx.client;

import com.xxzhwx.common.CmdCode;
import com.xxzhwx.core.handler.HandlerManager;
import com.xxzhwx.core.net.*;
import com.xxzhwx.protobuf.request.TestRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client {
    private final static String HOST = "127.0.0.1";
    private final static int PORT = 9090;
    private Channel channel;

    public void run() {
        HandlerManager.getInstance().register(new TestHandler());

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("FrameDecoder", new FrameDecoder())
                                    .addLast("FrameEncoder", new FrameEncoder())
                                    .addLast("CmdDecoder", new CmdDecoder())
                                    .addLast("CmdEncoder", new CmdEncoder())
                                    .addLast("handler", new ClientHandler());
                        }
                    });

            ChannelFuture f = b.connect(HOST, PORT).sync();
            channel = f.channel();
            if (f.isDone()) {
                System.out.println("Connect success.");

                sendTestReq();

                // Wait until the channel is closed.
                channel.closeFuture().sync();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private void sendTestReq() {
        TestRequest req = new TestRequest(37);

        Cmd cmd = new Cmd(CmdCode.CMD_TEST, req);
        channel.writeAndFlush(cmd);
    }

    public static void main(String[] args) {
        new Client().run();
    }
}

class ClientHandler extends SimpleChannelInboundHandler<Cmd> {
    public ClientHandler() {
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
        System.out.println("Received cmd: " + msg.getCmdCode());
        msg.getHandler().handle(ctx.channel(), msg.getData());
    }
}
