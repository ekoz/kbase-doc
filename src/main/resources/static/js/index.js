$.post($.kbase.ctx + '/index/getDataList', function(data){
	$('#list').html(template('templateList', data));
}, 'json');

$('#list').on('click', 'a', function(){
	location.href = $.kbase.ctx + '/edit?name=' + $(this).text();
});

$('#list').on('click', '.btn-danger', function(){
	var name = $(this).parents('tr').find('td:eq(0)').text();
	if (window.confirm('确认删除吗')){
		$.post($.kbase.ctx + '/index/delete', {name: name}, function(data){
			if (data.result==1){
				location.reload();
			}else{
				alert(data.msg);
			}
		}, 'json');
	}
});