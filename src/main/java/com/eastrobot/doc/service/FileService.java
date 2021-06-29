package com.eastrobot.doc.service;

import com.eastrobot.doc.model.entity.Attachment;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2021/6/29 20:08
 */
public interface FileService {

    /**
     * 上传文件并转换
     * @param multipartFile
     * @return
     */
    Boolean upload(MultipartFile multipartFile);

    void convert(File inputFile, File outputFile) throws FileNotFoundException;

    String loadData(String filename) throws IOException;

    /**
     * 获取指定目录下的文件列表
     * @return
     * @throws FileNotFoundException
     */
    List<Attachment> list() throws FileNotFoundException;
}
