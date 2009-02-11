	var myLayout; // a var is required because this page utilizes: myLayout.allowOverflow() method
	$(document).ready(function () {
//		alert($('#panels-layout').width());
//		alert($('#panelLeft-ui').width());
//		alert($('#panelRight-ui').width());
		$('#panels-layout').height($('#panelLeft-ui').height()+12);
		if ($('#panelRight-ui').height() > $('#panelLeft-ui').height()) {
			$('#panels-layout').height($('#panelRight-ui').height()+12);
		}
		var westSize = $('#panelLeft-ui').width();
		myLayout = $('#panels-layout').layout({
			west__size: westSize,
			west__onresize: function () {
				var newSize = Math.round($('#panelLeft-ui').width() * 100 / $('#panels-layout').width());
				JHomeController.changeTreeSize(newSize);
				alert(newSize);
			}
		});
//		alert($('#panelLeft-ui').width());
//		alert($('#panelRight-ui').width());
 	});

