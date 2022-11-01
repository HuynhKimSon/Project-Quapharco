const PREFIX = ctx + "system/notify/token"
$("#form-token-add").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/add", $('#form-token-add').serialize());
    }
}

$("input[name='expireTime']").datetimepicker({
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true
});