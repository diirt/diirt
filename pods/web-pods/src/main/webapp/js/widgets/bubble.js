google.load("visualization", "1", {packages: ["corechart"]});
google.setOnLoadCallback(drawSeriesChart);

function drawSeriesChart() {
    var nodes = document.getElementsByClassName("bubble-graph");
    var len = nodes.length;
    var charts = {};
    for (var i = 0; i < len; i++) {
        var k = 1;
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = true;
        var callback = function (evt, channel) {
            switch (evt.type) {
                case "connection": //connection state changed
                    break;
                case "value": //value changed
                    var xId = evt.value.columnNames.indexOf("wall");
                    var yId = evt.value.columnNames.indexOf("cpu");
                    var colorId = evt.value.columnNames.indexOf("queue");
                    var dataArray = [];
                    dataArray[0] = ['ID', 'Wall Time', 'CPU Time', 'Queue'];
                    var nPoints = evt.value.columnValues[xId].length;
                    for (var i=0; i < nPoints; i++) {
                        dataArray[i+1] = ['', evt.value.columnValues[xId][i], evt.value.columnValues[yId][i], evt.value.columnValues[colorId][i]];
                    }
                    var data = google.visualization.arrayToDataTable(dataArray);
                    

                    var options = {
                        hAxis: {title: 'CPU Time'},
                        vAxis: {title: 'Wall Time'},
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
            ['ID', 'Life Expectancy', 'Fertility Rate', 'Region', 'Population'],
            ['CAN', 80.66, 1.67, 'North America', 33739900],
            ['DEU', 79.84, 1.36, 'Europe', 81902307],
            ['DNK', 78.6, 1.84, 'Europe', 5523095],
            ['EGY', 72.73, 2.78, 'Middle East', 79716203],
            ['GBR', 80.05, 2, 'Europe', 61801570],
            ['IRN', 72.49, 1.7, 'Middle East', 73137148],
            ['IRQ', 68.09, 4.77, 'Middle East', 31090763],
            ['ISR', 81.55, 2.96, 'Middle East', 7485600],
            ['RUS', 68.6, 1.54, 'Europe', 141850000],
            ['USA', 78.09, 2.05, 'North America', 307007000]
        ]);

        var options = {
            title: 'Correlation between life expectancy, fertility rate and population of some world countries (2010)',
            hAxis: {title: 'Life Expectancy'},
            vAxis: {title: 'Fertility Rate'},
            bubble: {textStyle: {fontSize: 11}}
        };

        var chart = new google.visualization.BubbleChart(document.getElementById(id));
        chart.draw(data, options);

        charts[channel.getId()] = chart;
    }
}
