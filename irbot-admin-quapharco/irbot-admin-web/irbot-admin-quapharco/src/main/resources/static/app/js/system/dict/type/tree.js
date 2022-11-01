$(function () {
    var url = ctx + "system/dict/treeData";
    var options = {
        url: url,
        onClick: zOnClick
    };
    $.tree.init(options);
});

function zOnClick(event, treeId, treeNode) {
    $("#dictType").val(treeNode.title);
}