const PREFIX = ctx + "system/dict/data";
var searchHeight = 122;
var screenHeight = $(document).height(); // Height of screen

$(function () {
    var options = {
        url: PREFIX + "/list",
        createUrl: PREFIX + "/add/{id}",
        updateUrl: PREFIX + "/edit/{id}",
        removeUrl: PREFIX + "/remove",
        exportUrl: PREFIX + "/export",
        queryParams: queryParams,
        sortName: "dictSort",
        sortOrder: "asc",
        modalName: "Dữ liệu",
		height: screenHeight - searchHeight,
        columns: [{
            checkbox: true
        },
        {
            field: 'dictCode',
            title: 'Mã từ điển',
            sortable: true
        },
        {
            field: 'dictLabel',
            title: 'Thẻ từ điển',
            sortable: true,
            formatter: function (value, row, index) {
                var listClass = $.common.equals("default", row.listClass) || $.common.isEmpty(row.listClass) ? "" : "badge badge-" + row.listClass;
                return $.common.sprintf("<span class='%s'>%s</span>", listClass, value);
            }
        },
        {
            field: 'dictValue',
            title: 'Khóa từ điển',
            sortable: true,
        },
        {
            field: 'dictSort',
            title: 'Sắp xếp từ điển',
            sortable: true,
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
            field: 'remark',
            title: 'Ghi chú',
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
                actions.push('<a class="btn btn-success btn-xs ' + editFlag + '" href="javascript:void(0)" onclick="$.operate.edit(\'' + row.dictCode + '\')"><i class="fa fa-edit"></i>Sửa</a> ');
                actions.push('<a class="btn btn-danger btn-xs ' + removeFlag + '" href="javascript:void(0)" onclick="$.operate.remove(\'' + row.dictCode + '\')"><i class="fa fa-remove"></i>Xóa</a>');
                return actions.join('');
            }
        }]
    };
    $.table.init(options);
});

function queryParams(params) {
    var search = $.table.queryParams(params);
    search.dictType = $("#dictType").val();
    return search;
}

function add() {
    var dictType = $("#dictType option:selected").val();
    $.operate.add(dictType);
}

function resetPre() {
    $.form.reset();
    $("#dictType").val($("#dictType").val()).trigger("change");
}