package com.fairy.protobuf.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.shared.invoker.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import static com.fairy.protobuf.common.util.FileUtils.createZipFile;
import static com.fairy.protobuf.common.util.FileUtils.getGeneratedClassFiles;

@Slf4j
@Component
public class ProtoCodeGenerator {

    public byte[] generate() {
        return runMavenPlugin();
    }

    private void executeMavenPlugin() {
        // 用实际的 Maven 可执行文件路径替换此处 /path/to/maven/bin/mvn
        String mavenExecutable = "/path/to/maven/bin/mvn";
        InvocationRequest request = new DefaultInvocationRequest();
        request.setBatchMode(true);
        request.setGoals(Collections.singletonList("protobuf:compile"));
        try {
            new DefaultInvoker().setMavenExecutable(new File(mavenExecutable)).execute(request);
        } catch (Exception e) {
            log.error("异常：", e);
        }
    }

    private static byte[] runMavenPlugin() {
        try {
            String mavenExecutable = "/path/to/maven/bin/mvn";
            InvocationRequest request = new DefaultInvocationRequest();
            request.setBatchMode(true);
            request.setGoals(Collections.singletonList("protobuf:compile"));
            Invoker invoker = new DefaultInvoker().setMavenExecutable(new File(mavenExecutable));
            invoker.execute(request);
            String outputDir = System.getProperty("user.dir") + "/target/generated-sources/protobuf/java/";
            Map<String, byte[]> classFiles = getGeneratedClassFiles(outputDir);
            byte[] zipData = createZipFile(classFiles);
            return zipData;
        } catch (Exception e) {
            log.error("异常：", e);
            throw new RuntimeException(e);
        }
    }

}
