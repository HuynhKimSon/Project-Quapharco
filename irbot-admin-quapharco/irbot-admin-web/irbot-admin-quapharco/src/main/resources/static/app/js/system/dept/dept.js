const PREFIX = ctx + "system/dept"
var screenHeight = $(document).height(); // Height of screen
var searchHeight = 222;

$(function () {
    var options = {
        code: "deptId",
        parentCode: "parentId",
        uniqueId: "deptId",
        url: PREFIX + "/list",
        createUrl: PREFIX + "/add/{id}",
        updateUrl: PREFIX + "/edit/{id}",
        removeUrl: PREFIX + "/remove/{id}",
        modalName: "Phòng ban",
		height: screenHeight - searchHeight,
        columns: [{
            field: 'selectItem',
            radio: true
        },
        {
            field: 'deptName',
            title: 'Tên bộ phận',
            align: "left"
        },
        {
            field: 'orderNum',
            title: 'Sắp xếp',
            align: "left"
        },
        {
            field: 'status',
            title: 'Trạng thái',
            align: "left",
            formatter: function (value, item, index) {
                return $.table.selectDictLabel(datas, item.status);
            }
        },
        {
            field: 'createTime',
            title: 'Ngày tạo',
            align: "left"
        },
        {
            title: 'Hành động',
            align: 'left',
            formatter: function (value, row, index) {
                if (row.parentId != 0) {
                    var actions = [];
                    actions.push('<a class="btn btn-success btn-xs ' + editFlag + '" href="javascript:void(0)" onclick="$.operate.edit(\'' + row.deptId + '\')"><i class="fa fa-edit"></i>Sửa</a> ');
                    actions.push('<a class="btn btn-info  btn-xs ' + addFlag + '" href="javascript:void(0)" onclick="$.operate.add(\'' + row.deptId + '\')"><i class="fa fa-plus"></i>Thêm</a> ');
                    actions.push('<a class="btn btn-danger btn-xs ' + removeFlag + '" href="javascript:void(0)" onclick="$.operate.remove(\'' + row.deptId + '\')"><i class="fa fa-trash"></i>Xóa</a>');
                    return actions.join('');
                } else {
                    return "";
                }
            }
        }]
    };
    $.treeTable.init(options);
});