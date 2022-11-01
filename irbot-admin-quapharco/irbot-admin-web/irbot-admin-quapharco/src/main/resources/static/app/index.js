/**
 * Home method encapsulation processing
 * Copyright (c) 2019 ruoyi
 */

// Open left menu when click menuItem in topnav menu on mobile
function clickTopMenu(menuId) {
  if (isMobile) {
    // $("body").toggleClass("canvas-menu");
    $("body").removeClass("canvas-menu");
    $(".navbar-static-side").fadeIn();
  }
}

layer.config({
  extend: "moon/style.css",
  skin: "layer-ext-moon",
});

var isMobile = false;
var sidebarHeight = isMobile ? "100%" : "96%";

$(function () {
  // MetsiMenu
  $("#side-menu").metisMenu();

  // Fixed menu bar
  $(".sidebar-collapse").slimScroll({
    height: sidebarHeight,
    railOpacity: 0.9,
    alwaysVisible: false,
  });

  // Menu switch
  $(".navbar-minimalize").click(function () {
    if (isMobile) {
      $("body").toggleClass("canvas-menu");
    } else {
      $("body").toggleClass("mini-navbar");
    }
    SmoothlyMenu();
  });

  $("#side-menu>li").click(function () {
    if ($("body").hasClass("canvas-menu mini-navbar")) {
      NavToggle();
    }
  });
  $("#side-menu>li li a:not(:has(span))").click(function () {
    if ($(window).width() < 769) {
      NavToggle();
    }
  });

  $(".nav-close").click(NavToggle);

  //ios browser compatibility handling
  if (/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) {
    $("#content-main").css("overflow-y", "hidden");
  }
});

$(window).bind("load resize", function () {
  isMobile = $.common.isMobile() || $(window).width() < 769;
  if (isMobile) {
    $("body").addClass("canvas-menu");
    $("body").removeClass("mini-navbar");
    $("nav .logo").addClass("hide");
    $(".slimScrollDiv").css({ overflow: "hidden" });
    $(".navbar-static-side").fadeOut();
  } else {
    $("body").addClass("fixed-sidebar");
    $("body").removeClass("canvas-menu");
    $("body").removeClass("mini-navbar");
    $("nav .logo").removeClass("hide");
    $(".slimScrollDiv").css({ overflow: "visible" });
    $(".user-panel").css({ display: "none" });
    $(".navbar-static-side").fadeIn();
  }
});

function syncMenuTab(dataId) {
  if (isLinkage) {
    var $dataObj = $('a[href$="' + decodeURI(dataId) + '"]');
    if ($dataObj.attr("class") != null && !$dataObj.hasClass("noactive")) {
      $(".nav ul").removeClass("in");
      $dataObj.parents("ul").addClass("in");
      $dataObj
        .parents("li")
        .addClass("active")
        .siblings()
        .removeClass("active")
        .find("li")
        .removeClass("active");
      $dataObj.parents("ul").css("height", "auto").height();
      $(".nav ul li, .nav li").removeClass("selected");
      $dataObj.parent("li").addClass("selected");
      setIframeUrl(dataId);

      // Top menu synchronization
      var tabStr = $dataObj.parents(".tab-pane").attr("id");
      if ($.common.isNotEmpty(tabStr)) {
        var sepIndex = tabStr.lastIndexOf("_");
        var menuId = tabStr.substring(sepIndex + 1, tabStr.length);
        $("#tab_" + menuId + " a").click();
        console.log("clicked");
      }
    }
  }
}

function NavToggle() {
  $(".navbar-minimalize").trigger("click");
}

function fixedSidebar() {
  $("#side-menu").hide();
  $("nav .logo").addClass("hide");
  $(".user-panel").css({ display: "block" });
  setTimeout(function () {
    $("#side-menu").fadeIn(500);
  }, 100);
}

// 设置锚点
function setIframeUrl(href) {
  if ($.common.equals("history", mode)) {
    storage.set("publicPath", href);
  } else {
    var nowUrl = window.location.href;
    var newUrl = nowUrl.substring(0, nowUrl.indexOf("#"));
    window.location.href = newUrl + "#" + href;
  }
}

function SmoothlyMenu() {
  if (isMobile && !$("body").hasClass("canvas-menu")) {
    $(".navbar-static-side").fadeIn();
    $(".user-panel").css({ display: "none" });
    fixedSidebar();
  } else if (!isMobile && !$("body").hasClass("mini-navbar")) {
    fixedSidebar();
    $("nav .logo").removeClass("hide");
    $(".user-panel").css({ display: "none" });
  } else if (isMobile && $("body").hasClass("fixed-sidebar")) {
    $(".navbar-static-side").fadeOut();
    fixedSidebar();
  } else if (!isMobile && $("body").hasClass("fixed-sidebar")) {
    fixedSidebar();
  } else {
    $("#side-menu").removeAttr("style");
  }
}

/**
 * iframe handling
 */
$(function () {
  //Calculate the total width of the collection of elements
  function calSumWidth(elements) {
    var width = 0;
    $(elements).each(function () {
      width += $(this).outerWidth(true);
    });
    return width;
  }

  // Activate the specified tab
  function setActiveTab(element) {
    if (!$(element).hasClass("active")) {
      var currentId = $(element).data("id");
      syncMenuTab(currentId);
      // Display the content area corresponding to the tab
      $(".RuoYi_iframe").each(function () {
        if ($(this).data("id") == currentId) {
          $(this).show().siblings(".RuoYi_iframe").hide();
        }
      });
      $(element).addClass("active").siblings(".menuTab").removeClass("active");
      scrollToTab(element);
    }
  }

  //Scroll to the specified tab
  function scrollToTab(element) {
    var marginLeftVal = calSumWidth($(element).prevAll()),
      marginRightVal = calSumWidth($(element).nextAll());
    // Non-tab width of viewable area
    var tabOuterWidth = calSumWidth(
      $(".content-tabs").children().not(".menuTabs")
    );
    //Viewable area tab width
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    //Actual scroll width
    var scrollVal = 0;
    if ($(".page-tabs-content").outerWidth() < visibleWidth) {
      scrollVal = 0;
    } else if (
      marginRightVal <=
      visibleWidth -
        $(element).outerWidth(true) -
        $(element).next().outerWidth(true)
    ) {
      if (visibleWidth - $(element).next().outerWidth(true) > marginRightVal) {
        scrollVal = marginLeftVal;
        var tabElement = element;
        while (
          scrollVal - $(tabElement).outerWidth() >
          $(".page-tabs-content").outerWidth() - visibleWidth
        ) {
          scrollVal -= $(tabElement).prev().outerWidth();
          tabElement = $(tabElement).prev();
        }
      }
    } else if (
      marginLeftVal >
      visibleWidth -
        $(element).outerWidth(true) -
        $(element).prev().outerWidth(true)
    ) {
      scrollVal = marginLeftVal - $(element).prev().outerWidth(true);
    }
    $(".page-tabs-content").animate(
      { marginLeft: 0 - scrollVal + "px" },
      "fast"
    );
  }

  //View the hidden tabs on the left
  function scrollTabLeft() {
    var marginLeftVal = Math.abs(
      parseInt($(".page-tabs-content").css("margin-left"))
    );
    // Non-tab width of viewable area
    var tabOuterWidth = calSumWidth(
      $(".content-tabs").children().not(".menuTabs")
    );
    //Viewable area tab width
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    //Actual scroll width
    var scrollVal = 0;
    if ($(".page-tabs-content").width() < visibleWidth) {
      return false;
    } else {
      var tabElement = $(".menuTab:first");
      var offsetVal = 0;
      while (offsetVal + $(tabElement).outerWidth(true) <= marginLeftVal) {
        //找到离当前tab最近的元素
        offsetVal += $(tabElement).outerWidth(true);
        tabElement = $(tabElement).next();
      }
      offsetVal = 0;
      if (calSumWidth($(tabElement).prevAll()) > visibleWidth) {
        while (
          offsetVal + $(tabElement).outerWidth(true) < visibleWidth &&
          tabElement.length > 0
        ) {
          offsetVal += $(tabElement).outerWidth(true);
          tabElement = $(tabElement).prev();
        }
        scrollVal = calSumWidth($(tabElement).prevAll());
      }
    }
    $(".page-tabs-content").animate(
      { marginLeft: 0 - scrollVal + "px" },
      "fast"
    );
  }

  //View the hidden tabs on the right
  function scrollTabRight() {
    var marginLeftVal = Math.abs(
      parseInt($(".page-tabs-content").css("margin-left"))
    );
    // Non-tab width of viewable area
    var tabOuterWidth = calSumWidth(
      $(".content-tabs").children().not(".menuTabs")
    );
    //Viewable area tab width
    var visibleWidth = $(".content-tabs").outerWidth(true) - tabOuterWidth;
    //Actual scroll width
    var scrollVal = 0;
    if ($(".page-tabs-content").width() < visibleWidth) {
      return false;
    } else {
      var tabElement = $(".menuTab:first");
      var offsetVal = 0;
      while (offsetVal + $(tabElement).outerWidth(true) <= marginLeftVal) {
        //Find the element closest to the current tab
        offsetVal += $(tabElement).outerWidth(true);
        tabElement = $(tabElement).next();
      }
      offsetVal = 0;
      while (
        offsetVal + $(tabElement).outerWidth(true) < visibleWidth &&
        tabElement.length > 0
      ) {
        offsetVal += $(tabElement).outerWidth(true);
        tabElement = $(tabElement).next();
      }
      scrollVal = calSumWidth($(tabElement).prevAll());
      if (scrollVal > 0) {
        $(".page-tabs-content").animate(
          { marginLeft: 0 - scrollVal + "px" },
          "fast"
        );
      }
    }
  }

  //Add the data-index attribute to the menu item by traversing
  $(".menuItem").each(function (index) {
    if (!$(this).attr("data-index")) {
      $(this).attr("data-index", index);
    }
  });

  function menuItem() {
    // Get identification data
    var dataUrl = $(this).attr("href"),
      dataIndex = $(this).data("index"),
      menuName = $.trim($(this).text()),
      isRefresh = $(this).data("refresh"),
      flag = true;

    var $dataObj = $('a[href$="' + decodeURI(dataUrl) + '"]');
    if (!$dataObj.hasClass("noactive")) {
      $(".tab-pane li").removeClass("active");
      $(".nav ul").removeClass("in");
      $dataObj.parents("ul").addClass("in");
      $dataObj
        .parents("li")
        .addClass("active")
        .siblings()
        .removeClass("active")
        .find("li")
        .removeClass("active");
      $dataObj.parents("ul").css("height", "auto").height();
      $(".nav ul li, .nav li").removeClass("selected");

      $(this).parent("li").addClass("selected");
      if (isMobile) {
        // $("body").toggleClass("canvas-menu");
        if (!$("body").hasClass("canvas-menu")) {
          $("body").addClass("canvas-menu");
        }
        $(".navbar-static-side").fadeOut();
      }
    }
    setIframeUrl(dataUrl);
    if (dataUrl == undefined || $.trim(dataUrl).length == 0) return false;

    //Tab menu already exists
    $(".menuTab").each(function () {
      if ($(this).data("id") == dataUrl) {
        if (!$(this).hasClass("active")) {
          $(this).addClass("active").siblings(".menuTab").removeClass("active");
          scrollToTab(this);
          // Display the content area corresponding to the tab
          $(".mainContent .RuoYi_iframe").each(function () {
            if ($(this).data("id") == dataUrl) {
              $(this).show().siblings(".RuoYi_iframe").hide();
              return false;
            }
          });
        }
        if (isRefresh) {
          refreshTab();
        }
        flag = false;
        return false;
      }
    });
    // Tab menu does not exist
    if (flag) {
      var str =
        '<a href="javascript:;" class="active menuTab" data-id="' +
        dataUrl +
        '">' +
        menuName +
        ' <i class="fa fa-times-circle"></i></a>';
      $(".menuTab").removeClass("active");

      // Add the iframe corresponding to the tab
      var str1 =
        '<iframe class="RuoYi_iframe" name="iframe' +
        dataIndex +
        '" width="100%" height="100%" src="' +
        dataUrl +
        '" frameborder="0" data-id="' +
        dataUrl +
        '" seamless></iframe>';
      $(".mainContent")
        .find("iframe.RuoYi_iframe")
        .hide()
        .parents(".mainContent")
        .append(str1);

      $.modal.loading("Đang tải dữ liệu, vui lòng đợi ...");

      $(".mainContent iframe:visible").load(function () {
        $.modal.closeLoading();
      });

      // Add tab
      $(".menuTabs .page-tabs-content").append(str);
      scrollToTab($(".menuTab.active"));
    }
    return false;
  }

  function menuBlank() {
    // The new window opens the external network starts with http://
    var dataUrl = $(this).attr("href");
    window.open(dataUrl);
    return false;
  }

  $(".menuItem").on("click", menuItem);

  $(".menuBlank").on("click", menuBlank);

  // 关闭选项卡菜单
  function closeTab() {
    var closeTabId = $(this).parents(".menuTab").data("id");
    var currentWidth = $(this).parents(".menuTab").width();
    var panelUrl = $(this).parents(".menuTab").data("panel");
    // The current element is active
    if ($(this).parents(".menuTab").hasClass("active")) {
      // There are sibling elements behind the current element, making the next element active
      if ($(this).parents(".menuTab").next(".menuTab").size()) {
        var activeId = $(this)
          .parents(".menuTab")
          .next(".menuTab:eq(0)")
          .data("id");
        $(this).parents(".menuTab").next(".menuTab:eq(0)").addClass("active");

        $(".mainContent .RuoYi_iframe").each(function () {
          if ($(this).data("id") == activeId) {
            $(this).show().siblings(".RuoYi_iframe").hide();
            return false;
          }
        });

        var marginLeftVal = parseInt(
          $(".page-tabs-content").css("margin-left")
        );
        if (marginLeftVal < 0) {
          $(".page-tabs-content").animate(
            { marginLeft: marginLeftVal + currentWidth + "px" },
            "fast"
          );
        }

        //  Remove current tab
        $(this).parents(".menuTab").remove();

        //Remove the content area corresponding to the tab
        $(".mainContent .RuoYi_iframe").each(function () {
          if ($(this).data("id") == closeTabId) {
            $(this).remove();
            return false;
          }
        });
      }

      // There is no sibling element behind the current element, so that the previous element of the current element is active
      if ($(this).parents(".menuTab").prev(".menuTab").size()) {
        var activeId = $(this)
          .parents(".menuTab")
          .prev(".menuTab:last")
          .data("id");
        $(this).parents(".menuTab").prev(".menuTab:last").addClass("active");
        $(".mainContent .RuoYi_iframe").each(function () {
          if ($(this).data("id") == activeId) {
            $(this).show().siblings(".RuoYi_iframe").hide();
            return false;
          }
        });

        //  Remove current tab
        $(this).parents(".menuTab").remove();

        // Remove the content area corresponding to the tab
        $(".mainContent .RuoYi_iframe").each(function () {
          if ($(this).data("id") == closeTabId) {
            $(this).remove();
            return false;
          }
        });

        if ($.common.isNotEmpty(panelUrl)) {
          $('.menuTab[data-id="' + panelUrl + '"]')
            .addClass("active")
            .siblings(".menuTab")
            .removeClass("active");
          $(".mainContent .RuoYi_iframe").each(function () {
            if ($(this).data("id") == panelUrl) {
              $(this).show().siblings(".RuoYi_iframe").hide();
              return false;
            }
          });
        }
      }
    }
    // The current element is not active
    else {
      //  Remove current tab
      $(this).parents(".menuTab").remove();

      // Remove the content area corresponding to the corresponding tab
      $(".mainContent .RuoYi_iframe").each(function () {
        if ($(this).data("id") == closeTabId) {
          $(this).remove();
          return false;
        }
      });
    }
    scrollToTab($(".menuTab.active"));
    syncMenuTab($(".page-tabs-content").find(".active").attr("data-id"));
    return false;
  }

  $(".menuTabs").on("click", ".menuTab i", closeTab);

  //Scroll to the activated tab
  function showActiveTab() {
    scrollToTab($(".menuTab.active"));
  }
  $(".tabShowActive").on("click", showActiveTab);

  // Click on the tab menu
  function activeTab() {
    if (!$(this).hasClass("active")) {
      var currentId = $(this).data("id");
      syncMenuTab(currentId);
      // Display the content area corresponding to the tab
      $(".mainContent .RuoYi_iframe").each(function () {
        if ($(this).data("id") == currentId) {
          $(this).show().siblings(".RuoYi_iframe").hide();
          return false;
        }
      });
      $(this).addClass("active").siblings(".menuTab").removeClass("active");

      scrollToTab(this);
    }
  }

  // Click on the tab menu
  $(".menuTabs").on("click", ".menuTab", activeTab);

  // Refresh iframe
  function refreshTab() {
    var currentId = $(".page-tabs-content").find(".active").attr("data-id");
    var target = $('.RuoYi_iframe[data-id="' + currentId + '"]');
    var url = target.attr("src");
    target.attr("src", url).ready();
  }

  // Tab full screen
  function fullScreenTab() {
    var currentId = $(".page-tabs-content").find(".active").attr("data-id");
    var target = $('.RuoYi_iframe[data-id="' + currentId + '"]');
    target.fullScreen(true);
  }

  // Close current tab
  function tabCloseCurrent() {
    $(".page-tabs-content").find(".active i").trigger("click");
  }

  //Close others tab
  function tabCloseOther() {
    $(".page-tabs-content")
      .children("[data-id]")
      .not(":first")
      .not(".active")
      .each(function () {
        $('.RuoYi_iframe[data-id="' + $(this).data("id") + '"]').remove();
        $(this).remove();
      });
    $(".page-tabs-content").animate({ marginLeft: "0px" }, "fast");
  }

  // Close all tabs
  function tabCloseAll() {
    $(".page-tabs-content")
      .children("[data-id]")
      .not(":first")
      .each(function () {
        $('.RuoYi_iframe[data-id="' + $(this).data("id") + '"]').remove();
        $(this).remove();
      });
    $(".page-tabs-content")
      .children("[data-id]:first")
      .each(function () {
        $('.RuoYi_iframe[data-id="' + $(this).data("id") + '"]').show();
        $(this).addClass("active");
      });
    $(".page-tabs-content").css("margin-left", "0");
    syncMenuTab($(".page-tabs-content").find(".active").attr("data-id"));
  }

  // FullScreen
  $("#fullScreen").on("click", function () {
    $(document).toggleFullScreen();
  });

  // LockScreen
  $("#lockScreen").on("click", function () {
    storage.set(
      "lockPath",
      $(".page-tabs-content").find(".active").attr("data-id")
    );
    location.href = ctx + "lockscreen";
  });

  // 页签刷新按钮
  $(".tabReload").on("click", refreshTab);

  // 页签全屏按钮
  $(".tabFullScreen").on("click", fullScreenTab);

  // 双击选项卡全屏显示
  $(".menuTabs").on("dblclick", ".menuTab", activeTabMax);

  // 左移按扭
  $(".tabLeft").on("click", scrollTabLeft);

  // 右移按扭
  $(".tabRight").on("click", scrollTabRight);

  // 关闭当前
  $(".tabCloseCurrent").on("click", tabCloseCurrent);

  // 关闭其他
  $(".tabCloseOther").on("click", tabCloseOther);

  // 关闭全部
  $(".tabCloseAll").on("click", tabCloseAll);

  // tab full screen display
  $(".tabMaxCurrent").on("click", function () {
    $(".page-tabs-content").find(".active").trigger("dblclick");
  });

  // Close full screen
  $("#ax_close_max").click(function () {
    $("#content-main").toggleClass("max");
    $("#ax_close_max").hide();
  });

  // Double-click the tab to display in full screen
  function activeTabMax() {
    $("#content-main").toggleClass("max");
    $("#ax_close_max").show();
  }

  $(window).keydown(function (event) {
    if (event.keyCode == 27) {
      $("#content-main").removeClass("max");
      $("#ax_close_max").hide();
    }
  });

  window.onhashchange = function () {
    var hash = location.hash;
    var url = hash.substring(1, hash.length);
    $('a[href$="' + url + '"]').click();
  };

  // Right-click menu implementation
  $.contextMenu({
    selector: ".menuTab",
    trigger: "right",
    autoHide: true,
    items: {
      close_current: {
        name: "Đóng",
        icon: "fa-close",
        callback: function (key, opt) {
          opt.$trigger.find("i").trigger("click");
        },
      },
      close_other: {
        name: "Đóng các thẻ khác",
        icon: "fa-window-close-o",
        callback: function (key, opt) {
          setActiveTab(this);
          tabCloseOther();
        },
      },
      close_left: {
        name: "Đóng các thẻ bên trái",
        icon: "fa-reply",
        callback: function (key, opt) {
          setActiveTab(this);
          this.prevAll(".menuTab")
            .not(":last")
            .each(function () {
              if ($(this).hasClass("active")) {
                setActiveTab(this);
              }
              $('.RuoYi_iframe[data-id="' + $(this).data("id") + '"]').remove();
              $(this).remove();
            });
          $(".page-tabs-content").animate({ marginLeft: "0px" }, "fast");
        },
      },
      close_right: {
        name: "Đóng các thẻ bên phải",
        icon: "fa-share",
        callback: function (key, opt) {
          setActiveTab(this);
          this.nextAll(".menuTab").each(function () {
            $('.menuTab[data-id="' + $(this).data("id") + '"]').remove();
            $(this).remove();
          });
        },
      },
      close_all: {
        name: "Đóng tất cả các thẻ",
        icon: "fa-window-close",
        callback: function (key, opt) {
          tabCloseAll();
        },
      },
      step: "---------",
      full: {
        name: "Hiển thị toàn màn hình",
        icon: "fa-arrows-alt",
        callback: function (key, opt) {
          setActiveTab(this);
          var target = $('.RuoYi_iframe[data-id="' + this.data("id") + '"]');
          target.fullScreen(true);
        },
      },
      refresh: {
        name: "Làm mới",
        icon: "fa-refresh",
        callback: function (key, opt) {
          setActiveTab(this);
          var target = $('.RuoYi_iframe[data-id="' + this.data("id") + '"]');
          var url = target.attr("src");
          $.modal.loading("Đang tải dữ liệu, vui lòng đợi ...");
          target.attr("src", url).load(function () {
            $.modal.closeLoading();
          });
        },
      },
      open: {
        name: "Mở trong một cửa sổ mới",
        icon: "fa-link",
        callback: function (key, opt) {
          var target = $('.RuoYi_iframe[data-id="' + this.data("id") + '"]');
          window.open(target.attr("src"));
        },
      },
    },
  });
});
