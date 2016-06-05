package com.xxzhwx.core.net;

import com.baidu.bjf.remoting.protobuf.Codec;
import com.baidu.bjf.remoting.protobuf.ProtobufProxy;

import java.io.IOException;

public final class ProtobufCodec {
    @SuppressWarnings("unchecked")
    public static byte[] encode(Object object) throws IOException {
        Codec codec = ProtobufProxy.create(object.getClass());
        return codec.encode(object);
    }

    public static Object decode(byte[] bytes, Class<?> cls) throws IOException {
        Codec codec = ProtobufProxy.create(cls);
        return codec.decode(bytes);
    }
}
