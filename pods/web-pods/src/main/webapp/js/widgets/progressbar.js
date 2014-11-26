/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/RGraph/libraries/RGraph.common.core.js" ></script>
 * <script type="text/javascript" language="javascript" src="../js/widgets/progressbar.js"></script>
 * <script src="../js/widgets/lib/RGraph/excanvas/excanvas.js"></script>
 * <script src="../js/widgets/lib/RGraph/libraries/RGraph.hprogress.js" ></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/


$(document).ready(function() {
	var nodes = document.getElementsByClassName("progressbar");
	var len = nodes.length;
    var progressbars = {};
	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var id='rgraph-hprogress-'+i;
        nodes[i].innerHTML = '<canvas id="'+id+'">[No canvas support] </canvas>';
        fitToContainer(nodes[i].firstChild);
        if (channelname != null && channelname.trim().length > 0) {
            var displayLow = nodes[i].getAttribute("data-displayLow") != null ? parseInt(nodes[i].getAttribute("data-displayLow")) : 0;
            var displayHigh = nodes[i].getAttribute("data-displayHigh") != null ? parseInt(nodes[i].getAttribute("data-displayHigh")) : 100;
            var callback = function(evt, channel) {
                               switch (evt.type) {
                               case "connection": //connection state changed
                                   channel.readOnly = !evt.writeConnected;
                                   break;
                               case "value": //value changed
                                    var channelValue = channel.getValue();
                                    if (channelValue.display.lowDisplay == null) {
                                    } else {
                                    progressbars[channel.getId()] =new RGraph.HProgress(progressbars[channel.getId()].id,
                                                                      channelValue.value, channelValue.display.highDisplay);
                                    }
                                    var color = 'green';

                                    if(channelValue.alarm.severity =="MINOR") {
                                         progressbars[channel.getId()].Set('chart.colors', ["Gradient(#660:yellow:#660)"]);
                                       } else if (channelValue.alarm.severity =="MAJOR") {
                                          progressbars[channel.getId()].Set('chart.colors', ["Gradient(#600:red:#600)"]);
                                       } else {
                                          progressbars[channel.getId()].Set('chart.colors', ["Gradient(#060:#0f0:#060)"]);
                                       }
                                       progressbars[channel.getId()].Set('tickmarks.inner', true);
                                       progressbars[channel.getId()].Set('tickmarks.zerostart', true);
                                       progressbars[channel.getId()].Set('bevel', true);
                                       progressbars[channel.getId()].Set('margin', 10);
                                    progressbars[channel.getId()].Draw();
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
            progressbars[channel.getId()] = new RGraph.HProgress(id,0, displayHigh);
            progressbars[channel.getId()].Set('tickmarks.inner', true);
            progressbars[channel.getId()].Set('tickmarks.zerostart', true);
            progressbars[channel.getId()].Set('bevel', true);
            progressbars[channel.getId()].Set('margin', 10);
            progressbars[channel.getId()].Draw();

            progressbars[channel.getId()].canvas.onclick = function (e)
            {
                var obj   = e.target.__object__;
                var value = obj.getValue(e);
                obj.value = value;
                for(var sl in   progressbars) {
                    if(progressbars[sl].id == obj.id) {
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
