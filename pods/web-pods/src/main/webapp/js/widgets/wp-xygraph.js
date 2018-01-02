/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script type="text/javascript" language="javascript" src="../js/widgets/xygraph.js"></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/


$(document).ready(function() {

	var nodes = document.getElementsByClassName("wp-xygraph");
		var len = nodes.length;
		var charts = {};
		var point = {};
		var maxPoints = {};
	for ( var i = 0; i < len; i++) {
	    var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var maxPoint = nodes[i].getAttribute("data-max-points");
        //default chart type will be line
        var dataType = nodes[i].getAttribute("data-graph-type") != null ? nodes[i].getAttribute("data-graph-type") : 'spline';
        var callback = function(evt, channel) {
           switch (evt.type) {
           case "connection": //connection state changed
               channel.readOnly = !evt.writeConnected;
               break;
           case "value": //value changed
               var channelValue = channel.getValue();
               // Display the new value
               if ("value" in channelValue) {
               //if the Vtype is an array display only the current values
                  if (channelValue.type.name.indexOf("Array") !=-1) {
                      var dataValues = [];
                      var i=0;
                      for(v in channelValue.value) {
                            dataValues[i] = ([channelValue.time.unixSec * 1000 + i++, Number(v)]);
                      }
                      var y = channelValue.value[0];
                      var c = charts[channel.getId()];
                      c.series[0].setData(dataValues);
                  } else {
                        var y = channelValue.value;
                        var c = charts[channel.getId()];
                            if (point[channel.getId()] >= maxPoints[channel.getId()]) {
                                c.series[0].addPoint([channelValue.time.unixSec * 1000, y], true, true);
                                point[channel.getId()] ++;
                            } else {
                                c.series[0].addPoint([channelValue.time.unixSec * 1000, y], true, false);
                                point[channel.getId()] ++;
                            }
                  }
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
                type: 'datetime',
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
            tooltip: {
                formatter: function () {
                //the displayed time will be without the timezone
                    return '<b>' + this.series.name + '</b><br/>' +
                        Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +
                        Highcharts.numberFormat(this.y, 2);
                }
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
