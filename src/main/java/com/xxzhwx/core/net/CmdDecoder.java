package com.xxzhwx.core.net;

import com.xxzhwx.core.handler.CmdHandler;
import com.xxzhwx.core.handler.HandlerManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

public class CmdDecoder extends MessageToMessageDecoder<Frame> {
    @Override
    protected void decode(ChannelHandlerContext ctx, Frame msg, List<Object> out) throws Exception {
        short cmdCode = msg.getCmdCode();

        HandlerManager hMgr = HandlerManager.getInstance();
        Class<?> reqType = hMgr.getReqType(cmdCode);
        Object obj = ProtobufCodec.decode(msg.getData(), reqType);

        CmdHandler handler = hMgr.getHandler(cmdCode);
        Cmd cmd = new Cmd(cmdCode, obj);
        cmd.setHandler(handler);
        cmd.setChannel(ctx.channel());
        out.add(cmd);
    }
}
