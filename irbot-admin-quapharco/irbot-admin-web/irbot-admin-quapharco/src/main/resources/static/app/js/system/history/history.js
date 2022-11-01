const PREFIX = ctx + "system/history";

$(function () {
    var options = {
        url: PREFIX + "/list",
        createUrl: PREFIX + "/add",
        updateUrl: PREFIX + "/edit/{id}",
        removeUrl: PREFIX + "/remove",
        exportUrl: PREFIX + "/export",
        modalName: "Lịch sử",
        columns: [{
            checkbox: true
        }, {
            field: 'ediId',
            title: '${comment}'
        }, {
            field: 'validtoDay',
            title: '${comment}'
        }, {
            field: 'emptycontDepot',
            title: '${comment}'
        }, {
            field: 'haulage',
            title: '${comment}'
        }, {
            title: 'Action',
            align: 'center',
            formatter: function (value, row, index) {
                var actions = [];
                actions.push('<a class="btn btn-success btn-xs ' + editFlag + '" href="javascript:void(0)" onclick="$.operate.edit(\'' + row.ediId + '\')"><i class="fa fa-edit"></i>Edit</a> ');
                actions.push('<a class="btn btn-danger btn-xs ' + removeFlag + '" href="javascript:void(0)" onclick="$.operate.remove(\'' + row.ediId + '\')"><i class="fa fa-remove"></i>Delete</a>');
                return actions.join('');
            }
        }]
    };
    $.table.init(options);
});