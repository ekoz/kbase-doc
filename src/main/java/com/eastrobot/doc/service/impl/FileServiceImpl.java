package com.eastrobot.doc.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.eastrobot.doc.config.SystemConstants;
import com.eastrobot.doc.config.WebappContext;
import com.eastrobot.doc.model.entity.Attachment;
import com.eastrobot.doc.service.FileService;
import com.eastrobot.doc.util.HtmlUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @version 1.0
 * @date 2021/6/29 20:11
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {

    private final String outputExtension = "html";

    private Map<String, Integer> atConvertMap = Maps.newHashMap();

    @Override
    public Boolean upload(MultipartFile multipartFile) {
        try{
            File targetDir = ResourceUtils.getFile("classpath:static/DATAS/");
            String inputExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
            String inputFilename = String.valueOf(Calendar.getInstance().getTimeInMillis());
            File file = new File(targetDir.getAbsolutePath() + "/" + inputFilename + "." + inputExtension);
            FileCopyUtils.copy(multipartFile.getBytes(), file);

            File outputFile = new File(targetDir.getAbsolutePath() + "/" + inputFilename + "/" + inputFilename + "." + outputExtension);
            convert(file, outputFile);
        } catch (FileNotFoundException e){
            return false;
        } catch (IOException e){
            return false;
        } finally {
            return true;
        }
    }

    @Async
    public void convert(File inputFile, File outputFile) throws FileNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        WebappContext webappContext = WebappContext.get(request.getServletContext());
        OfficeDocumentConverter converter = webappContext.getDocumentConverter();
        String inputExtension = FilenameUtils.getExtension(inputFile.getName());

        try {
            long startTime = System.currentTimeMillis();
            atConvertMap.put(inputFile.getName(), 1);
            converter.convert(inputFile, outputFile);
            atConvertMap.remove(inputFile.getName());
            long conversionTime = System.currentTimeMillis() - startTime;
            log.info(String.format("successful conversion: %s [%db] to %s in %dms", inputExtension, inputFile.length(), outputExtension, conversionTime));
        } catch (Exception e) {
            log.error(String.format("failed conversion: %s [%db] to %s; %s; input file: %s", inputExtension, inputFile.length(), outputExtension, e, inputFile.getName()));
            throw new FileNotFoundException();
        }
    }

    @Override
    public String loadData(String filename) throws IOException {
        File file = ResourceUtils.getFile("classpath:static/DATAS/" + filename);

        if (atConvertMap.get(file.getName())!=null) {
            // 转换中
            return String.format(" 文件 [%s] 正在转换中", file.getName());
        }

        String basename = FilenameUtils.getBaseName(file.getName());
        File targetFile = new File(file.getParent() + "/" + basename + "/" + basename + "." + outputExtension);
        if (targetFile.exists()){
            //logger.debug(HtmlUtils.getFileEncoding(targetFile));
            String data = IOUtils.toString(new FileInputStream(targetFile), HtmlUtils.getFileEncoding(targetFile));
            //logger.debug(data);
            //获取文件头部，每个文件转换出的html头部样式都不一样，动态获取
            //截图 <BODY 之前的所有代码
            String header = HtmlUtils.HEAD_TEMPLATE;
            try{
                header = data.substring(0, data.toLowerCase().indexOf("<body"));
                String tmp = data.substring(data.toLowerCase().indexOf("<body"));
                header += tmp.substring(0, tmp.indexOf(">") + 1);
                header = HtmlUtils.replaceCharset(header);
            }catch(StringIndexOutOfBoundsException e){
                e.printStackTrace();
                log.error("html页面数据解析异常");
            }

//            HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
//            request.getSession().setAttribute(SystemConstants.HTML_HEADER, header);
            //TODO 如果是网络图片，如何处理？
            //TODO 保存后再次打开html文档，如何处理？
            //data = HtmlUtils.replaceHtmlTag(data, "img", "src", "src=\"" + request.getContextPath() + "/index/loadFileImg?name=" + name + "&imgname=", "\"");
            return data;
        }
        return "";
    }

    @Override
    public List<Attachment> list() throws FileNotFoundException {
        File dir = ResourceUtils.getFile("classpath:static/DATAS");
        File[] files = dir.listFiles();
        List<Attachment> list = Lists.newArrayList();
        for (File file : files){
            if (file.isFile()){
                list.add(Attachment
                        .builder()
                        .path(file.getPath())
                        .name(file.getName())
                        .size(file.length())
                        .build());
            }
        }
        return list;
    }
}
