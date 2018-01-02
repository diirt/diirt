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
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var max = parseFloat(nodes[i].getAttribute("data-displayHigh"));
        var min = parseFloat(nodes[i].getAttribute("data-displayLow"));
        var step = parseFloat(nodes[i].getAttribute("data-step"));
        var id = nodes[i].getAttribute("id");
        if (id === null) {
            id = "slider-" + i;
            nodes[i].id = id;
        }
        var callback = function(evt, channel) {
           switch (evt.type) {
           case "connection": //connection state changed
               channel.readOnly = !evt.writeConnected;
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
        };
        var channel = wp.subscribeChannel(channelname, callback, readOnly);
        var slider = $("#" + id).labeledslider({max: max, step: step, orientation: 'vertical', range: false, tickInterval: 10  });
        sliders[channel.getId()] = slider;
        $( ".ui-slider-vertical" ).labeledslider({
            stop: function( event, ui ) {
                for(var sl in   sliders) {
                    if(sliders[sl][0].id == event.target.id) {
                        var ch = wp.getChannel(sl);
                        break;
                    }
                }
                ch.setValue(slider.labeledslider( "value"));
            }
        });;
    }

});


window.onbeforeunload = function() {
	wp.close();
};
