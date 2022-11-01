const PREFIX = ctx + "system/menu";

$("#form-menu-add").validate({
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
        $.operate.save(PREFIX + "/add", $('#form-menu-add').serialize());
    }
}

$(function () {
    $("input[name='icon']").focus(function () {
        $(".icon-drop").show();
    });
    $("#form-menu-add").click(function (event) {
        var obj = event.srcElement || event.target;
        if (!$(obj).is("input[name='icon']")) {
            $(".icon-drop").hide();
        }
    });
    $(".icon-drop").find(".ico-list i").on("click", function () {
        $('#icon').val($(this).attr('class'));
    });
    $('input').on('ifChecked', function (event) {
        var menuType = $(event.target).val();
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
    });
});

function selectMenuTree() {
    var treeId = $("#treeId").val();
    var menuId = treeId > 0 ? treeId : 1;
    var url = PREFIX + "/selectMenuTree/" + menuId;
    var options = {
        title: 'Lựa chọn menu',
        width: "380",
        url: url,
        callBack: doSubmit
    };
    $.modal.openOptions(options);
}

function doSubmit(index, layero) {
    var body = layer.getChildFrame('body', index);
    $("#treeId").val(body.find('#treeId').val());
    $("#treeName").val(body.find('#treeName').val());
    layer.close(index);
}