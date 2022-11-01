const PREFIX = ctx + "business/hddv";

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
    updateModeUrl: PREFIX + "/update-mode",
    checkModifyUrl: PREFIX + "/check-modify",
    retryUrl: PREFIX + "/retry",
    modalName: "Hoá đơn",
    sortName: "createTime",
    sortOrder: "desc",
    pageSize: 1000,
    pageList: [1000],
    columns: [
      {
        checkbox: true,
      },
      {
        field: 'fastId',
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
        field: 'processStatus',
        title: 'Trạng thái',
        align: 'center',
        formatter: function (value, row, index) {
          return processStatusFormat(row.processStatus);
        }
      },
      {
        field: 'invoiceDate',
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
            row.fastId +
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
  if (mode == 1) {
    return '<span class="label label-success">Đã điều chỉnh</span>';
  } else {
    return '<span class="label label-default">Gốc</span>';
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

function checkModify() {
  table.set();
  var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
  if (rows.length > 1) {
    $.modal.alertWarning("Vui lòng chỉ chọn 1 hoá đơn cần kiểm tra điều chỉnh!");
    return;
  }
  let url = table.options.checkModifyUrl + "/" + rows.join();
  var options = {
    title: 'Kiểm tra điều chỉnh',
    width: '1250',
    height: '500',
    url: url,
    skin: 'layui-layer-gray',
    btn: ['Đóng'],
    yes: function (index, layero) {
      layer.close(index);
    }

  };
  $.modal.openOptions(options);
}

function updateMode() {
  table.set();
  var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
  if (rows.length == 0) {
    $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
    return;
  }
  $.modal.confirm("Xác nhận cập nhật " + rows.length + " bản ghi đã chọn ?", function () {
    let dataRequest = {
      ids: rows.join()
    };
    $.ajax({
      type: "POST",
      url: table.options.updateModeUrl,
      contentType: "application/json; charset=utf-8",
      dataType: "json",
      data: JSON.stringify(dataRequest),
      beforeSend: function () {
        $.modal.loading("Đang xử lý, vui lòng đợi...");
      },
      error: function (request) {
        $.modal.closeLoading();
        $.modal.alertError("System error!");
      },
      success: function (data) {
        if (data == null) {
          return;
        }
        if (data.code == 0) {
          $.modal.closeLoading();
          $.modal.close();
          parent.$.modal.alertSuccess("Cập nhật thành công!");
          $.table.refresh();
        } else {
          $.modal.closeLoading();
          $.modal.alertError("System error! \r\n " + data.msg);
        }
      },
    })
  });
}