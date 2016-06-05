package com.xxzhwx.core.runs;

import com.xxzhwx.core.handler.CmdHandler;
import com.xxzhwx.core.net.Cmd;
import com.xxzhwx.core.queue.BaseQueue;

public class CmdRunnable extends AbstractCmdRunnable {
    public CmdRunnable(BaseQueue<Cmd> cmdQueue) {
        super(cmdQueue);
    }

    @Override
    public void handleCmd(Cmd cmd) {
        CmdHandler handler = cmd.getHandler();
        handler.handle(cmd.getChannel(), cmd.getData());
        System.out.println("Handled cmd: " + cmd.getCmdCode());
    }
}
