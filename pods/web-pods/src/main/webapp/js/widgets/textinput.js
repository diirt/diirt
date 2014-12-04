/*******************************************************************************
 * @author: eschuhmacher
 *
 * scripts to be included on the html file
 * <script type="text/javascript" language="javascript" src="../js/widgets/textinput.js"></script>
 * <script src="../js/widgets/lib/jquery-2.0.3.min.js"></script>
 ******************************************************************************/

$(document).ready(function() {

	var nodes = document.getElementsByClassName("textinput");
	var len = nodes.length;
    var inputs = {};

	for ( var i = 0; i < len; i++) {
        var channelname = nodes[i].getAttribute("data-channel");
        var readOnly = nodes[i].getAttribute("data-channel-readonly");
        var id = nodes[i].getAttribute("id");
        var input = document.createElement("textarea");
        input.id = id;
        input.style.resize="none";
        var div = document.getElementById(id);
        div.appendChild(input);
        fitToContainer(div.firstChild);

        if (channelname != null && channelname.trim().length > 0) {
            var callback = function(evt, channel) {
                               switch (evt.type) {
                               case "connection": //connection state changed
                                   channel.readOnly = !evt.writeConnected;
                                   break;
                               case "value": //value changed
                                   var channelValue = channel.getValue();
                                   inputs[channel.getId()].value =(channelValue.value);
                                   if(channelValue.alarm.severity =="MINOR") {
                                       inputs[channel.getId()].style.backgroundColor ="yellow";
                                   } else if (channelValue.alarm.severity =="MAJOR") {
                                       inputs[channel.getId()].style.backgroundColor = "red";
                                   } else {
                                       inputs[channel.getId()].style.backgroundColor ="white";
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
            inputs[channel.getId()] = input;
        }
        input.onkeyup = function(evt) {
            if (evt.keyCode == 13) {
                for(var sl in   inputs  ) {
                    if(inputs[sl].id == evt.target.id) {
                        var ch = wp.getChannel(sl);
                        break;
                    }
                }
                ch.setValue(input.value.trim());
            }
        };
    }

});

function fitToContainer(canvas){
	  canvas.style.width='100%';
	  canvas.style.height='100%';
	  canvas.width  = canvas.offsetWidth;
	  canvas.height = canvas.offsetHeight;
}

window.onbeforeunload = function() {
	wp.close();
};

