$(function () {
    var url = ctx + "system/menu/menuTreeData";
    var options = {
        url: url,
        expandLevel: 1,
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