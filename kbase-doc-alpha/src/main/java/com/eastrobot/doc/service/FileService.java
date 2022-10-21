package com.eastrobot.doc.service;

import com.eastrobot.doc.model.entity.Attachment;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

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

    String loadData(Long id) throws IOException;

    /**
     * 获取指定目录下的文件列表
     * @return
     * @throws FileNotFoundException
     */
    Iterable<Attachment> list() throws FileNotFoundException;

    void delete(Long id);
}
