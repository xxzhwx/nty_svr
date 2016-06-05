package com.xxzhwx.core.extension;

public interface Extension {
    default String getName() {
        return getClass().getSimpleName();
    }

    boolean initialize();
}
