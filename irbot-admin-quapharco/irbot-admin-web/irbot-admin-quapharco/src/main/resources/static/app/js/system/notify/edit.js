const PREFIX = ctx + "system/notify";
$("#form-notify-edit").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/edit", $('#form-notify-edit').serialize());
    }
}