/**
 * A javascript library for accessing live process data in web browser using
 * WebSocket.
 *
 * @version 3.0.0
 *
 * @author Enrique Schuhmacher
 *
 */

/**
 * Create a new Client object, which establish a new connection to the server.
 * @class Client
 * @constructor
 * @param url url of the pods server.
 * @param debug debug flag
 * @returns a new Client object.
 */
function Client(url, debug, maxRate, username, password) {

	var channelIDIndex = 0;
	var channelArray = [];
	var websocket = null;
	var webSocketOnOpenCallbacks = [];
	var webSocketOnCloseCallbacks = [];
	var webSocketOnErrorCallbacks = [];
	var onServerMessageCallbacks = [];
	var clientSelf = this;
	var debug = debug;
	var defaultTypeVersion = 1;
    var isLive = false;
    var forcedClose = false;
    var jsonFilteredReceived = []; // Contains JSON organized by id
    var jsonSent = []; // Contains JSON organized by id


	openWebSocket(url, username, password, maxRate);

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

	/**
	 * Create a control system channel.
	 * @param {string} name name of the channel.
	 * @param {object} callback related with the channel.
	 * @param {boolean} readOnly select if the channel can be use as read/write or just read.
     * @param {string} type select the vtype that the channel values will contain.
     * @param {intiger} maxRate select update value rate.
	 * @returns the channel.
	 */
    this.subscribeChannel = function(name, callback, readOnly, type, maxRate) {
        var typeJson;
        if(readOnly != false) {
            readOnly = true;
        }
        var json = JSON.stringify({
            "message" : "subscribe",
            "id" : channelIDIndex,
            "channel" : name,
            "readOnly" : readOnly,
            "maxRate" : maxRate,
            "type" : type
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
        if(debug) {
            jsonFilteredReceived.unshift([json]);
        }
        channel.channelCallback = callback;
        channelIDIndex++;
        return channel;
    };

	/**
	 * reconnect a control system channel.
	 * @param {Channel} ch channel that needs to reconnect.
	 * @returns the channel.
	 */
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
            channel.channelCallback = ch.channelCallback;
        return channel;
    };

	/**
	 * connect to a websocket.
	 * @param {string} url url of the service websocket.
	 */
    function openWebSocket(url, username, password, maxRate) {
        if((url.indexOf("wss://") != -1  && username != null) || maxRate != null) {
            url = url + '?';
            if(maxRate != null) {
                url = url + 'maxRate=' + maxRate;
            }
            if(url.indexOf("wss://") != -1) {
                if(username != null) {
                    if(maxRate != null) {
                        url = url + '&';
                    }
                    url = url + 'user=' + username + '&password=' + password;
                }
            }
        }
        if ('WebSocket' in window) {
            websocket = new WebSocket(url);
        } else if ('MozWebSocket' in window) {
            websocket = new MozWebSocket(url);
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
        if(debug) {
            onServerMessageCallbacks.push(function(json) {
              jsonFilteredReceived[json.id].unshift(JSON.stringify(json));
            });
            webSocketOnErrorCallbacks.push(function(evt) {
              jsonFilteredReceived[json.id].unshift(JSON.stringify(json));
            });
        }
    }

    this.getReceivedMessagesPerChannel= function(channelId) {
       if(channelId == '*') {
           return jsonFilteredReceived.join("\n");
       } else {
           return jsonFilteredReceived[channelId].join("\n");
       }
    };


    this.getSentMessages= function() {
           return jsonSent.join("\n");
    };
    /**
     * Close Websocket.
     */
    this.close = function() {
        forcedClose = true;
        if (websocket != null)
            websocket.close();
        websocket = null;
    };

    /**
    * Send text to server using WebSocket.
    * This function is for internal use only.
    * @param {string} text
	*/
    this.sendText =function(text) {
        websocket.send(text);
        if(debug) {
            jsonSent.unshift(text);
        }
    };


    /**
     * Get the Channel from its id.
     * @param {number} id id of the Channel.
     * @returns {Channel} the Channel.
     */
    this.getChannel = function(id){
        return channelArray[id];
    };

    /**Get all Channels on this client.
     * @returns {Array.<Channel>} All Channels in an array.
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
        clientSelf.isLive = true;
        for ( var i in webSocketOnOpenCallbacks) {
            webSocketOnOpenCallbacks[i](evt);
        }
    }

    function fireOnClose(evt) {
        clientSelf.isLive = false;
        var url = evt.currentTarget.url;
        if(forcedClose) {
            for(var c in channelArray){
                channelArray[c].unsubscribe();
            }
            for ( var i in webSocketOnCloseCallbacks) {
                webSocketOnCloseCallbacks[i](evt);
            }
        } else {
            setTimeout(function(){
                openWebSocket(url);
                for(c in channelArray) {
                    clientSelf.resubscribeChannel(channelArray[c]);
                }
            }, 10000);
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

    /**
     * unsubscribe the Channel.
     * @param {number} channelIDIndex id of the Channel.
     */
    function unsubscribe(channelIDIndex) {
		var json = JSON.stringify({
			"message" : "unsubscribe",
			"id" : channelIDIndex
		});
		clientSelf.sendText(json);
		delete channelArray[channelIDIndex];
	}

    /**
     * pause the Channel.
     * @param {number} channelIDIndex id of the Channel.
     */
	function pauseChannel(channelIDIndex){
		var json = JSON.stringify({
			"message" : "pause",
			"id" : channelIDIndex,
		});
		clientSelf.sendText(json);
	}

    /**
     * resume the Channel.
     * @param {number} channelIDIndex id of the Channel.
     */
	function resumeChannel(channelIDIndex){
		var json = JSON.stringify({
			"message" : "resume",
			"id" : channelIDIndex,
		});
		clientSelf.sendText(json);
	}

    /**
     * Set cahnnel value.
     * @param {number} channelIDIndex id of the Channel.
     * @param {object} value updated value to be set.
     */
	function setChannelValue(channelIDIndex, value){
		var json = JSON.stringify({
            "message" : "write",
			"id" : channelIDIndex,
			"value" : value
		});
		clientSelf.sendText(json);
	}

    /**
     * Represents a channel.
     * @constructor
     * @param {string} name - The name of the channel.
     * @returns the channel object.
    */
	function Channel(name) {
        /**Name of the Channel.
         * @type {string}
         */
        this.name = name;
        this.id = -1;
        this.value = null;
        // for the moment only allowed one callback that will be pass at the time of subscription
        this.channelCallback = null;
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
     * Remove a callback.
     * @param {Client~Channel~ChannelCallback} callback the callback function.
     */
    Channel.prototype.removeCallback = function(callback) {
        channelCallback = null;
    };

    /**
     * Set channel value.
     * @param {object} value
     *        the value to be set. It must be a value type that the Channel can accept,
     *        for example, a number for numeric Channel.
     */
    Channel.prototype.setValue = function(value) {
        if(!this.readOnly) {
            setChannelValue(this.id, value);
        }
    };

    /**
     * update channel value.
     */
    Channel.prototype.updateValue = function() {
        if(!this.readOnly) {
            setChannelValue(this.id, this.value.value);
        }
    };

    /**
     * unsubscribe channel .
     *
     */
    Channel.prototype.unsubscribe = function() {
        unsubscribe(this.id);
    };

	/**
	 * Pause notification on this channel.
	 */
    Channel.prototype.pause = function() {
        this.paused = true;
        pauseChannel(this.id);
    };

	/**
	 * resume notification on this channel.
	 */
    Channel.prototype.resume = function() {
        this.paused = false;
        resumeChannel(this.id);
    };

	// fire a channel event
    Channel.prototype.fireChannelEventFunc = function(json) {
        // update the  properties of the channel
        // processJson should be implemented in specific protocol library
        processJsonForChannel(json, this);
        this.channelCallback(json, this);
    };
}