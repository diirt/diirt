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

	var nodes = document.getElementsByClassName("spinner");
    var len = nodes.length;
    var spinners = {};
	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var max = parseFloat(nodes[i].getAttribute("data-max"));
        var min = parseFloat(nodes[i].getAttribute("data-min"));
        var step = parseFloat(nodes[i].getAttribute("data-step"));
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var id = nodes[i].getAttribute("id");
        var callback = function(evt, channel) {
                           switch (evt.type) {
                           case "connection": //connection state changed
                               channel.readOnly = !evt.writeConnected;
                               break;
                           case "value": //value changed
                               var channelValue = channel.getValue();
                               spinners[channel.getId()].spinner( "value", channelValue.value );
                               if(channelValue.alarm.severity =="MINOR") {
                               spinners[channel.getId()].animate({ backgroundColor: "yellow"});
                               } else if (channelValue.alarm.severity =="MAJOR") {
                                   spinners[channel.getId()].animate({ backgroundColor: "red"});
                               } else {
                                   spinners[channel.getId()].animate({ backgroundColor: "white"});
                               }
                               break;
                           case "error": //error happened
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
        var spinner = $("#" + id).spinner({ step: step, min: min, max: max});
        spinners[channel.getId()] = spinner;

        $('.ui-spinner-button').click(function(event) {
            for(var sl in   spinners) {
                if(spinners[sl][0].id == event.currentTarget.parentElement.childNodes[0].id) {
                    var ch = wp.getChannel(sl);
                    break;
                }
            }
            ch.value.value = spinner.labeledspinner( "value");
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
                ch.setValue(spinner.labeledspinner( "value"));
            }
        });
    }

});


window.onbeforeunload = function() {
	wp.close();
};

