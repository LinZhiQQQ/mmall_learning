package com.mmall.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author LinZhiQ
 * @date 2018/9/10 16:15
 */
public interface IFileService {

    String upload(MultipartFile file, String path);
}
