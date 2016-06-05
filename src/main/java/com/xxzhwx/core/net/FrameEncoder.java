package com.xxzhwx.core.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class FrameEncoder extends MessageToByteEncoder<Frame> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Frame msg, ByteBuf out) throws Exception {
        byte[] data = msg.getData();
        int dataLength = data == null ? 0 : data.length;

        out.ensureWritable(4 + dataLength);
        out.writeShort(dataLength);
        out.writeShort(msg.getCmdCode());
        if (dataLength > 0) {
            out.writeBytes(data);
        }
    }
}
