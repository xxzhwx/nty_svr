package com.xxzhwx.core.net;

public class Frame {
    private short cmdCode;
    private byte[] data;

    public Frame(short cmdCode, byte[] data) {
        this.cmdCode = cmdCode;
        this.data = data;
    }

    public short getCmdCode() {
        return cmdCode;
    }

    public byte[] getData() {
        return data;
    }
}
