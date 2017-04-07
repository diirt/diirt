/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/HighCharts/js/highcharts.js" ></script>
 * <script src="../js/widgets/lib/HighCharts/js/highcharts-more.js"></script>
 * <script type="text/javascript" language="javascript" src="../js/widgets/xygraph.js"></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/


$(document).ready(function() {

	var nodes = document.getElementsByClassName("xygraph");
		var len = nodes.length;
		var charts = {};
		var point = {};
		var maxPoints = {};
	for ( var i = 0; i < len; i++) {
	    var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var maxPoint = nodes[i].getAttribute("data-max-points");
        var dataType = nodes[i].getAttribute("data-grap-type") != null ? nodes[i].getAttribute("data-grap-type") : 'spline';
        var callback = function(evt, channel) {
                           switch (evt.type) {
                           case "connection": //connection state changed
                               channel.readOnly = !evt.writeConnected;
                               break;
                           case "value": //value changed
                               var channelValue = channel.getValue();
                               var time = (new Date()).getTime();
                               var y = channelValue.value;
                               var c = charts[channel.getId()];
                                   if (point[channel.getId()] >= maxPoints[channel.getId()]) {
                                       c.series[0].addPoint([point[channel.getId()]++, y], true, true);
                               } else {
                                       c.series[0].addPoint([point[channel.getId()] ++, y], true, false);
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
        var id = nodes[i].getAttribute("id");
        if (id === null) {
            id = "xygraph-" + i;
            nodes[i].id = id;
        }
        var options = {
            chart: {
                renderTo: id,
                type: dataType,
                animation: Highcharts.svg, // don't animate in old IE
                marginRight: 10

            },
            title: {
                text: channelname
            },
            xAxis: {
                type: 'linear',
                tickPixelInterval: 150
            },
            yAxis: {
                title: {
                    text: channelname
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            legend: {
                enabled: false
            },
            exporting: {
                enabled: false
            },
            series: [{
                name: channelname,
                data: []
            }]
        }
        var chart = new Highcharts.Chart(options);
        charts[channel.getId()] = chart;
        maxPoints[channel.getId()] = maxPoint != null ? maxPoint : 50;
        point[channel.getId()] = 0;
    }
});


window.onbeforeunload = function() {
	wp.close();
};
