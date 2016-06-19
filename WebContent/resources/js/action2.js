
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
    
    $('#autoeval').fullpage({
        sectionSelector: '.vertical-scrolling',
        slideSelector: '.horizontal-scrolling',
        controlArrows: true,
        // more options here
    });
    
    $("[id$='circle']").percircle();

  });



$(document).ready(function(){
	$('div.helptext').hide();
	$('div.horizontal-scrolling #help').click(function(){
		$(this).next().toggle();
		//$(this).children().find('div.helptext').css("display" , "block");
		console.log($(this).next());
		console.log($(this).children().children());
	});
	var y = 1;
	   $('div.fp-next').click(function () {
	    	y = y + 1;
	    	document.getElementById('auto:y').value = y;
	    	});
	   
	   $('div.fp-prev').click(function () {
	    	y = y - 1;
	    	document.getElementById('auto:y').value = y;
	    	});
	

});