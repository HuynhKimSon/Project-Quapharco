const PREFIX = ctx + "system/history"
$("#form-history-add").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/add", $('#form-history-add').serialize());
    }
}

$("input[name='validtoDay']").datetimepicker({
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true
});