window.onload = function() {

    // References to elements on the page
    var form = document.getElementById('message-form');
    var serverField = document.getElementById('server');
    var channelField = document.getElementById('channel');
    var json = document.getElementById('json');
    var socketStatus = document.getElementById('status');
    var connectBtn = document.getElementById('connect');
    var pauseBtn = document.getElementById('pause');
    var closeBtn = document.getElementById('close');
    var paused = false;
    var socket;
    var channel;
    
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
        channel = channelField.value;
        socket = new WebSocket(server);
        var message = '{"message" : "subscribe", "id" : 0, "channel" :"' + channel + '"}';
        sendMessage(message); // Sends the message through socket
        serverField.value = '';
        channelField.value = '';
        testSocket();
        return false;
    };
    
    function testSocket() {
      socket.onmessage = function(e) { newMessage(e) };
      socket.onopen = function(e) { openSocket(e) };
      socket.onerror = function(e) { error(e) };
    };
   
    // When a message is sent by the server, retrieve the data and display in div results
   function newMessage (event) {
        var response = JSON.parse(event.data);
        console.log(response.value.value);
        result.innerHTML = response.value.value + "<br>" + result.innerHTML;
        //json.innerHTML =  JSON.stringify(response, null, '    ');
    };
    
    // Updates connection status
   function openSocket (event) {
        socketStatus.innerHTML = 'Connected to: ' + socket.URL;
        socketStatus.className = 'open';
        pause.innerHTML = 'PAUSE';
        paused = false;
    };
    
    
    // Log errors to the console
    function error (error) {
        console.log('WebSocket Error: ' + error);
    };
    
   
    // Updates the connection status when socket is closed
    function closeSocket(event) {
        socketStatus.innerHTML = 'Disconnected';
        socketStatus.className = 'Closed';
        result.innerHTML = '';
    };

    // Close the socket when the close button is clicked
    closeBtn.onclick = function(e) {
        e.preventDefault();
        socket.close(); // Close socket
        socket.onclose = function(e) { closeSocket(e) };
        return false;
    };
    
    //Pause or unpause
    pauseBtn.onclick = function(e) {
        if (paused === false) {
            var message = '{"message":"pause","id":0}';
            sendMessage(message);
            socketStatus.innerHTML = 'Paused';
            pause.innerHTML = 'RESUME';
            paused = true;
        }
        else if (paused === true) {
            var message = '{"message" : "resume", "id" : 0}';
            sendMessage(message);
            socketStatus.innerHTML = 'Connected to: ' + socket.URL;
            pause.innerHTML = 'PAUSE';
            paused = false;
        }
    };
 
};




