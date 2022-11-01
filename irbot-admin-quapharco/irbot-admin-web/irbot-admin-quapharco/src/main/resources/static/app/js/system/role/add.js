$(function () {
    var url = ctx + "system/menu/roleMenuTreeData";
    var options = {
        id: "menuTrees",
        url: url,
        check: { enable: true },
        expandLevel: 0
    };
    $.tree.init(options);
});

$("#form-role-add").validate({
    rules: {
        onkeyup: false,
        roleName: {
            remote: {
                url: ctx + "system/role/checkRoleNameUnique",
                type: "post",
                dataType: "json",
                data: {
                    "roleName": function () {
                        return $.common.trim($("#roleName").val());
                    }
                },
                dataFilter: function (data, type) {
                    return $.validate.unique(data);
                }
            }
        },
        roleKey: {
            remote: {
                url: ctx + "system/role/checkRoleKeyUnique",
                type: "post",
                dataType: "json",
                data: {
                    "roleKey": function () {
                        return $.common.trim($("#roleKey").val());
                    }
                },
                dataFilter: function (data, type) {
                    return $.validate.unique(data);
                }
            }
        },
        roleSort: {
            digits: true
        },
    },
    messages: {
        "roleName": {
            remote: "Tên vai trò đã tồn tại"
        },
        "roleKey": {
            remote: "Mã vai trò đã tồn tại"
        }
    },
    focusCleanup: true
});

$('input').on('ifChanged', function (obj) {
    var type = $(this).val();
    var checked = obj.currentTarget.checked;
    if (type == 1) {
        if (checked) {
            $._tree.expandAll(true);
        } else {
            $._tree.expandAll(false);
        }
    } else if (type == "2") {
        if (checked) {
            $._tree.checkAllNodes(true);
        } else {
            $._tree.checkAllNodes(false);
        }
    } else if (type == "3") {
        if (checked) {
            $._tree.setting.check.chkboxType = { "Y": "ps", "N": "ps" };
        } else {
            $._tree.setting.check.chkboxType = { "Y": "", "N": "" };
        }
    }
})

function submitHandler() {
    if ($.validate.form()) {
        add();
    }
}

function add() {
    var roleName = $("input[name='roleName']").val();
    var roleKey = $("input[name='roleKey']").val();
    var roleSort = $("input[name='roleSort']").val();
    var status = $("input[id='status']").is(':checked') == true ? 0 : 1;
    var remark = $("input[name='remark']").val();
    var menuIds = $.tree.getCheckedNodes();
    $.ajax({
        cache: true,
        type: "POST",
        url: ctx + "system/role/add",
        data: {
            "roleName": roleName,
            "roleKey": roleKey,
            "roleSort": roleSort,
            "status": status,
            "remark": remark,
            "menuIds": menuIds
        },
        async: false,
        error: function (request) {
            $.modal.alertError("Lỗi hệ thống");
        },
        success: function (data) {
            $.operate.successCallback(data);
        }
    });
}