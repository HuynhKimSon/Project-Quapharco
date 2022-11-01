const PREFIX = ctx + "system/history";
$("#form-history-edit").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/edit", $('#form-history-edit').serialize());
    }
}

$("input[name='validtoDay']").datetimepicker({
    format: "yyyy-mm-dd",
    minView: "month",
    autoclose: true
});