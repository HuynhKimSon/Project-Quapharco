var PREFIX = ctx + "system/notify/receiver";
$("#form-receiver-edit").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/edit", $('#form-receiver-edit').serialize());
    }
}

$("input[name='seenTime']").datetimepicker({
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true
});