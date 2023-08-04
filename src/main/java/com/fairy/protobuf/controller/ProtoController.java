package com.fairy.protobuf.controller;

import com.fairy.protobuf.service.ProtoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping
public class ProtoController {

    @Autowired
    private ProtoService protoService;

    @GetMapping("/generateProtoClasses")
    public byte[] generateProtoClasses() throws InterruptedException {
        return protoService.generateProtoClasses();
    }

    @PostMapping("/uploadProto")
    public byte[] uploadProtoFile(@RequestParam("file") MultipartFile file) {
        return protoService.uploadProtoFile(file);
    }

}
