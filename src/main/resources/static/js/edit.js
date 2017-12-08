var config = {
	// Define the toolbar: http://docs.ckeditor.com/#!/guide/dev_toolbar
	// The full preset from CDN which we used as a base provides more features than we need.
	// Also by default it comes with a 3-line toolbar. Here we put all buttons in two rows.
	toolbar: [
		{ name: 'clipboard', items: [ 'PasteFromWord', '-', 'Undo', 'Redo' ] },
		{ name: 'basicstyles', items: [ 'Bold', 'Italic', 'Underline', 'Strike', 'RemoveFormat', 'Subscript', 'Superscript' ] },
		{ name: 'links', items: [ 'Link', 'Unlink' ] },
		{ name: 'paragraph', items: [ 'NumberedList', 'BulletedList', '-', 'Outdent', 'Indent', '-', 'Blockquote' ] },
		{ name: 'insert', items: [ 'Image', 'Table' ] },
		{ name: 'editing', items: [ 'Scayt' ] },
		'/',

		{ name: 'styles', items: [ 'Format', 'Font', 'FontSize' ] },
		{ name: 'colors', items: [ 'TextColor', 'BGColor', 'CopyFormatting' ] },
		{ name: 'align', items: [ 'JustifyLeft', 'JustifyCenter', 'JustifyRight', 'JustifyBlock' ] },
		{ name: 'document', items: [ 'Print', 'Source' ] }
	],

	// Since we define all configuration options here, let's instruct CKEditor to not load config.js which it does by default.
	// One HTTP request less will result in a faster startup time.
	// For more information check http://docs.ckeditor.com/#!/api/CKEDITOR.config-cfg-customConfig
	customConfig: '',

	// Upload images to a CKFinder connector (note that the response type is set to JSON).
//		uploadUrl: '/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Files&responseType=json',

	// Configure your file manager integration. This example uses CKFinder 3 for PHP.
//		filebrowserBrowseUrl: '/ckfinder/ckfinder.html',
//		filebrowserImageBrowseUrl: '/ckfinder/ckfinder.html?type=Images',
//		filebrowserUploadUrl: '/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Files',
//		filebrowserImageUploadUrl: '/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Images',

	// Sometimes applications that convert HTML to PDF prefer setting image width through attributes instead of CSS styles.
	// For more information check:
	//  - About Advanced Content Filter: http://docs.ckeditor.com/#!/guide/dev_advanced_content_filter
	//  - About Disallowed Content: http://docs.ckeditor.com/#!/guide/dev_disallowed_content
	//  - About Allowed Content: http://docs.ckeditor.com/#!/guide/dev_allowed_content_rules
	disallowedContent: 'img{width,height,float}',
	extraAllowedContent: 'img[width,height,align];span{background}',

	// Enabling extra plugins, available in the full-all preset: http://ckeditor.com/presets-all
	extraPlugins: 'colorbutton,font,justify,print,tableresize,uploadimage,uploadfile,pastefromword,liststyle',

	/*********************** File management support ***********************/
	// In order to turn on support for file uploads, CKEditor has to be configured to use some server side
	// solution with file upload/management capabilities, like for example CKFinder.
	// For more information see http://docs.ckeditor.com/#!/guide/dev_ckfinder_integration

	// Uncomment and correct these lines after you setup your local CKFinder instance.
	// filebrowserBrowseUrl: 'http://example.com/ckfinder/ckfinder.html',
	// filebrowserUploadUrl: 'http://example.com/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Files',
	/*********************** File management support ***********************/

	// Make the editing area bigger than default.
	height: 480,
	width: 940,

	// An array of stylesheets to style the WYSIWYG area.
	// Note: it is recommended to keep your own styles in a separate file in order to make future updates painless.
	contentsCss: [ CKEDITOR.basePath + 'contents.css', 'http://sdk.ckeditor.com/samples/assets/css/pastefromword.css' ],

	// This is optional, but will let us define multiple different styles for multiple editors using the same CSS file.
	bodyClass: 'document-editor',

	// Reduce the list of block elements listed in the Format dropdown to the most commonly used.
	format_tags: 'p;h1;h2;h3;pre',

	// Simplify the Image and Link dialog windows. The "Advanced" tab is not needed in most cases.
	removeDialogTabs: 'image:advanced;link:advanced',

	// Define the list of styles which should be available in the Styles dropdown list.
	// If the "class" attribute is used to style an element, make sure to define the style for the class in "mystyles.css"
	// (and on your website so that it rendered in the same way).
	// Note: by default CKEditor looks for styles.js file. Defining stylesSet inline (as below) stops CKEditor from loading
	// that file, which means one HTTP request less (and a faster startup).
	// For more information see http://docs.ckeditor.com/#!/guide/dev_styles
	stylesSet: [
		/* Inline Styles */
		{ name: 'Marker', element: 'span', attributes: { 'class': 'marker' } },
		{ name: 'Cited Work', element: 'cite' },
		{ name: 'Inline Quotation', element: 'q' },

		/* Object Styles */
		{
			name: 'Special Container',
			element: 'div',
			styles: {
				padding: '5px 10px',
				background: '#eee',
				border: '1px solid #ccc'
			}
		},
		{
			name: 'Compact table',
			element: 'table',
			attributes: {
				cellpadding: '5',
				cellspacing: '0',
				border: '1',
				bordercolor: '#ccc'
			},
			styles: {
				'border-collapse': 'collapse'
			}
		},
		{ name: 'Borderless Table', element: 'table', styles: { 'border-style': 'hidden', 'background-color': '#E6E6FA' } },
		{ name: 'Square Bulleted List', element: 'ul', styles: { 'list-style-type': 'square' } }
	]
};
$.post($.kbase.ctx + '/index/loadFileData', {name: $('#name').val()}, function(data){
	CKEDITOR.replace( 'docEditor', config );
	//CKEDITOR.instances.docEditor.setData(data);
	
	CKEDITOR.on('instanceReady', function (event) {
        var editor = event.editor;
        editor.setData(data);
        editor.setReadOnly(true);
        
    	window.setTimeout(function(){
    		var imgList = $($('iframe')[0].contentWindow.document).find('img');
    		$(imgList).each(function(i, img){
    			var _url = $(img).attr('src');
    			if (_url.indexOf('http')==-1){
    				$(img).attr('src', $.kbase.ctx + '/index/loadFileImg?name=' + $('#name').val() + '&imgname=' + $(img).attr('src'));
    			}
    		});
    	}, 100);
    });
	
}, 'text');

$('#btnModify').show();
$('#btnDelete').show();
$('#btnDownload').show();

//修改 和 保存
$('#btnModify').click(function(){
	var status = $(this).attr('_status');
	if (status==0){
		//修改
		CKEDITOR.instances.docEditor.setReadOnly(false);
		$(this).text('保存');
		$(this).attr('_status', 1);
		$('#btnSaveAsVersion').show();
	}else{
		//保存
		if (window.confirm('确认保存修改吗')){
			var data = CKEDITOR.instances.docEditor.getData();
			$.post($.kbase.ctx + '/index/saveFileData', {name: $('#name').val(), data: data}, function(data){
				if (data.result==1){
					alert('保存成功');
				}
			}, 'json');
		}
	}
});

//保存为版本
$('#btnSaveAsVersion').click(function(){
	alert('请填写版本信息');
});