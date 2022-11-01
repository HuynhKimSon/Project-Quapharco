var stompClient = null;
const ENDPOINT = ctx + 'app_admin';
const DESTINATION_PREFIX = ctx + 'app_admin/';

(function ($) {
  $.extend({
    websocket: {
      connect: function(headers = {}, connectCallback, errorCallback){
        let socket = new SockJS(ENDPOINT);
        stompClient = Stomp.over(socket);
      
        stompClient.connect(headers, connectCallback, errorCallback);
      },
      disconnect: function(disconnectCallback, headers = {}){
        if (stompClient != null){
          stompClient.disconnect(disconnectCallback, headers);
        }
      },
      subscribe: function(destination, callback, headers = {}){
        if (stompClient == null){
          console.error("Please connect to websocket server before subcribe!");
          return;
        }
        return stompClient.subscribe(DESTINATION_PREFIX + destination, callback, headers);
      },
      client: stompClient
    }
  });
})(jQuery);