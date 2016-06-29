package com.xxzhwx.core.handler;

import com.xxzhwx.common.CmdCode;
import com.xxzhwx.core.net.Cmd;
import com.xxzhwx.protobuf.request.TestRequest;
import com.xxzhwx.protobuf.response.TestResponse;
import io.netty.channel.Channel;

@HandleCmd(cmdCodes = {CmdCode.RSP_TEST}, reqType = TestResponse.class)
public class TestResponseHandler implements CmdHandler<TestResponse> {
    @Override
    public void handle(Channel ch, TestResponse msg) {
        System.out.println("<<TestHandler>> id: " + msg.getId() + ", msg: " + msg.getWelcomeMsg());
        sendTestReq(ch);
    }

    private void sendTestReq(Channel ch) {
        TestRequest req = new TestRequest(37);

        Cmd cmd = new Cmd(CmdCode.REQ_TEST, req);
        ch.writeAndFlush(cmd);
    }
}
