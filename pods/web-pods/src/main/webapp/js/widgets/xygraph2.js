/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/dygraph-combined.js"></script>
 * <script type="text/javascript" language="javascript" src="../js/widgets/xygraph2.js"></script>
 ******************************************************************************/


$(document).ready(function() {

	var nodes = document.getElementsByClassName("xygraph2");
		var len = nodes.length;
		var charts = {};
	for ( var i = 0; i < len; i++) {
        var k=1;
	    var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("channel-readonly");
	    var buffer=[[new Date(), 0]], totalPoints=200;
	    var callback = function(evt, channel) {
                           switch (evt.type) {
                           case "connection": //connection state changed
                               channel.readOnly = !evt.writeConnected;
                               break;
                           case "value": //value changed
                               var channelValue = channel.getValue();
                               var c = charts[channel.getId()];
                               var b = c.rawData_;
                               if(b.length>=totalPoints) {
                                   b=b.slice(1);
                               }
                               b.push([new Date(), channelValue.value]);
                               c.updateOptions( { 'file': b } );
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
        var id = nodes[i].getAttribute("id");

        var chart = new Dygraph(id, buffer,
                                 {
                                   drawPoints: true,
                                   labels: ['Time', channelname]
                                 });
        charts[channel.getId()] = chart;
    }
});


window.onbeforeunload = function() {
	wp.close();
};
