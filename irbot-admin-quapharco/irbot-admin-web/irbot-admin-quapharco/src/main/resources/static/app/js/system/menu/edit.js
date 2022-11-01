const PREFIX = ctx + "system/menu";

$(function () {
    var menuType = $('input[name="menuType"]:checked').val();
    menuVisible(menuType);
});

$("#form-menu-edit").validate({
    onkeyup: false,
    rules: {
        menuType: {
            required: true,
        },
        menuName: {
            remote: {
                url: PREFIX + "/checkMenuNameUnique",
                type: "post",
                dataType: "json",
                data: {
                    "menuId": function () {
                        return $("#menuId").val();
                    },
                    "parentId": function () {
                        return $("input[name='parentId']").val();
                    },
                    "menuName": function () {
                        return $.common.trim($("#menuName").val());
                    }
                },
                dataFilter: function (data, type) {
                    return $.validate.unique(data);
                }
            }
        },
        orderNum: {
            digits: true
        },
    },
    messages: {
        "menuName": {
            remote: "Menu đã tồn tại"
        }
    },
    focusCleanup: true
});

function submitHandler() {
    if ($.validate.form()) {
        $.operate.save(PREFIX + "/edit", $('#form-menu-edit').serialize());
    }
}

$(function () {
    $("input[name='icon']").focus(function () {
        $(".icon-drop").show();
    });
    $("#form-menu-edit").click(function (event) {
        var obj = event.srcElement || event.target;
        if (!$(obj).is("input[name='icon']")) {
            $(".icon-drop").hide();
        }
    });
    $(".icon-drop").find(".ico-list i").on("click",
        function () {
            $('#icon').val($(this).attr('class'));
        });
    $('input').on('ifChecked',
        function (event) {
            var menuType = $(event.target).val();
            menuVisible(menuType);
        });
});

function menuVisible(menuType) {
    if (menuType == "M") {
        $("#url").parents(".form-group").hide();
        $("#perms").parents(".form-group").hide();
        $("#icon").parents(".form-group").show();
        $("#target").parents(".form-group").hide();
        $("input[name='visible']").parents(".form-group").show();
        $(".is-refresh").hide();
    } else if (menuType == "C") {
        $("#url").parents(".form-group").show();
        $("#perms").parents(".form-group").show();
        $("#icon").parents(".form-group").show();
        $("#target").parents(".form-group").show();
        $("input[name='visible']").parents(".form-group").show();
        $(".is-refresh").show();
    } else if (menuType == "F") {
        $("#url").parents(".form-group").hide();
        $("#perms").parents(".form-group").show();
        $("#icon").parents(".form-group").hide();
        $("#target").parents(".form-group").hide();
        $("input[name='visible']").parents(".form-group").hide();
        $(".is-refresh").hide();
    }
}

function selectMenuTree() {
    var menuId = $("#treeId").val();
    if (menuId > 0) {
        var url = PREFIX + "/selectMenuTree/" + menuId;
        $.modal.open("Chọn menu", url, '380', '380');
    } else {
        $.modal.alertError("Không thể chọn menu chính");
    }
}

function selectMenuTree() {
    var menuId = $("#treeId").val();
    if (menuId > 0) {
        var url = PREFIX + "/selectMenuTree/" + menuId;
        var options = {
            title: 'Lựa chọn menu',
            width: "380",
            url: url,
            callBack: doSubmit
        };
        $.modal.openOptions(options);
    } else {
        $.modal.alertError("Không thể chọn menu chính");
    }
}

function doSubmit(index, layero) {
    var body = layer.getChildFrame('body', index);
    $("#treeId").val(body.find('#treeId').val());
    $("#treeName").val(body.find('#treeName').val());
    layer.close(index);
}