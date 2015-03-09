window.onload = function() {
    
    // References to elements on the page
    var form = document.getElementById('message-form');
    var serverField = document.getElementById('server');
    var channelField = document.getElementById('channel');
    var idField = document.getElementById('idNum');
    var result = document.getElementById('results');
    var details = document.getElementById('details');
    var subscriptionList = document.getElementById('subscriptions');
    var connectBtn = document.getElementById('connect');
    var disconnectBtn = document.getElementById('disconnect');
    var subscribeBtn = document.getElementById('subscribe');
    var pauseBtn = document.getElementById('pause');
    var resumeBtn = document.getElementById('resume');
    var unsubscribeBtn = document.getElementById('unsubscribe');
    var clearBtn = document.getElementById('clear');
    
    var socket;

    serverField.value = "ws://" + window.location.host + "/web-pods/socket";
    
    connectBtn.onclick = function() {
        socket = new Client(serverField.value);
        idField.value = 0;
    }
    
    function callbackTest(event) {
        console.log('this is the callback test');
        console.log(event.value.value);
    }
    
    subscribeBtn.onclick = function() {
        var channel = channelField.value;
        var id = idField.value;
        socket.subscribeChannel(channel, callbackTest);
        idField.value = idField.value + 1;
    }
    
    
    
}