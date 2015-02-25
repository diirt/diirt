google.load("visualization", "1", {packages: ["table"]});
google.setOnLoadCallback(drawSeriesChart);

function drawSeriesChart() {
    var nodes = document.getElementsByClassName("wp-table");
    var len = nodes.length;
    var counter = 0;
    var tables = {};

    for (var i = 0; i < len; i++) {
        // Extract the node and all its properties
        var node = nodes[i];
        var id = nodes[i].getAttribute("id");
        if (id === null) {
            counter++;
            id = "led-" + counter;
            nodes[i].id = id;
        }
        var dataChannel = nodes[i].getAttribute("data-channel");
        
        
        var convertVTableToDataTable = function(vtable) {
            var data = new google.visualization.DataTable();
            var rows = [];
            for (var col=0; col < vtable.columnNames.length; col++) {
                switch (vtable.columnTypes[col]) {
                    case "String":
                        data.addColumn('string', vtable.columnNames[col]);
                        for (var row=0; row < vtable.columnValues[col].length; row++) {
                            if (!rows[row]) {
                                rows[row] = [];
                            }
                            rows[row][col] = vtable.columnValues[col][row];
                        }
                        break;
                    case "double":
                    case "float":
                    case "long":
                    case "int":
                    case "short":
                    case "byte":
                        data.addColumn('number', vtable.columnNames[col]);
                        for (var row=0; row < vtable.columnValues[col].length; row++) {
                            if (!rows[row]) {
                                rows[row] = [];
                            }
                            rows[row][col] = vtable.columnValues[col][row];
                        }
                        break;
                    default:
                        break;
                }
            }
            data.addRows(rows);
            return data;
        };
        
        var processValue = function (id, value) {
            var dataTable = convertVTableToDataTable(value);
            var parameters = new Object();
            if (tables[id].getSortInfo()) {
                parameters.sortColumn = tables[id].getSortInfo().column;
                parameters.sortAscending = tables[id].getSortInfo().ascending;
            }
            parameters.height = "inherit";
            parameters.width = "inherit";
            tables[id].draw(dataTable, parameters);
        };
        
        var addError = function (message, channel, node) {
            console.log("table " + tables[channel.getId()]);
            google.visualization.errors.addError(node,
                message, "", {'removable': true});
        };
        
        var createCallback = function (nNode) {

            return function (evt, channel) {
                switch (evt.type) {
                    case "connection": //connection state changed
                        break;
                    case "value": //value changed
                        processValue(channel.getId(), evt.value);
                        break;
                    case "error": //error happened
                        addError(evt.error, channel, nNode);
                        break;
                    case "writePermission":	// write permission changed.
                        break;
                    case "writeCompleted": // write finished.
                        break;
                    default:
                        break;
                }
            };
        
        };
        
        var channel = wp.subscribeChannel(dataChannel, createCallback(node), true);

        var data = google.visualization.arrayToDataTable([
            ['', '', ''],
            ['', '', '']
        ]);

        var options = {
            title: 'Waiting for data',
            hAxis: {title: ''},
            vAxis: {title: ''},
            bubble: {textStyle: {fontSize: 11}}
        };
        
        // Prepare html
        var table = new google.visualization.Table(node);
        tables[channel.getId()] = table;

        //table.draw(data, options);
        
    }
}
