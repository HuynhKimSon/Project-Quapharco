const PREFIX = ctx + "business/hddv-modify";

let date = new Date();
let firstDay = lastDay = date;
$("input[name='params[beginTime]']").val(parseTime(firstDay, "{y}-{m}-{d}"));
$("input[name='params[endTime]']").val(parseTime(lastDay, "{y}-{m}-{d}"));
$("input[name='params[fastInvoiceNo]']").val();

// Init screen
$(document).ready(function () {
  loadList();

  $("input").keyup(function (event) {
    if (event.keyCode == 13) {
      $.table.search();
      event.preventDefault();
    }
  });
});

function loadList() {
  const OPTIONS = {
    url: PREFIX + "/list",
    detailUrl: PREFIX + "/detail/{id}",
    updateStatusUrl: PREFIX + "/update-status",
    retryUrl: PREFIX + "/retry",
    modalName: "Hoá đơn",
    sortName: "createTime",
    sortOrder: "desc",
    pageSize: 20,
    pageList: [20, 50, 100, 200],
    columns: [
      {
        checkbox: true,
      },
      {
        field: 'id',
        title: 'Id',
        visible: false
      },
      {
        field: 'fastId',
        title: 'Khoá đồng bộ',
        visible: false
      },
      {
        field: 'fastInvoiceNo',
        title: 'Số chứng từ'
      },
      {
        field: 'mode',
        sortable: true,
        title: 'Loại phiếu',
        align: 'center',
        formatter: function (value, row, index) {
          return modeFormatter(row.mode);
        }
      },
      {
        field: 'status',
        title: 'Trạng thái',
        align: 'center',
        formatter: function (value, row, index) {
          return processStatusFormat(row.status);
        }
      },
      {
        field: 'invoiceModifyDate',
        title: 'Ngày chứng từ'
      },
      {
        field: 'unitCode',
        title: 'Đơn vị',
      },
      {
        field: 'address',
        title: 'Địa chỉ',
        visible: false
      },
      {
        field: 'paymentMethod',
        title: 'Kiểu thanh toán',
        visible: false
      },
      {
        field: 'customerTaxCode',
        title: 'Mã số thuế',
      },
      {
        field: 'customerName',
        title: 'Tên khách hàng'
      },
      {
        field: 'subtotalAmount',
        title: 'Thành tiền',
        formatter: function (value, row, index) {
          return $.common.currencyFormat(row.subtotalAmount);
        }
      },
      {
        field: 'totalTaxAmount',
        title: 'Tổng tiền thuế',
        formatter: function (value, row, index) {
          return $.common.currencyFormat(row.totalTaxAmount);
        }
      },
      {
        field: 'totalAmount',
        title: 'Tổng tiền',
        formatter: function (value, row, index) {
          return $.common.currencyFormat(row.totalAmount);
        },
        align: 'right'
      },
      {
        field: 'eInvoiceNo',
        title: 'Số hoá đơn'
      },
      {
        title: "Hành động",
        align: "center",
        formatter: function (value, row, index) {
          var actions = [];
          actions.push(
            '<a class="btn btn-primary btn-xs ma2" href="javascript:void(0)" title="Chi tiết" onclick="$.operate.detail(\'' +
            row.id +
            '\')"><i class="fa fa-eye"  ></i>&nbsp;Chi tiết</a> '
          );
          actions.push(
            '<a class="btn btn-success btn-xs ma2" href="javascript:void(0)" title="Lịch sử làm lệnh" onclick="viewHistory(\'' +
            row.fastId +
            '\')"><i class="fa fa-history"></i>&nbsp;Lịch sử làm lệnh</a> '
          );
          return actions.join("");
        },
      },
    ],
  };
  $.table.init(OPTIONS);
}
function modeFormatter(mode) {
  if (mode == 2) {
    return '<span class="label label-success">Điều chỉnh tăng</span>';
  } else {
    return '<span class="label label-primary">Điều chỉnh giảm</span>';
  }
}

function processStatusFormat(status) {
  if (status == 1) {
    return '<span class="label label-warning"> Chờ thực hiện</span>'
  } else if (status == 2) {
    return '<span class="label label-warning"> Đã gửi</span>'
  } else if (status == 3) {
    return '<span  class="label label-success"> Đang làm</span>'
  } else if (status == 4) {
    return '<span class="label label-danger"> Thất bại</span>'
  } else if (status == 5) {
    return '<span class="label label-primary">Thành công</span>';
  } else {
    return '<span class="label label-default"> Chưa làm</span>';
  }
}

function viewHistory(id) {
  var _url = PREFIX + "/history/" + id;
  var options = {
    title: "Chi tiết làm lệnh",
    width: "900px",
    url: _url,
    skin: 'layui-layer-gray',
    btn: ['Đóng'],
    yes: function (index, layero) {
      layer.close(index);
    }
  };
  $.modal.openOptions(options);
}

function updateStatus() {
  table.set();
  var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
  if (rows.length == 0) {
    $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
    return;
  }
  $.modal.open("Cập nhật trạng thái", table.options.updateStatusUrl, '500', '380');
}

function retry() {
  table.set();
  var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
  if (rows.length == 0) {
    $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
    return;
  }
  $.modal.confirm("Xác nhận gửi robot làm lệnh " + rows.length + " bản ghi đã chọn ?", function () {
    var url = table.options.retryUrl;
    var data = { "ids": rows.join() };
    $.operate.submit(url, "post", "json", data);
  });
}

