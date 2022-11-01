const PREFIX = ctx + "business/hdx-ncc";

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
        field: "id",
        title: "STT",
        align: "center",
        formatter: function (value, row, index) {
          return index + 1;
        },
      },
      {
        field: "productId",
        title: "Mã hàng",
      },
      {
        field: "productName",
        title: "Tên hàng",
      },
      {
        field: "lotNo",
        title: "Lô",
      },
      {
        field: "productExpiredDate",
        title: "HD",
      },
      {
        field: "productUnit",
        title: "ĐVT",
      },
      {
        field: "productQuantity",
        title: "Số lượng",
      },
      {
        field: "productPrice",
        title: "Giá",
        formatter: function (value, row, index) {
          return $.common.currencyFormat(row.productPrice);
        },
      },
      {
        field: "productTaxAmount",
        title: "Thuế",
        formatter: function (value, row, index) {
          return $.common.currencyFormat(row.productTaxAmount);
        },
      },
      {
        field: "productDiscount",
        title: "Ck",
      },
    ],
  };
  $.table.init(OPTIONS);
}
