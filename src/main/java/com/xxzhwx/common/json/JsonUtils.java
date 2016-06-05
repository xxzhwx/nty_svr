package com.xxzhwx.common.json;

import com.alibaba.fastjson.JSON;

import java.io.*;

public final class JsonUtils {

    public static <T> T load(String filename, Class<T> clazz) {
        File file = new File(filename);
        if (!file.isFile()) {
            throw new RuntimeException("File not exist: " + filename);
        }

        int length = (int) file.length();
        byte[] content = new byte[length];

        try {
            FileInputStream in = new FileInputStream(file);
            if (in.read(content) == -1) {
                in.close();
                return null;
            }

            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String text = new String(content, "utf-8");
            return JSON.parseObject(text, clazz);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
