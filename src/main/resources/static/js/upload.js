var uploader = new ss.SimpleUpload({
      button: 'btnUpload', // HTML element used as upload button
      url: $.kbase.ctx + '/index/uploadData', // URL of server-side upload handler
      name: 'uploadFile', // Parameter name of the uploaded file
      responseType: 'json',
      onSubmit: function (filename, extension) {
          this.setFileSizeBox($('#sizeBox')[0]); // designate this element as file size container
          this.setProgressBar($('#progress')[0]); // designate as progress bar
      },
      onComplete: function(filename, response, uploadBtn, fileSize){
    	  if (!response) {
              alert(filename + ' 上传失败');
              return false;
          }
    	  alert(filename + ' 上传成功');
    	  location.href = $.kbase.ctx;
      }
});