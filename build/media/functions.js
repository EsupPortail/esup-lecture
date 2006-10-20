/**
* Simule a click for a link.
*/

function simulateLinkClick(linkId) {
	var fireOnThis = document.getElementById(linkId)
	if (document.createEvent) {
		// Firefox
		var evObj = document.createEvent('MouseEvents');
		evObj.initEvent( 'click', true, false );
		fireOnThis.dispatchEvent(evObj);
	} else if (document.createEventObject) {
		// IE
		fireOnThis.fireEvent('onclick');
	}
}
/**
* HighLight table rows.
*/
function highlightTableRows(tableId) {
    var previousClass = null;
    var table = document.getElementById(tableId); 
   	var tbody = table.getElementsByTagName("tbody")[0];
    if (tbody == null) {
        var rows = table.getElementsByTagName("tr");
     } else {
        var rows = tbody.getElementsByTagName("tr");
    }
    // add event handlers so rows light up and are clickable
    for (i=0; i < rows.length; i++) {
        rows[i].onmouseover = function() {previousClass=this.className;this.className='portlet-table-selected';};
        rows[i].onmouseout = function() { this.className=previousClass };
    }
}
/*
* Hide buttons in dataTable if javascript is activated.
*/
function hideDataTableButton(tableId, btnId){
		var dataTable = document.getElementById(tableId);
		var tbody = dataTable.getElementsByTagName("tbody")[0];
		if (tbody == null) {
        	var rows = dataTable.getElementsByTagName("tr");
    	 } else {
       		 var rows = tbody.getElementsByTagName("tr");
    	}
		// hide button for each row.
   		 for (i=0; i < rows.length; i++) {
       	 var btn = document.getElementById(tableId+":"+i+":"+btnId);
       	 if(btn!=null){
       	 	btn.style.visibility = "hidden";
       	 	btn.style.width = 1;
   		 }
   	}
}

