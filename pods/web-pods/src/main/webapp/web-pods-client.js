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
    var filterBtn = document.getElementById('filter');
    var showAllBtn = document.getElementById('showAll');
    var socket;
    var channel;
    var id;
    var filter = 'none';
    var currentId = 0;
    var channelList = [];
    var resultsInfoFiltered = []; // Contains JSON organized by id
    var resultsInfo = []; // Contains JSON
    var results = [];
    var resultsFiltered = [];
    
    serverField.value = "ws://" + window.location.host + "/web-pods/socket";
    
    
    function waitForConnection(callback) {
        setTimeout(
            function(){
                if (socket.readyState === 1) {
                    callback();
                    return;
                } else {
                    waitForConnection(callback);
                }
        },5);
    };
    
    
    //Delays sending a message until the socket connection is established
    function sendMessage(message) {
        waitForConnection(function(){
           socket.send(message); 
        });
    };
  
    
    // When the form is submitted, send a message and add it to the sent message list
    connectBtn.onclick = function(e) {
        e.preventDefault();
        var server = serverField.value;
        socket = new WebSocket(server);
        testSocket();
        return false;
    };
    
    subscriptionList.onchange = function(e) {
        var index = subscriptionList.selectedIndex;
        channel = channelList[index];
        id = index;
        console.log('id: ' + id + 'channel: ' + channel);
    };
    
    // Subscribe
    subscribeBtn.onclick = function(e) {
        channel = channelField.value;
        id = idField.value;
        var message = '{"message" : "subscribe", "id" : ' + id + ', "channel" :"' + channel + '"}';
        currentId++;
        idField.value = currentId;
        sendMessage(message); // Sends the message through socket
        var newSubscription = document.createElement('option'); // New subscription to be added to sub list
        subscriptionList.appendChild(newSubscription);
        newSubscription.appendChild(document.createTextNode('id: ' + id + ', channel: ' + channel));
        channelList.unshift(channel);
        var subscriptionNotification = document.createElement('option'); // New subscription to be added to results window
        result.insertBefore(subscriptionNotification, result.childNodes[0]);
        subscriptionNotification.appendChild(document.createTextNode('Subscribe: ' + channel + ', ' + id));
        resultsInfo.unshift(message);
        resultsInfoFiltered.unshift([message]);
        socket.onmessage = function(e) { newMessage(e) };
    };
    
    
    // Unsubscribe
    unsubscribeBtn.onclick = function(e) {
        // TODO: Change class of subscriptions when unsubscribed (change to color red)
        var message = '{"message" : "unsubscribe", "id" : ' + id + '}';
        sendMessage(message);
        // TODO: Change to appendChild
        result.innerHTML = '<option>Unsubscribe: ' + channel + ', ' + id + '</option>' + result.innerHTML;
        resultsInfo.unshift(message);
    };
    
    
    function testSocket() {
      // socket.onmessage = function(e) { newMessage(e) };
      socket.onopen = function(e) { openSocket(e) };
      socket.onerror = function(e) { error(e) };
    };
   
   
    // Message received
   function newMessage (event) {
       var response = JSON.parse(event.data);
       var value;
       var filterValue;
       if (response.type === "connection") { // Successful subscription
           // subscriptionList.innerHTML = '<option> id: ' + id + ', channel: ' + channel + '</option>' + subscriptionList.innerHTML;
       }
      if (response.value.type.name === "VTable") {
           console.log('table');
           value = '<option>table</option>';
           filterValue = 'table';
       }
       else if (response.type === "error") {
           value = '<option class = "error">' + response.error + '</option>';
           filterValue = response.error;
       } 
       else if (response.type === "value") {
            value = '<option>' + response.value.value + '</option>';
            filterValue = response.value.value;
        }
        resultsInfo.unshift('<div><pre>' + JSON.stringify(response, null, '     ') + '</pre></div>');
        resultsInfoFiltered[id].unshift('<div><pre>' + JSON.stringify(response, null, '     ') + '</pre></div>');      
        // Display event details based on filter status
        if (filter == 'none') { // No filter
           // TODO: Change to apendChild 
           result.innerHTML = value + result.innerHTML;
        }
        else if (response.id == filter) { // If the event should be displayed - matches filter
            var option = document.createElement('option');
            result.insertBefore(option, result.childNodes[0]);
            option.appendChild(document.createTextNode(filterValue));
        }
    };
    
    // Updates connection status
   function openSocket (event) {
       //TODO: change to appeendChild
       result.innerHTML = '<option class="open">Connected</option>' + result.innerHTML;
       console.log('connected to socket');
       resultsInfo.unshift('Connected to ' + serverField.value);
       idField.value = currentId;
       //TODO: Change to clear all children...or something like that
       subscriptionList.innerHTML = '';
    };
    
    
    // Log errors to the console
    function error (error) {
        console.log('WebSocket Error: ' + error);
    };
    
    
    // Updates the connection status when socket is closed
    function closeSocket(event) {
        //TODO: Change to appendChild
        result.innerHTML = '<option class="closed">Disconnected</option>' + result.innerHTML;
        resultsInfo.unshift('Disconnected from ' + socket.URL);
        currentId = 0;
        // socketStatus.innerHTML = 'Disconnected';
        // socketStatus.className = 'Closed';
    };


    // Close the socket when the disconnect button is clicked
    disconnectBtn.onclick = function(e) {
        e.preventDefault();
        socket.close(); // Close socket
        socket.onclose = function(e) { closeSocket(e) };
        return false;
    };
    
    // Pause
    pauseBtn.onclick = function(e) {
        var message = '{"message":"pause","id": ' + id + '}';
        sendMessage(message);
        // TODO: appendChild
        result.innerHTML = '<option>Paused</option>' + result.innerHTML;
        resultsInfo.unshift(message);
        // socketStatus.innerHTML = 'Paused';
    };
    
    // Resume
    resumeBtn.onclick = function(e) {
        var message = '{"message" : "resume", "id" : ' + id + '}';
        sendMessage(message);
        //TODO: appendChild
        result.innerHTML = '<option>Resume</option>' + result.innerHTML;
        resultsInfo.unshift(message);
        // socketStatus.innerHTML = 'Connected to: ' + socket.URL;
    };
    
    
    filterBtn.onclick = function(e) {
        filter = id;
        // result.innerHTML = '';
        // TODO: Everything here
    }
    
    showAllBtn.onclick = function(e) {
        filter = 'none';
        // TODO: Everything here
    }
    
    
    // Clears event info
    clearBtn.onclick = function(e) {
        result.innerHTML= "";
        details.innerHTML = "";
    }
    
    
    // Displays details for selected event
    // TODO: fix this whatever is happening here it is bad
    result.onchange = function(e) {
        console.log('result onchange');
        var i = result.selectedIndex;
        console.log(i);
        details.innerHTML = resultsInfo[i];
    };
 
};




