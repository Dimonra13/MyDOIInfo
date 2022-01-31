//For local development
let BASE_URL = "http://localhost:8080/api/";
//For production
//let BASE_URL = "https://mydoiinfo.com/api/";
let PAGE_SIZE = 12;

function generateHTMLForArticle(article,page){
    if(article===undefined || article.title===undefined || article.title==="" || article.title===" ")
        return undefined;
    let articleCardTemplate = [
        '<div class="card request-result paginated page-'+page+'">',
        '<div class="card-header">',
        '<h3 class="fw-bolder" style="color: darkorange;">'+ article.title+'</h3>',
        (article.DOI != undefined && article.DOI!="" && article.DOI!=" ") ?
            '<a href="'+ article.DOI +'">' + article.DOI +'</a>' : '',
        '</div>',
        '<div class="card-body">',
        '<ul class="response-list">',
        '<li class="lead text-md-start">',
        '<span class="fw-bolder">Autores: </span>',
        (article.authors!=undefined && Array.isArray(article.authors) && article.authors.length>0) ?
            article.authors.join(", ") : 'Desconocidos',
        '</li>',
        '<li class="lead text-md-start">',
        '<span class="fw-bolder">Publicado en: </span>',
        (article.journalTitle!=undefined && article.journalTitle!="") ?
            (article.journalTitle + ((article.volumeInfo!=undefined && article.volumeInfo!="") ? ', ' + article.volumeInfo : ''))
            : 'Desconocido',
        '</li>',
        '<li class="lead text-md-start">',
        '<span class="fw-bolder">Fecha de publicación: </span>',
        (article.publicationDateText!=undefined && article.publicationDateText!="") ?
            article.publicationDateText : 'Desconocida',
        '</li>',
        '<li class="lead text-md-start">',
        '<span class="fw-bolder">Número de citas (CrossRef): </span>',
        (article.citations!=undefined) ? article.citations : 'Desconocido',
        '</li>',
        '</ul>',
        '</div>',
        ((article.jcrRegistry!=undefined) ?
                (
                    '<div class="card-footer">'+
                        '<h4 class="fw-bolder" style="color: darkorange;">Información sobre el JCR</h4>'+
                        ((article.jcrRegistry.year!=undefined) ? '<p class="lead">Año '+article.jcrRegistry.year+'</p>': '<br>') +
                        '<div class="row">'+
                            '<div class="col-4">'+
                                '<h5 class="fw-bolder">Factor de impacto:</h5>'+
                                '<p class="lead">'+ ((article.jcrRegistry.impactFactor!=undefined) ? article.jcrRegistry.impactFactor : 'N/D')+'</p>'+
                            '</div>'+
                            '<div class="col-4">'+
                                '<h5 class="fw-bolder">Factor de impacto a cinco años:</h5>'+
                                '<p class="lead">'+ ((article.jcrRegistry.impactFactorFiveYear!=undefined) ? article.jcrRegistry.impactFactorFiveYear: 'N/D')+'</p>'+
                            '</div>'+
                            '<div class="col-4">'+
                                '<h5 class="fw-bolder">Cuartil:</h5>'+
                                '<p class="lead">'+ ((article.jcrRegistry.quartile!=undefined) ? article.jcrRegistry.quartile : 'N/D')+'</p>'+
                            '</div>'+
                        '</div>'+
                    '</div>'
                )
            : ((article.conference!=undefined && (article.conference.coreClass!=undefined || article.conference.ggsClass!=undefined)) ?
                    ('<div class="card-footer">'+
                        '<h4 class="fw-bolder" style="color: darkorange;">Información sobre la conferencia</h4>'+
                        '<br>'+
                        '<div class="row">'+
                            '<div class="col-4">'+
                                '<h5 class="fw-bolder">Clase GGS:</h5>'+
                                '<p class="lead">'+ ((article.conference.ggsClass!=undefined) ? article.conference.ggsClass : 'N/D')+'</p>'+
                            '</div>'+
                            '<div class="col-4">'+
                                '<h5 class="fw-bolder">Clasificación GGS:</h5>'+
                                '<p class="lead">'+ ((article.conference.ggsRating!=undefined) ? article.conference.ggsRating : 'N/D')+'</p>'+
                            '</div>'+
                            '<div class="col-4">'+
                                '<h5 class="fw-bolder">Clase CORE:</h5>'+
                                '<p class="lead">'+ ((article.conference.coreClass!=undefined) ? article.conference.coreClass : 'N/D')+'</p>'+
                            '</div>'+
                        '</div>'+
                    '</div>')
                :
                    ''
              )
        ),
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
    //Scroll to the request container
    document.getElementById("request-result-container").scrollIntoView({behavior: 'smooth'});
}

function generatePaginationButtonHTML(pageIndex){
    let paginationButtonTemplate = [
        '<button class="request-result pagination-button btn btn-primary" id="pagination-button-'+pageIndex+'" onclick="changePage('+pageIndex+')" style="margin-right: 0.3rem;">',
        pageIndex+1,
        '</button>'
    ];
    //Create the jQuery node for the article
    return $(paginationButtonTemplate.join(''));
}

function generateTableEntry(article){
    if(article===undefined || article.title===undefined || article.title==="" || article.title===" ")
        return undefined;
    let articleRowTemplate = [
        '<tr>',
            '<td>',
                article.title,
            '</td>',
            '<td>',
                (article.DOI != undefined && article.DOI!="" && article.DOI!=" ") ? article.DOI : 'N/D',
            '</td>',
            '<td>',
                (article.authors!=undefined && Array.isArray(article.authors) && article.authors.length>0) ?
                    article.authors.join(", ") : 'N/D',
            '</td>',
            '<td>',
                (article.citations!=undefined) ? article.citations : 'N/D',
            '</td>',
            '<td>',
                (article.journalTitle!=undefined && article.journalTitle!="") ? article.journalTitle : 'N/D',
            '</td>',
            '<td>',
                (article.volumeInfo!=undefined && article.volumeInfo!="") ? article.volumeInfo : '',
            '</td>',
            '<td>',
                (article.publicationDateText!=undefined && article.publicationDateText!="") ?
                    article.publicationDateText : 'N/D',
            '</td>',
            '<td>',
                (article.conference!=undefined && article.conference.ggsClass!=undefined) ?
                    article.conference.ggsClass : '',
            '</td>',
            '<td>',
                (article.conference!=undefined && article.conference.ggsRating!=undefined) ?
                    article.conference.ggsRating : '',
            '</td>',
            '<td>',
                (article.conference!=undefined && article.conference.coreClass!=undefined) ?
                    article.conference.coreClass : '',
            '</td>',
            '<td>',
                (article.jcrRegistry!=undefined && article.jcrRegistry.year!=undefined) ? article.jcrRegistry.year : '',
            '</td>',
            '<td>',
                (article.jcrRegistry!=undefined && article.jcrRegistry.impactFactor!=undefined) ?
                    article.jcrRegistry.impactFactor : '',
            '</td>',
            '<td>',
                (article.jcrRegistry!=undefined && article.jcrRegistry.impactFactorFiveYear!=undefined) ?
                    article.jcrRegistry.impactFactorFiveYear : '',
            '</td>',
            '<td>',
                (article.jcrRegistry!=undefined && article.jcrRegistry.quartile!=undefined) ?
                    article.jcrRegistry.quartile : '',
            '</td>',
            '<td>',
                (article.jcrRegistry!=undefined && article.jcrRegistry.categoryRankingList!=undefined
                    && Array.isArray(article.jcrRegistry.categoryRankingList) && article.jcrRegistry.categoryRankingList.length>0) ?
                    "[" + article.jcrRegistry.categoryRankingList.map(function(category) {
                            return ('Category:' + ((category!=undefined && category.name!==undefined) ? category.name : '') + ',' +
                                    'Ranking:' + ((category!=undefined && category.ranking!==undefined) ? category.ranking : '') + ',' +
                                    'Field:' + ((category!=undefined && category.journalField!==undefined) ? category.journalField : '')
                                    );
                        }).join("], [") + "]"
                    : '',
            '</td>',
        '</tr>'
    ];
    //Create the jQuery node for the article row
    return $(articleRowTemplate.join(''));
}

function generateTable(){
    let exceltable = [
        '<table id="excel-table" class="request-result" hidden>',
            '<thead>',
            '<tr>',
                '<th>Title</th>',
                '<th>DOI</th>',
                '<th>Authors</th>',
                '<th>Citations (CrossRef)</th>',
                '<th>Journal/Conference</th>',
                '<th>Volume</th>',
                '<th>Publication Date</th>',
                '<th>GGS Class</th>',
                '<th>GGS Rating</th>',
                '<th>CORE</th>',
                '<th>JCR Date</th>',
                '<th>Impact Factor</th>',
                '<th>Impact Factor Five Years</th>',
                '<th>JCR Quartile</th>',
                '<th>Category Ranking List</th>',
            '</tr>',
            '</thead>',
            '<tbody id="excel-table-body">',
            '</tbody>',
        '</table>'
    ];
    //Create the jQuery node for the excel table
    return $(exceltable.join(''));
}

function generateHTML(data){
    let nPages = (data.length % PAGE_SIZE == 0) ? Math.trunc(data.length/PAGE_SIZE) : Math.trunc(data.length/PAGE_SIZE)+1;
    //Generate the article list HTML
    let articleListHTML = $();
    let excelTableHTMLBody = $();
    data.forEach(function(article, index) {
        let page = Math.trunc(index/PAGE_SIZE);
        let articleHTML = generateHTMLForArticle(article,page);
        if(articleHTML!=undefined)
            articleListHTML = articleListHTML.add(articleHTML);
        let articleRow = generateTableEntry(article);
        if(articleRow!=undefined)
            excelTableHTMLBody = excelTableHTMLBody.add(articleRow);
    });
    $("#request-result-container").append(articleListHTML);
    //Generate the article excel table HTML
    $("#request-result-container").append(generateTable());
    $("#excel-table-body").append(excelTableHTMLBody);
    //Generate the pagination HTML
    let paginationButtonListHTML = $();
    for (let i = 0; i < nPages; i++) {
        paginationButtonListHTML = paginationButtonListHTML.add(generatePaginationButtonHTML(i));
    }
    $("#pagination").append(paginationButtonListHTML);
    changePage(0);
}

function showErrorHTML(statusCode){
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
    $.getJSON(BASE_URL+url+$("#search-input").val(),function (data){
        //Hide the waiting for response message when the response is received
        $("#wait-response").hide();
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

//Requires the table2excel plugin
$("#export_button").click(function(){
    $("#excel-table").table2excel({
        name: "1",
        filename: "articles_data",
        fileext: ".xls"
    });
});

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
    let input = $("#search-input").val();
    if(input!=undefined && input!="" && input!=" "){
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