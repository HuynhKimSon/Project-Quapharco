const PREFIX = ctx + "system/role/authUser";

$(function () {
    var options = {
        url: PREFIX + "/unallocatedList",
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
        showSearch: false,
        showRefresh: false,
        showToggle: false,
        showColumns: false,
        clickToSelect: true,
        rememberSelected: true,
        columns: [{
            field: 'state',
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
        }]
    };
    $.table.init(options);
});

function queryParams(params) {
    var search = $.table.queryParams(params);
    search.roleId = $("#roleId").val();
    return search;
}

function submitHandler() {
    var rows = $.table.selectFirstColumns();
    if (rows.length == 0) {
        $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
        return;
    }
    var data = { "roleId": $("#roleId").val(), "userIds": rows.join() };
    $.operate.save(PREFIX + "/selectAll", data);
}