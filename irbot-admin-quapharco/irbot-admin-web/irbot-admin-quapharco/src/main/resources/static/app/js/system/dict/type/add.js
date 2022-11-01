const PREFIX = ctx + "system/dict";

$("#form-dict-add").validate({
    onkeyup: false,
    rules: {
        dictType: {
            minlength: 5,
            remote: {
                url: PREFIX + "/checkDictTypeUnique",
                type: "post",
                dataType: "json",
                data: {
                    name: function () {
                        return $.common.trim($("#dictType").val());
                    }
                },
                dataFilter: function (data, type) {
                    return $.validate.unique(data);
                }
            }
        },
    },
    messages: {
        "dictType": {
            remote: "Loại từ điển đã tồn tại"
        }
    },
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/add", $('#form-dict-add').serialize());
    }
}