//For local development
let BASE_URL = "http://localhost:8080/api/";
//For production
//let BASE_URL = "https://mydoiinfo.com/api/";
let PAGE_SIZE = 12;
//Agregated data for ORCID id request
let totalcitations = 0;
let totalJCR = 0;
let totalConference = 0;
let totalQ1 = 0;
let totalQ2 = 0;
let totalQ3 = 0;
let totalQ4 = 0;
let totalGGSClass1 = 0;
let totalGGSClass2 = 0;
let totalGGSClass3 = 0;
let hIndex = 0;
let foundHI = false;
//Graphs variables
let jcrData = undefined;
let jcrConfig = undefined;
let jcrChart = undefined;
let conferenceData = undefined;
let conferenceConfig = undefined;
let conferenceChart = undefined;

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
    document.getElementById("page-start").scrollIntoView({behavior: 'smooth'});
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

function updateAgregatedData(article,position){
    if(article===undefined || article.title===undefined || article.title==="" || article.title===" ")
        return undefined;
    if(article.citations!=undefined)
        totalcitations = totalcitations + article.citations;
    if(article.jcrRegistry!=undefined){
        totalJCR=totalJCR+1;
        if(article.jcrRegistry.quartile!=undefined){
            switch (article.jcrRegistry.quartile){
                case 'Q1':
                    totalQ1=totalQ1+1;
                    break;
                case 'Q2':
                    totalQ2=totalQ2+1;
                    break;
                case 'Q3':
                    totalQ3=totalQ3+1;
                    break;
                case 'Q4':
                    totalQ4=totalQ4+1;
                    break;
            }
        }
    }
    if(article.conference!=undefined){
        totalConference = totalConference+1;
        if(article.conference.ggsClass!=undefined){
            switch (article.conference.ggsClass){
                case 1:
                    totalGGSClass1=totalGGSClass1+1;
                    break;
                case 2:
                    totalGGSClass2=totalGGSClass2+1;
                    break;
                case 3:
                    totalGGSClass3=totalGGSClass3+1;
                    break;
            }
        }
    }
    if(!foundHI){
        if(article.citations!=undefined){
            if(article.citations<position){
                hIndex=position-1;
                foundHI=true;
            } else if(article.citations==position){
                hIndex=position;
                foundHI=true;
            }
        }else{
            hIndex=position-1;
            foundHI=true;
        }
    }
}

function resetAgregatedData(){
    totalcitations = 0;
    totalJCR = 0;
    totalConference = 0;
    totalQ1 = 0;
    totalQ2 = 0;
    totalQ3 = 0;
    totalQ4 = 0;
    totalGGSClass1 = 0;
    totalGGSClass2 = 0;
    totalGGSClass3 = 0;
    hIndex = 0;
    foundHI = false;
    jcrData = undefined;
    jcrConfig = undefined;
    if (jcrChart != undefined){
        jcrChart.destroy();
        jcrChart = undefined;
    }
    conferenceData = undefined;
    conferenceConfig = undefined;
    if (conferenceChart != undefined){
        conferenceChart.destroy();
        conferenceChart = undefined;
    }
}

function generateAgregatedDataHTML(){
    let agregatedData = [
        '<div class="request-result">',
            '<h3 class="fw-bolder">Información general de los resultados</h3>',
            '<p class="lead text-center">El número total de citas (CrossRef) de todos los artículos del investigador es <span class="bold-text">'+ totalcitations + '</span>.</p>',
            '<p class="lead text-center">El <span class="bold-text">h-index</span> de este investigador es de <span class="bold-text">' + hIndex + '</span>.</p>',
            '<div class="row gx-5 justify-content-center align-items-center">',
                '<div class="col-lg-6 order-lg-1">',
                    '<div class="card">',
                        '<div class="card-header">',
                            '<h4 class="fw-bolder" style="color: darkorange;">JCR</h4>',
                        '</div>',
                        '<div class="card-body">',
                            '<p class="lead text-center">Se han publicado <span class="bold-text">' + totalJCR +'</span> artículos en revistas dentro del JCR</p>',
                        '</div>',
                        '<div class="card-footer">',
                            ((totalQ1+totalQ2+totalQ3+totalQ4)>0) ? '<h5>Artículos publicados en revistas de cada cuartil</h5><canvas id="jcrChart"></canvas>'
                                : '<p>No se ha encontrado ningún artículo con un JCR asociado con información del cuartil, por lo que no puede generarse un gráfico.</p>',
                        '</div>',
                    '</div>',
                '</div>',
                '<div class="col-lg-6 order-lg-2">',
                    '<div class="card">',
                        '<div class="card-header">',
                            '<h4 class="fw-bolder" style="color: darkorange;">Conferencias</h4>',
                        '</div>',
                        '<div class="card-body">',
                            '<p class="lead text-center">Se han publicado <span class="bold-text">' + totalConference + '</span> artículos en conferencias importantes</p>',
                        '</div>',
                        '<div class="card-footer">',
                            ((totalGGSClass1+totalGGSClass2+totalGGSClass3)>0) ? '<h5>Artículos publicados en conferencias de cada clase GGS</h5><canvas id="conferenceChart"></canvas>'
                                : '<p>No se ha encontrado ningún artículo publicado en una conferencia con una clase GSS asociada, por lo que no puede generarse un gráfico.</p>',
                        '</div>',
                    '</div>',
                '</div>',
            '</div>',
        '</div>',
        '<br class="request-result">',
        '<br class="request-result">'
    ]
    //Create the jQuery node for the agregated data html
    return $(agregatedData.join(''));
}

function generateCharts(){
    if((totalJCR>0) && (totalQ1+totalQ2+totalQ3+totalQ4>0)){
        jcrData = {
            labels: [
                'Primer cuartil',
                'Segundo cuartil',
                'Tercer cuartil',
                'Cuarto cuartil'
            ],
            datasets: [{
                label: 'JCRDATA',
                data: [totalQ1, totalQ2, totalQ3, totalQ4],
                backgroundColor: [
                    'rgb(58,210,80)',
                    'rgb(54, 162, 235)',
                    'rgb(255, 205, 86)',
                    'rgb(255, 99, 132)'
                ],
                hoverOffset: 4
            }]
        };
        jcrConfig = {
            type: 'pie',
            data: jcrData,
        };
        jcrChart = new Chart(
            document.getElementById('jcrChart'),
            jcrConfig
        );
    }
    if((totalConference>0) && (totalGGSClass1+totalGGSClass2+totalGGSClass3>0)){
        conferenceData = {
            labels: [
                'Clase GGS 1',
                'Clase GGS 2',
                'Clase GGS 3',
            ],
            datasets: [{
                label: 'CONFERENCEDATA',
                data: [totalGGSClass1, totalGGSClass2, totalGGSClass3],
                backgroundColor: [
                    'rgb(54, 162, 235)',
                    'rgb(255, 205, 86)',
                    'rgb(255, 99, 132)'
                ],
                hoverOffset: 4
            }]
        };
        conferenceConfig = {
            type: 'pie',
            data: conferenceData,
        };
        conferenceChart = new Chart(
            document.getElementById('conferenceChart'),
            conferenceConfig
        );
    }
}

function compareArticlesByCitations(a, b){
    if(a.citations!=undefined){
        if(b.citations!=undefined){
            if(a.citations>b.citations){
                return -1;
            } else if(a.citations<b.citations){
                return 1;
            }else{
                return 0;
            }
        }else{
            return -1;
        }
    } else {
        if(b.citations!=undefined){
            return 1;
        }else{
            return 0;
        }
    }
}

function generateHTML(data,endpointUrl){
    let nPages = (data.length % PAGE_SIZE == 0) ? Math.trunc(data.length/PAGE_SIZE) : Math.trunc(data.length/PAGE_SIZE)+1;
    //Generate the article list HTML
    let articleListHTML = $();
    let excelTableHTMLBody = $();
    let articleList = data;
    //It is necessary to sort the list in order to calculate the h-index if the endpoint is orcid
    if(endpointUrl!=undefined && endpointUrl==="orcid/"){
        articleList = data.sort(compareArticlesByCitations);
        //To solve the particular case in which all of the researcher's articles have more citations than his position,
        //the h-index is set to the length of the array initially
        hIndex = data.length;
    }
    articleList.forEach(function(article, index) {
        let page = Math.trunc(index/PAGE_SIZE);
        let articleHTML = generateHTMLForArticle(article,page);
        if(articleHTML!=undefined)
            articleListHTML = articleListHTML.add(articleHTML);
        let articleRow = generateTableEntry(article);
        if(articleRow!=undefined)
            excelTableHTMLBody = excelTableHTMLBody.add(articleRow);
        if(endpointUrl!=undefined && endpointUrl==="orcid/"){
            updateAgregatedData(article,index+1);
        }
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
    if(endpointUrl!=undefined && endpointUrl==="orcid/"){
        let agregatedDataHTML = generateAgregatedDataHTML();
        agregatedDataHTML.insertBefore("#page-start");
        generateCharts();
    }
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
    }else if(statusCode==504){
        //Show the 504 error html
        $("#request-error-gateway-timeout").show();
        //Scroll to the gateway-timeout section
        document.getElementById("request-error-gateway-timeout").scrollIntoView({behavior: 'smooth'});
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
    //Clear the export form input value
    $("#export_name").val('');
    //Hide the result pagination
    $("#pagination").hide();
    //Remove the last request result
    $(".request-result").remove();
    //Reset the agregated data
    resetAgregatedData();
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
            generateHTML(data,url);
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

//Requires the tableToCsv plugin
function exportData(){
    let exportname = $("#export_name").val();
    if (exportname === undefined || exportname === "")
        exportname = "articles_data";
    $("#excel-table").tableToCsv({
        filename: exportname+'.csv',
        separator: ';',
    });
}

function init(){
    //Activate the search button
    $("#search-button").prop('disabled', false);
    //Hide the waiting for response message
    $("#wait-response").hide();
    //Hide the result container
    $("#request-result-container").hide();
    //Clear the export form input value
    $("#export_name").val('');
    //Hide the result pagination
    $("#pagination").hide();
    //Remove the last request result
    $(".request-result").remove();
    //Reset the agregated data
    resetAgregatedData();
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
    //Disable the export-form standard behavior
    $("#export-form").submit(function(e) {
        e.preventDefault();
    });
});