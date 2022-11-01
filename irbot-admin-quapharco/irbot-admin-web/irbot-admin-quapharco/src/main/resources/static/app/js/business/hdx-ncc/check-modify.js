const PREFIX = ctx + "business/hdx-ncc";
// Init screen
$(document).ready(function () {
  if (result.msg != null) {
    $.modal.close();
    parent.$.modal.alertError("System error! \r\n " + result.msg);
  }
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
    data: result,
    modalName: "Kiểm tra điều chỉnh",
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
        field: 'invoiceDate',
        title: 'Ngày chứng từ'
      },
      {
        field: 'invoiceModifyDate',
        title: 'Ngày điều chỉnh'
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
        field: 'totalQuantity',
        title: 'Tổng số lượng',
        visible: false
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
            '<a class="btn btn-primary btn-xs ma2" href="javascript:void(0)" title="Chi tiết Kiểm tra điều chỉnh" onclick="viewModifyDetail(\'' + row.fastId + '\' ,' +
            index +
            ')"><i class="fa fa-eye"  ></i>&nbsp;Chi tiết</a> '
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

function viewModifyDetail(fastId, id) {
  var url = PREFIX + "/view-modify-detail/" + fastId + "/" + id;
  var options = {
    title: "Chi tiết điều chỉnh",
    width: "900px",
    url: url,
    skin: 'layui-layer-gray',
    btn: ['Đóng'],
    yes: function (index, layero) {
      layer.close(index);
    }
  };
  $.modal.openOptions(options);
}

function confirmModify() {
  table.set();
  var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
  if (rows.length == 0) {
    $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
    return;
  }
  $.modal.confirm("Xác nhận điều chỉnh " + rows.length + " bản ghi đã chọn ?", function () {
    let dataRequest = { "fastIds": rows.join() };
    $.ajax({
      type: "POST",
      url: PREFIX + "/confirm-modify",
      dataType: "json",
      data: dataRequest,
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
          parent.$.modal.alertSuccess("Thành công!");
          parent.$.table.refresh();
        } else {
          $.modal.closeLoading();
          $.modal.alertError("System error! \r\n " + data.msg);
        }
      },
    })
  });
}
