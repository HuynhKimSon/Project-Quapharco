$(function () {
    //Handle button search by keyboard "enter"
    $(".select-list li input").on('keyup', function (e) {
        if (e.key === 'Enter' || e.keyCode === 13) {
            //Search for type table
            $.table.search();
        }
    });

    //Handle button search when click option
    $('.select-list li select').on('change', function () {
        //Search for type table
        $.table.search();

        //Search for type treetable
        $.treeTable.search();
    });
})
