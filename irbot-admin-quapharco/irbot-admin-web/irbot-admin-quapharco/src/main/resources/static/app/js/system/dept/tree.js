const PREFIX = ctx + "system/dept";

$(function () {
    var url = $.common.isEmpty(excludeId) ? PREFIX + "/treeData" : PREFIX + "/treeData/" + excludeId;
    var options = {
        url: url,
        expandLevel: 2,
        onClick: zOnClick
    };
    $.tree.init(options);
});

function zOnClick(event, treeId, treeNode) {
    var treeId = treeNode.id;
    var treeName = treeNode.name;
    $("#treeId").val(treeId);
    $("#treeName").val(treeName);
}