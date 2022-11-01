const PREFIX = ctx + "system/config";

$("#form-config-edit").validate({
    onkeyup: false,
    rules: {
        configKey: {
            remote: {
                url: PREFIX + "/checkConfigKeyUnique",
                type: "post",
                dataType: "json",
                data: {
                    "configId": function () {
                        return $("#configId").val();
                    },
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
            remote: "Parameter key name already exists"
        }
    },
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/edit", $('#form-config-edit').serialize());
    }
}