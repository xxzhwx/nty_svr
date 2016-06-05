package com.xxzhwx.core.net;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class CmdEncoder extends MessageToMessageEncoder<Cmd> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Cmd msg, List<Object> out) throws Exception {
        short cmdCode = msg.getCmdCode();
        byte[] data = ProtobufCodec.encode(msg.getData());
        out.add(new Frame(cmdCode, data));
    }
}
