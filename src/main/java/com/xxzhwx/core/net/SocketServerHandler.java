package com.xxzhwx.core.net;

import com.xxzhwx.core.queue.CmdQueue;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.net.InetSocketAddress;

public class SocketServerHandler extends SimpleChannelInboundHandler<Cmd> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Cmd msg) throws Exception {
        if (msg == null) {
            System.out.println("Netty internal error ?");
            return;
        }

        CmdQueue.getInstance().put(msg);
        System.out.println("Received cmd: " + msg.getCmdCode());
    }

//    @Override
//    public void channelActive(ChannelHandlerContext ctx) throws Exception {
//        super.channelActive(ctx);
//    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);

        InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
        System.out.println("Disconnect channel: " + address.getHostName() + ":" + address.getPort());
    }

//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        super.exceptionCaught(ctx, cause);
//    }


//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        super.userEventTriggered(ctx, evt);
//    }
}
