## 文档在线预览编辑系统
之前做过在线编辑文档的功能，使用 [webdav](https://www.webdavsystem.com/) 来实现在线编辑，
存在的问题是编辑页面保存不可控（是否可以保存为版本、是否可以放弃保存等）。如果大家有实现类似功能，希望能提供帮助。

当前应用的主要功能是采用 openoffice 和 ckeditor 来实现文档的预览和编辑功能，先介绍操作步骤，再谈下面临的问题。

### 操作步骤

#### 文件列表
展示当前用户已上传的文件，并提供预览和删除操作

![列表页面](src/main/resources/static/img/list.png?raw=true "列表页面")

#### 上传文件
上传文件时直接利用 openoffice 将文件转换成 html并保存

![上传页面](src/main/resources/static/img/upload.png?raw=true "上传页面")

#### 预览文件
用户可以在预览界面单击编辑按钮，并实现保存或保存为版本功能

![预览页面](src/main/resources/static/img/read.png?raw=true "预览页面")

![编辑页面](src/main/resources/static/img/edit.png?raw=true "编辑页面")

### 已知问题
1. word中包含图片如何处理？目前是用Html正则将图片加上一个 地址 进行加载，这样在保存的时候，html文件的图片地址有异常

	答：word 中的图片，不在后端进行处理，如果该图片是网络图片，可以进行正常访问吗，如果该图片是本地图片，前端采用js处理

2. html 图片的正则未区分网络图片还是本地图片 

	答：见第1点

3. 用户修改word中的图片如何处理？

	答：按照第1点处理后，不存在该问题

4. 将html转换成doc后，文件内容格式有误

	答：html直接转换成docx，采用 LibreOffice 转换，效果略优于 OpenOffice

5. windows 操作系统上如何删除一个正在被占用的进程？
6. 是否能很好的兼容 excel 和 ppt 的预览编辑功能 

	答：暂时不能
	
### 总结
1. visio 文件可采用 LibreOffice 转换成 pdf 实现在线预览，OpenOffice无法实现
2. LibreOffice 支持转换加密的 office 文件，前提是给出明文密码。详见 [ConverteTests#testEncrypt](src/test/java/com/eastrobot/util/ConverteTests.java)
3. pdf 文件可以调用 pdf2dom 来实现转换 html，从而也可以在线编辑。详见 [PdfToHtmlTests#pdf2html](src/test/java/com/eastrobot/util/PdfToHtmlTests.java)
4. 水印（WaterMark）实现方案，本案例中只实现了 docx 的文本水印，有一种实现方案是将所有 office 文件转换成 pdf 文件，然后采用 itextpdf 对 pdf 文件进行水印处理。详见  [WaterMarkTests#testVisioAsPdfWithImg](src/test/java/com/eastrobot/util/WaterMarkTests.java)

### Restful Apis
[http://localhost:8080/kbase-doc/swagger-ui.html](http://localhost:8080/kbase-doc/swagger-ui.html)

### 感谢
[mirkonasato / jodconverter](https://github.com/mirkonasato/jodconverter)

[sbraconnier/jodconverter](https://github.com/sbraconnier/jodconverter)
