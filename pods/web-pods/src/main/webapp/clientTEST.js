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
    var selectedEvent;
    var selectedId;
    
    var eventDetails = [];

    serverField.value = "ws://" + window.location.host + "/web-pods/socket";

    
    function addEventDetails(event) {
        eventDetails.unshift('<div><pre>' + JSON.stringify(event, null, '     ') + '</pre></div>');
    }
    
    function addNewSubscription(channel, id) {
        var newChannel = document.createElement('option');
        subscriptionList.appendChild(newChannel);
        newChannel.appendChild(document.createTextNode('id: ' + id + ', channel: ' + channel));
    }
    
    function newMessage(event) {
        var eventDisplay = getEventDisplay(event);
        var newEvent = document.createElement('option');
        results.insertBefore(newEvent, results.childNodes[0]);
        newEvent.appendChild(document.createTextNode(eventDisplay));
        addEventDetails(event);
    }
    
    function getEventDisplay(event) {
        if (event.type == 'error') { // Error
            return 'Error';
        }
        else if (event.type == 'connection') { // New subscription
            return 'Successful subscription';
        }
        else if (event.value.type.name == 'VTable') { // Table
            return 'Table';
        }
        else if (event.type == 'value') { // New value
            return event.value.value;
        }
    }
    
    function displayDetails(index) {
        details.innerHTML = eventDetails[index];
    }
        
    connectBtn.onclick = function() {
        socket = new Client(serverField.value);
        idField.value = 0;
    }
    
    disconnectBtn.onclick = function() {
        socket.close();
    }
    
    subscribeBtn.onclick = function() {
        var channel = channelField.value;
        var id = idField.value;
        socket.subscribeChannel(channel, newMessage);
        addNewSubscription(channel, id);
        subscriptionList.selectedIndex = id;
        selectedId = id;
        id++;
        idField.value = id;
    }
    
    pauseBtn.onclick = function() {
        var channel = socket.getChannel(selectedId);
        channel.pause();
    }
    
    resumeBtn.onclick = function() {
        var channel = socket.getChannel(selectedId);
        channel.resume();
    }
  
    results.onchange = function() {
        selectedEvent = results.selectedIndex;
        displayDetails(selectedEvent);
    }
    
    subscriptionList.onchange = function() {
        selectedId = subscriptionList.selectedIndex;
    }
    
}