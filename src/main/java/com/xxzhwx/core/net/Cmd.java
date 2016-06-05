package com.xxzhwx.core.net;

import com.xxzhwx.core.handler.CmdHandler;
import io.netty.channel.Channel;

public class Cmd {
    private short cmdCode;
    private Object data;
    private CmdHandler handler;
    private Channel channel;

    public Cmd(short cmdCode, Object data) {
        this.cmdCode = cmdCode;
        this.data = data;
    }

    public short getCmdCode() {
        return cmdCode;
    }

    public Object getData() {
        return data;
    }

    public CmdHandler getHandler() {
        return handler;
    }

    void setHandler(CmdHandler handler) {
        this.handler = handler;
    }

    public Channel getChannel() {
        return channel;
    }

    void setChannel(Channel channel) {
        this.channel = channel;
    }
}
