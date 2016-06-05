package com.xxzhwx.core.extension;

import gnu.trove.iterator.TShortObjectIterator;
import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;

public final class ExtensionManager {
    private static final ExtensionManager I = new ExtensionManager();

    private final TShortObjectMap<Extension> extensionMap = new TShortObjectHashMap<>();

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
        if (extensionMap.containsKey(extensionId)) {
            throw new RuntimeException("Duplicated extension: " + extensionId);
        }

        extensionMap.put(extensionId, extension);
    }

    public boolean initialize() {
        TShortObjectIterator<Extension> it = extensionMap.iterator();
        while (it.hasNext()) {
            Extension extension = it.value();
            if (!extension.initialize()) {
                System.out.println("Fail to initialize extension: " + extension.getName());
                return false;
            }
        }
        return true;
    }
}
