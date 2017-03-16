var zipcode;
var units;
var currWeatherData;

$(document).ready(function () {

    $('#submit-button').click(displayTemp);
    $('#get-zipcode').click(function() {
        
    });

});

function getInputData() {
    if ($('#get-zipcode').val() < 10000 || $('#get-zipcode').val() > 99999) {
        return false;
    }
    units = $('#get-units').val();
    zipcode = $('#get-zipcode').val();
    console.log(units);
    return true;
}

function displayTemp() {
    $('#temp-data').hide();

    if (getInputData() == false) {
        //THROW ERROR HERE
    }

    var currentURL = "";
    if (units == "imperial") {
        currentURL = 'http://api.openweathermap.org/data/2.5/weather?zip=' + zipcode + ',us&appid=c8959ae21814e3a1b1cdd09608967fac&units=imperial';
    } else {
        currentURL = 'http://api.openweathermap.org/data/2.5/weather?zip=' + zipcode + ',us&appid=c8959ae21814e3a1b1cdd09608967fac&units=metric';
    }



    $.ajax({
        type: 'GET',
        url: currentURL,
        success: function (weatherData) {
            
            $('#current-location').html('Current Conditions in ' + weatherData.name);
            $('#current-weather-pic').attr('src', 'http://openweathermap.org/img/w/' + weatherData.weather[0].icon + '.png');


            $('#current-weather').html(weatherData.weather[0].main);



            $('#current-humidity').html("Humidity: " + weatherData.main.humidity);
            $('#current-temp').html("Temp: " + weatherData.main.temp);
            $('#current-wind').html("Wind: " + weatherData.wind.speed);
        },
        error: function () {
            alert("NO GOOD");
            $('#temp-data').hide();
            return;
        }

    });

    var weeklyURL = "";
    if (units == "imperial") {
        weeklyURL = 'http://api.openweathermap.org/data/2.5/forecast/daily?zip=' + zipcode + ',us&appid=c8959ae21814e3a1b1cdd09608967fac&units=imperial';
    } else {
        weeklyURL = 'http://api.openweathermap.org/data/2.5/forecast/daily?zip=' + zipcode + ',us&appid=c8959ae21814e3a1b1cdd09608967fac&units=metric';
    }

    $.ajax({
        type: 'GET',
        url: weeklyURL,
        success: function (weatherData) {
            for (var i = 1; i <= 5; i++) {
                //Date
                var currDate = new Date(weatherData.list[i].dt * 1000).toUTCString().substring(0, 7);


                $('#day-' + i).append($('<h5 class="text-center">')).text(currDate);
                $('#day-' + i).append($('<br>'));
                //Image and simple description
                $('#day-' + i).append($('<img src="http://openweathermap.org/img/w/' + weatherData.list[i].weather[0].icon + '.png">'));
                $('#day-' + i).append(weatherData.list[i].weather[0].main);
                $('#day-' + i).append($('<br>'));
                //High and low
                $('#day-' + i).append('H: ' + weatherData.list[i].temp.max + ' L: ' + weatherData.list[i].temp.min);
            }

            $('#temp-data').show();

        },
        error: function () {
            alert("NO GOOD");
            $('#temp-data').hide();
        }

    });
}
