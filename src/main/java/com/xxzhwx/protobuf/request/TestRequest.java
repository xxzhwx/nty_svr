package com.xxzhwx.protobuf.request;

import com.baidu.bjf.remoting.protobuf.FieldType;
import com.baidu.bjf.remoting.protobuf.annotation.Protobuf;

public class TestRequest {
    @Protobuf(required = true, order = 1, fieldType = FieldType.INT32)
    private int id;

    public TestRequest() {}

    public TestRequest(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
