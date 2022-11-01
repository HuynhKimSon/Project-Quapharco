const PREFIX = ctx + "system/dict/data";

$("#form-dict-add").validate({
    rules: {
        dictSort: {
            digits: true
        },
    },
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/add", $('#form-dict-add').serialize());
    }
}