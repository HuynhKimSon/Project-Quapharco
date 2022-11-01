const PREFIX = ctx + "system/dept";

$("#form-dept-edit").validate({
    onkeyup: false,
    rules: {
        deptName: {
            remote: {
                url: PREFIX + "/checkDeptNameUnique",
                type: "post",
                dataType: "json",
                data: {
                    "deptId": function () {
                        return $("#deptId").val();
                    },
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
        $.operate.save(PREFIX + "/edit", $('#form-dept-edit').serialize());
    }
}

function selectDeptTree() {
    var deptId = $("#treeId").val();
    var excludeId = $("input[name='deptId']").val();
    if (deptId > 0) {
        var options = {
            title: 'Lựa chọn bộ phận',
            width: "380",
            url: PREFIX + "/selectDeptTree/" + $("#treeId").val() + "/" + excludeId,
            callBack: doSubmit
        };
        $.modal.openOptions(options);
    } else {
        $.modal.alertError("Không thể chọn bộ phận cao hơn");
    }
}

function doSubmit(index, layero) {
    var tree = layero.find("iframe")[0].contentWindow.$._tree;
    if ($.tree.notAllowLastLevel(tree)) {
        var body = layer.getChildFrame('body', index);
        $("#treeId").val(body.find('#treeId').val());
        $("#treeName").val(body.find('#treeName').val());
        layer.close(index);
    }
}