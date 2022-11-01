$(function(){
	// Set maxlength for validation data
	dictTypes.forEach(function(type){
		$('#' + type.dictLabel).attr('maxlength',type.dictValue);
	});
});