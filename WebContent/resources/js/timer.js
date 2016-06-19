var paramhours = $('#hour').attr('value');
var parammins = $('#min').attr('value');

 //alert(paramhours);
 //alert(parammins);
 
 
 //modify
 var mins =  parammins;
 var hours = paramhours;

//var mins = 1;
//var hours = 0;

//Set the number of minutes you need
    var secs = mins * 60 + hours * 3600;
    var currentSeconds = 0;
    var currentMinutes = 0;
    setTimeout('Decrement()',1000);

    function Decrement() {
        currentMinutes = Math.floor(secs / 60);
        currentSeconds = secs % 60;
        if(currentSeconds <= 9) currentSeconds = "0" + currentSeconds;
        secs--;
        document.getElementById("timerText").innerHTML = currentMinutes + ":" + currentSeconds; //Set the element id you need the time put into.
        if(secs !== -1) setTimeout('Decrement()',1000);
    }
//action when 00:00
    
   if(currentSeconds == 1 && currentMinutes == 0){
	   $('.row .enonce-location').hide();
	   $('.fp-controlArrow .fp-prev').hide();
	   $('.fp-controlArrow .fp-next').hide();
	   alert('toto');
	   
    }
    
   