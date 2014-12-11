/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/Drinks/Drinks.js" ></script>
 * <script type="text/javascript" language="javascript" src="../js/widgets/led.js"></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 * <script src="../js/widgets/lib/Drinks/Led.js" ></script>
 ******************************************************************************/


$(document).ready(function() {
	var nodes = document.getElementsByClassName("led");
	var len = nodes.length;
    var leds = {};
	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var type = nodes[i].getAttribute("data-type");
        var id = nodes[i].getAttribute("id");
        nodes[i].innerHTML = '<canvas id="'+id+'">[No canvas support]</canvas>';
        fitToContainer(nodes[i].firstChild);
        if (channelname != null && channelname.trim().length > 0) {
            var callback = function(evt, channel) {
                               switch (evt.type) {
                               case "connection": //connection state changed
                                   channel.readOnly = !evt.writeConnected;
                                   break;
                               case "value": //value changed
                                   var channelValue = channel.getValue();
                                   if(channelValue.value) {
                                      leds[channel.getId()].fillStyle = "#00FF00";
                                   } else {
                                      leds[channel.getId()].fillStyle = "#ccc";
                                   }
                                   leds[channel.getId()].fill();
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
            var canvas = nodes[i].firstChild;
            var context = canvas.getContext("2d");
            var centerX = canvas.width / 2;
            var centerY = canvas.height / 2;
            var radius = 10;
            context.beginPath();
            context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
            context.fillStyle = "#ccc";
            context.fill();
            context.lineWidth = 1;
            context.strokeStyle = "black";
            context.stroke();
            leds[channel.getId()] = context;
        }
    }
});


function fitToContainer(canvas){
      canvas.style.width='100%';
      canvas.style.height='100%';
      canvas.width  = canvas.offsetWidth;
      canvas.height = canvas.offsetHeight;
}

window.onbeforeunload = function() {
	wp.close();
};

