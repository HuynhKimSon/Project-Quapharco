const PREFIX = ctx + "business/hddv";

// Init screen
$(document).ready(function () {
    loadList();
});

function loadList() {

    const OPTIONS = {
        id: "bootstrap-table",
        pagination: false,
        showSearch: false,
        showRefresh: false,
        modalName: 'Chi tiết',
        data: details,
        columns: [
            {
                field: 'id',
                title: 'STT',
                align: 'center',
                formatter: function (value, row, index) {
                    return index + 1;
                }
            },
            {
                field: 'productName',
                title: 'Tên hàng',
            },
            {
                field: 'productTaxId',
                title: 'Mã thuế',
            },
            {
                field: 'productSubtotalAmount',
                title: 'Tiền trước thuế',
                formatter: function (value, row, index) {
                    return $.common.currencyFormat(row.productSubtotalAmount);
                },
            },
            {
                field: 'productTaxAmount',
                title: 'Tiền thuế',
                formatter: function (value, row, index) {
                    return $.common.currencyFormat(row.productTaxAmount);
                },
            },
            {
                field: 'productTotalAmount',
                title: 'Tiền sau thuế',
                formatter: function (value, row, index) {
                    return $.common.currencyFormat(row.productTotalAmount);
                },
            },
        ],
    };
    $.table.init(OPTIONS);
}

function updateEInvoiceNo() {
    let newInvNo = $("#eInvoiceNo").val();
    let dataRequest = {
        fastId: hddv.fastId,
        eInvoiceNo: newInvNo,
    };
    $.ajax({
        type: "POST",
        url: PREFIX + "/updateEInvoiceNo",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify(dataRequest),
        error: function (request) {
            $.modal.alertError("System error!");
        },
        success: function (data) {
            if (data == null) {
                return;
            }
            if (data.code == 0) {
                $.modal.close();
                parent.$.modal.alertSuccess("Cập nhật thành công!");
                parent.$.table.refresh();
            } else {
                $.modal.alertError("System error! \r\n " + data.msg);
            }
        },
    });
}
