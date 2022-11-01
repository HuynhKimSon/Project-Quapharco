const PREFIX = ctx + "system/dict/data";

$("#form-dict-edit").validate({
    rules: {
        dictSort: {
            digits: true
        },
    },
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/edit", $('#form-dict-edit').serialize());
    }
}