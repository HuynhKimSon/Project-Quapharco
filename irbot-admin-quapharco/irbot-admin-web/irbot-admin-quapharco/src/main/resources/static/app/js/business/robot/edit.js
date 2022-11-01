const PREFIX = ctx + "business/robot";

function edit() {
  // Get status
  let status;
  if ($("#offlineRadio").prop("checked")) {
    status = "0";
  } else if ($("#busyRadio").prop("checked")) {
    status = "1";
  } else {
    status = "2";
  }

  // $.operate.save(PREFIX + "/add", $("#formAddRobot").serialize());
  let services = [];
  if ($("input[name='cbService1']").is(":checked") == true) {
    // Đồng bộ hệ thống giám sát lưu chuyển than
    services.push(100);
  }
  
  let data = {
    id: robot.id,
    ipAddress: $("input[name='ipAddress']").val(),
    status: status,
    services: services,
    remark: $("textarea[name='remark']").val()
  };


  $.ajax({
    cache: true,
    type: "POST",
    url: PREFIX + "/edit",
    data: JSON.stringify(data),
    contentType: "application/json; charset=utf-8",
    dataType : "json",
    async: false,
    error: function (request) {
      $.modal.alertError("System error!");
    },
    success: function (data) {
      $.operate.successCallback(data);
    },
  });

}

function submitHandler() {
  if ($.validate.form()) {
    edit();
  }
}
