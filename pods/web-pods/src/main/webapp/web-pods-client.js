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
    var currentId = 0;
    var channelList = [];
    var resultsInfo = []; // Contains JSON
    
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
        id = channelList.length - 1 - index;
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
        subscriptionList.innerHTML = '<option> id: ' + id + ', channel: ' + channel + '</option>' + subscriptionList.innerHTML;
        channelList.unshift(channel);
        result.innerHTML = '<option>Subscribe: ' + channel + ', ' + id + '</option>' + result.innerHTML;
        resultsInfo.unshift(message);
        socket.onmessage = function(e) { newMessage(e) };
    };
    
    
    // Unsubscribe
    unsubscribeBtn.onclick = function(e) {
        var message = '{"message" : "unsubscribe", "id" : ' + id + '}';
        sendMessage(message);
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
       if (response.type === "connection") { // Successful subscription
           // subscriptionList.innerHTML = '<option> id: ' + id + ', channel: ' + channel + '</option>' + subscriptionList.innerHTML;
       }
      if (response.value.type.name === "VTable") {
           value = '<option>table</option>';
       }
       if (response.type === "error") {
           value = '<option class = "error">' + response.error + '</option>';
       } 
       if (response.type === "value") {
            value = '<option>' + response.value.value + '</option>';
        }
        result.innerHTML = value + result.innerHTML;
        resultsInfo.unshift('<div><pre>' + JSON.stringify(response, null, '     ') + '</pre></div>');
    };
    
    // Updates connection status
   function openSocket (event) {
       result.innerHTML = '<option class="open">Connected</option>' + result.innerHTML;
       console.log('connected to socket');
       resultsInfo.unshift('Connected to ' + serverField.value);
       idField.value = currentId;
    };
    
    
    // Log errors to the console
    function error (error) {
        console.log('WebSocket Error: ' + error);
    };
    
    
    // Updates the connection status when socket is closed
    function closeSocket(event) {
        result.innerHTML = '<option class="closed">Disconnected</option>' + result.innerHTML;
        resultsInfo.unshift('Disconnected from ' + socket.URL);
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
        result.innerHTML = '<option>Paused</option>' + result.innerHTML;
        resultsInfo.unshift(message);
        // socketStatus.innerHTML = 'Paused';
    };
    
    // Resume
    resumeBtn.onclick = function(e) {
        var message = '{"message" : "resume", "id" : ' + id + '}';
        sendMessage(message);
        result.innerHTML = '<option>Resume</option>' + result.innerHTML;
        resultsInfo.unshift(message);
        // socketStatus.innerHTML = 'Connected to: ' + socket.URL;
    };
    
    
    filterBtn.onclick = function(e) {
        // TODO: filter event info by subscription
    }
    
    showAllBtn.onclick = function(e) {
        // TODO: remove subscription filter, show all data
    }
    
    // Clears event info
    clearBtn.onclick = function(e) {
        result.innerHTML= "";
        details.innerHTML = "";
    }
    
    
    // Displays details for selected event
    results.onchange = function(e) {
        var index = results.selectedIndex;
        console.log(index);
        details.innerHTML = resultsInfo[index];
    };
 
};




