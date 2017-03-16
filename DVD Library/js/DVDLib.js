var MIN_YEAR = 1850;
var MAX_YEAR = 2030;

$(document).ready(function () {

    loadDVDs();

    $('#create-dvd-button').click(createNewDVD);

    $('#edit-dvd-button').click(editDVD);

    $('#show-all-button').click(showAll);


});

function showAll() {

    loadDVDs();
    $('#show-all-button').hide();
}

function showDVDInfo(id) {
    

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/dvd/' + id,
        success: function (dvdData, status) {
            $('#show-a-dvd').html(dvdData.title),
            $('#dvd-info-year').text(dvdData.realeaseYear),
            $('#dvd-info-director').text(dvdData.director),
            $('#dvd-info-rating').text(dvdData.rating),
            $('#dvd-info-notes').text(dvdData.notes)
        },
        error: function () {
            $('#errorMessages').append($('<li>')
                .attr({ class: 'list-group-item list-group-item-danger' })
                .text("Error calling web service, please try again later"));
        }

    });

    showDvdHideMain();

}

function fromShowToMain() {
    loadDVDs();
    $('#main-display-buttons').show();
    $('#main-display-list').show();
    $('#errorMessages').empty();
    $('#show-dvd-banner').hide();
    $('#display-a-dvd').hide();
    $('#show-all-button').hide();
}

function showDvdHideMain() {
    $('#errorMessages').empty();
    $('#main-display-buttons').hide();
    $('#main-display-list').hide();
    $('#show-dvd-banner').show();
    $('#display-a-dvd').show();

}

function loadDVDs() {
    $('#errorMessages').empty();
    $('#display-table-body').empty();
    var contentRows = $('#display-table-body');

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/dvds',
        success: function (allDvdData) {
            $.each(allDvdData, function (index, dvd) {

                var row = '<tr>';
                row += '<td><a onclick="showDVDInfo(' + dvd.dvdId + ')">' + dvd.title + '</a></td>';
                row += '<td>' + dvd.realeaseYear + '</td>';
                row += '<td>' + dvd.director + '</td>';
                row += '<td>' + dvd.rating + '</td>';
                row += '<td><a onclick="getDvdToEdit(' + dvd.dvdId + ')">Edit</a> | <a onclick="deleteDVD(' + dvd.dvdId + ')">Delete</a></td>';
                row += '</tr>';

                contentRows.append(row);
            });
        },
        error: function () {
            $('#errorMessages')
                .append($('<li>')
                    .attr({ class: 'list-group-item list-group-item-danger' })
                    .text("Error calling web service, please try again later"));
        }


    });



}

function searchDVDs() {
    if (validSearchFields() == false)
        return;
    // var searchCat = $('#search-term').val();
    // console.log(searchCat);
    search();

}

function search() {
    $('#errorMessages').empty();
    $('#display-table-body').empty();
    var contentRows = $('#display-table-body');

    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/dvds/' + $('#search-category').val() + '/' + $('#search-term').val(),
        success: function (allDvdData) {
            $.each(allDvdData, function (index, dvd) {
                var row = '<tr>';
                row += '<td><a onclick="showDVDInfo(' + dvd.dvdId + ')">' + dvd.title + '</a></td>';
                row += '<td>' + dvd.realeaseYear + '</td>';
                row += '<td>' + dvd.director + '</td>';
                row += '<td>' + dvd.rating + '</td>';
                row += '<td><a onclick="getDvdToEdit(' + dvd.dvdId + ')">Edit</a> | <a onclick="deleteDVD(' + dvd.dvdId + ')">Delete</a></td>';
                row += '</tr>';

                contentRows.append(row);
            });
        },
        error: function () {
            $('#errorMessages')
                .append($('<li>')
                    .attr({ class: 'list-group-item list-group-item-danger' })
                    .text("Error calling web service, please try again later"));
        }

    });
    $('#show-all-button').show();
}


function validSearchFields() {
    $('#errorMessages').empty();
    var stat = true;

    // Null as a string, stupid as shit
    if ($('#search-category').val() == "null") {
        $('#errorMessages')
            .append($('<li>')
                .attr({ class: 'list-group-item list-group-item-danger' })
                .text("A search category is required, try again"));
        stat = false;
    }
    if ($('#search-term').val().length == 0) {
        $('#errorMessages')
            .append($('<li>')
                .attr({ class: 'list-group-item list-group-item-danger' })
                .text("A search term is required, try again"));
        stat = false;
    }
    return stat;

}


function showMainFromCreate() {
    $('#create-dvd-div').hide();
    $('#create-dvd-banner').hide();
    clearFields();
    loadDVDs();
    $('#main-display-buttons').show();
    $('#main-display-list').show();
    
}

function createDVDMenu() {
    $('#errorMessages').empty();
    $('#main-display-buttons').hide();
    $('#main-display-list').hide();
    $('#create-dvd-div').show();
    $('#create-dvd-banner').show();



}

function createNewDVD() {
    $('#errorMessages').empty();
    var inputs = $('#create-dvd-div').find('input');

    // VALIDATION / INPUT CHECKING
    if (hasValidYear($('#add-dvd-year')) == false)
        return;

    if (hasValidInputs(inputs) == false)
        return;







    $.ajax({
        type: 'POST',
        url: 'http://localhost:8080/dvd',
        data: JSON.stringify({
            title: $('#add-dvd-title').val(),
            realeaseYear: $('#add-dvd-year').val(),
            director: $('#add-dvd-director').val(),
            rating: $('#add-dvd-rating').val(),
            notes: $('#add-dvd-notes').val()
        }),
        headers: {
            'Accept': 'application/json',
            'Content-type': 'application/json'
        },
        'data-type': 'json',
        success: function () {
            showMainFromCreate();
        },
        error: function () {
            $('#errorMessages')
                .append($('<li>')
                    .attr({ class: 'list-group-item list-group-item-danger' })
                    .text("Error calling web service, please try again later"));
        }


    });


}

function clearFields() {
    $('#emptyMessages').empty();
    $('#add-dvd-title').val('');
    $('#add-dvd-year').val('');
    $('#add-dvd-director').val('');
    $('#add-dvd-rating').prop('selectedIndex', 0);


    $('#add-dvd-notes').val('');
}

function hasValidInputs(inputs) {
    $('#errorMessages').empty();

    var errors = [];


    inputs.each(function () {
        if (!this.validity.valid) {
            var errorField = $('label[for=' + this.id + ']').text();
            errors.push(errorField + ' ' + this.validationMessage);
        }
    });

    if (errors.length > 0) {
        $.each(errors, function (index, message) {
            $('#errorMessages')
                .append($('<li>')
                    .attr('class', 'list-group-item list-group-item-danger')
                    .text(message));
        });
        return false;
    } else {
        return true;
    }
}

function hasValidYear(yearInput) {

    if (yearInput.val() < MIN_YEAR || yearInput.val() > MAX_YEAR) {
        $('#errorMessages')
            .append($('<li>')
                .attr({ class: 'list-group-item list-group-item-danger' })
                .text("Year must be between " + MIN_YEAR + " and "
                + MAX_YEAR + ", please try again."));
        return false;
    } else {
        return true;
    }

}

function deleteDVD(id) {
    $('#errorMessages').empty();
    if (confirm("Are you sure you want to delete this dvd from your collection ? ?") == false)
        return;

    $.ajax({
        type: 'DELETE',
        url: 'http://localhost:8080/dvd/' + id,
        success: function () {
            loadDVDs();
        }
    });
}

function showMainFromEdit() {
    $('#edit-dvd-div').hide();
    $('#edit-dvd-banner').hide();
    clearFields();
    loadDVDs();
    $('#main-display-buttons').show();
    $('#main-display-list').show();

}

function editDVDMenu() {
    $('#main-display-buttons').hide();
    $('#main-display-list').hide();
    $('#edit-dvd-div').show();
    $('#edit-dvd-banner').show();



}



function getDvdToEdit(id) {
    $('#errorMessages').empty();
    editDVDMenu();
//todo ADD headers and dataType calls for these guys !!!!! Just to be clera
    $.ajax({
        type: 'GET',
        url: 'http://localhost:8080/dvd/' + id,
        success: function (dvdData, status) {
            $('#edit-dvd-title').val(dvdData.title);
            $('#edit-dvd-year').val(dvdData.realeaseYear);
            $('#edit-dvd-director').val(dvdData.director);
            $('#edit-dvd-notes').val(dvdData.notes)
            $('#edit-dvd-rating').val(dvdData.rating)
            $('#edit-dvd-id').val(dvdData.dvdId);
        },
        error: function () {
            $('#errorMessages').append($('<li>')
                .attr({ class: 'list-group-item list-group-item-danger' })
                .text("Error calling web service, please try again later"));
        }

    });
}

function editDVD() {
    $('#errorMessages').empty();
    var inputs = $('#edit-dvd-div').find('input');

    // VALIDATION / INPUT CHECKING
    if (hasValidYear($('#edit-dvd-year')) == false)
        return;

    if (hasValidInputs(inputs) == false)
        return;

    $.ajax({
        type: 'PUT',
        url: 'http://localhost:8080/dvd/' + $('#edit-dvd-id').val(),
        data: JSON.stringify({
            title: $('#edit-dvd-title').val(),
            realeaseYear: $('#edit-dvd-year').val(),
            director: $('#edit-dvd-director').val(),
            notes: $('#edit-dvd-notes').val(),
            rating: $('#edit-dvd-rating').val(),
            dvdId: $('#edit-dvd-id').val()
        }),
        headers: {
            'Accept': 'application/json',
            'Content-type': 'application/json'
        },
        'dataType': 'json',
        success: function () {
            showMainFromEdit();
        },
        error: function () {
            $('#errorMessages')
                .append($('<li>')
                    .attr({ class: 'list-group-item list-group-item-danger' })
                    .text("Error calling web service, please try again later"));
        }


    })

}
