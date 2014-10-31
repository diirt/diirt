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
	for ( var i = 0; i < len; i++) {
        var k=1;
	    var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("channel-readonly");
        var callback = function(evt, channel) {
                           switch (evt.type) {
                           case "connection": //connection state changed
                               break;
                           case "value": //value changed
                               var channelValue = channel.getValue();
                               var time = (new Date()).getTime();
                               var y = channelValue.value;
                               var c = charts[channel.getId()];
                               var series = c.options.series[0];
                                   if (k % 50 == 0) {
                                       c.series[0].addPoint([k++, y], true, true);
                               } else {
                                       c.series[0].addPoint([k++, y], true, false);
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
        var options = {
            chart: {
                renderTo: id,
                type: 'spline',
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
            tooltip: {
                formatter: function () {
                    return '<b>' + this.series.name + '</b><br/>' +
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                        Highcharts.numberFormat(this.y, 2);
                }
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
    }
});

window.onbeforeunload = function() {
};
