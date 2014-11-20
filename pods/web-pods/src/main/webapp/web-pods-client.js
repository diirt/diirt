window.onload = function() {

    // References to elements on the page
    var form = document.getElementById('message-form');
    var serverField = document.getElementById('server');
    var channelField = document.getElementById('channel');
    var idField = document.getElementById('idNum');
    var result = document.getElementById('results');
    var details = document.getElementById('details');
    var connectBtn = document.getElementById('connect');
    var disconnectBtn = document.getElementById('disconnect');
    var subscribeBtn = document.getElementById('subscribe');
    var pauseBtn = document.getElementById('pause');
    var resumeBtn = document.getElementById('resume');
    var unsubscribeBtn = document.getElementById('unsubscribe');
    var socket;
    var channel;
    var id;
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
    
    
    // Subscribe
    subscribeBtn.onclick = function(e) {
        channel = channelField.value;
        id = idField.value;
        var message = '{"message" : "subscribe", "id" : ' + id + ', "channel" :"' + channel + '"}';
        sendMessage(message); // Sends the message through socket
        result.innerHTML = '<option>Subscribe: ' + channel + ', ' + id + '</option>' + result.innerHTML;
        resultsInfo.unshift(message);
        socket.onmessage = function(e) { newMessage(e) };
    };
    
    
    // Unsubscribe
    unsubscribeBtn.onclick = function(e) {
        var message = '{"message" : "unsubscribe", "id" : ' + id + ', "channel" :"' + channel + '"}';
        sendMessage(message);
        result.innerHTML = '<option>Unsubscribe: ' + channel + ', ' + id + '</option>' + result.innerHTML;
        resultsInfo.unshift(message);
    };
    
    
    function testSocket() {
      // socket.onmessage = function(e) { newMessage(e) };
      socket.onopen = function(e) { openSocket(e) };
      socket.onerror = function(e) { error(e) };
    };
   
   
    // When a message is sent by the server, retrieve the data and display in div results
   function newMessage (event) {
       //console.log(event);
        var response = JSON.parse(event.data);
        var value = response.value.value;
        result.innerHTML = '<option>' + value + '</option>' + result.innerHTML;
        resultsInfo.unshift('<div><pre>' + JSON.stringify(response, null, '     ') + '</pre></div>');
    };
    
    // Updates connection status
   function openSocket (event) {
       result.innerHTML = '<option class="open">Connected</option>' + result.innerHTML;
       resultsInfo.unshift('Connected to ' + socket.URL);
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
    
    
    // Displays details for selected event
    results.onchange = function(e) {
        var index = results.selectedIndex;
        details.innerHTML = resultsInfo[index];
    };
 
};




