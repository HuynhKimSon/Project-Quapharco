const PREFIX = ctx + "system/notify/token";
$("#form-token-edit").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/edit", $('#form-token-edit').serialize());
    }
}

$("input[name='expireTime']").datetimepicker({
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true
});