
/*	$(".toggle").click(function () {


	    $('.panel').toggle(500);
	});*/

$(".toggle").click(function () {
	$('.panel').hide();
	$(".row").find(".panel").slideToggle(500);
});

$(function(){
    $('#landing .container').fullpage({
      anchors: ['firstPage', 'secondPage', '3rdPage','4thPage','5thPage'],
      navigation: true,
      navigationPosition: 'right',
      navigationTooltips: ['Accueil', 'Notre But', 'Fonctionnalités', 'Interface', 'Contact']
    });

  });