$(function () {
    var url = ctx + "system/dept/roleDeptTreeData?roleId=" + $("#roleId").val();
    var options = {
        id: "deptTrees",
        url: url,
        check: { enable: true, nocheckInherit: true, chkboxType: { "Y": "ps", "N": "ps" } },
        expandLevel: 2
    };
    $.tree.init(options);
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
        edit();
    }
}

function edit() {
    var roleId = $("input[name='roleId']").val();
    var roleName = $("input[name='roleName']").val();
    var roleKey = $("input[name='roleKey']").val();
    var dataScope = $("#dataScope").val();
    var deptIds = $.tree.getCheckedNodes();
    $.ajax({
        cache: true,
        type: "POST",
        url: ctx + "system/role/authDataScope",
        data: {
            "roleId": roleId,
            "roleName": roleName,
            "roleKey": roleKey,
            "dataScope": dataScope,
            "deptIds": deptIds
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

$("#dataScope").change(function (event) {
    var dataScope = $(event.target).val();
    dataScopeVisible(dataScope);
});

function dataScopeVisible(dataScope) {
    if (dataScope == 2) {
        $("#authDataScope").show();
    } else {
        $._tree.checkAllNodes(false);
        $("#authDataScope").hide();
    }
}