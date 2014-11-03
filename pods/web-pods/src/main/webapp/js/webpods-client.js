function Client(url, debug, debugMessageBox) {

	var channelIDIndex = 0;
	var channelArray = [];
	var websocket = null;
	var webSocketOnOpenCallbacks = [];
	var webSocketOnCloseCallbacks = [];
	var webSocketOnErrorCallbacks = [];
	var onServerMessageCallbacks = [];
	var clientSelf = this;
	var debug = debug;
	var debugMessageBox = debugMessageBox;
	var defaultTypeVersion = 1;
    this.isLive = false;
    var  forcedClose = false;

	openWebSocket(url);

    /**
	 * Add a callback to WebSocket onOpen event.
	 * @param {Client~WebSocketEventCallback} callback the callback function on WebSocket open event.
	 */
	this.addWebSocketOnOpenCallback = function(callback) {
		webSocketOnOpenCallbacks.push(callback);
	};

	/**
	 * Remove a WebSocket onOpen callback.
	 * @param {Client~WebSocketEventCallback} callback the callback function on WebSocket open event.
	 */
	this.removeWebSocketOnOpenCallback = function(callback){
		webSocketOnOpenCallbacks.splice(webSocketOnOpenCallbacks.indexOf(callback), 1);
	};


	/**
	 * Add a callback to WebSocket onClose event.
	 * @param {Client~WebSocketEventCallback} callback the callback function on WebSocket close event.
	 *
	 */
	this.addWebSocketOnCloseCallback = function(callback) {
		webSocketOnCloseCallbacks.push(callback);
	};



	/**
	 * Add a callback to WebSocket onError event.
	 * @param {Client~WebSocketEventCallback} callback the callback function on WebSocket error event.
	 *
	 */
	this.addWebSocketOnErrorCallback = function(callback) {
		webSocketOnErrorCallbacks.push(callback);
	};

	/**A callback function that will be notified when there is a message from server.
	 * @callback Client~OnServerMessageCallback
	 *
	 */

	/**
	 * Add a callback that will be notified when there is a notification message from server.
	 * @param {Client~OnServerMessageCallback} callback the callback
	 *
	 */
	this.addOnServerMessageCallback = function(callback) {
		onServerMessageCallbacks.push(callback);
	};

    this.subscribeChannel = function(name, callback, readOnly, type, version, maxRate) {
        var typeJson;
        if(readOnly == null) {
            readOnly = true;
        }
        if (type != null) {
            if (version == null)
                version = defaultVersion;
            typeJson = JSON.stringify({
                "name" : type,
                "version" : version
            });

        }
        var json = JSON.stringify({
            "message" : "subscribe",
            "id" : channelIDIndex,
            "channel" : name,
            "readOnly" : readOnly,
            "maxRate" : maxRate,
            "type" : typeJson
        });
        var channel = new Channel(name);
        channelArray[channelIDIndex] = channel;
        channel.id = channelIDIndex;
        channel.name = name;
        channel.readOnly = readOnly;
        channel.connected = true;
        if(this.isLive)
            this.sendText(json);
        else{
            var webpdaSelf = this;
            var listener = null;
            listener = function(evt){
                clientSelf.sendText(json);
                setTimeout(function(){
                    clientSelf.removeWebSocketOnOpenCallback(listener);
                }, 0);
            };
            this.addWebSocketOnOpenCallback(listener);
        }
        channel.addCallback(callback);
        channelIDIndex++;
        return channel;
    };

    this.resubscribeChannel = function(ch) {
        var typeJson;
        if (ch.value.type != null) {
            typeJson = JSON.stringify({
                "name" : ch.value.type.name,
                "version" : ch.value.type.version
            });

        }
        var json = JSON.stringify({
            "message" : "subscribe",
            "id" : ch.id,
            "channel" : ch.name,
            "readOnly" : ch.readOnly,
            "type" : typeJson,
            "maxRate" : ch.maxRate
        });
        var channel = new Channel(ch.name);
            channelArray[ch.id] = channel;
            channel.id = ch.id;
            channel.name = ch.name;
            channel.readOnly = ch.readOnly;
            channel.connected = true;
            if(this.isLive)
                this.sendText(json);
            else{
                var webpdaSelf = this;
                var listener = null;
                listener = function(evt){
                    clientSelf.sendText(json);
                    setTimeout(function(){
                        clientSelf.removeWebSocketOnOpenCallback(listener);
                    }, 0);
                };
                this.addWebSocketOnOpenCallback(listener);
            }
            channel.addCallback(ch.channelCallbacks[0]);
        return channel;
    };

    function openWebSocket(url, reconnect) {
        if ('WebSocket' in window) {
            websocket = new WebSocket(url, "org.client");
        } else if ('MozWebSocket' in window) {
            websocket = new MozWebSocket(url, "org.client");
        } else {
            throw new Error('WebSocket is not supported by this browser.');
        }
        websocket.binaryType = "arraybuffer";
        websocket.onopen = function(evt) {
            fireOnOpen(evt);
        };

        websocket.onmessage = function(evt) {
            var json;
            json = JSON.parse(evt.data);
            dispatchMessage(json);

        };

        websocket.onerror = function(evt) {
            fireOnError(evt);
        };

        websocket.onclose = function(evt) {
            fireOnClose(evt);
        };
    }

    function writeToScreen(message) {
        if (debugMessageBox != null)
            document.getElementById(debugMessageBox).value = message;
    }


    /**
     * Close Websocket.
     */
    this.close = function() {
        forcedClose = true;
        if (websocket != null)
            websocket.close();
        websocket = null;
    };

    this.sendText =function(text) {
        websocket.send(text);
        if(debug)
            writeToScreen(text);
    };


    /**
     * Get the Channel from its id.
     * @param {number} id id of the Channel.
     * @returns {Client~Channel} the Channel.
     */
    this.getChannel = function(id){
        return channelArray[id];
    };

    /**Get all Channels on this client.
     * @returns {Array.<Client~Channel>} All Channels in an array.
     */
    this.getAllChannels = function() {
        return channelArray;
    };

    function dispatchMessage(json) {
        if (json.message != null)
            handleServerMessage(json);
        if (json.id != null) {
            if (channelArray[json.id] != null)
                channelArray[json.id].fireChannelEventFunc(json);
        }
    }

    function handleServerMessage(json) {
		if(json.type == "error")
			console.log("Error: " + json.error);
		fireOnServerMessage(json);
	}

	function processJsonForChannel(json, Channel) {
        switch (json.type) {
        case "connection":
            Channel.connected = json.connected;
            Channel.readOnly = !json.writeConnected;
            break;
        case "value":
            Channel.value =  json.value;
            break;
        default:
            break;
        }
    };
    function fireOnOpen(evt) {
        console.log("Open");

        clientSelf.isLive = true;
        for ( var i in webSocketOnOpenCallbacks) {
            webSocketOnOpenCallbacks[i](evt);
        }
    }

    function fireOnClose(evt) {
        clientSelf.isLive = false;
        if(forcedClose) {
            for(var c in channelArray){
                channelArray[c].unsubscribe();
            }
            for ( var i in webSocketOnCloseCallbacks) {
                webSocketOnCloseCallbacks[i](evt);
            }
        } else {
            openWebSocket(evt.currentTarget.url);
            for(c in channelArray) {
                clientSelf.resubscribeChannel(channelArray[c]);
            }
        }

    }


    function fireOnError(evt) {
        for ( var i in webSocketOnErrorCallbacks) {
            webSocketOnErrorCallbacks[i](evt);
        }
    }

    function fireOnServerMessage(json) {
        for ( var i in onServerMessageCallbacks) {
            onServerMessageCallbacks[i](json);
        }
    }

    function unsubscribe(channelIDIndex) {
		var json = JSON.stringify({
			"message" : "unsubscribe",
			"id" : channelIDIndex
		});
		clientSelf.sendText(json);
		delete channelArray[channelIDIndex];
	}

	function pauseChannel(channelIDIndex){
		var json = JSON.stringify({
			"message" : "pause",
			"id" : channelIDIndex,
		});
		clientSelf.sendText(json);
	}

	function resumeChannel(channelIDIndex){
		var json = JSON.stringify({
			"message" : "resume",
			"id" : channelIDIndex,
		});
		clientSelf.sendText(json);
	}

	function setChannelValue(channelIDIndex, value){
		var json = JSON.stringify({
            "message" : "write",
			"id" : channelIDIndex,
			"value" : value
		});
		clientSelf.sendText(json);
	}

	function Channel(name) {
        /**Name of the Channel.
         * @type {string}
         */
        this.name = name;
        this.id = -1;
        this.value = null;
        this.channelCallbacks = [];
        this.paused = false;
        this.connected = false;
        this.readOnly = true;

    }
    /**If the channel is connected to the device.
     * @returns {boolean} true if the channel is connected.
     */
    Channel.prototype.isConnected = function(){
        return this.connected;
    };
    /**
     * Get id of the Channel.
     * return {object} the value which is a data structure depending on the Channel.
     */
    Channel.prototype.getId = function(){
        return this.id;
    };

    /**
     * If write operation is allowed on the channel
     * @return {boolean}
     */
    Channel.prototype.isWriteAllowed = function(){
        return !this.readOnly;
    };

    /**
     * If the channel is paused
     * @return {boolean}
     */
    Channel.prototype.isPaused = function(){
        return this.paused;
    };

    /**
     * Get value of the Channel.
     * return {object} the value which is a data structure depending on the Channel.
     */
    Channel.prototype.getValue = function(){
        return this.value;
    };
    /**
     * Add a callback to the Channel that will be notified on Channel's event.
     * @param {Client~Channel~ChannelCallback} callback the callback function.
     */
    Channel.prototype.addCallback = function(callback) {
        this.channelCallbacks.push(callback);
    };

    /**
     * Remove a callback.
     * @param {Client~Channel~ChannelCallback} callback the callback function.
     */
    Channel.prototype.removeCallback = function(callback) {
        for ( var i in this.channelCallbacks) {
            if (this.channelCallbacks[i] == callback)
                delete this.channelCallbacks[i];
        }
    };
    /**
     * Set channel value.
     * @param {object} value
     *        the value to be set. It must be a value type that the Channel can accept,
     *        for example, a number for numeric Channel.
     */
    Channel.prototype.setValue = function(value) {
        setChannelValue(this.id, value);
    };

    Channel.prototype.updateValue = function() {
            setChannelValue(this.id, this.value.value);
    };

    /**
     * unsubscribe channel .
     *
     */
    Channel.prototype.unsubscribe = function() {
        unsubscribe(this.id);
    };

    Channel.prototype.pause = function() {
        this.paused = true;
        pauseChannel(this.id);
    };

    Channel.prototype.resume = function() {
        this.paused = false;
        resumeChannel(this.id);
    };


    Channel.prototype.fireChannelEventFunc = function(json) {
        // update the  properties of the channel
        // processJson should be implemented in specific protocol library
        processJsonForChannel(json, this);
        for ( var i in this.channelCallbacks) {
            this.channelCallbacks[i](json, this);
        }
    };
}