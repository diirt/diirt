google.load("visualization", "1", {packages: ["corechart"]});
google.setOnLoadCallback(drawSeriesChart);

function drawSeriesChart() {
    var nodes = document.getElementsByClassName("bubble-graph");
    var len = nodes.length;
    var charts = {};
    for (var i = 0; i < len; i++) {
        // Extract the node and all its properties
        var masterDiv = nodes[i];
        var id = nodes[i].getAttribute("id");
        var channelname = nodes[i].getAttribute("data-channel");
        var xColumn = nodes[i].getAttribute("data-x-column");
        var yColumn = nodes[i].getAttribute("data-y-column");
        var colorColumn = nodes[i].getAttribute("data-color-column");
        var readOnly = true;
        
        // Prepare html
        var tableDiv = document.createElement("div");
        tableDiv.style.display = "table";
        tableDiv.style.height = "100%";
        tableDiv.style.width = "100%";
        masterDiv.appendChild(tableDiv);
        
        var selectX = document.createElement("select");
        selectX.id = id + "-select-x";
        var option = document.createElement("option");
        option.text = "Kiwi";
//        selectX.add(option);
//        selectX.style.display = "table-row";
        tableDiv.appendChild(selectX);

        var graphDiv = document.createElement("div");
        graphDiv.style.display = "table-row";
        graphDiv.style.height = "100%";
        tableDiv.appendChild(graphDiv);
        
        // Helper functions
        var populateSelect = function (select, list) {
            var currentSelection = null;
            if (select.selectedIndex !== -1) {
                currentSelection = select.options[select.selectedIndex].text;
            }
            for (i = 0; i < list.length; i++) {
                if (i < select.options.length) {
                    select.options[i].text = list[i];
                } else {
                    var option = document.createElement("option");
                    option.text = list[i];
                    select.add(option, select[i]);
                }
                if (select[i].text === currentSelection) {
                    select.selectedIndex = i;
                }
            }
            if (select.options.length > list.length) {
                for (i = select.options.length - 1; i >= list.length; i--) {
                    select.remove(i);
                }
            }
        }

        // Connect to live data
        var callback = function (evt, channel) {
            switch (evt.type) {
                case "connection": //connection state changed
                    channel.readOnly = !evt.writeConnected;
                    break;
                case "value": //value changed
                    populateSelect(selectX, evt.value.columnNames);
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
        
        populateSelect(selectX, [xColumn]);

        var chart = new google.visualization.BubbleChart(graphDiv);
        chart.draw(data, options);

        charts[channel.getId()] = chart;
    }
}
