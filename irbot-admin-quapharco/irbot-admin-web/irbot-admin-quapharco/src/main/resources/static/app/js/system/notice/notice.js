const PREFIX = ctx + "system/notice";
var screenHeight = $(document).height(); // Height of screen

$(function () {
    var options = {
        url: PREFIX + "/list",
        createUrl: PREFIX + "/add",
        updateUrl: PREFIX + "/edit/{id}",
        removeUrl: PREFIX + "/remove",
        modalName: "Thông báo",
		height: screenHeight - SEARCH_HEIGHT,
        columns: [{
            checkbox: true
        },
        {
            field: 'noticeId',
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
            field: 'noticeTitle',
            title: 'Tiêu đề',
            sortable: true,
        },
        {
            field: 'noticeType',
            title: 'Loại',
            align: 'center',
            sortable: true,
            formatter: function (value, row, index) {
                return $.table.selectDictLabel(types, value);
            }
        },
        {
            field: 'status',
            title: 'Trạng thái',
            align: 'center',
            sortable: true,
            formatter: function (value, row, index) {
                return $.table.selectDictLabel(datas, value);
            }
        },
        {
            field: 'createBy',
            title: 'Tạo bởi',
            sortable: true,
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
                actions.push('<a class="btn btn-success btn-xs ' + editFlag + '" href="javascript:void(0)" onclick="$.operate.editFull(\'' + row.noticeId + '\')"><i class="fa fa-edit"></i>Sửa</a> ');
                actions.push('<a class="btn btn-danger btn-xs ' + removeFlag + '" href="javascript:void(0)" onclick="$.operate.remove(\'' + row.noticeId + '\')"><i class="fa fa-remove"></i>Xóa</a>');
                return actions.join('');
            }
        }]
    };
    $.table.init(options);
});