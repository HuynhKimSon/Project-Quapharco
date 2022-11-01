const PREFIX = ctx + "system/menu";
var screenHeight = $(document).height(); // Height of screen
var searchHeight = 222;

$(function () {
  var options = {
    code: "menuId",
    parentCode: "parentId",
    uniqueId: "menuId",
    expandAll: false,
    expandFirst: false,
    url: PREFIX + "/list",
    createUrl: PREFIX + "/add/{id}",
    updateUrl: PREFIX + "/edit/{id}",
    removeUrl: PREFIX + "/remove/{id}",
    modalName: "Menu",
    height: screenHeight - searchHeight,
    columns: [
      {
        field: "selectItem",
        radio: true,
        width: "10",
        widthUnit: "%",
      },
      {
        title: "Tên menu",
        field: "menuName",
        width: "50",
        widthUnit: "%",
        formatter: function (value, row, index) {
          if ($.common.isEmpty(row.icon)) {
            return row.menuName;
          } else {
            return (
              '<i class="' +
              row.icon +
              '"></i> <span class="nav-label">' +
              row.menuName +
              "</span>"
            );
          }
        },
      },
      {
        field: "orderNum",
        title: "Sắp xếp",
        width: "20",
        widthUnit: "%",
        align: "left",
      },
      {
        field: "url",
        title: "Đường dẫn",
        width: "25",
        widthUnit: "%",
        align: "left",
        formatter: function (value, row, index) {
          return $.table.tooltip(value);
        },
      },
      {
        title: "Loại",
        field: "menuType",
        width: "20",
        widthUnit: "%",
        align: "left",
        formatter: function (value, item, index) {
          if (item.menuType == "M") {
            return '<span class="label label-success">Mục lục</span>';
          } else if (item.menuType == "C") {
            return '<span class="label label-primary">Menu</span>';
          } else if (item.menuType == "F") {
            return '<span class="label label-warning">Nút</span>';
          }
        },
      },
      {
        field: "visible",
        title: "Hiện ra",
        width: "20",
        widthUnit: "%",
        align: "left",
        formatter: function (value, row, index) {
          if (row.menuType == "F") {
            return "-";
          }
          return $.table.selectDictLabel(datas, row.visible);
        },
      },
      {
        field: "perms",
        title: "ID cơ quan",
        width: "25",
        widthUnit: "%",
        align: "left",
        formatter: function (value, row, index) {
          return $.table.tooltip(value);
        },
      },
      {
        title: "Hành động",
        width: "30",
        widthUnit: "%",
        align: "left",
        formatter: function (value, row, index) {
          var actions = [];
          actions.push(
            '<a class="btn btn-success btn-xs ' +
              editFlag +
              '" href="javascript:void(0)" onclick="$.operate.edit(\'' +
              row.menuId +
              '\')"><i class="fa fa-edit"></i>Sửa</a> '
          );
          actions.push(
            '<a class="btn btn-info btn-xs ' +
              addFlag +
              '" href="javascript:void(0)" onclick="$.operate.add(\'' +
              row.menuId +
              '\')"><i class="fa fa-plus"></i>Thêm</a> '
          );
          actions.push(
            '<a class="btn btn-danger btn-xs ' +
              removeFlag +
              '" href="javascript:void(0)" onclick="$.operate.remove(\'' +
              row.menuId +
              '\')"><i class="fa fa-trash"></i>Xóa</a>'
          );
          return actions.join("");
        },
      },
    ],
  };
  $.treeTable.init(options);
});
