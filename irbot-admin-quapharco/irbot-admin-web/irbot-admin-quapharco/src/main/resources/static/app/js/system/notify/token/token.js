const PREFIX = ctx + "system/notify/token";

$(function () {
    var options = {
        url: PREFIX + "/list",
        createUrl: PREFIX + "/add",
        updateUrl: PREFIX + "/edit/{id}",
        removeUrl: PREFIX + "/remove",
        exportUrl: PREFIX + "/export",
        modalName: "Notify Token",
        columns: [{
            checkbox: true
        },
        {
            field: 'id',
            title: 'ID',
            visible: false
        },
        {
            field: 'userId',
            title: 'User ID'
        },
        {
            field: 'userType',
            title: 'User Type'
        },
        {
            field: 'userLoginToken',
            title: 'Login Token'
        },
        {
            field: 'deviceToken',
            title: 'Device Token'
        },
        {
            field: 'expireTime',
            title: 'Expired Time'
        },
        {
            field: 'expireFlg',
            title: 'Expired Flag'
        },
        {
            field: 'loginIp',
            title: 'Login IP'
        },
        {
            title: 'Action',
            align: 'center',
            formatter: function (value, row, index) {
                var actions = [];
                actions.push('<a class="btn btn-success btn-xs ' + editFlag + '" href="javascript:void(0)" onclick="$.operate.edit(\'' + row.id + '\')"><i class="fa fa-edit"></i>Edit</a> ');
                actions.push('<a class="btn btn-danger btn-xs ' + removeFlag + '" href="javascript:void(0)" onclick="$.operate.remove(\'' + row.id + '\')"><i class="fa fa-remove"></i>Delete</a>');
                return actions.join('');
            }
        }]
    };
    $.table.init(options);
});