package com.xxzhwx.core.handler;

import io.netty.channel.Channel;

public interface CmdHandler<T> {
    void handle(Channel ch, T msg);
}
