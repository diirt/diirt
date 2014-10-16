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
        var max = parseFloat(nodes[i].getAttribute("max"));
        var min = parseFloat(nodes[i].getAttribute("min"));
        var step = parseFloat(nodes[i].getAttribute("step"));

        var id = nodes[i].getAttribute("id");
        var channel = wp.subscribeChannel(channelname, false);
        var spinner = $("#" + id).spinner({ step: step, min: min, max: max});
        spinners[channel.getId()] = spinner;
        channel.addCallback(function(evt, channel) {
            switch (evt.type) {
            case "connection": //connection state changed
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
            case "writeFinished": // write finished.
                break;
            default:
                break;
            }
        });

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
                ch.value.value = spinner.labeledspinner( "value");
                ch.updateValue();
            }
        });
    }

});


window.onbeforeunload = function() {
	ws.close();
};

