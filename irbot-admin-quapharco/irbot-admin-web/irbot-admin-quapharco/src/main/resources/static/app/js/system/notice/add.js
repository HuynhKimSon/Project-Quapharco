const PREFIX = ctx + "system/notice";

$('.summernote').summernote({
    placeholder: 'Vui lòng nhập nội dung thông báo',
    height: 192,
    lang: 'zh-CN',
    followingToolbar: false,
    callbacks: {
        onImageUpload: function (files) {
            sendFile(files[0], this);
        }
    }
});

function sendFile(file, obj) {
    var data = new FormData();
    data.append("file", file);
    $.ajax({
        type: "POST",
        url: ctx + "common/upload",
        data: data,
        cache: false,
        contentType: false,
        processData: false,
        dataType: 'json',
        success: function (result) {
            if (result.code == web_status.SUCCESS) {
                $(obj).summernote('editor.insertImage', result.url, result.fileName);
            } else {
                $.modal.alertError(result.msg);
            }
        },
        error: function (error) {
            $.modal.alertWarning("Tải lên hình ảnh không thành công.");
        }
    });
}

$("#form-notice-add").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        var sHTML = $('.summernote').summernote('code');
        $("#noticeContent").val(sHTML);
        $.operate.save(PREFIX + "/add", $('#form-notice-add').serialize());
    }
}