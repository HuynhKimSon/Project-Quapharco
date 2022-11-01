/*this is basic form validation using for validation person's basic information author:Clara Guo data:2017/07/20*/
$(document).ready(function () {
	$.validator.setDefaults({
		submitHandler: function (form) {
			form.submit();
		}
	});
	//手机号码验证身份证正则合并：(^\d{15}$)|(^\d{17}([0-9]|X)$)
	jQuery.validator.addMethod("isPhone", function (value, element) {
		var length = value.length;
		var phone = /\d$/;
		return this.optional(element) || (length == 10 && phone.test(value));
	}, "Vui lòng điền đúng số điện thoại di động 10 chữ số");
	//电话号码验证
	jQuery.validator.addMethod("isTel", function (value, element) {
		var tel = /^(0\d{2,3}-)?\d{7,8}$/g;//区号3,4位,号码7,8位
		return this.optional(element) || (tel.test(value));
	}, "Vui lòng điền đúng số điện thoại cố định");
	//姓名校验
	jQuery.validator.addMethod("isName", function (value, element) {
		var name = /^[a-zA-Z0-9]$/;
		return this.optional(element) || (name.test(value));
	}, "Tên chỉ được sử dụng chữ cái");
	//校验用户名
	jQuery.validator.addMethod("isUserName", function (value, element) {
		var userName = /^[a-zA-Z0-9]{2,13}$/;
		return this.optional(element) || (userName).test(value);
	}, 'Vui lòng nhập số hoặc chữ cái, không bao gồm các ký tự đặc biệt');

	//校验身份证
	jQuery.validator.addMethod("isIdentity", function (value, element) {
		var id = /^(\d{12}$|^\d{9}$)$/;
		return this.optional(element) || (id.test(value));
	}, "Vui lòng nhập đúng số ID 12 hoặc 9 chữ số");
	//校验出生日期
	jQuery.validator.addMethod("isBirth", function (value, element) {
		var birth = /^(19|20)\d{2}-(1[0-2]|0?[1-9])-(0?[1-9]|[1-2][0-9]|3[0-1])$/;
		return this.optional(element) || (birth).test(value);
	}, "Ví dụ về định dạng ngày sinh năm 2000-01-01");
	//校验IP地址
	jQuery.validator.addMethod("isIp", function (value, element) {
		var ip = /^(?:(?:2[0-4][0-9]\.)|(?:25[0-5]\.)|(?:1[0-9][0-9]\.)|(?:[1-9][0-9]\.)|(?:[0-9]\.)){3}(?:(?:2[0-4][0-9])|(?:25[0-5])|(?:1[0-9][0-9])|(?:[1-9][0-9])|(?:[0-9]))$/;
		return this.optional(element) || (ip).test(value);
	}, "Ví dụ về định dạng địa chỉ IP 127.0.0.1");
	jQuery.validator.addMethod("notEqual", function (value, element, param) {
		return value != param;
	}, $.validator.format("Giá trị đầu vào không được phép là {0}"));
	jQuery.validator.addMethod("gt", function (value, element, param) {
		return value > param;
	}, $.validator.format("Giá trị đầu vào phải lớn hơn {0}"));
	//校验新旧密码是否相同
	jQuery.validator.addMethod("isdiff", function () {
		var p1 = $("#pwdOld").val();
		var p2 = $("#pwdNew").val();
		if (p1 == p2) {
			return false;
		} else {
			return true;
		}
	});
	//校验新密码和确认密码是否相同
	jQuery.validator.addMethod("issame", function () {
		var p3 = $("#confirm_password").val();
		var p4 = $("#pwdNew").val();
		if (p3 == p4) {
			return true;
		} else {
			return false;
		}
	});
	//校验基础信息表单
	$("#basicInfoForm").validate({
		errorElement: 'span',
		errorClass: 'help-block error-mes',
		rules: {
			name: {
				required: true,
				isName: true
			},
			sex: "required",
			birth: "required",
			mobile: {
				required: true,
				isPhone: true
			},
			email: {
				required: true,
				email: true
			}
		},
		messages: {
			name: {
				required: "Vui lòng nhập tên chính xác",
				isName: "Tên chỉ có thể là ký tự chữ cái"
			},
			sex: {
				required: "Vui lòng nhập giới tính"
			},
			birth: {
				required: "Vui lòng nhập năm và tháng sinh"
			},
			mobile: {
				required: "Vui lòng nhập số điện thoại",
				isPhone: "Vui lòng điền đúng số điện thoại di động 11 chữ số"
			},
			email: {
				required: "Vui lòng nhập email của bạn",
				email: "Vui lòng điền đúng định dạng email"
			}
		},

		errorPlacement: function (error, element) {
			element.next().remove();
			element.closest('.gg-formGroup').append(error);
		},

		highlight: function (element) {
			$(element).closest('.gg-formGroup').addClass('has-error has-feedback');
		},
		success: function (label) {
			var el = label.closest('.gg-formGroup').find("input");
			el.next().remove();
			label.closest('.gg-formGroup').removeClass('has-error').addClass("has-feedback has-success");
			label.remove();
		},
		submitHandler: function (form) {
			alert("保存成功!");
		}
	});

	//校验修改密码表单
	$("#modifyPwd").validate({
		onfocusout: function (element) { $(element).valid() },
		debug: false, //表示校验通过后是否直接提交表单
		onkeyup: false, //表示按键松开时候监听验证
		rules: {
			pwdOld: {
				required: true,
				minlength: 6
			},
			pwdNew: {
				required: true,
				minlength: 6,
				isdiff: true,
				//issame:true,
			},
			confirm_password: {
				required: true,
				minlength: 6,
				issame: true,
			}

		},
		messages: {
			pwdOld: {
				required: 'Bắt buộc',
				minlength: $.validator.format('Độ dài mật khẩu phải lớn hơn 6')
			},
			pwdNew: {
				required: 'Bắt buộc',
				minlength: $.validator.format('Độ dài mật khẩu phải lớn hơn 6'),
				isdiff: 'Mật khẩu ban đầu và mật khẩu mới không thể lặp lại',

			},
			confirm_password: {
				required: 'Bắt buộc',
				minlength: $.validator.format('Độ dài mật khẩu phải lớn hơn 6'),
				issame: 'Mật khẩu mới phải giống với mật khẩu mới đã xác nhận',
			}

		},
		errorElement: "mes",
		errorClass: "gg-star",
		errorPlacement: function (error, element) {
			element.closest('.gg-formGroup').append(error);

		}
	});
});