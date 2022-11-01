const PREFIX = ctx + "system/post";
var screenHeight = $(document).height(); // Height of screen

$(function () {
    var options = {
        url: PREFIX + "/list",
        createUrl: PREFIX + "/add",
        updateUrl: PREFIX + "/edit/{id}",
        removeUrl: PREFIX + "/remove",
        exportUrl: PREFIX + "/export",
        sortName: "postSort",
        modalName: "Bài đăng",
		height: screenHeight - SEARCH_HEIGHT,
        columns: [{
            checkbox: true
        },
        {
            field: 'postId',
            title: 'ID',
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
            field: 'postCode',
            title: 'Mã chức danh',
            sortable: true
        },
        {
            field: 'postName',
            title: 'Chức danh',
            sortable: true
        },
        {
            field: 'postSort',
            title: 'Sắp xếp',
            sortable: true
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
            field: 'createTime',
            title: 'Ngày tạo',
            sortable: true
        },
        {
            title: 'Hành động',
            align: 'center',
            formatter: function (value, row, index) {
                var actions = [];
                actions.push('<a class="btn btn-success btn-xs ' + editFlag + '" href="javascript:void(0)" onclick="$.operate.edit(\'' + row.postId + '\')"><i class="fa fa-edit"></i>Sửa</a> ');
                actions.push('<a class="btn btn-danger btn-xs ' + removeFlag + '" href="javascript:void(0)" onclick="$.operate.remove(\'' + row.postId + '\')"><i class="fa fa-remove"></i>Xóa</a>');
                return actions.join('');
            }
        }]
    };
    $.table.init(options);
});