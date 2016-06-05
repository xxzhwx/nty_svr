package com.xxzhwx.core.handler;

import com.xxzhwx.common.CmdCode;
import com.xxzhwx.core.net.Cmd;
import com.xxzhwx.protobuf.request.TestRequest;
import com.xxzhwx.protobuf.response.TestResponse;
import io.netty.channel.Channel;

@HandleCmd(cmdCodes = {CmdCode.CMD_TEST}, reqType = TestRequest.class)
public class TestHandler implements CmdHandler<TestRequest> {
    public void handle(Channel ch, TestRequest msg) {
        System.out.println("<<TestHandler>> id: " + msg.getId());

        TestResponse response = new TestResponse(msg.getId(), "Welcome to nty_svr.");
        Cmd cmd = new Cmd(CmdCode.CMD_TEST, response);
        ch.writeAndFlush(cmd);
    }
}
