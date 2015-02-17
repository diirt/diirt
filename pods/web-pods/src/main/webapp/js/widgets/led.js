/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/Drinks/Drinks.js" ></script>
 * <script type="text/javascript" language="javascript" src="../js/widgets/led.js"></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 * <script src="../js/widgets/lib/Drinks/Led.js" ></script>
 ******************************************************************************/


$(document).ready(function () {

    var nodes = document.getElementsByClassName("led");
    var len = nodes.length;
    var leds = {};
    var currentValueStyles = {};
    counter = 0;
    
    function changeValue(value, id, cicle) {
        var currentValueStyle = currentValueStyles[id];
        if (currentValueStyle) {
            cicle.classList.remove(currentValueStyle);
        }
        currentValueStyle = "value-" + value;
        currentValueStyles[id] = currentValueStyle;
        cicle.classList.add(currentValueStyle);
    }
    
    for (var i = 0; i < len; i++) {
        var dataChannel = nodes[i].getAttribute("data-channel");
        var id = nodes[i].getAttribute("id");
        if (id === null) {
            counter++;
            id = "led-" + counter;
            nodes[i].id = id;
        }
        
        nodes[i].innerHTML = '<svg style="height:100%; width:100%; vertical-align:top; overflow:visible"><circle class="value-0" cx="50%" cy="50%" r="50%" stroke="black" stroke-width="1" fill="red" /></svg>';
        var circle = nodes[i].firstChild.firstChild;
        
        if (dataChannel != null && dataChannel.trim().length > 0) {
            var callback = function (evt, channel) {
                switch (evt.type) {
                    case "connection": //connection state changed
                        channel.readOnly = !evt.writeConnected;
                        break;
                    case "value": //value changed
                        var channelValue = channel.getValue();
                        // Display the new value
                        if ("value" in channelValue) {
                            // If a scalar/array, use the actual value
                            if (channelValue.value) {
                                changeValue(1, channel.getId(), leds[channel.getId()]);
                            } else {
                                changeValue(0, channel.getId(), leds[channel.getId()]);
                            }
                        } else {
                            // If another type, just check whether there is a value
                            if (channelValue) {
                                changeValue(1, channel.getId(), leds[channel.getId()]);
                            } else {
                                changeValue(0, channel.getId(), leds[channel.getId()]);
                                
                            }
                        }
                        break;
                    case "error": //error happened
                        // Change displayed alarm to invalid, and set the
                        // tooltip to the error message
                        changeValue("error", channel.getId(), leds[channel.getId()]);
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
        }
    }
});

window.onbeforeunload = function () {
    wp.close();
};

