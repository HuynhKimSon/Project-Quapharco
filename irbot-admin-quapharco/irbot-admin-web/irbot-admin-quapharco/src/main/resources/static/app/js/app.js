/**
 * 通用js方法封装处理
 * Copyright (c) 2019 ruoyi
 */

// Current table related information
var table = {
    config: {},
    // Current instance configuration
    options: {},
    // Set instance configuration
    set: function (id) {
        if ($.common.getLength(table.config) > 1) {
            var tableId = $.common.isEmpty(id) ? $(event.currentTarget).parents(".bootstrap-table").find("table.table").attr("id") : id;
            if ($.common.isNotEmpty(tableId)) {
                table.options = table.get(tableId);
            }
        }
    },
    // Get instance configuration
    get: function (id) {
        return table.config[id];
    },
    // Remember to select the instance group
    rememberSelecteds: {},
    // Remember to select the ID group
    rememberSelectedIds: {}
};

(function ($) {
    $.extend({
        _tree: {},
        bttTable: {},
        // Form packaging processing
        table: {
            // Initialize table parameters
            init: function (options) {
                var defaults = {
                    id: "bootstrap-table",
                    type: 0, // 0 代表bootstrapTable 1代表bootstrapTreeTable
                    method: 'post',
                    height: undefined,
                    sidePagination: "server",
                    sortName: undefined,
                    sortOrder: "asc",
                    pagination: true,
                    paginationLoop: false,
                    pageSize: 25,
                    pageNumber: 1,
                    pageList: [10, 25, 50],
                    toolbar: "toolbar",
                    loadingFontSize: 13,
                    striped: false,
                    escape: false,
                    firstLoad: true,
                    showFooter: false,
                    search: false,
                    showSearch: true,
                    showPageGo: false,
                    showRefresh: true,
                    showColumns: true,
                    showToggle: true,
                    showExport: false,
                    exportDataType: 'all',
                    exportTypes: ['csv', 'txt', 'doc', 'excel'],
                    clickToSelect: false,
                    singleSelect: false,
                    mobileResponsive: true,
                    maintainSelected: false,
                    rememberSelected: false,
                    fixedColumns: false,
                    fixedNumber: 0,
                    fixedRightNumber: 0,
                    queryParams: $.table.queryParams,
                    rowStyle: {},
                };
                var options = $.extend(defaults, options);
                table.options = options;
                table.config[options.id] = options;
                $.table.initEvent();
                $('#' + options.id).bootstrapTable({
                    id: options.id,
                    url: options.url, // URL of request backend (*)
                    contentType: "application/x-www-form-urlencoded", // encoding type
                    method: options.method, // request method (*)
                    cache: false, // Whether to use cache
                    height: options.height, // height of the table
                    striped: options.striped, // Whether to display the line spacing color
                    sortable: true, // Whether to enable sorting
                    sortStable: true, // Set to true to get a stable sort
                    sortName: options.sortName, // Sort column name
                    sortOrder: options.sortOrder, // sorting method asc or desc
                    pagination: options.pagination, // Whether to display pagination (*)
                    paginationLoop: options.paginationLoop, // Whether to enable the infinite loop of the paging bar
                    pageNumber: 1, // Initially load the first page, default the first page
                    pageSize: options.pageSize, // The number of rows per page (*)
                    pageList: options.pageList, // The number of rows per page available for selection (*)
                    firstLoad: options.firstLoad, // Whether to request to load data for the first time, you can configure false for larger data
                    escape: options.escape, // escape HTML string
                    showFooter: options.showFooter, // Whether to show the end of the table
                    iconSize: 'outline', // Icon size: undefined default button size xs super small button sm small button lg large button
                    toolbar: '#' + options.toolbar, // Specify the work bar
                    loadingFontSize: options.loadingFontSize, // Customize the font size of the loaded text
                    sidePagination: options.sidePagination, // server enables server paging and client client paging
                    search: options.search, // Whether to display the search box function
                    searchText: options.searchText, // The initial display content of the search box, the default is empty
                    showSearch: options.showSearch, // Whether to display search information
                    showPageGo: options.showPageGo, // Whether to display the jump page
                    showRefresh: options.showRefresh, // Whether to display the refresh button
                    showColumns: options.showColumns, // Whether to show or hide a column drop-down box
                    showToggle: options.showToggle, // Whether to display the switch button of detailed view and list view
                    showExport: options.showExport, // Whether to support export files
                    showHeader: options.showHeader, // Whether to display the header
                    showFullscreen: options.showFullscreen, // Whether to show the full screen button
                    uniqueId: options.uniqueId, // unique identifier
                    clickToSelect: options.clickToSelect, // Whether to enable click to select rows
                    singleSelect: options.singleSelect, // Whether to single select checkbox
                    mobileResponsive: options.mobileResponsive, // Whether to support mobile terminal adaptation
                    cardView: options.cardView, // Whether to enable the display card view
                    detailView: options.detailView, // Whether to enable the display of the detail view
                    onCheck: options.onCheck, // trigger when this row is selected
                    onUncheck: options.onUncheck, // trigger when this line is canceled
                    onCheckAll: options.onCheckAll, // trigger when all rows are selected
                    onUncheckAll: options.onUncheckAll, // Triggered when all rows are canceled
                    onClickRow: options.onClickRow, // event triggered by clicking on a row
                    onDblClickRow: options.onDblClickRow, // event triggered by double-clicking a row
                    onClickCell: options.onClickCell, // event triggered by clicking a cell
                    onDblClickCell: options.onDblClickCell, // event triggered by double-clicking a cell
                    onEditableSave: options.onEditableSave, // inline edit and save event
                    onExpandRow: options.onExpandRow, // Click the event of the detailed view
                    onPostBody: options.onPostBody, // event executed after rendering
                    maintainSelected: options.maintainSelected, // Keep the selected row when the front end turns the page
                    rememberSelected: options.rememberSelected, // enable page turning to remember the previous selection
                    fixedColumns: options.fixedColumns, // Whether to enable frozen columns (left side)
                    fixedNumber: options.fixedNumber, // the number of frozen columns (left side)
                    fixedRightNumber: options.fixedRightNumber, // the number of frozen columns (right side)
                    onReorderRow: options.onReorderRow, // Processing function when dragging is over
                    queryParams: options.queryParams, // Pass parameters (*)
                    rowStyle: options.rowStyle, // Set the row style through a custom function
                    footerStyle: options.footerStyle,
                    headerStyle: options.headerStyle, // Set the header style through a custom function
                    columns: options.columns, // display column information (*)
                    data: options.data, // loaded data
                    responseHandler: $.table.responseHandler, // Process the function before loading the data sent by the server
                    onLoadSuccess: $.table.onLoadSuccess, // trigger the processing function when all data is loaded
                    exportOptions: options.exportOptions, // Front-end export ignores column index
                    exportDataType: options.exportDataType, // Export method (default all: export all data; basic: export the data of the current page; selected: export the selected data)
                    exportTypes: options.exportTypes, // export file types (json, xml, png, csv, txt, sql, doc, excel, xlsx, powerpoint, pdf)
                    printPageBuilder: options.printPageBuilder, // Custom print page template
                    detailFormatter: options.detailFormatter, // display a list of other data below the line

                });
            },
            // Get the instance ID, return #id1,#id2 delimeter separator if there are multiple
            getOptionsIds: function (separator) {
                var _separator = $.common.isEmpty(separator) ? "," : separator;
                var optionsIds = "";
                $.each(table.config, function (key, value) {
                    optionsIds += "#" + key + _separator;
                });
                return optionsIds.substring(0, optionsIds.length - 1);
            },
            // Query conditions
            queryParams: function (params) {
                var curParams = {
                    // Pass parameter query parameters
                    pageSize: params.limit,
                    pageNum: params.offset / params.limit + 1,
                    searchValue: params.search,
                    orderByColumn: params.sort,
                    isAsc: params.order
                };
                var currentId = $.common.isEmpty(table.options.formId) ? $('form').attr('id') : table.options.formId;
                return $.extend(curParams, $.common.formToJSON(currentId));
            },
            // Process callback function after requesting to obtain data
            responseHandler: function (res) {
                if (typeof table.get(this.id).responseHandler == "function") {
                    table.get(this.id).responseHandler(res);
                }
                if (res.code == web_status.SUCCESS) {
                    if ($.common.isNotEmpty(table.options.sidePagination) && table.options.sidePagination == 'client') {
                        return res.rows;
                    } else {
                        if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                            var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
                            $.each(res.rows, function (i, row) {
                                row.state = $.inArray(row[column], table.rememberSelectedIds[table.options.id]) !== -1;
                            })
                        }
                        return { rows: res.rows, total: res.total };
                    }
                } else {
                    $.modal.alertWarning(res.msg);
                    return { rows: [], total: 0 };
                }
            },
            // Initialization event
            initEvent: function () {
                // Instance ID information
                var optionsIds = $.table.getOptionsIds();
                // Monitor event processing
                $(optionsIds).on(TABLE_EVENTS, function () {
                    table.set($(this).attr("id"));
                });
                // Triggered after the table body is rendered and visible in the DOM (event)
                $(optionsIds).on("post-body.bs.table", function (e, args) {
                    // Floating prompt box special effects
                    $(".table [data-toggle='tooltip']").tooltip();
                    // Bubble pop-up box special effects
                    $('.table [data-toggle="popover"]').popover();
                });
                // select, cancel, select all, cancel all (event)
                $(optionsIds).on("check.bs.table check-all.bs.table uncheck.bs.table uncheck-all.bs.table", function (e, rowsAfter, rowsBefore) {
                    // Checkbox pagination keeps and saves the selected array
                    var rows = $.common.equals("uncheck-all", e.type) ? rowsBefore : rowsAfter;
                    var rowIds = $.table.affectedRowIds(rows);
                    if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                        func = $.inArray(e.type, ['check', 'check-all']) > -1 ? 'union' : 'difference';
                        var selectedIds = table.rememberSelectedIds[table.options.id];
                        if ($.common.isNotEmpty(selectedIds)) {
                            table.rememberSelectedIds[table.options.id] = _[func](selectedIds, rowIds);
                        } else {
                            table.rememberSelectedIds[table.options.id] = _[func]([], rowIds);
                        }
                        var selectedRows = table.rememberSelecteds[table.options.id];
                        if ($.common.isNotEmpty(selectedRows)) {
                            table.rememberSelecteds[table.options.id] = _[func](selectedRows, rows);
                        } else {
                            table.rememberSelecteds[table.options.id] = _[func]([], rows);
                        }
                    }
                });
                // 加载成功、选中、取消、全部选中、全部取消（事件）
                $(optionsIds).on("check.bs.table uncheck.bs.table check-all.bs.table uncheck-all.bs.table load-success.bs.table", function () {
                    var toolbar = table.options.toolbar;
                    var uniqueId = table.options.uniqueId;
                    // 工具栏按钮控制
                    var rows = $.common.isEmpty(uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(uniqueId);
                    // 非多个禁用
                    $('#' + toolbar + ' .multiple').toggleClass('disabled', !rows.length);
                    // 非单个禁用
                    $('#' + toolbar + ' .single').toggleClass('disabled', rows.length != 1);
                });
                // 图片预览事件
                $(optionsIds).off("click").on("click", '.img-circle', function () {
                    var src = $(this).attr('src');
                    var target = $(this).data('target');
                    if ($.common.equals("self", target)) {
                        var height = $(this).data('height');
                        var width = $(this).data('width');
                        // 如果是移动端，就使用自适应大小弹窗
                        if ($.common.isMobile()) {
                            width = 'auto';
                            height = 'auto';
                        }
                        layer.open({
                            title: false,
                            type: 1,
                            closeBtn: true,
                            shadeClose: true,
                            area: ['auto', 'auto'],
                            content: "<img src='" + src + "' height='" + height + "' width='" + width + "'/>"
                        });
                    } else if ($.common.equals("blank", target)) {
                        window.open(src);
                    }
                });
                // 单击tooltip事件
                $(optionsIds).on("click", '.tooltip-show', function () {
                    var target = $(this).data('target');
                    var input = $(this).prev();
                    if ($.common.equals("copy", target)) {
                        input.select();
                        document.execCommand("copy");
                    } else if ($.common.equals("open", target)) {
                        parent.layer.alert(input.val(), {
                            title: "Thông tin",
                            shadeClose: true,
                            btn: ['Xác nhận'],
                            btnclass: ['btn btn-primary'],
                        });
                    }
                });
            },
            // 当所有数据被加载时触发
            onLoadSuccess: function (data) {
                if (typeof table.options.onLoadSuccess == "function") {
                    table.options.onLoadSuccess(data);
                }
            },
            // 表格销毁
            destroy: function (tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('destroy');
                delete table.rememberSelectedIds[currentId];
                delete table.rememberSelecteds[currentId];
            },
            // 序列号生成
            serialNumber: function (index, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                var tableParams = $("#" + currentId).bootstrapTable('getOptions');
                var pageSize = $.common.isNotEmpty(tableParams.pageSize) ? tableParams.pageSize : table.options.pageSize;
                var pageNumber = $.common.isNotEmpty(tableParams.pageNumber) ? tableParams.pageNumber : table.options.pageNumber;
                return pageSize * (pageNumber - 1) + index + 1;
            },
            // 列超出指定长度浮动提示 target（copy单击复制文本 open弹窗打开文本）
            tooltip: function (value, length, target) {
                var _length = $.common.isEmpty(length) ? 20 : length;
                var _text = "";
                var _value = $.common.nullToStr(value);
                var _target = $.common.isEmpty(target) ? 'copy' : target;
                if (_value.length > _length) {
                    _text = _value.substr(0, _length) + "...";
                    _value = _value.replace(/\'/g, "&apos;");
                    _value = _value.replace(/\"/g, "&quot;");
                    var actions = [];
                    actions.push($.common.sprintf('<input style="opacity: 0;position: absolute;width:5px;z-index:-1" type="text" value="%s"/>', _value));
                    actions.push($.common.sprintf('<a href="###" class="tooltip-show" data-toggle="tooltip" data-target="%s" title="%s">%s</a>', _target, _value, _text));
                    return actions.join('');
                } else {
                    _text = _value;
                    return _text;
                }
            },
            // 下拉按钮切换
            dropdownToggle: function (value) {
                var actions = [];
                actions.push('<div class="btn-group">');
                actions.push('<button type="button" class="btn btn-xs dropdown-toggle" data-toggle="dropdown" aria-expanded="false">');
                actions.push('<i class="fa fa-cog"></i>&nbsp;<span class="fa fa-chevron-down"></span></button>');
                actions.push('<ul class="dropdown-menu">');
                actions.push(value.replace(/<a/g, "<li><a").replace(/<\/a>/g, "</a></li>"));
                actions.push('</ul>');
                actions.push('</div>');
                return actions.join('');
            },
            // 图片预览
            imageView: function (value, height, width, target) {
                if ($.common.isEmpty(width)) {
                    width = 'auto';
                }
                if ($.common.isEmpty(height)) {
                    height = 'auto';
                }
                // blank or self
                var _target = $.common.isEmpty(target) ? 'self' : target;
                if ($.common.isNotEmpty(value)) {
                    return $.common.sprintf("<img class='img-circle img-xs' data-height='%s' data-width='%s' data-target='%s' src='%s'/>", height, width, _target, value);
                } else {
                    return $.common.nullToStr(value);
                }
            },
            // 搜索-默认第一个form
            search: function (formId, tableId) {
                table.set(tableId);
                table.options.formId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $.common.isEmpty(tableId) ? $("#" + table.options.id).bootstrapTable('getOptions') : $("#" + tableId).bootstrapTable('getOptions');
                if ($.common.isNotEmpty(tableId)) {
                    $("#" + tableId).bootstrapTable('refresh', params);
                } else {
                    $("#" + table.options.id).bootstrapTable('refresh', params);
                }
            },
            // 导出数据
            exportExcel: function (formId) {
                table.set();
                $.modal.confirm("Xác nhận xuất tất cả " + table.options.modalName + "？", function () {
                    var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                    var params = $("#" + table.options.id).bootstrapTable('getOptions');
                    var dataParam = $("#" + currentId).serializeArray();
                    dataParam.push({ "name": "orderByColumn", "value": params.sortName });
                    dataParam.push({ "name": "isAsc", "value": params.sortOrder });
                    $.modal.loading("Đang xuất dữ liệu, vui lòng đợi ...");
                    $.post(table.options.exportUrl, dataParam, function (result) {
                        if (result.code == web_status.SUCCESS) {
                            window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
                        } else if (result.code == web_status.WARNING) {
                            $.modal.alertWarning(result.msg)
                        } else {
                            $.modal.alertError(result.msg);
                        }
                        $.modal.closeLoading();
                    });
                });
            },
            // 下载模板
            importTemplate: function () {
                table.set();
                $.get(table.options.importTemplateUrl, function (result) {
                    if (result.code == web_status.SUCCESS) {
                        window.location.href = ctx + "common/download?fileName=" + encodeURI(result.msg) + "&delete=" + true;
                    } else if (result.code == web_status.WARNING) {
                        $.modal.alertWarning(result.msg)
                    } else {
                        $.modal.alertError(result.msg);
                    }
                });
            },
            // 导入数据
            importExcel: function (formId, width, height) {
                table.set();
                var currentId = $.common.isEmpty(formId) ? 'importTpl' : formId;
                var _width = $.common.isEmpty(width) ? "400" : width;
                var _height = $.common.isEmpty(height) ? "230" : height;
                layer.open({
                    type: 1,
                    area: [_width + 'px', _height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: 'Import ' + table.options.modalName,
                    content: $('#' + currentId).html(),
                    btn: ['<i class="fa fa-check"></i> Import', '<i class="fa fa-remove"></i> Hủy bỏ'],
                    // 弹层外区域关闭
                    shadeClose: true,
                    btn1: function (index, layero) {
                        var file = layero.find('#file').val();
                        if (file == '' || (!$.common.endWith(file, '.xls') && !$.common.endWith(file, '.xlsx'))) {
                            $.modal.msgWarning("Vui lòng chọn tệp có đuôi 'xls' hoặc ''xlsx'");
                            return false;
                        }
                        var index = layer.load(2, { shade: false });
                        $.modal.disable();
                        var formData = new FormData(layero.find('form')[0]);
                        $.ajax({
                            url: table.options.importUrl,
                            data: formData,
                            cache: false,
                            contentType: false,
                            processData: false,
                            type: 'POST',
                            success: function (result) {
                                if (result.code == web_status.SUCCESS) {
                                    $.modal.closeAll();
                                    $.modal.alertSuccess(result.msg);
                                    $.table.refresh();
                                } else if (result.code == web_status.WARNING) {
                                    layer.close(index);
                                    $.modal.enable();
                                    $.modal.alertWarning(result.msg)
                                } else {
                                    layer.close(index);
                                    $.modal.enable();
                                    $.modal.alertError(result.msg);
                                }
                            }
                        });
                    }
                });
            },
            // 刷新表格
            refresh: function (tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('refresh', {
                    silent: true
                });
            },
            // 查询表格指定列值
            selectColumns: function (column) {
                var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
                    return $.common.getItemField(row, column);
                });
                if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    var selectedRows = table.rememberSelecteds[table.options.id];
                    if ($.common.isNotEmpty(selectedRows)) {
                        rows = $.map(table.rememberSelecteds[table.options.id], function (row) {
                            return $.common.getItemField(row, column);
                        });
                    }
                }
                return $.common.uniqueFn(rows);
            },
            // 获取当前页选中或者取消的行ID
            affectedRowIds: function (rows) {
                var column = $.common.isEmpty(table.options.uniqueId) ? table.options.columns[1].field : table.options.uniqueId;
                var rowIds;
                if ($.isArray(rows)) {
                    rowIds = $.map(rows, function (row) {
                        return $.common.getItemField(row, column);
                    });
                } else {
                    rowIds = [rows[column]];
                }
                return rowIds;
            },
            // 查询表格首列值
            selectFirstColumns: function () {
                var rows = $.map($("#" + table.options.id).bootstrapTable('getSelections'), function (row) {
                    return $.common.getItemField(row, table.options.columns[1].field);
                });
                if ($.common.isNotEmpty(table.options.rememberSelected) && table.options.rememberSelected) {
                    var selectedRows = table.rememberSelecteds[table.options.id];
                    if ($.common.isNotEmpty(selectedRows)) {
                        rows = $.map(selectedRows, function (row) {
                            return $.common.getItemField(row, table.options.columns[1].field);
                        });
                    }
                }
                return $.common.uniqueFn(rows);
            },
            // 回显数据字典
            selectDictLabel: function (datas, value) {
                if ($.common.isEmpty(datas) || $.common.isEmpty(value)) {
                    return '';
                }
                var actions = [];
                $.each(datas, function (index, dict) {
                    if (dict.dictValue == ('' + value)) {
                        var listClass = $.common.equals("default", dict.listClass) || $.common.isEmpty(dict.listClass) ? "" : "badge badge-" + dict.listClass;
                        actions.push($.common.sprintf("<span class='%s'>%s</span>", listClass, dict.dictLabel));
                        return false;
                    }
                });
                return actions.join('');
            },
            // 回显数据字典（字符串数组）
            selectDictLabels: function (datas, value, separator) {
                if ($.common.isEmpty(datas) || $.common.isEmpty(value)) {
                    return '';
                }
                var currentSeparator = $.common.isEmpty(separator) ? "," : separator;
                var actions = [];
                $.each(value.split(currentSeparator), function (i, val) {
                    $.each(datas, function (index, dict) {
                        if (dict.dictValue == ('' + val)) {
                            var listClass = $.common.equals("default", dict.listClass) || $.common.isEmpty(dict.listClass) ? "" : "badge badge-" + dict.listClass;
                            actions.push($.common.sprintf("<span class='%s'>%s </span>", listClass, dict.dictLabel));
                            return false;
                        }
                    });
                });
                return actions.join('');
            },
            // 显示表格指定列
            showColumn: function (column, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('showColumn', column);
            },
            // 隐藏表格指定列
            hideColumn: function (column, tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('hideColumn', column);
            },
            // 显示所有表格列
            showAllColumns: function (tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('showAllColumns');
            },
            // 隐藏所有表格列
            hideAllColumns: function (tableId) {
                var currentId = $.common.isEmpty(tableId) ? table.options.id : tableId;
                $("#" + currentId).bootstrapTable('hideAllColumns');
            }
        },
        // 表格树封装处理
        treeTable: {
            // 初始化表格
            init: function (options) {
                var defaults = {
                    id: "bootstrap-tree-table",
                    type: 1, // 0 代表bootstrapTable 1代表bootstrapTreeTable
                    height: 0,
                    rootIdValue: null,
                    ajaxParams: {},
                    toolbar: "toolbar",
                    striped: false,
                    expandColumn: 1,
                    showSearch: true,
                    showRefresh: true,
                    showColumns: true,
                    expandAll: true,
                    expandFirst: true
                };
                var options = $.extend(defaults, options);
                table.options = options;
                table.config[options.id] = options;
                $.table.initEvent();
                $.bttTable = $('#' + options.id).bootstrapTreeTable({
                    code: options.code,                                 // 用于设置父子关系
                    parentCode: options.parentCode,                     // 用于设置父子关系
                    type: 'post',                                       // 请求方式（*）
                    url: options.url,                                   // 请求后台的URL（*）
                    data: options.data,                                 // 无url时用于渲染的数据
                    ajaxParams: options.ajaxParams,                     // 请求数据的ajax的data属性
                    rootIdValue: options.rootIdValue,                   // 设置指定根节点id值
                    height: options.height,                             // 表格树的高度
                    expandColumn: options.expandColumn,                 // 在哪一列上面显示展开按钮
                    striped: options.striped,                           // 是否显示行间隔色
                    bordered: false,                                    // 是否显示边框
                    toolbar: '#' + options.toolbar,                     // 指定工作栏
                    showSearch: options.showSearch,                     // 是否显示检索信息
                    showRefresh: options.showRefresh,                   // 是否显示刷新按钮
                    showColumns: options.showColumns,                   // 是否显示隐藏某列下拉框
                    expandAll: options.expandAll,                       // 是否全部展开
                    expandFirst: options.expandFirst,                   // 是否默认第一级展开--expandAll为false时生效
                    columns: options.columns,                           // 显示列信息（*）
                    responseHandler: $.treeTable.responseHandler,       // 在加载服务器发送来的数据之前处理函数
                    onLoadSuccess: $.treeTable.onLoadSuccess            // 当所有数据被加载时触发处理函数
                });
            },
            // 条件查询
            search: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var params = $.common.formToJSON(currentId);
                $.bttTable.bootstrapTreeTable('refresh', params);
            },
            // 刷新
            refresh: function () {
                $.bttTable.bootstrapTreeTable('refresh');
            },
            // 查询表格树指定列值
            selectColumns: function (column) {
                var rows = $.map($.bttTable.bootstrapTreeTable('getSelections'), function (row) {
                    return $.common.getItemField(row, column);
                });
                return $.common.uniqueFn(rows);
            },
            // 请求获取数据后处理回调函数，校验异常状态提醒
            responseHandler: function (res) {
                if (typeof table.options.responseHandler == "function") {
                    table.options.responseHandler(res);
                }
                if (res.code != undefined && res.code != web_status.SUCCESS) {
                    $.modal.alertWarning(res.msg);
                    return [];
                } else {
                    return res;
                }
            },
            // 当所有数据被加载时触发
            onLoadSuccess: function (data) {
                if (typeof table.options.onLoadSuccess == "function") {
                    table.options.onLoadSuccess(data);
                }
                $(".table [data-toggle='tooltip']").tooltip();
            },
        },
        // 表单封装处理
        form: {
            // 表单重置
            reset: function (formId, tableId) {
                table.set(tableId);
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                $("#" + currentId)[0].reset();
                if (table.options.type == table_type.bootstrapTable) {
                    if ($.common.isEmpty(tableId)) {
                        $("#" + table.options.id).bootstrapTable('refresh');
                    } else {
                        $("#" + tableId).bootstrapTable('refresh');
                    }
                } else if (table.options.type == table_type.bootstrapTreeTable) {
                    if ($.common.isEmpty(tableId)) {
                        $("#" + table.options.id).bootstrapTreeTable('refresh', []);
                    } else {
                        $("#" + tableId).bootstrapTreeTable('refresh', []);
                    }
                }
            },
            // 获取选中复选框项
            selectCheckeds: function (name) {
                var checkeds = "";
                $('input:checkbox[name="' + name + '"]:checked').each(function (i) {
                    if (0 == i) {
                        checkeds = $(this).val();
                    } else {
                        checkeds += ("," + $(this).val());
                    }
                });
                return checkeds;
            },
            // 获取选中下拉框项
            selectSelects: function (name) {
                var selects = "";
                $('#' + name + ' option:selected').each(function (i) {
                    if (0 == i) {
                        selects = $(this).val();
                    } else {
                        selects += ("," + $(this).val());
                    }
                });
                return selects;
            }
        },
        // 弹出层封装处理
        modal: {
            // 显示图标
            icon: function (type) {
                var icon = "";
                if (type == modal_status.WARNING) {
                    icon = 0;
                } else if (type == modal_status.SUCCESS) {
                    icon = 1;
                } else if (type == modal_status.FAIL) {
                    icon = 2;
                } else {
                    icon = 3;
                }
                return icon;
            },
            // 消息提示
            msg: function (content, type) {
                if (type != undefined) {
                    layer.msg(content, { icon: $.modal.icon(type), time: 2000, shift: 5 });
                } else {
                    layer.msg(content);
                }
            },
            // 错误消息
            msgError: function (content) {
                $.modal.msg(content, modal_status.FAIL);
            },
            // 成功消息
            msgSuccess: function (content) {
                $.modal.msg(content, modal_status.SUCCESS);
            },
            // 警告消息
            msgWarning: function (content) {
                $.modal.msg(content, modal_status.WARNING);
            },
            // 弹出提示
            alert: function (content, type) {
                layer.alert(content, {
                    icon: $.modal.icon(type),
                    title: "Gợi ý hệ thống",
                    btn: ['Xác nhận'],
                    btnclass: ['btn btn-primary'],
                });
            },
            // 消息提示并刷新父窗体
            msgReload: function (msg, type) {
                layer.msg(msg, {
                    icon: $.modal.icon(type),
                    time: 500,
                    shade: [0.1, '#8F8F8F']
                },
                    function () {
                        $.modal.reload();
                    });
            },
            // 错误提示
            alertError: function (content) {
                $.modal.alert(content, modal_status.FAIL);
            },
            // 成功提示
            alertSuccess: function (content) {
                $.modal.alert(content, modal_status.SUCCESS);
            },
            // 警告提示
            alertWarning: function (content) {
                $.modal.alert(content, modal_status.WARNING);
            },
            // 关闭窗体
            close: function (index) {
                if ($.common.isEmpty(index)) {
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                } else {
                    layer.close(index);
                }
            },
            // 关闭全部窗体
            closeAll: function () {
                layer.closeAll();
            },
            // 确认窗体
            confirm: function (content, callBack) {
                layer.confirm(content, {
                    icon: 3,
                    title: "Gợi ý hệ thống",
                    btn: ['Xác nhận', 'Đóng']
                }, function (index) {
                    layer.close(index);
                    callBack(true);
                });
            },
            // 弹出层指定宽度
            open: function (title, url, width, height, callback) {
                if ($.common.isMobile()) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = 800;
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50);
                }
                if ($.common.isEmpty(callback)) {
                    callback = function (index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero);
                    }
                }
                layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['Xác nhận', 'Đóng'],
                    // 弹层外区域关闭
                    shadeClose: true,
                    yes: callback,
                    cancel: function (index) {
                        return true;
                    }
                });
            },
            // 弹出层指定参数选项
            openOptions: function (options) {
                var _url = $.common.isEmpty(options.url) ? "/404.html" : options.url;
                var _title = $.common.isEmpty(options.title) ? "Cửa sổ hệ thống" : options.title;
                var _width = $.common.isEmpty(options.width) ? "800px" : ($.common.numValid(options.width) ? options.width + "px" : options.width);
                var _height = $.common.isEmpty(options.height) ? ($(window).height() - 50) + "px" : ($.common.numValid(options.height) ? options.height + "px" : options.height);
                var _btn = ['<i class="fa fa-check"></i> Xác nhận', '<i class="fa fa-close"></i> Đóng'];
                // 如果是移动端，就使用自适应大小弹窗
                if ($.common.isMobile()) {
                    _width = 'auto';
                    _height = 'auto';
                }
                if ($.common.isEmpty(options.yes)) {
                    options.yes = function (index, layero) {
                        options.callBack(index, layero);
                    }
                }
                var btnCallback = {};
                if (options.btn instanceof Array) {
                    for (var i = 1, len = options.btn.length; i < len; i++) {
                        var btn = options["btn" + (i + 1)];
                        if (btn) {
                            btnCallback["btn" + (i + 1)] = btn;
                        }
                    }
                }
                var index = layer.open($.extend({
                    type: 2,
                    maxmin: $.common.isEmpty(options.maxmin) ? true : options.maxmin,
                    shade: 0.3,
                    title: _title,
                    fix: false,
                    area: [_width, _height],
                    content: _url,
                    shadeClose: $.common.isEmpty(options.shadeClose) ? true : options.shadeClose,
                    skin: options.skin,
                    btn: $.common.isEmpty(options.btn) ? _btn : options.btn,
                    yes: options.yes,
                    cancel: function () {
                        return true;
                    }
                }, btnCallback));
                if ($.common.isNotEmpty(options.full) && options.full === true) {
                    layer.full(index);
                }
            },
            // 弹出层全屏
            openFull: function (title, url, width, height) {
                // 如果是移动端，就使用自适应大小弹窗
                if ($.common.isMobile()) {
                    width = 'auto';
                    height = 'auto';
                }
                if ($.common.isEmpty(title)) {
                    title = false;
                }
                if ($.common.isEmpty(url)) {
                    url = "/404.html";
                }
                if ($.common.isEmpty(width)) {
                    width = 800;
                }
                if ($.common.isEmpty(height)) {
                    height = ($(window).height() - 50);
                }
                var index = layer.open({
                    type: 2,
                    area: [width + 'px', height + 'px'],
                    fix: false,
                    //不固定
                    maxmin: true,
                    shade: 0.3,
                    title: title,
                    content: url,
                    btn: ['Xác nhận', 'Đóng'],
                    // 弹层外区域关闭
                    shadeClose: true,
                    yes: function (index, layero) {
                        var iframeWin = layero.find('iframe')[0];
                        iframeWin.contentWindow.submitHandler(index, layero);
                    },
                    cancel: function (index) {
                        return true;
                    }
                });
                layer.full(index);
            },
            // 选卡页方式打开
            openTab: function (title, url, isRefresh) {
                createMenuItem(url, title, isRefresh);
            },
            // 选卡页同一页签打开
            parentTab: function (title, url) {
                var dataId = window.frameElement.getAttribute('data-id');
                createMenuItem(url, title);
                closeItem(dataId);
            },
            // 关闭选项卡
            closeTab: function (dataId) {
                closeItem(dataId);
            },
            // 禁用按钮
            disable: function () {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).addClass("layer-disabled");
            },
            // 启用按钮
            enable: function () {
                var doc = window.top == window.parent ? window.document : window.parent.document;
                $("a[class*=layui-layer-btn]", doc).removeClass("layer-disabled");
            },
            // 打开遮罩层
            loading: function (message) {
                $.blockUI({ message: '<div class="loaderbox"><div class="loading-activity"></div> ' + message + '</div>' });
            },
            // 关闭遮罩层
            closeLoading: function () {
                setTimeout(function () {
                    $.unblockUI();
                }, 50);
            },
            // 重新加载
            reload: function () {
                parent.location.reload();
            }
        },
        // 操作封装处理
        operate: {
            // 提交数据
            submit: function (url, type, dataType, data, callback) {
                var config = {
                    url: url,
                    type: type,
                    dataType: dataType,
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("Đang xử lý, vui lòng đợi...");
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.ajaxSuccess(result);
                    }
                };
                $.ajax(config)
            },
            submitCustom: function (ajaxConfig, callback) {
                var config = {
                    beforeSend: function () {
                        $.modal.loading("Đang xử lý, vui lòng đợi...");
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.ajaxSuccess(result);
                    }
                };
                config.url = ajaxConfig.url;
                config.type = ajaxConfig.type;
                config.data = ajaxConfig.data;
                config.contentType = ajaxConfig.contentType;
                config.processData = ajaxConfig.processData;
                $.ajax(config)
            },
            // post请求传输
            post: function (url, data, callback) {
                $.operate.submit(url, "post", "json", data, callback);
            },
            // get请求传输
            get: function (url, callback) {
                $.operate.submit(url, "get", "json", "", callback);
            },
            // 详细信息
            detail: function (id, width, height) {
                table.set();
                var _url = $.operate.detailUrl(id);
                var options = {
                    title: "Chi tiết " + table.options.modalName,
                    width: width,
                    height: height,
                    url: _url,
                    skin: 'layui-layer-gray',
                    btn: ['Đóng'],
                    yes: function (index, layero) {
                        layer.close(index);
                    }
                };
                $.modal.openOptions(options);
            },
            // 详细信息，以tab页展现
            detailTab: function (id) {
                table.set();
                $.modal.openTab("Chi tiết " + table.options.modalName, $.operate.detailUrl(id));
            },
            // 详细访问地址
            detailUrl: function (id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.detailUrl.replace("{id}", id);
                } else {
                    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("Vui lòng chọn ít nhất 1 bản ghi");
                        return;
                    }
                    url = table.options.detailUrl.replace("{id}", id);
                }
                return url;
            },
            // 删除信息
            remove: function (id) {
                table.set();
                $.modal.confirm("Bạn có chắc chắn để xóa " + table.options.modalName + " này không？", function () {
                    var url = $.common.isEmpty(id) ? table.options.removeUrl : table.options.removeUrl.replace("{id}", id);
                    if (table.options.type == table_type.bootstrapTreeTable) {
                        $.operate.get(url);
                    } else {
                        var data = { "ids": id };
                        $.operate.submit(url, "post", "json", data);
                    }
                });
            },
            // Custom function remove with DELETE method
            removeCustom: function (id) {
                table.set();
                $.modal.confirm("Bạn có chắc chắn để xóa " + table.options.modalName + " này không？", function () {
                    var url = $.common.isEmpty(id) ? table.options.removeUrl : table.options.removeUrl.replace("{id}", id);
                    if (table.options.type == table_type.bootstrapTreeTable) {
                        $.operate.get(url);
                    } else {
                        var data = { "ids": id };
                        $.operate.submit(url, "delete", "json", data);
                    }
                });
            },
            // 批量删除信息
            removeAll: function () {
                table.set();
                var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                if (rows.length == 0) {
                    $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
                    return;
                }
                $.modal.confirm("Bạn có chắc chắn muốn xóa " + rows.length + " bản ghi đã chọn không?", function () {
                    var url = table.options.removeUrl;
                    var data = { "ids": rows.join() };
                    $.operate.submit(url, "post", "json", data);
                });
            },
            // Custom removeAll with DELETE method
            removeAllCustom: function () {
                table.set();
                var rows = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                if (rows.length == 0) {
                    $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
                    return;
                }
                $.modal.confirm("Bạn có chắc chắn muốn xóa " + rows.length + " bản ghi đã chọn không?", function () {
                    var url = table.options.removeUrl;
                    var data = { "ids": rows.join() };
                    $.operate.submit(url, "delete", "json", data);
                });
            },
            // 清空信息
            clean: function () {
                table.set();
                $.modal.confirm("Bạn có chắc chắn xóa tất cả " + table.options.modalName + " không?", function () {
                    var url = table.options.cleanUrl;
                    $.operate.submit(url, "post", "json", "");
                });
            },
            // 添加信息
            add: function (id) {
                table.set();
                $.modal.open("Thêm " + table.options.modalName, $.operate.addUrl(id));
            },
            // 添加信息，以tab页展现
            addTab: function (id) {
                table.set();
                $.modal.openTab("Thêm " + table.options.modalName, $.operate.addUrl(id));
            },
            // 添加信息 全屏
            addFull: function (id) {
                table.set();
                $.modal.openFull("Thêm " + table.options.modalName, $.operate.addUrl(id));
            },
            // 添加访问地址
            addUrl: function (id) {
                var url = $.common.isEmpty(id) ? table.options.createUrl.replace("{id}", "") : table.options.createUrl.replace("{id}", id);
                return url;
            },
            // 修改信息
            edit: function (id) {
                table.set();
                if ($.common.isEmpty(id) && table.options.type == table_type.bootstrapTreeTable) {
                    var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
                    if ($.common.isEmpty(row)) {
                        $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
                        return;
                    }
                    var url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    $.modal.open("Sửa đổi " + table.options.modalName, url);
                } else {
                    $.modal.open("Sửa đổi " + table.options.modalName, $.operate.editUrl(id));
                }
            },
            // 修改信息，以tab页展现
            editTab: function (id) {
                table.set();
                $.modal.openTab("Sửa đổi " + table.options.modalName, $.operate.editUrl(id));
            },
            // 修改信息 全屏
            editFull: function (id) {
                table.set();
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.updateUrl.replace("{id}", id);
                } else {
                    if (table.options.type == table_type.bootstrapTreeTable) {
                        var row = $("#" + table.options.id).bootstrapTreeTable('getSelections')[0];
                        if ($.common.isEmpty(row)) {
                            $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
                            return;
                        }
                        url = table.options.updateUrl.replace("{id}", row[table.options.uniqueId]);
                    } else {
                        var row = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                        url = table.options.updateUrl.replace("{id}", row);
                    }
                }
                $.modal.openFull("Sửa đổi " + table.options.modalName, url);
            },
            // 修改访问地址
            editUrl: function (id) {
                var url = "/404.html";
                if ($.common.isNotEmpty(id)) {
                    url = table.options.updateUrl.replace("{id}", id);
                } else {
                    var id = $.common.isEmpty(table.options.uniqueId) ? $.table.selectFirstColumns() : $.table.selectColumns(table.options.uniqueId);
                    if (id.length == 0) {
                        $.modal.alertWarning("Vui lòng chọn ít nhất một bản ghi");
                        return;
                    }
                    url = table.options.updateUrl.replace("{id}", id);
                }
                return url;
            },
            // 保存信息 刷新表格
            save: function (url, data, callback) {
                var config = {
                    url: url,
                    type: "post",
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("Đang xử lý .. Xin vui lòng chờ đợi...");
                        $.modal.disable();
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successCallback(result);
                    }
                };
                $.ajax(config)
            },
            // save data update 
            saveDataUpdate: function (url, data, callback) {
                var config = {
                    url: url,
                    type: "PUT",
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("Đang xử lý .. Xin vui lòng chờ đợi...");
                        $.modal.disable();
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successCallback(result);
                    }
                };
                $.ajax(config)
            },
            // Save form with more option content type/process data
            saveCustom: function (url, ajaxConfig, data, callback) {
                var config = {
                    url: url,
                    type: "post",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("Đang xử lý .. Xin vui lòng chờ đợi...");
                        $.modal.disable();
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successCallback(result);
                    }
                };
                if (ajaxConfig) {
                    config.contentType = ajaxConfig.contentType;
                    config.processData = ajaxConfig.contentType;
                    // Set method type if method parameter exists
                    if (ajaxConfig.method) {
                        config.type = ajaxConfig.method;
                    }
                }
                $.ajax(config)
            },
            // 保存信息 弹出提示框
            saveModal: function (url, data, callback) {
                var config = {
                    url: url,
                    type: "post",
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("Đang xử lý .. Xin vui lòng chờ đợi...");
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        if (result.code == web_status.SUCCESS) {
                            $.modal.alertSuccess(result.msg)
                        } else if (result.code == web_status.WARNING) {
                            $.modal.alertWarning(result.msg)
                        } else {
                            $.modal.alertError(result.msg);
                        }
                        $.modal.closeLoading();
                    }
                };
                $.ajax(config)
            },
            // 保存选项卡信息
            saveTab: function (url, data, callback) {
                var config = {
                    url: url,
                    type: "post",
                    dataType: "json",
                    data: data,
                    beforeSend: function () {
                        $.modal.loading("Đang xử lý .. Xin vui lòng chờ đợi...");
                    },
                    success: function (result) {
                        if (typeof callback == "function") {
                            callback(result);
                        }
                        $.operate.successTabCallback(result);
                    }
                };
                $.ajax(config)
            },
            // 保存结果弹出msg刷新table表格
            ajaxSuccess: function (result) {
                if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTable) {
                    $.modal.msgSuccess(result.msg);
                    $.table.refresh();
                } else if (result.code == web_status.SUCCESS && table.options.type == table_type.bootstrapTreeTable) {
                    $.modal.msgSuccess(result.msg);
                    $.treeTable.refresh();
                } else if (result.code == web_status.SUCCESS && $.common.isEmpty(table.options.type)) {
                    $.modal.msgSuccess(result.msg)
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            },
            // 成功结果提示msg（父窗体全局更新）
            saveSuccess: function (result) {
                if (result.code == web_status.SUCCESS) {
                    $.modal.msgReload("Đã lưu thành công, dữ liệu đang được làm mới, vui lòng đợi ...", modal_status.SUCCESS);
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            },
            // 成功回调执行事件（父窗体静默更新）
            successCallback: function (result) {
                if (result.code == web_status.SUCCESS) {
                    var parent = window.parent;
                    if (parent.table.options.type == table_type.bootstrapTable) {
                        $.modal.close();
                        parent.$.modal.msgSuccess(result.msg);
                        parent.$.table.refresh();
                    } else if (parent.table.options.type == table_type.bootstrapTreeTable) {
                        $.modal.close();
                        parent.$.modal.msgSuccess(result.msg);
                        parent.$.treeTable.refresh();
                    } else {
                        $.modal.msgReload("Đã lưu thành công, dữ liệu đang được làm mới, vui lòng đợi ...", modal_status.SUCCESS);
                    }
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
                $.modal.enable();
            },
            // 选项卡成功回调执行事件（父窗体静默更新）
            successTabCallback: function (result) {
                if (result.code == web_status.SUCCESS) {
                    var topWindow = $(window.parent.document);
                    var currentId = $('.page-tabs-content', topWindow).find('.active').attr('data-panel');
                    var $contentWindow = $('.RuoYi_iframe[data-id="' + currentId + '"]', topWindow)[0].contentWindow;
                    $.modal.close();
                    $contentWindow.$.modal.msgSuccess(result.msg);
                    $contentWindow.$(".layui-layer-padding").removeAttr("style");
                    if ($contentWindow.table.options.type == table_type.bootstrapTable) {
                        $contentWindow.$.table.refresh();
                    } else if ($contentWindow.table.options.type == table_type.bootstrapTreeTable) {
                        $contentWindow.$.treeTable.refresh();
                    }
                    $.modal.closeTab();
                } else if (result.code == web_status.WARNING) {
                    $.modal.alertWarning(result.msg)
                } else {
                    $.modal.alertError(result.msg);
                }
                $.modal.closeLoading();
            }
        },
        // 校验封装处理
        validate: {
            // 判断返回标识是否唯一 false 不存在 true 存在
            unique: function (value) {
                if (value == "0") {
                    return true;
                }
                return false;
            },
            // 表单验证
            form: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                var id = "#" + currentId;
                var validator = $(id).validate();
                let validateFormData = null;
                try {
                    validateFormData = $(id).validate().form();
                } catch (error) {
                    console.log('Error validate form function in firefox ' + error);
                    validateFormData = validator.numberOfInvalids() == 0 ? true : false;
                }
                return validateFormData;
            },
            // 重置表单验证（清除提示信息）
            reset: function (formId) {
                var currentId = $.common.isEmpty(formId) ? $('form').attr('id') : formId;
                return $("#" + currentId).validate().resetForm();
            }
        },
        // 树插件封装处理
        tree: {
            _option: {},
            _lastValue: {},
            // 初始化树结构
            init: function (options) {
                var defaults = {
                    id: "tree",                    // 属性ID
                    expandLevel: 0,                // 展开等级节点
                    view: {
                        selectedMulti: false,      // 设置是否允许同时选中多个节点
                        nameIsHTML: true           // 设置 name 属性是否支持 HTML 脚本
                    },
                    check: {
                        enable: false,             // 置 zTree 的节点上是否显示 checkbox / radio
                        nocheckInherit: true,      // 设置子节点是否自动继承
                        chkboxType: { "Y": "ps", "N": "ps" } // 父子节点的关联关系
                    },
                    data: {
                        key: {
                            title: "title"         // 节点数据保存节点提示信息的属性名称
                        },
                        simpleData: {
                            enable: true           // true / false 分别表示 使用 / 不使用 简单数据模式
                        }
                    },
                };
                var options = $.extend(defaults, options);
                $.tree._option = options;
                // 树结构初始化加载
                var setting = {
                    callback: {
                        onClick: options.onClick,                      // 用于捕获节点被点击的事件回调函数
                        onCheck: options.onCheck,                      // 用于捕获 checkbox / radio 被勾选 或 取消勾选的事件回调函数
                        onDblClick: options.onDblClick                 // 用于捕获鼠标双击之后的事件回调函数
                    },
                    check: options.check,
                    view: options.view,
                    data: options.data
                };
                $.get(options.url, function (data) {
                    var treeId = $("#treeId").val();
                    tree = $.fn.zTree.init($("#" + options.id), setting, data);
                    $._tree = tree;
                    for (var i = 0; i < options.expandLevel; i++) {
                        var nodes = tree.getNodesByParam("level", i);
                        for (var j = 0; j < nodes.length; j++) {
                            tree.expandNode(nodes[j], true, false, false);
                        }
                    }
                    var node = tree.getNodesByParam("id", treeId, null)[0];
                    $.tree.selectByIdName(treeId, node);
                    // 回调tree方法
                    if (typeof (options.callBack) === "function") {
                        options.callBack(tree);
                    }
                });
            },
            // 搜索节点
            searchNode: function () {
                // 取得输入的关键字的值
                var value = $.common.trim($("#keyword").val());
                if ($.tree._lastValue == value) {
                    return;
                }
                // 保存最后一次搜索名称
                $.tree._lastValue = value;
                var nodes = $._tree.getNodes();
                // 如果要查空字串，就退出不查了。
                if (value == "") {
                    $.tree.showAllNode(nodes);
                    return;
                }
                $.tree.hideAllNode(nodes);
                // 根据搜索值模糊匹配
                $.tree.updateNodes($._tree.getNodesByParamFuzzy("name", value));
            },
            // 根据Id和Name选中指定节点
            selectByIdName: function (treeId, node) {
                if ($.common.isNotEmpty(treeId) && node && treeId == node.id) {
                    $._tree.selectNode(node, true);
                }
            },
            // 显示所有节点
            showAllNode: function (nodes) {
                nodes = $._tree.transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    if (nodes[i].getParentNode() != null) {
                        $._tree.expandNode(nodes[i], true, false, false, false);
                    } else {
                        $._tree.expandNode(nodes[i], true, true, false, false);
                    }
                    $._tree.showNode(nodes[i]);
                    $.tree.showAllNode(nodes[i].children);
                }
            },
            // 隐藏所有节点
            hideAllNode: function (nodes) {
                var tree = $.fn.zTree.getZTreeObj("tree");
                var nodes = $._tree.transformToArray(nodes);
                for (var i = nodes.length - 1; i >= 0; i--) {
                    $._tree.hideNode(nodes[i]);
                }
            },
            // 显示所有父节点
            showParent: function (treeNode) {
                var parentNode;
                while ((parentNode = treeNode.getParentNode()) != null) {
                    $._tree.showNode(parentNode);
                    $._tree.expandNode(parentNode, true, false, false);
                    treeNode = parentNode;
                }
            },
            // 显示所有孩子节点
            showChildren: function (treeNode) {
                if (treeNode.isParent) {
                    for (var idx in treeNode.children) {
                        var node = treeNode.children[idx];
                        $._tree.showNode(node);
                        $.tree.showChildren(node);
                    }
                }
            },
            // 更新节点状态
            updateNodes: function (nodeList) {
                $._tree.showNodes(nodeList);
                for (var i = 0, l = nodeList.length; i < l; i++) {
                    var treeNode = nodeList[i];
                    $.tree.showChildren(treeNode);
                    $.tree.showParent(treeNode)
                }
            },
            // 获取当前被勾选集合
            getCheckedNodes: function (column) {
                var _column = $.common.isEmpty(column) ? "id" : column;
                var nodes = $._tree.getCheckedNodes(true);
                return $.map(nodes, function (row) {
                    return row[_column];
                }).join();
            },
            // 不允许根父节点选择
            notAllowParents: function (_tree) {
                var nodes = _tree.getSelectedNodes();
                if (nodes.length == 0) {
                    $.modal.msgError("Please select the node and submit");
                    return false;
                }
                for (var i = 0; i < nodes.length; i++) {
                    if (nodes[i].level == 0) {
                        $.modal.msgError("Cannot select the root node (" + nodes[i].name + "）");
                        return false;
                    }
                    if (nodes[i].isParent) {
                        $.modal.msgError("Cannot select parent node (" + nodes[i].name + "）");
                        return false;
                    }
                }
                return true;
            },
            // 不允许最后层级节点选择
            notAllowLastLevel: function (_tree) {
                var nodes = _tree.getSelectedNodes();
                for (var i = 0; i < nodes.length; i++) {
                    if (!nodes[i].isParent) {
                        $.modal.msgError("Cannot select the last level node (" + nodes[i].name + "）");
                        return false;
                    }
                }
                return true;
            },
            // 隐藏/显示搜索栏
            toggleSearch: function () {
                $('#search').slideToggle(200);
                $('#btnShow').toggle();
                $('#btnHide').toggle();
                $('#keyword').focus();
            },
            // 折叠
            collapse: function () {
                $._tree.expandAll(false);
            },
            // 展开
            expand: function () {
                $._tree.expandAll(true);
            }
        },
        // 通用方法封装处理
        common: {
            // 判断字符串是否为空
            isEmpty: function (value) {
                if (value == null || this.trim(value) == "") {
                    return true;
                }
                return false;
            },
            // 判断一个字符串是否为非空串
            isNotEmpty: function (value) {
                return !$.common.isEmpty(value);
            },
            // 空对象转字符串
            nullToStr: function (value) {
                if ($.common.isEmpty(value)) {
                    return "-";
                }
                return value;
            },
            // 是否显示数据 为空默认为显示
            visible: function (value) {
                if ($.common.isEmpty(value) || value == true) {
                    return true;
                }
                return false;
            },
            // 空格截取
            trim: function (value) {
                if (value == null) {
                    return "";
                }
                return value.toString().replace(/(^\s*)|(\s*$)|\r|\n/g, "");
            },
            // 比较两个字符串（大小写敏感）
            equals: function (str, that) {
                return str == that;
            },
            // 比较两个字符串（大小写不敏感）
            equalsIgnoreCase: function (str, that) {
                return String(str).toUpperCase() === String(that).toUpperCase();
            },
            // 将字符串按指定字符分割
            split: function (str, sep, maxLen) {
                if ($.common.isEmpty(str)) {
                    return null;
                }
                var value = String(str).split(sep);
                return maxLen ? value.slice(0, maxLen - 1) : value;
            },
            // 字符串格式化(%s )
            sprintf: function (str) {
                var args = arguments, flag = true, i = 1;
                str = str.replace(/%s/g, function () {
                    var arg = args[i++];
                    if (typeof arg === 'undefined') {
                        flag = false;
                        return '';
                    }
                    return arg == null ? '' : arg;
                });
                return flag ? str : '';
            },
            // 日期格式化 时间戳  -> yyyy-MM-dd HH-mm-ss
            dateFormat: function (date, format) {
                var that = this;
                if (that.isEmpty(date)) return "";
                if (!date) return;
                if (!format) format = "yyyy-MM-dd";
                switch (typeof date) {
                    case "string":
                        date = new Date(date.replace(/-/, "/"));
                        break;
                    case "number":
                        date = new Date(date);
                        break;
                }
                if (!date instanceof Date) return;
                var dict = {
                    "yyyy": date.getFullYear(),
                    "M": date.getMonth() + 1,
                    "d": date.getDate(),
                    "H": date.getHours(),
                    "m": date.getMinutes(),
                    "s": date.getSeconds(),
                    "MM": ("" + (date.getMonth() + 101)).substr(1),
                    "dd": ("" + (date.getDate() + 100)).substr(1),
                    "HH": ("" + (date.getHours() + 100)).substr(1),
                    "mm": ("" + (date.getMinutes() + 100)).substr(1),
                    "ss": ("" + (date.getSeconds() + 100)).substr(1)
                };
                return format.replace(/(yyyy|MM?|dd?|HH?|ss?|mm?)/g,
                    function () {
                        return dict[arguments[0]];
                    });
            },
            // 获取节点数据，支持多层级访问
            getItemField: function (item, field) {
                var value = item;
                if (typeof field !== 'string' || item.hasOwnProperty(field)) {
                    return item[field];
                }
                var props = field.split('.');
                for (var p in props) {
                    value = value && value[props[p]];
                }
                return value;
            },
            // 指定随机数返回
            random: function (min, max) {
                return Math.floor((Math.random() * max) + min);
            },
            // 判断字符串是否是以start开头
            startWith: function (value, start) {
                var reg = new RegExp("^" + start);
                return reg.test(value)
            },
            // 判断字符串是否是以end结尾
            endWith: function (value, end) {
                var reg = new RegExp(end + "$");
                return reg.test(value)
            },
            // 数组去重
            uniqueFn: function (array) {
                var result = [];
                var hashObj = {};
                for (var i = 0; i < array.length; i++) {
                    if (!hashObj[array[i]]) {
                        hashObj[array[i]] = true;
                        result.push(array[i]);
                    }
                }
                return result;
            },
            // 数组中的所有元素放入一个字符串
            join: function (array, separator) {
                if ($.common.isEmpty(array)) {
                    return null;
                }
                return array.join(separator);
            },
            // 获取form下所有的字段并转换为json对象
            formToJSON: function (formId) {
                var json = {};
                $.each($("#" + formId).serializeArray(), function (i, field) {
                    if (json[field.name]) {
                        json[field.name] += ("," + field.value);
                    } else {
                        json[field.name] = field.value;
                    }
                });
                return json;
            },
            // 数据字典转下拉框
            dictToSelect: function (datas, value, name) {
                var actions = [];
                actions.push($.common.sprintf("<select class='form-control' name='%s'>", name));
                $.each(datas, function (index, dict) {
                    actions.push($.common.sprintf("<option value='%s'", dict.dictValue));
                    if (dict.dictValue == ('' + value)) {
                        actions.push(' selected');
                    }
                    actions.push($.common.sprintf(">%s</option>", dict.dictLabel));
                });
                actions.push('</select>');
                return actions.join('');
            },
            // 获取obj对象长度
            getLength: function (obj) {
                var count = 0;
                for (var i in obj) {
                    if (obj.hasOwnProperty(i)) {
                        count++;
                    }
                }
                return count;
            },
            // 判断移动端
            isMobile: function () {
                return navigator.userAgent.match(/(Android|iPhone|SymbianOS|Windows Phone|iPad|iPod)/i);
            },
            // 数字正则表达式，只能为0-9数字
            numValid: function (text) {
                var patten = new RegExp(/^[0-9]+$/);
                return patten.test(text);
            },
            // 英文正则表达式，只能为a-z和A-Z字母
            enValid: function (text) {
                var patten = new RegExp(/^[a-zA-Z]+$/);
                return patten.test(text);
            },
            // 英文、数字正则表达式，必须包含（字母，数字）
            enNumValid: function (text) {
                var patten = new RegExp(/^(?=.*[a-zA-Z]+)(?=.*[0-9]+)[a-zA-Z0-9]+$/);
                return patten.test(text);
            },
            // 英文、数字、特殊字符正则表达式，必须包含（字母，数字，特殊字符!@#$%^&*()-=_+）
            charValid: function (text) {
                var patten = new RegExp(/^(?=.*[A-Za-z])(?=.*\d)(?=.*[~!@#\$%\^&\*\(\)\-=_\+])[A-Za-z\d~!@#\$%\^&\*\(\)\-=_\+]{6,}$/);
                return patten.test(text);
            },
            currencyFormat: function(money){
                if (!money){
                    return '';
                }
                return Number(money).toFixed(2).replace(/\d(?=(\d{3})+\.)/g, '$&,');
                // if (!this.numValid(money)){
                //     return money;
                // }
                // return (Number(money)).toLocaleString('it-IT', {style : 'currency', currency : 'VND'});
            }
        }
    });
})(jQuery);

/** 表格类型 */
table_type = {
    bootstrapTable: 0,
    bootstrapTreeTable: 1
};

/** 消息状态码 */
web_status = {
    SUCCESS: 0,
    FAIL: 500,
    WARNING: 301
};

/** 弹窗状态码 */
modal_status = {
    SUCCESS: "success",
    FAIL: "error",
    WARNING: "warning"
};