package com.fairy.protobuf.common.util;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class FileUtils {

    public static byte[] createZipFile(Map<String, byte[]> files) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            for (Map.Entry<String, byte[]> entry : files.entrySet()) {
                ZipEntry zipEntry = new ZipEntry(entry.getKey());
                zipOutputStream.putNextEntry(zipEntry);
                zipOutputStream.write(entry.getValue());
                zipOutputStream.closeEntry();
            }
        }
        return outputStream.toByteArray();
    }

    public static byte[] readClassFile(Path filePath) throws IOException {
        return Files.readAllBytes(filePath);
    }

    public static Map<String, byte[]> getGeneratedClassFiles(String outputDir) throws IOException {
        Map<String, byte[]> classFiles = new HashMap<>();
        Path outPath = Paths.get(outputDir);
        Files.walk(outPath)
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        String relativePath = outPath.relativize(file).toString();
                        byte[] fileData = readClassFile(file);
                        classFiles.put(relativePath, fileData);
                    } catch (IOException e) {
                        log.error("异常：", e);
                    }
                });
        return classFiles;
    }

}
