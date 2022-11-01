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
    modalName: "Chi tiết",
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