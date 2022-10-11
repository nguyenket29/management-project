package com.hau.ketnguyen.it.common.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static byte[] readFile(String path) throws IOException {
        InputStream inputStream = null;
        ByteArrayOutputStream outputStream = null;
        byte[] bytes = null;
        inputStream = new FileInputStream(path);
        outputStream = new ByteArrayOutputStream();
        bytes = new byte[512];
        int readByte = inputStream.read(bytes);
        while (readByte >= 0) {
            outputStream.write(bytes, 0, readByte);
            readByte = inputStream.read(bytes);
        }
        return outputStream.toByteArray();
    }

    public static String getImageExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        String rs = "";

        int i = fileName != null ? fileName.length() : 0;

        if (fileName == null || fileName.charAt(i - 1) == '/'
                || fileName.charAt(i - 1) == '\\'
                || fileName.charAt(i - 1) == '.')
            return rs;

        int j = fileName.lastIndexOf('.');
        int k = Math.max(fileName.lastIndexOf('/'), fileName.lastIndexOf('\\'));

        if (j < k) rs = "";
        else rs = fileName.substring(j + 1).toLowerCase();

        return rs;
    }
}
