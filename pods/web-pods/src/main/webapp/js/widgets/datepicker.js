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

	var nodes = document.getElementsByClassName("datepicker");
    var len = nodes.length;
    var datepickers = {};
	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("channel-readonly");
        var max = parseFloat(nodes[i].getAttribute("max"));
        var min = parseFloat(nodes[i].getAttribute("min"));
        var step = parseFloat(nodes[i].getAttribute("step"));
        var id = nodes[i].getAttribute("id");
        var callback = function(evt, channel) {
                           switch (evt.type) {
                           case "connection": //connection state changed
                               channel.readOnly = !evt.writeConnected;
                               break;
                           case "value": //value changed
                               var channelValue = channel.getValue();
                               $("#" + datepickers[channel.getId()].id).datepicker("setDate", new Date(channelValue.value / 1000000));
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
        var datepicker = $("#" + id).datepicker({
                                 showOn: "button",
                                 buttonImage: "http://jqueryui.com/resources/demos/datepicker/images/calendar.gif",
                                 buttonImageOnly: true,
                                 onSelect: function(dateText, inst)
                                         {
                                            for(var sl in   datepickers) {
                                                if(datepickers[sl][0].id == inst.id) {
                                                    var ch = wp.getChannel(sl);
                                                    break;
                                                }
                                            }
                                            var date = $("#" + inst.id).datepicker( "getDate" ).getTime() * 1000000;
                                            ch.setValue(dateText);
                                         }
                                 });
        datepickers[channel.getId()] = datepicker;
    }
});


window.onbeforeunload = function() {
	wp.close();
};
