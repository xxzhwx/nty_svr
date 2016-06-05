package com.xxzhwx.protobuf.response;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class TestResponse {
    @Protobuf(required = true, order = 1, fieldType = FieldType.INT32)
    private int id;
    @Protobuf(required = true, order = 2, fieldType = FieldType.STRING)
    private String welcomeMsg;

    public TestResponse() {}

    public TestResponse(int id, String welcomeMsg) {
        this.id = id;
        this.welcomeMsg = welcomeMsg;
    }

    public int getId() {
        return id;
    }

    public String getWelcomeMsg() {
        return welcomeMsg;
    }
}
