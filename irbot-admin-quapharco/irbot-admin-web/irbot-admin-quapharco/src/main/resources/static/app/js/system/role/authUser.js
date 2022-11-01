const PREFIX = ctx + "system/role/authUser";

$(function () {
    var options = {
        url: PREFIX + "/allocatedList",
        createUrl: PREFIX + "/add",
        updateUrl: PREFIX + "/edit/{id}",
        removeUrl: PREFIX + "/remove",
        exportUrl: PREFIX + "/export",
        importUrl: PREFIX + "/importData",
        importTemplateUrl: PREFIX + "/importTemplate",
        queryParams: queryParams,
        sortName: "createTime",
        sortOrder: "desc",
        modalName: "Người dùng",
        columns: [{
            checkbox: true
        },
        {
            field: 'userId',
            title: 'STT',
            visible: false
        },
        {
            field: 'loginName',
            title: 'Tên đăng nhập',
            sortable: true
        },
        {
            field: 'userName',
            title: 'Tên tài khoản'
        },
        {
            field: 'email',
            title: 'Email'
        },
        {
            field: 'phonenumber',
            title: 'Điện thoại di động'
        },
        {
            field: 'status',
            title: 'Trạng thái',
            align: 'center',
            formatter: function (value, row, index) {
                return $.table.selectDictLabel(datas, value);
            }
        },
        {
            field: 'createTime',
            title: 'Ngày tạo',
            sortable: true
        },
        {
            title: 'Hành động',
            align: 'center',
            formatter: function (value, row, index) {
                var actions = [];
                actions.push('<a class="btn btn-danger btn-xs ' + removeFlag + '" href="javascript:void(0)" onclick="cancelAuthUser(\'' + row.userId + '\')"><i class="fa fa-remove"></i>Hủy ủy quyền</a> ');
                return actions.join('');
            }
        }]
    };
    $.table.init(options);
});

function queryParams(params) {
    var search = $.table.queryParams(params);
    search.roleId = $("#roleId").val();
    return search;
}

function selectUser() {
    var url = PREFIX + '/selectUser/' + $("#roleId").val();
    $.modal.open("Chọn người dùng", url);
}

function cancelAuthUserAll(userId) {
    var rows = $.table.selectFirstColumns();
    if (rows.length == 0) {
        $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
        return;
    }
    $.modal.confirm("Xác nhận để xóa phần đã chọn" + rows.length + " dữ liệu?", function () {
        var data = { "roleId": $("#roleId").val(), "userIds": rows.join() };
        $.operate.submit(PREFIX + "/cancelAll", "post", "json", data);
    });
}

function cancelAuthUser(userId) {
    $.modal.confirm("Bạn có chắc chắn muốn hủy vai trò người dùng này không?", function () {
        $.operate.post(PREFIX + "/cancel", { "roleId": $("#roleId").val(), "userId": userId });
    })
}