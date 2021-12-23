let BASE_URL = "http://localhost:8080/api/";
let PAGE_SIZE = 12;

function generateHTMLForArticle(article,page){
    let articleCardTemplate = [
        '<div class="card request-result paginated page-'+page+'">',
        '<p>Título: ',
        article.title,
        '</p>',
        '</div>',
        '<br class="request-result paginated page-'+page+'">'
    ];
    //Create the jQuery node for the article
    return $(articleCardTemplate.join(''));
}

function changePage(page){
    $(".pagination-button").prop('disabled', false);
    $("#pagination-button-"+page).prop('disabled', true);
    $(".paginated").hide();
    $(".page-"+page).show();
}

function generatePaginationButtonHTML(pageIndex){
    let paginationButtonTemplate = [
        '<button class="request-result pagination-button" id="pagination-button-'+pageIndex+'" onclick="changePage('+pageIndex+')">',
        pageIndex+1,
        '</button>'
    ];
    //Create the jQuery node for the article
    return $(paginationButtonTemplate.join(''));
}

function generateHTML(data){
    let nPages = (data.length % PAGE_SIZE == 0) ? Math.trunc(data.length/PAGE_SIZE) : Math.trunc(data.length/PAGE_SIZE)+1;
    let articleListHTML = $();
    data.forEach(function(article, index) {
        let page = Math.trunc(index/PAGE_SIZE);
        let articleHTML = generateHTMLForArticle(article,page);
        if(articleHTML!=undefined)
            articleListHTML = articleListHTML.add(articleHTML);
    });
    $("#request-result-container").append(articleListHTML);
    let paginationButtonListHTML = $();
    for (let i = 0; i < nPages; i++) {
        paginationButtonListHTML = paginationButtonListHTML.add(generatePaginationButtonHTML(i));
    }
    $("#pagination").append(paginationButtonListHTML);
    changePage(0);
}

function showErrorHTML(statusCode){
    console.log(statusCode);
    if(statusCode==404){
        //Show the 404 error html
        $("#request-error-not-found").show();
        //Scroll to the not-found section
        document.getElementById("request-error-not-found").scrollIntoView({behavior: 'smooth'});
    }else if(statusCode==400){
        //Show the 400 error html
        $("#request-error-bad-request").show();
        //Scroll to the bad-request section
        document.getElementById("request-error-bad-request").scrollIntoView({behavior: 'smooth'});
    }else{
        //Show generic error html
        $("#request-error-general").show();
        //Scroll to the error section
        document.getElementById("request-error-general").scrollIntoView({behavior: 'smooth'});
    }
}

function performRequest(url){
    //Deactivate the search button
    $("#search-button").prop('disabled', true);
    //Hide the result container
    $("#request-result-container").hide();
    //Hide the result pagination
    $("#pagination").hide();
    //Remove the last request result
    $(".request-result").remove();
    //Hide the result error container
    $(".request-error-container").hide();
    //Show the waiting for response message while the request is being performed
    $("#wait-response").show();
    //Scroll to the wait-response section
    document.getElementById("wait-response").scrollIntoView({behavior: 'smooth'});
    //perform request
    $.getJSON(BASE_URL+url+$("#input-doi").val(),function (data){
        //Hide the waiting for response message when the response is received
        $("#wait-response").hide();
        console.log(data)
        if(data===undefined || !Array.isArray(data) || data.length===0){
            showErrorHTML(404);
        } else {
            //Generate the html code for the request result
            generateHTML(data);
            //Show the result container
            $("#request-result-container").show();
            //Show the result pagination
            $("#pagination").show();
            //Scroll to the request container
            document.getElementById("request-result-container").scrollIntoView({behavior: 'smooth'});
        }
        //Re-activate the search button
        $("#search-button").prop('disabled', false);
    }).fail(function(response){
        //Hide the waiting for response message
        $("#wait-response").hide();
        showErrorHTML(response.status)
        //Re-activate the search button
        $("#search-button").prop('disabled', false);
    });

}

function init(){
    //Activate the search button
    $("#search-button").prop('disabled', false);
    //Hide the waiting for response message
    $("#wait-response").hide();
    //Hide the result container
    $("#request-result-container").hide();
    //Hide the result pagination
    $("#pagination").hide();
    //Remove the last request result
    $(".request-result").remove();
    //Hide the result error container
    $(".request-error-container").hide();
}

/*
This method calls performRequest if the search bar is not empty in order to perform the initial request. It must be
called from the HTML with the correct url when the document is ready.
 */
function performInitialRequest(url){
    let inputDoi = $("#input-doi").val();
    if(inputDoi!=undefined && inputDoi!="" && inputDoi!=" "){
        performRequest(url)
    }
}

$(document).ready(function(){
    init();
    //Disable the search-form standard behavior
    $("#search-form").submit(function(e) {
        e.preventDefault();
    });
});