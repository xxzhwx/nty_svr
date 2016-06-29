package com.xxzhwx.common;

public interface CmdCode {
    short REQ_CREATE_USER = 1001;
    short RSP_CREATE_USER = 1002;
    short REQ_LOGIN_USER = 1003;
    short RSP_LOGIN_USER = 1004;

    short REQ_TEST = (short) 0x7FFD;
    short RSP_TEST = (short) 0x7FFE;
}
