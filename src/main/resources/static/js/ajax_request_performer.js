let BASE_URL = "http://localhost:8080/api/";

function performRequest(url){
    //Deactivate the search button
    $("#search-button").prop('disabled', true);
    //Hide the result container
    $("#request-result-container").hide();
    //Remove the last request result
    $(".request-result").remove();
    //Hide the result error container
    $(".request-error-container").hide();
    //Show the waiting for response message while the request is being performed
    $("#wait-response").show();

    document.getElementById("wait-response").scrollIntoView({behavior: 'smooth'});
    //perform request
    $.getJSON(BASE_URL+url+$("#input-doi").val(),function (data){
        //Hide the waiting for response message when the response is received
        $("#wait-response").hide();
        console.log(data)
        if(data===undefined || !Array.isArray(data) || data.length===0){
            showErrorHTML(404);
        } else {
            //Show the result container
            $("#request-result-container").show();
            document.getElementById("request-result-container").scrollIntoView({behavior: 'smooth'});
            //Generate the html code for the request result
            generateHTML(data);
        }
        //Re-activate the search button
        $("#search-button").prop('disabled', false);
    }).fail(function(response){
        showErrorHTML(response.status)
        //Hide the waiting for response message
        $("#wait-response").hide();
        //Re-activate the search button
        $("#search-button").prop('disabled', false);
    });

}

function generateHTML(data){
    //TODO
}

function showErrorHTML(statusCode){
    console.log(statusCode);
    if(statusCode==404){
        //Show the 404 error html
        $("#request-error-not-found").show();
        document.getElementById("request-error-not-found").scrollIntoView({behavior: 'smooth'});
    }else{
        //Show generic error html
        $("#request-error-general").show();
        document.getElementById("request-error-general").scrollIntoView({behavior: 'smooth'});
    }
}

function init(){
    //Activate the search button
    $("#search-button").prop('disabled', false);
    //Hide the waiting for response message
    $("#wait-response").hide();
    //Hide the result container
    $("#request-result-container").hide();
    //Remove the last request result
    $(".request-result").remove();
    //Hide the result error container
    $(".request-error-container").hide();
    //TODO: Call perform request if the search bar is not empty
}

$(document).ready(function(){
    init();
    //Disable the search-form standard behavior
    $("#search-form").submit(function(e) {
        e.preventDefault();
    });
});