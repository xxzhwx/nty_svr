package com.xxzhwx.core.queue;

import com.xxzhwx.core.net.Cmd;

public final class CmdQueue extends BaseQueue<Cmd> {
    private static final CmdQueue I = new CmdQueue();

    private CmdQueue() {}

    public static CmdQueue getInstance() {
        return I;
    }
}
