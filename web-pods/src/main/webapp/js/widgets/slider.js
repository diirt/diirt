/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/jquery-ui/jquery-ui.js"></script>
 * <script src="../js/widgets/lib/jquery-ui/jquery.ui.labeledslider.js"></script>
 * <link href="../js/widgets/lib/jquery-ui/jquery.ui.labeledslider.css" rel="stylesheet"  type="text/css">
 *<script type="text/javascript" language="javascript" src="../js/widgets/slider.js"></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/


$(document).ready(function() {

	var nodes = document.getElementsByClassName("slider");
    var len = nodes.length;
    var sliders = {};
	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var max = parseFloat(nodes[i].getAttribute("max"));
        var min = parseFloat(nodes[i].getAttribute("min"));
        var step = parseFloat(nodes[i].getAttribute("step"));
        var id = nodes[i].getAttribute("id");
        var channel = wp.subscribeChannel(channelname, false);
        var slider = $("#" + id).labeledslider({max: max, step: step, orientation: 'vertical', range: false, tickInterval: 10  });
        sliders[channel.getId()] = slider;
        channel.addCallback(function(evt, channel) {
            switch (evt.type) {
            case "connection": //connection state changed
                break;
            case "value": //value changed
                var channelValue = channel.getValue();
                sliders[channel.getId()].labeledslider( "value", channelValue.value );
                if(channelValue.alarm.severity =="MINOR") {
                  sliders[channel.getId()].animate({ backgroundColor: "yellow"});
                } else if (channelValue.alarm.severity =="MAJOR") {
                   sliders[channel.getId()].animate({ backgroundColor: "red"});
                } else {
                   sliders[channel.getId()].animate({ backgroundColor: "blue"});
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
        });
        $( ".ui-slider-vertical" ).labeledslider({
            stop: function( event, ui ) {
                for(var sl in   sliders) {
                    if(sliders[sl][0].id == event.target.id) {
                        var ch = wp.getChannel(sl);
                        break;
                    }
                }
                ch.value.value = slider.labeledslider( "value");
                ch.updateValue();
            }
        });;
    }

});

window.onbeforeunload = function() {
	ws.close();
};

