package com.fairy.protobuf.service.impl;

import com.fairy.protobuf.common.ProtoCodeGenerator;
import com.fairy.protobuf.service.ProtoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j
@Service
public class ProtoServiceImpl implements ProtoService {

    @Autowired
    private ProtoCodeGenerator protoCodeGenerator;

    private Lock lock = new ReentrantLock();

    public byte[] generateProtoClasses() {
        boolean locked = false;
        try {
            locked = lock.tryLock(60, TimeUnit.SECONDS);
            if (locked) {
                return protoCodeGenerator.generate();
            }
            log.info("获取锁失败");
            return "有任务正在执行，请稍后尝试！".getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("异常：", e);
            throw new RuntimeException(e);
        } finally {
            if (locked) {
                lock.unlock();
            }
        }
    }

    public byte[] uploadProtoFile(MultipartFile file) {
        boolean locked = false;
        File tempFile = null;
        try {
            log.info("上传文件名：{}", file.getOriginalFilename());
            locked = lock.tryLock(60, TimeUnit.SECONDS);
            if (locked) {
                // 将上传的.proto文件保存到临时目录
                tempFile = new File(System.getProperty("user.dir") + "/src/main/resources/proto/", file.getOriginalFilename());
                file.transferTo(tempFile);
                return protoCodeGenerator.generate();
            }
            log.info("获取锁失败");
            return "有任务正在执行，请稍后尝试！".getBytes(StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("异常：", e);
            return "异常，请检查文件是否正确".getBytes(StandardCharsets.UTF_8);
        } finally {
            if (locked) {
                lock.unlock();
            }
            if(Objects.nonNull(tempFile)){
                tempFile.delete();
            }
        }
    }

}
