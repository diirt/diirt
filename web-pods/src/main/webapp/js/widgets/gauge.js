/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/RGraph/libraries/RGraph.common.core.js" ></script>
 * <script type="text/javascript" language="javascript" src="../js/widgets/gauge.js"></script>
 * <script src="../js/widgets/lib/RGraph/excanvas/excanvas.js"></script>
 * <script src="../js/widgets/lib/RGraph/libraries/RGraph.gauge.js" ></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/


$(document).ready(function() {
	var nodes = document.getElementsByClassName("gauge");
	var len = nodes.length;
    var gauges = {};
	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("channel-readonly");
        var id='rgraph-gauge-'+i;
        nodes[i].innerHTML = '<canvas id="'+id+'">[No canvas support]</canvas>';
        fitToContainer(nodes[i].firstChild);
        if (channelname != null && channelname.trim().length > 0) {
            var displayLow = nodes[i].getAttribute("displayLow") != null ? parseInt(nodes[i].getAttribute("displayLow")) : 0;
            var displayHigh = nodes[i].getAttribute("displayHigh") != null ? parseInt(nodes[i].getAttribute("displayHigh")) : 100;
            var channel = wp.subscribeChannel(channelname, readOnly);
            gauges[channel.getId()] = new RGraph.Gauge(id,
                            displayLow, displayHigh,
                            0).Set('colors.ranges', []);
            channel.addCallback(function(evt, channel) {
                            switch (evt.type) {
                            case "connection": //connection state changed
                                break;
                            case "value": //value changed
                                var channelValue = channel.getValue();
                                if (channelValue.display.lowDisplay == null) {
                                     gauges[channel.getId()] = new RGraph.Gauge(gauges[channel.getId()].id,
                                            displayLow, displayHigh,
                                            0).Set('colors.ranges', []);
                                } else {
                                gauges[channel.getId()] = new RGraph.Gauge(gauges[channel.getId()].id,
                                    channelValue.display.lowDisplay, channelValue.display.highDisplay,
                                    channelValue.value).Set('colors.ranges', []);
                                }
                                var color = 'green';
								if (channelValue.alarm.severity == "MAJOR")
									color = 'red';
								else if (channelValue.alarm.severity == "MINOR")
									color = "yellow";
								gauges[channel.getId()].Set("chart.needle.colors", [ color ]);
								gauges[channel.getId()].value = channelValue.value;
								gauges[channel.getId()].Set ('chart.colors.ranges', [[channelValue.display.lowAlarm, channelValue.display.highAlarm, 'Gradient(#060:#0f0:#060)'], [channelValue.display.lowDisplay, channelValue.display.lowAlarm, 'Gradient(#660:yellow:#660)'],
                                                                                    [channelValue.display.highAlarm, channelValue.display.highDisplay, 'Gradient(#600:red:#600)']]);
								gauges[channel.getId()].Draw();
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

            gauges[channel.getId()].canvas.onclick = function (e)
            {
                var obj   = e.target.__object__;
                var value = obj.getValue(e);
                obj.value = value;
                for(var sl in   gauges) {
                    if(gauges[sl].id == obj.id) {
                        var ch = wp.getChannel(sl);
                        break;
                    }
                }
                ch.value.value = value;
                ch.updateValue();
            }
        }
    }
});

window.onbeforeunload = function() {
	ws.close();
};

function fitToContainer(canvas){
	  canvas.style.width='100%';
	  canvas.style.height='100%';
	  canvas.width  = canvas.offsetWidth;
	  canvas.height = canvas.offsetHeight;
}
