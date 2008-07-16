function test() {
	var reply27 = function(data) {
		if (data != null && typeof data == 'object') alert(dwr.util.toDescriptiveString(data, 2));
		else dwr.util.setValue('home:rightSubview:d27', dwr.util.toDescriptiveString(data, 1));
	};
	JDate.toLocaleString(reply27);
}

