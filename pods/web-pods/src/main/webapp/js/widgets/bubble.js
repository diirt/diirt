google.load("visualization", "1", {packages: ["corechart"]});
google.setOnLoadCallback(drawSeriesChart);

function drawSeriesChart() {
    var nodes = document.getElementsByClassName("bubble-graph");
    var len = nodes.length;
    var charts = {};
    for (var i = 0; i < len; i++) {
        var k = 1;
        var channelname = nodes[i].getAttribute("data-channel");
        var xColumn = nodes[i].getAttribute("data-x-column");
        var yColumn = nodes[i].getAttribute("data-y-column");
        var colorColumn = nodes[i].getAttribute("data-color-column");
        var readOnly = true;
        var callback = function (evt, channel) {
            switch (evt.type) {
                case "connection": //connection state changed
                    channel.readOnly = !evt.writeConnected;
                    break;
                case "value": //value changed
                    var xId = evt.value.columnNames.indexOf(xColumn);
                    var yId = evt.value.columnNames.indexOf(yColumn);
                    var colorId = evt.value.columnNames.indexOf(colorColumn);
                    var dataArray = [];
                    dataArray[0] = ['ID', xColumn, yColumn, colorColumn];
                    var nPoints = evt.value.columnValues[xId].length;
                    for (var i=0; i < nPoints; i++) {
                        dataArray[i+1] = ['', evt.value.columnValues[xId][i], evt.value.columnValues[yId][i], evt.value.columnValues[colorId][i]];
                    }
                    var data = google.visualization.arrayToDataTable(dataArray);
                    

                    var options = {
                        hAxis: {title: xColumn},
                        vAxis: {title: yColumn},
                        bubble: {textStyle: {fontSize: 11}},
                        sizeAxis: {minValue: 0,  maxSize: 10}
                    };
                    
                    charts[channel.getId()].draw(data, options);
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


        var data = google.visualization.arrayToDataTable([
            ['ID', 'X', 'Y', 'Color', 'Size'],
            ['', 0, 0, '', 0]
        ]);

        var options = {
            title: 'Waiting for data',
            hAxis: {title: ''},
            vAxis: {title: ''},
            bubble: {textStyle: {fontSize: 11}}
        };

        var chart = new google.visualization.BubbleChart(document.getElementById(id));
        chart.draw(data, options);

        charts[channel.getId()] = chart;
    }
}
