
/*	$(".toggle").click(function () {


	    $('.panel').toggle(500);
	});*/

$(".toggle").click(function () {
	$('.panel').hide();
	$(".row").find(".panel").slideToggle(500);
});