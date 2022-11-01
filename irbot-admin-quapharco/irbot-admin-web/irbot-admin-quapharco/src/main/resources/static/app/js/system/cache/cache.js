const PREFIX = ctx + "system/cache";

$(function () {
    var options = {
        url: PREFIX + "/list",
        createUrl: PREFIX + "/add",
        modalName: "cache",
        pagination: false,
        columns: [{
            field: 'id',
            title: 'ID'
        },
        {
            field: 'key',
            title: 'Key'
        },
        {
            field: 'value',
            title: 'Value',
            formatter: function (value, row, index) {
                return $.table.tooltip(value);
            }
        },
        {
            title: 'Hành động',
            align: 'center',
            formatter: function (value, row, index) {
                var actions = [];
                actions.push('<a class="btn btn-danger btn-xs" href="javascript:void(0)" onclick="remove(\'' + row.key + '\',\'' + row.keyName + '\')"><i class="fa fa-remove"></i>Delete</a>');
                return actions.join('');
            }
        }]
    };
    $.table.init(options);
});

function removeAll() {
    layer.confirm("Xác nhận xóa toàn bộ cache.", {
        icon: 3,
        title: "Xác Nhận",
        btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {
        $.modal.loading("Đang xử lý ...");
        layer.close(layer.index);
        $.ajax({
            url: PREFIX + "/remove-all",
            method: "delete",
            success: function (res) {
                $.modal.closeLoading();
                if (res.code == 0) {
                    $.modal.alertSuccess(res.msg);
                    $.table.search();
                } else {
                    $.modal.alertError(res.msg);
                }
            },
            error: function (data) {
                $.modal.closeLoading();
            }
        });
    }, function () {
        // close form
    });
}

function remove(key, keyName) {
    layer.confirm("Xác nhận xóa cache này.", {
        icon: 3,
        title: "Xác Nhận",
        btn: ['Đồng Ý', 'Hủy Bỏ']
    }, function () {
        $.modal.loading("Đang xử lý ...");
        layer.close(layer.index);
        $.ajax({
            url: PREFIX + "/" + keyName + "/" + key + "/remove",
            method: "delete",
            success: function (res) {
                $.modal.closeLoading();
                if (res.code == 0) {
                    $.modal.alertSuccess(res.msg);
                    $.table.search();
                } else {
                    $.modal.alertError(res.msg);
                }
            },
            error: function (data) {
                $.modal.closeLoading();
            }
        });
    }, function () {
        // close form
    });
}