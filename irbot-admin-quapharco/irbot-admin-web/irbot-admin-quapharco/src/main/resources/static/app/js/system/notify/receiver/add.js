const PREFIX = ctx + "system/notify/receiver";
$("#form-receiver-add").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/add", $('#form-receiver-add').serialize());
    }
}

$("input[name='seenTime']").datetimepicker({
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true
});