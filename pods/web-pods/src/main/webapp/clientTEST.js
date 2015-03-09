window.onload = function() {
    
    // References to elements on the page
    var form = document.getElementById('message-form');
    var serverField = document.getElementById('server');
    var channelField = document.getElementById('channel');
    var idField = document.getElementById('idNum');
    var results = document.getElementById('results');
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
    var selectedId;
    
    var eventDetails = [];

    serverField.value = "ws://" + window.location.host + "/web-pods/socket";
    
    connectBtn.onclick = function() {
        socket = new Client(serverField.value);
        idField.value = 0;
    }
    
    function addEventDetails(event) {
        eventDetails.unshift('<div><pre>' + JSON.stringify(event, null, '     ') + '</pre></div>');
    }
    
    function newMessage(event) {
        var newEvent = document.createElement('option');
        results.insertBefore(newEvent, results.childNodes[0]);
        newEvent.appendChild(document.createTextNode(event.value.value));
        addEventDetails(event);
    }
    
    function displayDetails(id) {
        details.innerHTML = eventDetails[id];
    }
    
    subscribeBtn.onclick = function() {
        var channel = channelField.value;
        var id = idField.value;
        socket.subscribeChannel(channel, newMessage);
        idField.value = idField.value + 1;
    }
    
    results.onchange = function() {
        selectedId = results.selectedIndex;
        displayDetails(selectedId);
    }
    
    
    
}