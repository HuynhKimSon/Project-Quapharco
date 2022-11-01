const PREFIX = ctx + "system/notify";
$("#form-notify-add").validate({
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/add", $('#form-notify-add').serialize());
    }
}