const PREFIX = ctx + "system/notify";

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
            field: 'title',
            title: 'Title'
        },
        {
            field: 'content',
            title: 'Content'
        },
        {
            field: 'notifyLevel',
            title: 'Level',
            formatter: function (value, row, index) {
                return $.table.selectDictLabel(notifyLevelDatas, value);
            }
        },
        {
            field: 'notifyLink',
            title: 'Link'
        },
        {
            field: 'status',
            title: 'Status',
            formatter: function (value, row, index) {
                return $.table.selectDictLabel(statusDatas, value);
            }
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