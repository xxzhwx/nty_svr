package com.xxzhwx.core.handler;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;

public final class HandlerManager {
    private static final HandlerManager I = new HandlerManager();

    private TShortObjectMap<HandlerHolder> handlerMap = new TShortObjectHashMap<>();

    private HandlerManager() {}

    public static HandlerManager getInstance() {
        return I;
    }

    public void register(CmdHandler handler) {
        Class<?> cls = handler.getClass();
        HandleCmd hw = cls.getAnnotation(HandleCmd.class);
        if (hw == null) {
            throw new RuntimeException("Lack of annotation: " + cls.getSimpleName());
        }

        Class<?> reqType = hw.reqType();
        if (reqType == null) {
            throw new RuntimeException("Lack of reqType: " + cls.getSimpleName());
        }

        HandlerHolder holder = new HandlerHolder(reqType, handler);

        short[] cmdCodes = hw.cmdCodes();
        for (short cmdCode : cmdCodes) {
            if (handlerMap.containsKey(cmdCode)) {
                throw new RuntimeException("Duplicated register for cmd: " + cmdCode);
            }

            handlerMap.put(cmdCode, holder);
        }
    }

    public Class<?> getReqType(short cmdCode) {
        HandlerHolder holder = handlerMap.get(cmdCode);
        return holder == null ? null : holder.reqType;
    }

    public CmdHandler getHandler(short cmdCode) {
        HandlerHolder holder = handlerMap.get(cmdCode);
        return holder == null ? null : holder.handler;
    }

    private static class HandlerHolder {
        private Class<?> reqType;
        private CmdHandler handler;

        public HandlerHolder(Class<?> reqType, CmdHandler handler) {
            this.reqType = reqType;
            this.handler = handler;
        }
    }
}
