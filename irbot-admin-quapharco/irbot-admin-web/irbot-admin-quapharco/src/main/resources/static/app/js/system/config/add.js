const PREFIX = ctx + "system/config";

$("#form-config-add").validate({
    onkeyup: false,
    rules: {
        configKey: {
            remote: {
                url: PREFIX + "/checkConfigKeyUnique",
                type: "post",
                dataType: "json",
                data: {
                    "configKey": function () {
                        return $.common.trim($("#configKey").val());
                    }
                },
                dataFilter: function (data, type) {
                    return $.validate.unique(data);
                }
            }
        },
    },
    messages: {
        "configKey": {
            remote: "Tên khóa config đã tồn tại"
        }
    },
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/add", $('#form-config-add').serialize());
    }
}