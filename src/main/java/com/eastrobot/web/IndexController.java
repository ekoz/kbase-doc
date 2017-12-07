/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.web;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.eastrobot.config.SystemConstants;
import com.eastrobot.config.WebappContext;
import com.eastrobot.util.HtmlUtils;

/**
 * @author <a href="mailto:eko.z@outlook.com">eko.zhan</a>
 * @date 2017年7月29日 上午9:44:36
 * @version 1.0
 */
@RequestMapping("index")
@RestController
public class IndexController {
	
	private final static Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	private final String outputExtension = "html";

	/**
	 * 文件列表
	 * @author eko.zhan at 2017年8月9日 下午8:32:19
	 * @return
	 * @throws FileNotFoundException
	 */
	@ApiOperation(value="获取文件数据列表", notes="获取固定路径下的文件，并返回文件名，文件所在路径和文件大小")
	@RequestMapping(value="getDataList", method=RequestMethod.POST)
	public JSONArray getDataList() throws FileNotFoundException{
		JSONArray arr = new JSONArray();
		File dir = ResourceUtils.getFile("classpath:static/DATAS");
		File[] files = dir.listFiles();
		for (File file : files){
			if (file.isFile()){
				JSONObject json = new JSONObject();
				json.put("path", file.getPath());
				json.put("name", file.getName());
				json.put("size", file.length());
				arr.add(json);
			}
		}
		return arr;
	}
	
	/**
	 * 加载文件
	 * @author eko.zhan at 2017年8月9日 下午8:32:30
	 * @param name
	 * @param request
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@ApiOperation(value="根据指定的文件名称获取文件内容", notes="返回文件内容会自动过滤图片信息")
	@ApiImplicitParams({
		@ApiImplicitParam(name="name", value="文件相对路径", required=true, dataType="String")
	})
	@RequestMapping(value="loadFileData", produces="text/plain;charset=utf-8", method=RequestMethod.POST)
	public String loadFileData(String name, HttpServletRequest request) throws FileNotFoundException, IOException{
		File file = ResourceUtils.getFile("classpath:static/DATAS/" + name);
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
				logger.error("html页面数据解析异常");
			}
			
			request.getSession().setAttribute(SystemConstants.HTML_HEADER, header);
			//TODO 如果是网络图片，如何处理？
			//TODO 保存后再次打开html文档，如何处理？
			//data = HtmlUtils.replaceHtmlTag(data, "img", "src", "src=\"" + request.getContextPath() + "/index/loadFileImg?name=" + name + "&imgname=", "\"");
			return data;
		}
		return "";
	}
	
	/**
	 * 加载 html 中的图片资源
	 * @author eko.zhan at 2017年8月9日 下午8:32:06
	 * @param name
	 * @param imgname
	 * @return
	 * @throws IOException
	 */
	@ApiOperation(value="加载图片内容", notes="获取word文档中内嵌的图片资源，返回图片内容")
	@ApiImplicitParams({
		@ApiImplicitParam(name="name", value="文件相对路径", required=true, dataType="String"),
		@ApiImplicitParam(name="imgname", value="图片名称", required=true, dataType="String")
	})
	@RequestMapping(value="loadFileImg", method=RequestMethod.GET)
	public ResponseEntity<byte[]> loadFileImg(String name, String imgname){
		try {
			String basename = FilenameUtils.getBaseName(name);
			File file = ResourceUtils.getFile("classpath:static/DATAS/" + basename + "/" + imgname);
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.IMAGE_PNG);
			return new ResponseEntity<byte[]>(IOUtils.toByteArray(new FileInputStream(file)), headers, HttpStatus.OK);
		} catch (FileNotFoundException e) {
			logger.error("文件[" + name + "]不存在, " + e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} 
		return null;
	}
	/**
	 * 上传文件
	 * @author eko.zhan at 2017年8月9日 下午8:32:39
	 * @param uploadFile
	 * @param request
	 * @return
	 */
	@ApiOperation(value="上传文件", notes="")
	@ApiImplicitParams({
		@ApiImplicitParam(name="uploadFile", value="待上传的文件", required=true, dataType="MultipartFile")
	})
	@RequestMapping(value="uploadData", method=RequestMethod.POST)
	public JSONObject uploadData(@RequestParam("uploadFile") MultipartFile uploadFile, HttpServletRequest request){
		JSONObject json = new JSONObject();
		json.put("result", 1);
		WebappContext webappContext = WebappContext.get(request.getServletContext());
		OfficeDocumentConverter converter = webappContext.getDocumentConverter();
		
		try {
			File targetDir = ResourceUtils.getFile("classpath:static/DATAS/");
			String inputExtension = FilenameUtils.getExtension(uploadFile.getOriginalFilename());
			String inputFilename = String.valueOf(Calendar.getInstance().getTimeInMillis());
			File file = new File(targetDir.getAbsolutePath() + "/" + inputFilename + "." + inputExtension);
			FileCopyUtils.copy(uploadFile.getBytes(), file);
			
			File outputFile = new File(targetDir.getAbsolutePath() + "/" + inputFilename + "/" + inputFilename + "." + outputExtension);
			try {
	        	long startTime = System.currentTimeMillis();
	        	converter.convert(file, outputFile);
	        	long conversionTime = System.currentTimeMillis() - startTime;
	        	logger.info(String.format("successful conversion: %s [%db] to %s in %dms", inputExtension, file.length(), outputExtension, conversionTime));
	        } catch (Exception e) {
	            logger.error(String.format("failed conversion: %s [%db] to %s; %s; input file: %s", inputExtension, file.length(), outputExtension, e, file.getName()));
	        }
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			json.put("result", 0);
		} catch (IOException e) {
			e.printStackTrace();
			json.put("result", 0);
		} 
		return json;
	}
	
	/**
	 * 保存html内容
	 * @author eko.zhan at 2017年8月9日 下午9:04:20
	 * @param name
	 * @param data
	 * @throws IOException 
	 */
	@ApiOperation(value="保存文件", notes="")
	@ApiImplicitParams({
		@ApiImplicitParam(name="name", value="文件相对路径", required=true, dataType="String"),
		@ApiImplicitParam(name="data", value="文件内容", required=true, dataType="String")
	})
	@RequestMapping(value="saveFileData", method=RequestMethod.POST)
	public JSONObject saveFileData(String name, String data, HttpServletRequest request){
		//TODO 这是一个伪保存，只是修改了 HTML 内容，并未修改 file 文件，如果用户单击下载，依然会存在问题
		//TODO 如果用户修改了图片，如何处理？
		JSONObject json = new JSONObject();
		json.put("result", 1);
		try {
			//DONE 优化底层 DefaultDocumentFormatRegistry.java 后可实现html转docx
			//boolean is07Xml = false; //是否是07以后的文档（docx/xlsx/pptx/），如果是，需要将html转成97版本的office文件，再从97转成07，直接将html转成07存在问题
			File file = ResourceUtils.getFile("classpath:static/DATAS/" + name);
			File newFile = new File(file.getParent() + "/" + Calendar.getInstance().getTimeInMillis() + "_" + name);
			if (!name.toLowerCase().endsWith("x")){
				newFile = new File(file.getParent() + "/" + Calendar.getInstance().getTimeInMillis() + "_" + name + "x");
			}
			String basename = FilenameUtils.getBaseName(file.getName());
			File targetFile = new File(file.getParent() + "/" + basename + "/" + basename + "." + outputExtension);
			if (targetFile.exists()){
				//将html中的body内容替换为当前 data 数据
				//String htmlData = IOUtils.toString(new FileInputStream(targetFile), HtmlUtils.getFileEncoding(targetFile));
				String htmlData = (String)request.getSession().getAttribute(SystemConstants.HTML_HEADER) + data + HtmlUtils.FOOT_TEMPLATE;
				//DONE 如何处理文件编码？保存后尽管通过请求能访问中文内容，但是直接磁盘双击html文件显示乱码
				//add by eko.zhan at 2017-08-11 14:55 解决方案：重写Html头，编码修改为 utf-8
				//IOUtils.write(htmlData.getBytes(HtmlUtils.UTF8), new FileOutputStream(targetFile));
				IOUtils.write(htmlData.getBytes(), new FileOutputStream(targetFile));
				//DONE 由于html文件编码不正确，导致转换成word后文件编码也不正确
				//add by eko.zhan at 2017-08-11 15:05 上面处理了html编码后，转换的编码问题也相应解决了
				//TODO 由html转换成doc会导致doc样式有误
				WebappContext webappContext = WebappContext.get(request.getServletContext());
				OfficeDocumentConverter converter = webappContext.getDocumentConverter();
				try {
		        	long startTime = System.currentTimeMillis();
		        	converter.convert(targetFile, newFile);
		        	long conversionTime = System.currentTimeMillis() - startTime;
		        	logger.info(String.format("successful conversion: %s [%db] to %s in %dms", FilenameUtils.getExtension(targetFile.getName()), targetFile.length(), FilenameUtils.getExtension(newFile.getName()), conversionTime));
		        } catch (Exception e) {
		        	e.printStackTrace();
		            logger.error(String.format("failed conversion: %s [%db] to %s; %s; input file: %s", FilenameUtils.getExtension(targetFile.getName()), targetFile.length(), FilenameUtils.getExtension(newFile.getName()), e, newFile.getName()));
		        }
			}
		} catch (FileNotFoundException e) {
			json.put("result", 0);
			e.printStackTrace();
		} catch (IOException e) {
			json.put("result", 0);
			e.printStackTrace();
		}
		return json;
	}
	
	/**
	 * 删除文件
	 * @author eko.zhan at 2017年8月9日 下午9:32:18
	 * @param name
	 * @return
	 */
	@ApiOperation(value="删除文件", notes="")
	@ApiImplicitParams({
		@ApiImplicitParam(name="name", value="文件相对路径", required=true, dataType="String")
	})
	@RequestMapping(value="delete", method=RequestMethod.POST)
	public JSONObject delete(String name){
		//TODO windows操作系统上如果html文件被占用则无法删除，是否可以用 File.creteTempFile 来解决？
		JSONObject json = new JSONObject();
		json.put("result", 1);
		try {
			File file = ResourceUtils.getFile("classpath:static/DATAS/" + name);
			String basename = FilenameUtils.getBaseName(file.getName());
			File targetDir = new File(file.getParent() + "/" + basename);
			if (targetDir.exists()){
				FileUtils.deleteDirectory(targetDir);
			}
			FileUtils.forceDelete(file);
		} catch (FileNotFoundException e) {
			json.put("result", 0);
			json.put("msg", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			json.put("result", 0);
			json.put("msg", e.getMessage());
			e.printStackTrace();
		}
		return json;
	}
}
