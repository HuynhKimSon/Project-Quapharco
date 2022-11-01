const PREFIX = ctx + "business/robot";

// Init screen
$(document).ready(function () {

  $("#btnAdd").addClass(addFlag);

  loadListRobot();

  $("input").keyup(function (event) {
    if (event.keyCode == 13) {
      $.table.search();
      event.preventDefault();
    }
  });

});

function loadListRobot() {
  const OPTIONS = {
    url: PREFIX + "/list",
    createUrl: PREFIX + "/add",
    updateUrl: PREFIX + "/edit/{id}",
    removeUrl: PREFIX + "/remove",
    modalName: "Robot",
    height: $(document).height() - 105,
    pageSize: 25,
    columns: [
      {
        checkbox: true,
      },
      {
        field: 'id',
        title: "Id",
        visible: false,
      },
      {
        field: "uuid",
        title: "UUID",
        sortable: true
      },
      {
        field: "ipAddress",
        title: "Địa chỉ IP",
        sortable: true,
      },
      {
        field: "status",
        title: "Trạng thái",
        align: "left",
        sortable: true,
        formatter: function (value, row, index) {
          return statusFormatter(value, row, index);
        },
      },
      {
        field: 'disabled',
        title: 'Disabled',
        align: 'center',
        sortable: true,
        formatter: function (value, row, index) {
          return disabledFormatter(value, row, index);
        }
      },
      {
        field: "robotService",
        title: "Chức năng",
        align: "left",
        formatter: function (value, row, index) {
          return robotServiceFormater(value, row, index);
        },
      },
      {
        field: 'pingTime',
        title: "Ping"
      },
      {
        title: "Hành động",
        align: "center",
        formatter: function (value, row, index) {
          var actions = [];
          actions.push(
            '<a class="btn btn-success btn-xs ' + editFlag + '" href="javascript:void(0)" onclick="$.operate.edit(\'' +
            row.id +
            '\')"><i class="fa fa-edit"  ></i></a> '
          );
          actions.push(
            '<a class="btn btn-danger btn-xs ' + removeFlag + '" href="javascript:void(0)" onclick="$.operate.remove(\'' +
            row.id +
            '\')"><i class="fa fa-remove"></i></a> '
          );
          return actions.join("");
        },
      },
    ],
  };
  $.table.init(OPTIONS);
}

/* formatter for status column */
function statusFormatter(value, row, index) {
  if (row.status == 0) {
    return '<span class="text-danger"><i class="fa fa-circle"></i> Offline</span>';
  } else if (row.status == 1) {
    return '<span class="text-warning"><i class="fa fa-circle"></i> Busy</span>';
  } else {
    return '<span class="text-info"><i class="fa fa-circle"></i> Available</span>';
  }
}

/* formatter for disabled status */
function disabledFormatter(value, row, index) {
  if (row.disabled == 0) {
    return '<i class=\"fa fa-toggle-off text-info fa-2x\" onclick="disable(\'' + row.id + '\',\'' + row.uuid + '\')"></i> ';
  } else {
    return '<i class=\"fa fa-toggle-on text-info fa-2x\" style="color: #ed5565;" onclick="enable(\'' + row.id + '\',\'' + row.uuId + '\')"></i> ';
  }
}

/* formatter for ReceiveContFullOrder column */
function robotServiceFormater(value, row, index) {
  let content = '';
  if (row.services && Array.isArray(row.services)){
    row.services.forEach(s => {
      content += '<span class="badge badge-primary">' + s.name +'</span><span> </span>';
    })
  }
  return content;
}

/* disable robot */
function disable(id, uuid) {
  $.modal.confirm("Xác nhận vô hiệu hóa robot " + uuid + "？", function () {
    $.operate.post(PREFIX + "/disabled/change", { "id": id, "disabled": 1 });
  })
}

/* enable robot */
function enable(id, uuid) {
  $.modal.confirm("Xác nhận kích hoạt lại robot " + uuid + "？", function () {
    $.operate.post(PREFIX + "/disabled/change", { "id": id, "disabled": 0 });
  })
}

function openHistory(robotId) {
  $.modal.alertWarning('Coming soon...');
}

