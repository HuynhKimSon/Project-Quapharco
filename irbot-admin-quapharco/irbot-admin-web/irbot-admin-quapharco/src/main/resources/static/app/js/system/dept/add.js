const PREFIX = ctx + "system/dept";

$("#form-dept-add").validate({
    onkeyup: false,
    rules: {
        deptName: {
            remote: {
                url: PREFIX + "/checkDeptNameUnique",
                type: "post",
                dataType: "json",
                data: {
                    "parentId": function () {
                        return $("input[name='parentId']").val();
                    },
                    "deptName": function () {
                        return $.common.trim($("#deptName").val());
                    }
                },
                dataFilter: function (data, type) {
                    return $.validate.unique(data);
                }
            }
        },
        orderNum: {
            digits: true
        },
        email: {
            email: true,
        },
        phone: {
            isPhone: true,
        },
    },
    messages: {
        "deptName": {
            remote: "Bộ phận đã tồn tại"
        }
    },
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/add", $('#form-dept-add').serialize());
    }
}

function selectDeptTree() {
    var options = {
        title: 'Lựa chọn bộ phận',
        width: "380",
        url: PREFIX + "/selectDeptTree/" + $("#treeId").val(),
        callBack: doSubmit
    };
    $.modal.openOptions(options);
}

function doSubmit(index, layero) {
    var body = layer.getChildFrame('body', index);
    $("#treeId").val(body.find('#treeId').val());
    $("#treeName").val(body.find('#treeName').val());
    layer.close(index);
}