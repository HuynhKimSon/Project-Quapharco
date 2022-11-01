const PREFIX = ctx + "system/notify/receiver";

$(function () {
    var options = {
        url: PREFIX + "/list",
        createUrl: PREFIX + "/add",
        updateUrl: PREFIX + "/edit/{id}",
        removeUrl: PREFIX + "/remove",
        exportUrl: PREFIX + "/export",
        modalName: "Notification",
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
            field: 'notificationId',
            title: 'Notify ID'
        },
        {
            field: 'notificationType',
            title: 'Notify Type'
        },
        {
            field: 'seenFlg',
            title: 'Seen Flag'
        },
        {
            field: 'seenTime',
            title: 'Seen Time'
        },
        {
            field: 'sentFlg',
            title: 'Sent Flag'
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