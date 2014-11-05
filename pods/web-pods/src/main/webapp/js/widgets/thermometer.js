/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/RGraph/libraries/RGraph.common.core.js" ></script>
 * <script type="text/javascript" language="javascript" src="../js/widgets/thermometer.js"></script>
 * <script src="../js/widgets/lib/RGraph/excanvas/excanvas.js"></script>
 * <script src="../js/widgets/lib/RGraph/libraries/RGraph.thermometer.js" ></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/


$(document).ready(function() {
	var nodes = document.getElementsByClassName("thermometer");
	var len = nodes.length;
    var thermometers = {};
	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("channel-readonly");
        var id='rgraph-thermomether-'+i;
        nodes[i].innerHTML = '<canvas id="'+id+'">[No canvas support] </canvas>';
        fitToContainer(nodes[i].firstChild);
        if (channelname != null && channelname.trim().length > 0) {
            var displayLow = nodes[i].getAttribute("displayLow") != null ? parseInt(nodes[i].getAttribute("displayLow")) : 0;
            var displayHigh = nodes[i].getAttribute("displayHigh") != null ? parseInt(nodes[i].getAttribute("displayHigh")) : 100;
            var callback = function(evt, channel) {
                               switch (evt.type) {
                               case "connection": //connection state changed
                                   channel.readOnly = !evt.writeConnected;
                                   break;
                               case "value": //value changed
                                   var channelValue = channel.getValue();
                                   if (channelValue.display.lowDisplay == null) {
                                        thermometers[channel.getId()] = new RGraph.Thermometer(thermometers[channel.getId()].id,
                                                                            displayLow, displayHigh,
                                                                            channelValue.value);
                                   } else {
                                   thermometers[channel.getId()] =new RGraph.Thermometer(thermometers[channel.getId()].id,
                                                                      channelValue.display.lowDisplay, channelValue.display.highDisplay,
                                                                      channelValue.value);
                                   }
                                   var color = 'green';

                                   if(channelValue.alarm.severity =="MINOR") {
                                        thermometers[channel.getId()].Set('chart.colors', ["Gradient(#660:yellow:#660)"]);
                                   } else if (channelValue.alarm.severity =="MAJOR") {
                                        thermometers[channel.getId()].Set('chart.colors', ["Gradient(#600:red:#600)"]);
                                   } else {
                                        thermometers[channel.getId()].Set('chart.colors', ["Gradient(#060:#0f0:#060)"]);
                                   }
                                   thermometers[channel.getId()].Set('chart.scale.visible', true);
                                   thermometers[channel.getId()].Set('chart.shadow', false);
                                   thermometers[channel.getId()].Draw();
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
            thermometers[channel.getId()] = new RGraph.Thermometer(id,displayLow, displayHigh,0);
            thermometers[channel.getId()].Set('chart.scale.visible', true);
            thermometers[channel.getId()].Set('chart.shadow', false);
            thermometers[channel.getId()].Draw();
            thermometers[channel.getId()].canvas.onclick = function (e)
            {
                var obj   = e.target.__object__;
                var value = obj.getValue(e);
                obj.value = value;
                for(var sl in   thermometers) {
                    if(thermometers[sl].id == obj.id) {
                        var ch = wp.getChannel(sl);
                        break;
                    }
                }
                ch.setValue(value);
            }
        }
    }
});


window.onbeforeunload = function() {
	wp.close();
};

function fitToContainer(canvas){
	  canvas.style.width='100%';
	  canvas.style.height='100%';
	  canvas.width  = canvas.offsetWidth;
	  canvas.height = canvas.offsetHeight;
}
