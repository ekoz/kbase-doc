/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.doc.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.eastrobot.doc.config.BaseController;
import com.eastrobot.doc.dao.AttachmentRepository;
import com.eastrobot.doc.model.entity.Attachment;
import com.eastrobot.doc.service.FileService;
import com.eastrobot.doc.util.HtmlUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Optional;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年7月29日 上午9:44:36
 * @version 1.0
 */
@Api
@RequestMapping("index")
@RestController
@Slf4j
public class IndexController extends BaseController {

	private final String outputExtension = "html";

	@Resource
	FileService fileService;

	@Resource
  AttachmentRepository attachmentRepository;

	/**
	 * 文件列表
	 * @author eko.zhan at 2017年8月9日 下午8:32:19
	 * @return
	 * @throws FileNotFoundException
	 */
	@ApiOperation(value="获取文件数据列表", notes="获取固定路径下的文件，并返回文件名，文件所在路径和文件大小")
	@RequestMapping(value="getDataList", method=RequestMethod.POST)
	public Iterable<Attachment> getDataList() throws FileNotFoundException{
		return fileService.list();
	}
	
	/**
	 * 加载文件
	 * @author eko.zhan at 2017年8月9日 下午8:32:30
	 * @param id
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@ApiOperation(value="根据指定的文件id获取文件内容", notes="返回文件内容会自动过滤图片信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="文件id", required=true, dataType="String")
	})
	@RequestMapping(value="loadFileData", produces="text/plain;charset=utf-8", method=RequestMethod.POST)
	public String loadFileData(Long id) throws IOException{
		return fileService.loadData(id);
	}
	
	/**
	 * 加载 html 中的图片资源
	 * @author eko.zhan at 2017年8月9日 下午8:32:06
	 * @param id
	 * @param imgname
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value="加载图片内容", notes="获取word文档中内嵌的图片资源，返回图片内容")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="文件id", required=true, dataType="Long"),
		@ApiImplicitParam(name="imgname", value="图片名称", required=true, dataType="String")
	})
	@RequestMapping(value="loadFileImg", method=RequestMethod.GET)
	public ResponseEntity<byte[]> loadFileImg(Long id, String imgname){
		try {
			String basename = id + "";
			File file = ResourceUtils.getFile("classpath:static/DATAS/" + basename + "/" + imgname);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);
			return new ResponseEntity<byte[]>(IOUtils.toByteArray(new FileInputStream(file)), headers, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			log.error("文件[" + id + "]不存在, " + e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}
	/**
	 * 上传文件
	 * @author eko.zhan at 2017年8月9日 下午8:32:39
	 * @param uploadFile
	 * @return
	 */
	@ApiOperation(value="上传文件", notes="")
	@ApiImplicitParams({
		@ApiImplicitParam(name="uploadFile", value="待上传的文件", required=true, dataType="__file")
	})
	@RequestMapping(value="uploadData", method=RequestMethod.POST)
	public JSONObject uploadData(@RequestParam("uploadFile") MultipartFile uploadFile){
		JSONObject json = new JSONObject();
		json.put("result", fileService.upload(uploadFile)?1:0);
		return json;
	}
	
	/**
	 * 保存html内容
	 * @author eko.zhan at 2017年8月9日 下午9:04:20
	 * @param id
	 * @param data
	 * @throws IOException 
	 */
	@ApiOperation(value="保存文件", notes="")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="文件id", required=true, dataType="Long"),
		@ApiImplicitParam(name="data", value="文件内容", required=true, dataType="String")
	})
	@RequestMapping(value="saveFileData", method=RequestMethod.POST)
	public JSONObject saveFileData(Long id, String data, HttpServletRequest request){
		//TODO 这是一个伪保存，只是修改了 HTML 内容，并未修改 file 文件，如果用户单击下载，依然会存在问题
		//TODO 如果用户修改了图片，如何处理？
		JSONObject json = new JSONObject();
		json.put("result", 1);
		try {
			//DONE 优化底层 DefaultDocumentFormatRegistry.java 后可实现html转docx
			//boolean is07Xml = false; //是否是07以后的文档（docx/xlsx/pptx/），如果是，需要将html转成97版本的office文件，再从97转成07，直接将html转成07存在问题
      Optional<Attachment> attachmentOptional = attachmentRepository.findById(id);

      if (!attachmentOptional.isPresent()) {
        json.put("result", 0);
        return json;
      }

      Attachment attachment = attachmentOptional.get();

      File file = ResourceUtils.getFile(attachment.getPath());
      String name = FilenameUtils.getName(attachment.getPath());
			File newFile = new File(file.getParent() + "/" + Calendar.getInstance().getTimeInMillis() + "_" + name);
			if (!name.toLowerCase().endsWith("x")){
				newFile = new File(file.getParent() + "/" + Calendar.getInstance().getTimeInMillis() + "_" + name + "x");
			}
			String basename = FilenameUtils.getBaseName(file.getName());
			File targetFile = new File(file.getParent() + "/" + basename + "/" + basename + "." + outputExtension);
			if (targetFile.exists()){
				///将html中的body内容替换为当前 data 数据
				///String htmlData = IOUtils.toString(new FileInputStream(targetFile), HtmlUtils.getFileEncoding(targetFile));
//				String htmlData = (String)request.getSession().getAttribute(SystemConstants.HTML_HEADER) + data + HtmlUtils.FOOT_TEMPLATE;

        String htmlData = data + HtmlUtils.FOOT_TEMPLATE;


				//DONE 如何处理文件编码？保存后尽管通过请求能访问中文内容，但是直接磁盘双击html文件显示乱码
				//add by eko.zhan at 2017-08-11 14:55 解决方案：重写Html头，编码修改为 utf-8
				///IOUtils.write(htmlData.getBytes(HtmlUtils.UTF8), new FileOutputStream(targetFile));
				IOUtils.write(htmlData.getBytes(), new FileOutputStream(targetFile));

				//DONE 由于html文件编码不正确，导致转换成word后文件编码也不正确
				//add by eko.zhan at 2017-08-11 15:05 上面处理了html编码后，转换的编码问题也相应解决了
				//TODO 由html转换成doc会导致doc样式有误
				convert(targetFile, newFile);

			}
		} catch (IOException e) {
			json.put("result", 0);
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 删除文件
	 * @author eko.zhan at 2017年8月9日 下午9:32:18
	 * @param id
	 * @return
	 */
	@ApiOperation(value="删除文件", notes="")
	@ApiImplicitParams({
		@ApiImplicitParam(name="id", value="文件id", required=true, dataType="Long")
	})
	@DeleteMapping("{id}")
	public void delete(@PathVariable("id") Long id){
		fileService.delete(id);
	}
}
