/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/jquery-ui/jquery-ui.js"></script>
 * <script type="text/javascript" language="javascript" src="../js/widgets/spinner.js"></script>
 * <link href="../js/widgets/lib/jquery-ui/jquery-ui.css" rel="stylesheet" type="text/css">
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/


$(document).ready(function() {

	var nodes = document.getElementsByClassName("wp-spinner");
    var len = nodes.length;
    var spinners = {};
    var modified = {};
    var currentAlarms = {};

    function updateSpinnerAlarm(severity, id, widget) {
        var currentAlarm = currentAlarms[id];
        if (currentAlarm) {
            widget.removeClass(currentAlarm);
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
        widget.addClass(currentAlarm);
    }

	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var max = nodes[i].getAttribute("data-displayHigh") != null ? parseFloat(nodes[i].getAttribute("data-displayHigh")) : 100;
        var min = nodes[i].getAttribute("data-displayLow") != null ? parseFloat(nodes[i].getAttribute("data-displayLow")) : 0;
        var step = nodes[i].getAttribute("data-step") != null ? parseFloat(nodes[i].getAttribute("data-step")) : 1;
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var id = nodes[i].getAttribute("id");
        if (id === null) {
            id = "wp-spinner-" + i;
            nodes[i].id = id;
        }
        var callback = function(evt, channel) {
                   switch (evt.type) {
                   case "connection": //connection state changed
                       channel.readOnly = !evt.writeConnected;
                       break;
                   case "value": //value changed
                       var channelValue = channel.getValue();
                       if ("value" in channelValue) {
                           if(channelValue.display && channelValue.display.highDisplay != null) {
                                spinners[channel.getId()].spinner({max : channelValue.display.highDisplay, min : channelValue.display.lowDisplay});
                           }
                           spinners[channel.getId()].spinner("value" , channelValue.value);
                       } else {
                           // If something else, display the type name
                           spinners[channel.getId()].spinner( "value", channelValue.type.name);
                       }
                       // Change the style based on the alarm
                       if ("alarm" in channelValue) {
                           updateSpinnerAlarm(channelValue.alarm.severity, channel.getId(), spinners[channel.getId()]);
                       } else {
                           updateSpinnerAlarm("NONE", channel.getId(), spinners[channel.getId()]);
                       }
                       // Remove error tooltip
                           spinners[channel.getId()][0].title= "";
                           break;
                       case "error": //error happened
                           // Change displayed alarm to invalid, and set the
                           // tooltip to the error message
                           updateSpinnerAlarm("INVALID", channel.getId(), spinners[channel.getId()]);
                           spinners[channel.getId()][0].title = evt.error;
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
        var spinner = $("#" + id).spinner();
        spinners[channel.getId()] = spinner;

        $('.ui-spinner-button').click(function(event) {
            for(var sl in   spinners) {
                if(spinners[sl][0].id == event.currentTarget.parentElement.childNodes[0].id) {
                    var ch = wp.getChannel(sl);
                    break;
                }
            }
            ch.value.value = spinner.spinner( "value");
            ch.updateValue();
        });

        // keyup will catch any stroke on keyboard
        spinner.keyup(function(evt){
            if (evt.keyCode == 13) {
                for(var sl in   spinners) {
                    if(spinners[sl][0].id == evt.target.id) {
                        var ch = wp.getChannel(sl);
                        break;
                    }
                }
                ch.setValue(spinner.spinner( "value"));
            }
        });
        spinner[0].onfocus = function(evt) {
            for(var sl in   spinners ) {
                if(spinners[sl][0].id == evt.target.id) {
                    modified[sl] = true;
                    break;
                }
            }
        };

        spinner[0].onblur = function(evt) {
            for(var sl in   spinners ) {
                if(spinners[sl][0].id == evt.target.id) {
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