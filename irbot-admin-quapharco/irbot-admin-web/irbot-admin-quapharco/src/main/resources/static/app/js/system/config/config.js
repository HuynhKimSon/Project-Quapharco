const PREFIX = ctx + "system/config";
var screenHeight = $(document).height(); // Height of screen

$(function () {
    var options = {	
        url: PREFIX + "/list",
        createUrl: PREFIX + "/add",
        updateUrl: PREFIX + "/edit/{id}",
        removeUrl: PREFIX + "/remove",
        exportUrl: PREFIX + "/export",
        sortName: "configId",
        sortOrder: "asc",
        modalName: "tham số",
		height: screenHeight - SEARCH_HEIGHT,
        columns: [{
            checkbox: true
        },
        {
            field: 'configId',
            title: 'ID',
            sortable: true,
			visible: false
        },
		{
			title: "STT",
			sortable: true,
			formatter: function (value, row, index) {
				return $.table.serialNumber(index);
			}		
		},
        {
            field: 'configName',
            title: 'Tên config',
            sortable: true,
            formatter: function (value, row, index) {
                return $.table.tooltip(value);
            }
        },
        {
            field: 'configKey',
            title: 'Mã config',
            sortable: true,
            formatter: function (value, row, index) {
                return $.table.tooltip(value);
            }
        },
        {
            field: 'configValue',
            title: 'Giá trị config',
            sortable: true,
        },
        {
            field: 'configType',
            title: 'Hệ thống tích hợp',
            align: 'center',
            sortable: true,
            formatter: function (value, row, index) {
                return $.table.selectDictLabel(datas, value);
            }
        },
        {
            field: 'remark',
            title: 'Ghi chú',
            align: 'center',
            sortable: true,
            formatter: function (value, row, index) {
                return $.table.tooltip(value, 10, "open");
            }
        },
        {
            field: 'createTime',
            title: 'Ngày tạo',
            sortable: true,
        },
        {
            title: 'Hành động',
            align: 'center',
            formatter: function (value, row, index) {
                var actions = [];
                actions.push('<a class="btn btn-success btn-xs ' + editFlag + '" href="javascript:void(0)" onclick="$.operate.edit(\'' + row.configId + '\')"><i class="fa fa-edit"></i>Sửa</a> ');
                actions.push('<a class="btn btn-danger btn-xs ' + removeFlag + '" href="javascript:void(0)" onclick="$.operate.remove(\'' + row.configId + '\')"><i class="fa fa-remove"></i>Xóa</a>');
                return actions.join('');
            }
        }]
    };
    $.table.init(options);
});

/** Refresh cache */
function refreshCache() {
    $.operate.get(PREFIX + "/refreshCache");
}