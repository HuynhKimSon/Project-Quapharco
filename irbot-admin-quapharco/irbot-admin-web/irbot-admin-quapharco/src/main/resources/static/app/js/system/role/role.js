const PREFIX = ctx + "system/role";
var screenHeight = $(document).height(); // Height of screen

$(function () {
  var options = {
    url: PREFIX + "/list",
    createUrl: PREFIX + "/add",
    updateUrl: PREFIX + "/edit/{id}",
    removeUrl: PREFIX + "/remove",
    exportUrl: PREFIX + "/export",
    sortName: "roleSort",
    modalName: "Vai trò",
    height: screenHeight - SEARCH_HEIGHT,
    columns: [
      {
        checkbox: true,
      },
      {
        field: "roleId",
        title: "ID",
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
        field: "roleName",
        title: "Tên vai trò",
        sortable: true,
      },
      {
        field: "roleKey",
        title: "Mã vai trò",
        sortable: true,
      },
      {
        field: "roleSort",
        title: "Sắp xếp",
        sortable: true,
      },
      {
        visible: editFlag == "hidden" ? false : true,
        title: "Tình trạng",
        align: "center",
        sortable: true,
        formatter: function (value, row, index) {
          return statusTools(row);
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
              row.roleId +
              '\')"><i class="fa fa-edit"></i>Sửa</a> '
          );
          actions.push(
            '<a class="btn btn-danger btn-xs ' +
              removeFlag +
              '" href="javascript:void(0)" onclick="$.operate.remove(\'' +
              row.roleId +
              '\')"><i class="fa fa-remove"></i>Xóa</a> '
          );
          var more = [];
          more.push(
            "<a class='btn btn-default btn-xs " +
              editFlag +
              "' href='javascript:void(0)' onclick='authDataScope(" +
              row.roleId +
              ")'><i class='fa fa-check-square-o'></i>Quyền dữ liệu</a> "
          );
          more.push(
            "<a class='btn btn-default btn-xs " +
              editFlag +
              "' href='javascript:void(0)' onclick='authUser(" +
              row.roleId +
              ")'><i class='fa fa-user'></i>Chỉ định người dùng</a>"
          );
          actions.push(
            '<a tabindex="0" class="btn btn-info btn-xs" role="button" data-container="body" data-placement="left" data-toggle="popover" data-html="true" data-trigger="hover" data-content="' +
              more.join("") +
              '"><i class="fa fa-chevron-circle-right"></i>Nhiều hơn</a>'
          );
          return actions.join("");
        },
      },
    ],
  };
  $.table.init(options);
});

function authDataScope(roleId) {
  var url = PREFIX + "/authDataScope/" + roleId;
  $.modal.open("Chỉ định quyền dữ liệu", url);
}

function authUser(roleId) {
  var url = PREFIX + "/authUser/" + roleId;
  $.modal.openTab("Chỉ định người dùng", url);
}

function statusTools(row) {
  if (row.status == 1) {
    return (
      '<i class="fa fa-toggle-off text-info fa-2x" onclick="enable(\'' +
      row.roleId +
      "')\"></i> "
    );
  } else {
    return (
      '<i class="fa fa-toggle-on text-info fa-2x" onclick="disable(\'' +
      row.roleId +
      "')\"></i> "
    );
  }
}

function disable(roleId) {
  $.modal.confirm(
    "Bạn có chắc chắn muốn hủy kích hoạt vai trò này không?",
    function () {
      $.operate.post(PREFIX + "/changeStatus", { roleId: roleId, status: 1 });
    }
  );
}

function enable(roleId) {
  $.modal.confirm(
    "Bạn có chắc chắn muốn kích hoạt vai trò này không?",
    function () {
      $.operate.post(PREFIX + "/changeStatus", { roleId: roleId, status: 0 });
    }
  );
}
