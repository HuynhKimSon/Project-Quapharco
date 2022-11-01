const PREFIX = ctx + "business/hdbh-cn-modify";

function submitHandler() {
  var rows = $.common.isEmpty(parent.table.options.uniqueId) ? parent.$.table.selectFirstColumns() : parent.$.table.selectColumns(parent.table.options.uniqueId);
  let dataRequest = {
    ids: rows.join(),
    status: $("select[name='status']").val()
  }
  if ($.validate.form()) {
    $.ajax({
      type: "POST",
      url: PREFIX + "/update-status",
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
          parent.$.table.refresh();
        } else {
          $.modal.closeLoading();
          $.modal.alertError("System error! \r\n " + data.msg);
        }
      },
    });
  }
}