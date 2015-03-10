/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script src="../js/widgets/lib/jquery-ui/jquery-ui.js"></script>
 * <script src="../js/widgets/lib/jquery-ui/jquery.ui.labeledslider.js"></script>
 * <link href="../js/widgets/lib/jquery-ui/jquery.ui.labeledslider.css" rel="stylesheet"  type="text/css">
 *<script type="text/javascript" language="javascript" src="../js/widgets/slider.js"></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/


$(document).ready(function() {

	var nodes = document.getElementsByClassName("wp-progress-bar");
    var len = nodes.length;
    var progressbars = {};
    var currentAlarms = {};

    function updateProgressBarAlarm(severity, id, widget) {
        var currentAlarm = currentAlarms[id];
        if (currentAlarm) {
            widget.classList.remove(currentAlarm);
        }
        switch (severity) {
            case "MINOR":
                currentAlarm = "alarm-minor";
                break;
            case "MAJOR":
                currentAlarm = "alarm-major";
                break;
            case "INVALID":
                currentAlarm = "alarm-invalid";
                break;
            case "UNDEFINED":
                currentAlarm = "alarm-undefined";
                break;
            default:
                currentAlarm = "alarm-none";
                break;
        }
        currentAlarms[id] = currentAlarm;
        widget.css(currentAlarm);
    }
	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var max = nodes[i].getAttribute("data-displayHigh") != null ? parseFloat(nodes[i].getAttribute("data-displayHigh")) : 100;
        var min = nodes[i].getAttribute("data-displayLow") != null ? parseFloat(nodes[i].getAttribute("data-displayLow")) : 0;
        var id = nodes[i].getAttribute("id");
        if (id === null) {
            id = "progress-bar-" + i;
            nodes[i].id = id;
        }
        var callback = function(evt, channel) {
           switch (evt.type) {
           case "connection": //connection state changed
               channel.readOnly = !evt.writeConnected;
               break;
           case "value": //value changed
               var channelValue = channel.getValue();
               progressbars[channel.getId()].progressbar( "value", channelValue.value );
               // Change the style based on the alarm
               if ("alarm" in channelValue) {
                   updateProgressBarAlarm(channelValue.alarm.severity, channel.getId(), progressbars[channel.getId()]);
               } else {
               //    updateProgressBarAlarm("NONE", channel.getId(), progressbars[channel.getId()]);
               }

               // Remove error tooltip
               progressbars[channel.getId()].tooltip("");;
               break;
           case "error": //error happened
               // Change displayed alarm to invalid, and set the
               // tooltip to the error message
               updateProgressBarAlarm("INVALID", channel.getId(), progressbars[channel.getId()]);
               progressbars[channel.getId()].tooltip(evt.error);
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
        var progressbar = $("#" + id).progressbar({max: max, min: min});
        progressbar.style.width = "100%";
        progressbar.style.height = "100%";
        progressbar.style.font = "inherit";
        progressbar.style.textAlign = "inherit";
        progressbars[channel.getId()] = progressbar;
    }

});


window.onbeforeunload = function() {
	wp.close();
};
