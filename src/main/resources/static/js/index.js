function loadData(){
  $.post($.kbase.ctx + '/index/getDataList', function(data){
    $('#list').html(template('templateList', data));
  }, 'json');
};

loadData();

$('#list').on('click', 'a', function(){
	location.href = $.kbase.ctx + '/edit?id=' + $(this).attr('_id');
});

$('#list').on('click', '.btn-danger', function(){
	var id = $(this).parents('tr').find('td:eq(0) a').attr('_id');
	if (window.confirm('确认删除吗')){
		axios.delete($.kbase.ctx + '/index/' + id)
		.then(()=>{
      loadData();
		});
	}
});