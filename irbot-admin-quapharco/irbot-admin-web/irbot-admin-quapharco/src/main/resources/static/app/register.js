
$(function () {
    validateRule();
    $('.imgcode').click(function () {
        var url = ctx + "captcha/captchaImage?type=" + captchaType + "&s=" + Math.random();
        $(".imgcode").attr("src", url);
    });
});

$.validator.setDefaults({
    submitHandler: function () {
        register();
    }
});

function register() {
    $.modal.loading($("#btnSubmit").data("loading"));
    var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var validateCode = $("input[name='validateCode']").val();
    $.ajax({
        type: "post",
        url: ctx + "register",
        data: {
            "loginName": username,
            "password": password,
            "validateCode": validateCode
        },
        success: function (r) {
            if (r.code == web_status.SUCCESS) {
                layer.alert("<font color='red'>Xin chúc mừng, tài khoản của bạn " + username + " đăng ký thành công!</font>", {
                    icon: 1,
                    title: "Gợi ý hệ thống"
                },
                    function (index) {
                        //关闭弹窗
                        layer.close(index);
                        location.href = ctx + 'login';
                    });
            } else {
                $.modal.closeLoading();
                $('.imgcode').click();
                $(".code").val("");
                $.modal.msg(r.msg);
            }
        }
    });
}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#registerForm").validate({
        rules: {
            username: {
                required: true,
                minlength: 2
            },
            password: {
                required: true,
                minlength: 5
            },
            confirmPassword: {
                required: true,
                equalTo: "[name='password']"
            }
        },
        messages: {
            username: {
                required: icon + "Vui lòng nhập username",
                minlength: icon + "Username không được ít hơn 2 ký tự"
            },
            password: {
                required: icon + "Vui lòng nhập mật khẩu của bạn",
                minlength: icon + "Mật khẩu không được ít hơn 5 ký tự",
            },
            confirmPassword: {
                required: icon + "Vui lòng nhập lại mật khẩu của bạn",
                equalTo: icon + "Mật khẩu không trùng khớp"
            }
        }
    })
}
