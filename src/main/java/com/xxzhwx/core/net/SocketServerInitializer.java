package com.xxzhwx.core.net;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

public class SocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast("FrameDecoder", new FrameDecoder())
                .addLast("FrameEncoder", new FrameEncoder())
                .addLast("CmdDecoder", new CmdDecoder())
                .addLast("CmdEncoder", new CmdEncoder())
                .addLast("handler", new SocketServerHandler());
    }
}
