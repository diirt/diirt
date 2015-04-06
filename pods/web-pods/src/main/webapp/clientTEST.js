window.onload = function() {
    
    // References to elements on the page
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
    var filterBtn = document.getElementById('filter');
    var showAllBtn = document.getElementById('showAll');
    
    var socket;
    var selectedEvent;
    var selectedId;
    var filter = 'none';
    
    var events = [[], [], []]; // [eventDisplay, id, selected (bool)]
    var eventDetails = [];
    var eventDetailsFiltered = [];
    
    
    serverField.value = "ws://" + window.location.host + "/web-pods/socket";
    
    function addNewSubscription(channel, id) {
        var newChannel = document.createElement('option');
        subscriptionList.appendChild(newChannel);
        newChannel.appendChild(document.createTextNode('id: ' + id + ', channel: ' + channel));
        eventDetailsFiltered.push([]);
    }
    
    function addEventDetails(event) {
        eventDetails.unshift('<div><pre>' + JSON.stringify(event, null, '     ') + '</pre></div>');
        eventDetailsFiltered[event.id].unshift('<div><pre>' + JSON.stringify(event, null, '     ') + '</pre></div>');
    }
    
    function displayEvent(eventDisplay, event) {
        var scrollVal = results.scrollTop;
        var newEvent = document.createElement('option');
        results.insertBefore(newEvent, results.childNodes[0]);
        newEvent.appendChild(document.createTextNode(eventDisplay));
        var attId = document.createAttribute('eventId');
        attId.value = event.id;
        newEvent.setAttributeNode(attId);
        if (scrollVal == 0) {
            results.scrollTop = 0;
        }
        else {
            results.scrollTop = scrollVal + 17; // Height of an option elt added
        }
    }
    
    function getEventDisplay(event) {
        if (event.type == 'error') { // Error
            return 'Error';
        }
        else if (event.type == 'connection') { // New subscription
            subscriptionList.children[event.id].className = 'open';
            return 'Successful subscription';
        }
        else if (event.value.type.name == 'VTable') { // Table
            return 'Table';
        }
        else if (event.type == 'value') { // New value
            return event.value.value;
        }
    }
    
    function newMessage(event) {
        var eventDisplay = getEventDisplay(event);
        addEventDetails(event);
        events[0].unshift(eventDisplay);
        events[1].unshift(event.id);
        events[2].unshift(false);
        if (event.id == filter || filter == 'none') {
            displayEvent(eventDisplay, event);  
        }
    }
    
    function displayDetails(index) {
        if (filter == 'none') {
            details.innerHTML = eventDetails[index];
        }
        else {
            details.innerHTML = eventDetailsFiltered[filter][index];
        }
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
        subscriptionList.children[selectedId].className = 'closed';
    }
    
    resumeBtn.onclick = function() {
        var channel = socket.getChannel(selectedId);
        channel.resume();
        if (channel.isConnected()) {
            subscriptionList.children[selectedId].className = 'open';
        }
    }
    
    unsubscribeBtn.onclick = function() {
        var channel = socket.getChannel(selectedId);
        channel.unsubscribe();
        subscriptionList.children[selectedId].className = 'unsubscribed';
    }
  
    results.onchange = function() {
        selectedEvent = results.selectedIndex;
        for (var i = 0; i < events[2].length; i++) { // Sets all events to be unselected - this can probabl be more efficient
            events[2][i] = false;
        }
        events[2][selectedEvent] = true;
        displayDetails(selectedEvent);
    }
    
    subscriptionList.onchange = function() {
        selectedId = subscriptionList.selectedIndex;
    }
    
    clearBtn.onclick = function() {
       clearDisplay();
    }
    
    function clearDisplay() {
        var node = document.getElementById('results');
        while (node.firstChild) {
            node.removeChild(node.firstChild);
        }
    }
    
    function resetEventDisplay() {
        clearDisplay();
        for (var i = 0; i < events[0].length; i++) {
            var newEvent = document.createElement('option');   
            results.appendChild(newEvent);
            newEvent.selected = events[2][i];          
            newEvent.appendChild(document.createTextNode(events[0][i]));
            var attId = document.createAttribute('eventId');
            attId.value = events[1][i];
            newEvent.setAttributeNode(attId);
        }
    }
    
    filterBtn.onclick = function() {
        if (selectedId != filter) {
            filter = selectedId;
            resetEventDisplay();
            var children = results.children;
            for (var i = 0; i < children.length; i++) {
                if (children[i].getAttribute('eventId') != filter) {
                    results.removeChild(children[i]);
                    i--;
                }
            }
        }
    }
    
    showAllBtn.onclick = function() {
        if (filter != 'none') {
            resetEventDisplay();
        }
        filter = 'none';
    }
    
}