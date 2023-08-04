package com.fairy.protobuf.service;

import org.springframework.web.multipart.MultipartFile;


public interface ProtoService {

    byte[] generateProtoClasses();

    byte[] uploadProtoFile(MultipartFile file);
}
