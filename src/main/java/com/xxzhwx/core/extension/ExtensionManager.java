package com.xxzhwx.core.extension;

import java.util.ArrayList;
import java.util.List;

public final class ExtensionManager {
    private static final ExtensionManager I = new ExtensionManager();

    private List<Ext> extList = new ArrayList<>();

    private ExtensionManager() {}

    public static ExtensionManager getInstance() {
        return I;
    }

    public void register(Extension extension) {
        Class<?> cls = extension.getClass();
        ExtensionMeta ann = cls.getAnnotation(ExtensionMeta.class);
        if (ann == null) {
            throw new RuntimeException("Lack of annotation: " + cls.getSimpleName());
        }

        short extensionId = ann.id();
        if (this.contains(extensionId)) {
            throw new RuntimeException("Duplicated extension: " + extensionId);
        }

        extList.add(new Ext(extensionId, extension));
    }

    public boolean initialize() {
        extList.sort((o1, o2) -> o1.id - o2.id);
        for (Ext ext : extList) {
            Extension extension = ext.ext;
            if (!extension.initialize()) {
                System.out.println("Fail to initialize extension: " + extension.getName());
                return false;
            }
        }
        return true;
    }

    public void destroy() {
        for (int i = extList.size() - 1; i >= 0; --i) {
            Ext ext = extList.get(i);
            Extension extension = ext.ext;
            if (!extension.destroy()) {
                System.out.println("Fail to destroy extension: " + extension.getName());
            }
        }
    }

    private boolean contains(short extensionId) {
        for (Ext ext : extList) {
            if (ext.id == extensionId) {
                return true;
            }
        }
        return false;
    }

    private static class Ext {
        public final int id;
        public final Extension ext;

        public Ext(int id, Extension ext) {
            this.id = id;
            this.ext = ext;
        }
    }
}
