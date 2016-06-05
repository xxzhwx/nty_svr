package com.xxzhwx.core.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

public class FrameDecoder extends LengthFieldBasedFrameDecoder {
    public FrameDecoder() {
        /**
         * Before decode (16 bytes)             After decode (14 bytes)
         * +------+--------+--------------+     +--------+--------------+
         * |Length|Cmd Code|Actual Content|---->|Cmd Code|Actual Content|
         * |0x000C| 0xCAFE |"HELLO, WORLD"|     | 0xCAFE |"HELLO, WORLD"|
         * +------+--------+--------------+     +--------+--------------+
         */
        this(ByteOrder.BIG_ENDIAN, 65535, 0, 2, 2, 2, true);
    }

    public FrameDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(byteOrder, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, failFast);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = (ByteBuf) super.decode(ctx, in);
        if (frame == null) {
            return null;
        }

        short cmdCode = frame.readShort();
        byte[] data = new byte[frame.readableBytes()];
        frame.readBytes(data);

        return new Frame(cmdCode, data);
    }
}
