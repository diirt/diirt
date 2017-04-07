/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script type="text/javascript" language="javascript" src="../js/widgets/led.js"></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/


$(document).ready(function () {

    var nodes = document.getElementsByClassName("wp-led");
    var len = nodes.length;
    var leds = {};
    var currentStateStyles = {};
    var currentValueStyles = {};
    counter = 0;
    
    function change(id, nextState, nextValue) {
        var circle = leds[id];
        var currentState = currentStateStyles[id];
        var currentValue = currentValueStyles[id];
        if (currentState) {
            circle.classList.remove(currentState);
        }
        if (currentValue) {
            circle.classList.remove(currentValue);
        }
        circle.classList.add(nextState);
        if (nextValue) {
            circle.classList.add(nextValue);
        }
        currentStateStyles[id] = nextState;
        currentValueStyles[id] = nextValue;
    }
    
    function ledOn(id) {
        change(id, "on", "");
    }
    
    function ledOff(id) {
        change(id, "off", "");
    }
    
    function ledError(id) {
        change(id, "error", "");
    }
    
    function ledValue(index, labels, id) {
        var state = "off";
        if (index) {
            state = "on";
        }
        var value;
        if (labels && labels[index]) {
            value = "value-" + labels[index].toLowerCase();
        }
        change(id, state, value);
    }
    
    for (var i = 0; i < len; i++) {
        var dataChannel = nodes[i].getAttribute("data-channel");
        var id = nodes[i].getAttribute("id");
        if (id === null) {
            counter++;
            id = "led-" + counter;
            nodes[i].id = id;
        }
        
        
        // I experimented with using Font Awesome instead of SVG to create the LED.
        // It works, but the border is done with a text-shadow hack that 
        // does not render as nicely. One was able to customize the icon (changing
        // it from the default circle), but one would have to use the UNICODE
        // for the character. Also: most of the symbols provided by Font Awesome
        // would not be the type of symbol users of WebPODS would find interesting.
        // So, we are still using the SVG circle implementation.
        // 
        // Here are the few changes needed for Font Awesome. The CSS of the led
        // needs a "text outline" hack:
        //     text-shadow: -1px 0 black, 0 1px black, 1px 0 black, 0 -1px black;
        // And the following two lines need to be changed to:
        //        nodes[i].innerHTML = '<span class="fa fa-fw fa-circle off"></span>';
        //        var circle = nodes[i].firstChild;

        nodes[i].innerHTML = '<svg style="height:100%; width:100%; vertical-align:top; overflow:visible"><circle class="off" cx="50%" cy="50%" r="50%" stroke="black" stroke-width="1" fill="currentColor" /></svg>';
        var circle = nodes[i].firstChild.firstChild;
        
        if (dataChannel != null && dataChannel.trim().length > 0) {
            var callback = function (evt, channel) {
                switch (evt.type) {
                    case "connection": //connection state changed
                        break;
                    case "value": //value changed
                        var channelValue = channel.getValue();
                        // Display the new value
                        if ("value" in channelValue) {
                            if (channelValue.type.name == "VEnum") {
                                // If enum, use labels as styles
                                ledValue(channelValue.value, channelValue.enum.labels, channel.getId());
                            } else {
                                // If a scalar/array, use the actual value
                                if (channelValue.value) {
                                    ledOn(channel.getId());
                                } else {
                                    ledOff(channel.getId());
                                }
                            }
                        } else {
                            // If another type, just check whether there is a value
                            if (channelValue) {
                                ledOn(channel.getId());
                            } else {
                                ledOff(channel.getId());
                            }
                        }
                        break;
                    case "error": //error happened
                        // Change displayed alarm to invalid, and set the
                        // tooltip to the error message
                        ledError(channel.getId());
                        leds[channel.getId()].parentNode.parentNode.title = evt.error;
                        break;
                        break;
                    case "writePermission":	// write permission changed.
                        break;
                    case "writeCompleted": // write finished.
                        break;
                    default:
                        break;
                }
            };
            var channel = wp.subscribeChannel(dataChannel, callback, true);
            leds[channel.getId()] = circle;
            currentStateStyles[channel.getId()] = "off";
        }
    }
});

window.onbeforeunload = function () {
    wp.close();
};

