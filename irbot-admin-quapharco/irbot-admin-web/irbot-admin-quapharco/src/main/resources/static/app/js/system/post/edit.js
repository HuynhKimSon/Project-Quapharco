const PREFIX = ctx + "system/post";

$("#form-post-edit").validate({
    onkeyup: false,
    rules: {
        postName: {
            remote: {
                url: ctx + "system/post/checkPostNameUnique",
                type: "post",
                dataType: "json",
                data: {
                    "postId": function () {
                        return $("input[name='postId']").val();
                    },
                    "postName": function () {
                        return $.common.trim($("#postName").val());
                    }
                },
                dataFilter: function (data, type) {
                    return $.validate.unique(data);
                }
            }
        },
        postCode: {
            remote: {
                url: ctx + "system/post/checkPostCodeUnique",
                type: "post",
                dataType: "json",
                data: {
                    "postId": function () {
                        return $("input[name='postId']").val();
                    },
                    "postCode": function () {
                        return $.common.trim($("#postCode").val());
                    }
                },
                dataFilter: function (data, type) {
                    return $.validate.unique(data);
                }
            }
        },
        postSort: {
            digits: true
        },
    },
    messages: {
        "postCode": {
            remote: "Mã chức danh đã tồn tại"
        },
        "postName": {
            remote: "Tên công việc đã tồn tại"
        }
    },
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/edit", $('#form-post-edit').serialize());
    }
}