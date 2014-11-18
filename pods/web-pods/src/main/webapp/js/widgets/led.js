/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/RGraph/libraries/RGraph.common.core.js" ></script>
 * <script type="text/javascript" language="javascript" src="../js/widgets/led.js"></script>
 * <script src="../js/widgets/lib/RGraph/excanvas/excanvas.js"></script>
 * <script src="../js/widgets/lib/RGraph/libraries/RGraph.led.js" ></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/


$(document).ready(function() {
	var nodes = document.getElementsByClassName("led");
	var len = nodes.length;
    var leds = {};
	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var type = node[i].getAttribute("data-type");
        var id = nodes[i].getAttribute("id");
        if (channelname != null && channelname.trim().length > 0) {
            var callback = function(evt, channel) {
                               switch (evt.type) {
                               case "connection": //connection state changed
                                   channel.readOnly = !evt.writeConnected;
                                   break;
                               case "value": //value changed
                                   var channelValue = channel.getValue();
                                   var led_id = leds[channel.getId()].id;
                                   if(channelValue.value) {
                                      Drinks.getElementById(led_id).on();
                                   } else {
                                      Drinks.getElementById(led_id).off();
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
            leds[channel.getId()] = new Drinks.createElement('led', {"id":id});
            Drinks.appendChild(id, leds[channel.getId()]);
            leds[channel.getId()].onclick = function (e)
            {

            }
        }
    }
});


window.onbeforeunload = function() {
	wp.close();
};

