var currentMoney = 0.00;

$(document).ready(function () {
    loadInventory();
    $('#add-dollar-btn').click(addDollar);
    $('#add-quarter-btn').click(addQuarter);
    $('#add-dime-btn').click(addDime);
    $('#add-nickel-btn').click(addNickel);
    $('#purchase-btn').click(vendItem);
    
});


function loadInventory() {
    $('#item-display').empty();
    var itemDisplay = $('#item-display');

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/items',
        header: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        'data-type': 'json',
        success: function (AllItems, status) {
            $.each(AllItems, function (index, item) {

                itemDiv = $('#item-template').clone();

                itemDiv.removeClass('hidden');
                itemDiv.find('.item-number').text(item.id);
                itemDiv.attr('onclick', 'selectItem(' + item.id + ')');
                itemDiv.find('.item-name').text(item.name);
                itemDiv.find('.item-price').text(item.price.toFixed(2));
                itemDiv.find('.item-quantity').text('Quantity Left: ' + item.quantity);
                itemDisplay.append(itemDiv);


            });
        },
        error: function () {
            showServerDown();
        }
    });
}

function selectItem(id) {
    clearErrors();
    console.log(id);
    console.log($(this).id);
    $('#display-item-num').text(id);
}

function addDollar() {
    clearErrors();
    currentMoney += 1;
    $('#current-money').text(currentMoney.toFixed(2));
}

function addQuarter() {
    clearErrors();
    currentMoney += .25;
    $('#current-money').text(currentMoney.toFixed(2));
}

function addDime() {
    clearErrors();
    currentMoney += .10;
    $('#current-money').text(currentMoney.toFixed(2));
}

function addNickel() {
    clearErrors();
    currentMoney += .05;
    $('#current-money').text(currentMoney.toFixed(2));
}

function makeChange() {

    if (currentMoney <= 0)
        return;
    var numQuarters = 0;
    var numDimes = 0;
    var numNickels = 0;
    var numPennies = 0;
    while (currentMoney >= 0.25) {
        currentMoney -= 0.25
        currentMoney = currentMoney.toFixed(2);
        numQuarters++;
    }
    while (currentMoney >= .10) {
        currentMoney -= 0.10;
        currentMoney = currentMoney.toFixed(2);
        numDimes++;
    }
    while (currentMoney >= 0.05) {
        currentMoney -= 0.05;
        currentMoney = currentMoney.toFixed(2);
        numNickels++;
    }
    while (currentMoney >= 0.01) {
        currentMoney -= 0.01;
        currentMoney = currentMoney.toFixed(2);
        numPennies++;
    }

    var changeArray = [numQuarters, numDimes, numNickels, numPennies];
    displayChange(changeArray);
}

function displayChange(change) {

    var output = '';

    if (change[0] > 0)
        output += 'Quarters: ' + change[0];
    if (change[1] > 0)
        output += ' Dimes: ' + change[1]
    if (change[2] > 0)
        output += ' Nickels: ' + change[2];
    if (change[3] > 0)
        output += ' Pennies: ' + change[3];

    $('#display-change').text(output);

    currentMoney = 0;
    $('#current-money').text('');
    //clear after 5 seconds or next button click
    clear(clearChangeBox);
}

function vendItem() {
    clearErrors();
    var itemId = $('#display-item-num').text();
    if (!validateItemSelected(itemId))
        return;

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/money/' + currentMoney.toFixed(2) + '/item/' + itemId,
        header: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        'data-type': 'json',
        success: function (change, status) {

            var changeArray = getChangeFromObj(change);
            displaySuccess(changeArray);
            loadInventory();
        },
        error: function (error) {
            displayError(error.responseJSON.message);
            clear(clearMessageBox);
        }
    });
}

function getChangeFromObj(change) {
    var changeArray = [];
    changeArray.push(change.quarters);
    changeArray.push(change.dimes);
    changeArray.push(change.nickels);
    changeArray.push(change.pennies);
    return changeArray;
}

function validateItemSelected(itemId) {

    if (itemId < 1 || itemId > 9) {
        displayError("Make Selection First");
        clear(clearMessageBox);
        return false;
    }
    return true;
}

function showServerDown() {
    displayError("Server Down  -_-")
    var buttons = document.getElementsByClassName('btn');
    $.each(buttons, function (index, button) {
        button.disabled = true;
    })
}

function displaySuccess(changeArray) {
    displayChange(changeArray);

    $('#display-item-num').text('');
    $('#display-message').text('Thank You!');
}

function displayError(errorMsg) {
    $('#display-message').css("border-color", "red");
    $('#display-message').text(errorMsg);
}

function clear(msgBoxToClear) {
    setTimeout(msgBoxToClear, 4000);
}

//For timed clear
function clearMessageBox() {
    $('#display-message').css("border-color", "whitesmoke");
    $('#display-message').text('');
}

//For timed clear
function clearChangeBox() {
    $('#display-change').text('');
    $('#display-message').text('');
}

//For automatic (on click)
function clearErrors() {
    $('#display-change').text('');
    $('#display-message').css("border-color", "whitesmoke");
    $('#display-message').text('');
}