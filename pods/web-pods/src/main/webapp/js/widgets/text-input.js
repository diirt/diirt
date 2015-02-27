/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script type="text/javascript" language="javascript" src="../js/widgets/text-input.js"></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/

$(document).ready(function() {

	var nodes = document.getElementsByClassName("text-input");
	var len = nodes.length;
    var inputs = {};
    var modified = {};
    var currentAlarms = {};

    function updateAlarm(severity, id, widget) {
        var currentAlarm = currentAlarms[id];
        if (currentAlarm) {
            widget.classList.remove(currentAlarm);
        }
        switch (severity) {
            case "MINOR":
                currentAlarm = "alarm-minor";
                break;
            case "MAJOR":
                currentAlarm = "alarm-major";
                break;
            case "INVALID":
                currentAlarm = "alarm-invalid";
                break;
            case "UNDEFINED":
                currentAlarm = "alarm-undefined";
                break;
            default:
                currentAlarm = "alarm-none";
                break;
        }
        currentAlarms[id] = currentAlarm;
        widget.classList.add(currentAlarm);
    }

	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var disabled = nodes[i].getAttribute("data-disable") != null ? nodes[i].getAttribute("data-disable") : false;
        var resize = nodes[i].getAttribute("data-resize") != null ? nodes[i].getAttribute("data-resize") : false;
        var id = nodes[i].getAttribute("id");
        if (id === null) {
            id = "text-input-" + i;
            nodes[i].id = id;
        }
        var input = document.createElement("textarea");
        input.id = id;
        input.style.width = "100%";
        input.style.height = "100%";
        input.style.font = "inherit";
        input.style.textAlign = "inherit";
        input.rows = "1";
        if(!resize) {
            input.style.resize="none";
        }
        input.disabled = disabled;
        var div = document.getElementById(id);
        div.appendChild(input);

        if (channelname != null && channelname.trim().length > 0) {
            var callback = function(evt, channel) {
                               switch (evt.type) {
                               case "connection": //connection state changed
                                   channel.readOnly = !evt.writeConnected;
                                   break;
                               case "value": //value changed
                                   if(modified[channel.getId()] == true) {
                                       break;
                                   }
                                   var channelValue = channel.getValue();
                                   // Display the new value
                                   if ("value" in channelValue) {
                                       // If it's a scalar or array, display the value
                                       inputs[channel.getId()].value = channelValue.value;
                                   } else {
                                       // If something else, display the type name
                                       inputs[channel.getId()].value = channelValue.type.name;
                                   }

                                   // Change the style based on the alarm
                                   if ("alarm" in channelValue) {
                                       updateAlarm(channelValue.alarm.severity, channel.getId(), inputs[channel.getId()]);
                                   } else {
                                       updateAlarm("NONE", channel.getId(), inputs[channel.getId()]);
                                   }

                                   // Remove error tooltip
                                   inputs[channel.getId()].parentNode.removeAttribute("title");
                                   break;
                               case "error": //error happened
                                   // Change displayed alarm to invalid, and set the
                                   // tooltip to the error message
                                   updateAlarm("INVALID", channel.getId(), inputs[channel.getId()]);
                                   inputs[channel.getId()].parentNode.title = evt.error;
                                   break;
                               case "writePermission":	// write permission changed.
                                   break;
                               case "writeCompleted": // write finished.
                                   break;
                               default:
                                   break;
                               }
                           };
            var channel = wp.subscribeChannel(channelname, callback, readOnly);
            inputs[channel.getId()] = input;
        }

        input.onkeyup = function(evt) {
            if (evt.keyCode == 13) {
                for(var sl in   inputs  ) {
                    if(inputs[sl].id == evt.target.id) {
                        var ch = wp.getChannel(sl);
                        break;
                    }
                }
                ch.setValue(input.value.trim());
            }
        };

        input.onfocus = function(evt) {
            for(var sl in   inputs  ) {
                if(inputs[sl].id == evt.target.id) {
                    modified[sl] = true;
                    break;
                }
            }
        };

        input.onblur = function(evt) {
            for(var sl in   inputs  ) {
                if(inputs[sl].id == evt.target.id) {
                    modified[sl] = false;
                    break;
                }
            }
        };
    }

});

window.onbeforeunload = function() {
	wp.close();
};
