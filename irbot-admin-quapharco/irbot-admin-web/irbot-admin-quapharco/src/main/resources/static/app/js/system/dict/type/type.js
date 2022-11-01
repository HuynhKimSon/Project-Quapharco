const PREFIX = ctx + "system/dict";
var screenHeight = $(document).height(); // Height of screen

$(function () {
  var options = {
    url: PREFIX + "/list",
    createUrl: PREFIX + "/add",
    updateUrl: PREFIX + "/edit/{id}",
    removeUrl: PREFIX + "/remove",
    exportUrl: PREFIX + "/export",
    sortName: "dictId",
    sortOrder: "asc",
    modalName: "các loại",
    height: screenHeight - SEARCH_HEIGHT,
    columns: [
      {
        checkbox: true,
      },
      {
        field: "dictId",
        title: "ID",
        sortable: true,
        visible: false,
      },
      {
        title: "STT",
        sortable: true,
        formatter: function (value, row, index) {
          return $.table.serialNumber(index);
        },
      },
      {
        field: "dictName",
        title: "Tên từ điển",
        sortable: true,
      },
      {
        field: "dictType",
        title: "Loại từ điển",
        sortable: true,
        formatter: function (value, row, index) {
          return (
            '<a href="javascript:void(0)" onclick="detail(\'' +
            row.dictId +
            "')\">" +
            value +
            "</a>"
          );
        },
      },
      {
        field: "status",
        title: "Trạng thái",
        align: "center",
        sortable: true,
        formatter: function (value, row, index) {
          return $.table.selectDictLabel(datas, value);
        },
      },
      {
        field: "remark",
        title: "Ghi chú",
        sortable: true,
        formatter: function (value, row, index) {
          return $.table.tooltip(value);
        },
      },
      {
        field: "createTime",
        title: "Ngày tạo",
        sortable: true,
      },
      {
        title: "Hành động",
        align: "center",
        formatter: function (value, row, index) {
          var actions = [];
          actions.push(
            '<a class="btn btn-success btn-xs ' +
              editFlag +
              '" href="javascript:void(0)" onclick="$.operate.edit(\'' +
              row.dictId +
              '\')"><i class="fa fa-edit"></i>Sửa</a> '
          );
          actions.push(
            '<a class="btn btn-info btn-xs ' +
              listFlag +
              '" href="javascript:void(0)" onclick="detail(\'' +
              row.dictId +
              '\')"><i class="fa fa-list-ul"></i>Danh sách</a> '
          );
          actions.push(
            '<a class="btn btn-danger btn-xs ' +
              removeFlag +
              '" href="javascript:void(0)" onclick="$.operate.remove(\'' +
              row.dictId +
              '\')"><i class="fa fa-remove"></i>Xóa</a>'
          );
          return actions.join("");
        },
      },
    ],
  };
  $.table.init(options);
});

function detail(dictId) {
  var url = PREFIX + "/detail/" + dictId;
  $.modal.openTab("Dữ liệu từ điển", url);
}

function refreshCache() {
  $.operate.get(PREFIX + "/refreshCache");
}
