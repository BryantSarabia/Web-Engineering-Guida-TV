
"use strict";
// Constants and Variables Declaration

function getFormattedDate(d) {
    var year = d.getFullYear();
    var month = parseInt(d.getMonth()) + 1;
    var day = d.getDate();
    return year + '-' + month + '-' + day;
}

function setDateTimeCheck() {
    var d1 = new Date();
    var d2 = new Date();
    if (d1.getMonth() === 0) {
        d1.setMonth(12);
        d1.setFullYear(d1.getFullYear() - 1);
    } else
        d1.setMonth(d1.getMonth() - 1);

    if (d2.getMonth() === 12) {
        d2.setMonth(0);
        d2.setFullYear(d2.getFullYear() + 1);
    } else
        d2.setMonth(d2.getMonth() + 1);

    $(".dateTime").each(function (index, element) {
        $(element).dateTimePicker({
            mode: 'date',
            format: 'yyyy-MM-dd',
            limitMin: getFormattedDate(d1),
            limitMax: getFormattedDate(d2)
        });
    });

}

setDateTimeCheck();

if ($('#cerca').length) {
    let search_container = $('.search-container.nojs');
    search_container.removeClass('nojs');
    search_container.addClass('js');
    $('.search-container').addClass('d-none');
}

$(document).on('click', '#cerca', function (event) {
    $('.search-container.js').toggleClass('d-none');
});


$(document).ready(function () {
    $('#series-slider').slick();
});

/* Gestione pagination buttons */

function pagination_buttons(button, res, home = true) {
    // Gestione paginazione buttons

    let button_page = parseInt(button.attr("href").substring(6, 7));

    if (home) {
        if (button.text() === 'NEXT') {

            $('#home_pagination a').each(function (index) {
                let current_button_page = parseInt($(this).attr("href").slice(6));
                if (current_button_page === (button_page - 1)) {
                    $(this).removeClass('page_active');
                }
                if ($(this).attr("href") === button.attr("href")) {
                    $(this).addClass('page_active');
                    if (current_button_page < res.totpages) {
                        button.attr("href", "?page=" + (current_button_page + 1));

                        if (button.hasClass("d-none")) {
                            button.removeClass("d-none");
                        }
                    } else if (current_button_page === res.totpages) {
                        button.addClass("d-none");
                    }
                    return false;

                }
            });
        } else {

            if (!button.hasClass('page_active')) {
                $('#home_pagination a').each(function () {
                    if ($(this).hasClass('page_active')) {
                        $(this).removeClass('page_active');
                        return false;
                    }
                });
                if (button_page < res.totpages) {
                    $('#home_pagination a:last-child').attr("href", "?page=" + (button_page + 1));
                    if ($('#home_pagination a:last-child').hasClass("d-none")) {
                        $('#home_pagination a:last-child').removeClass("d-none");
                    }

                } else if (button_page === res.totpages) {
                    $('#home_pagination a:last-child').addClass('d-none');
                }
                button.addClass('page_active');
            }
        }
    } else {

        if (button.text() === 'NEXT') {

            $('#palinsesto_pagination a').each(function (index) {
                let current_button_page = parseInt($(this).attr("href").substring(6, 7));
                if (current_button_page === (button_page - 1)) {
                    $(this).removeClass('page_active');
                }
                if ($(this).attr("href") === button.attr("href")) {
                    $(this).addClass('page_active');
                    if (current_button_page < res.totpages) {
                        button.attr("href", $(this).attr("href").replace("?page=" + button_page, "?page=" + (button_page + 1)));
                        console.log(button.attr("href"))
                        if (button.hasClass("d-none")) {
                            button.removeClass("d-none");
                        }
                    } else if (current_button_page === res.totpages) {
                        button.addClass("d-none");
                    }
                    return false;

                }
            });
        } else {

            if (!button.hasClass('page_active')) {
                $('#palinsesto_pagination a').each(function () {
                    if ($(this).hasClass('page_active')) {
                        $(this).removeClass('page_active');
                        return false;
                    }
                });
                if (button_page < res.totpages) {
                    $('#palinsesto_pagination a:last-child').attr("href", button.attr("href").replace("?page=" + button_page, "?page=" + (button_page + 1)));
                    if ($('#palinsesto_pagination a:last-child').hasClass("d-none")) {
                        $('#palinsesto_pagination a:last-child').removeClass("d-none");
                    }

                } else if (button_page === res.totpages) {
                    $('#palinsesto_pagination a:last-child').addClass('d-none');
                }
                button.addClass('page_active');
            }
        }
}
}

/* Palinsesto generale dynamic funciton */

function palinsesto_generale(data) {

    let container = $('.canali');

    container.fadeOut(500, function () {
        $(this).empty().show();

        for (let x of data.data) {
            let canale = x.canale;
            let programmazioni;
            if (canale.programmazioni) {
                programmazioni = canale.programmazioni;
            }

            let target = $('#palinsesto_generale_prototype').clone();
            let lista_programmi = target.find('.lista_programmi');
            let li = lista_programmi.children('li').clone();
            lista_programmi.empty();
            let canale_logo = target.children(".canale-logo");

            /* CANALE LOGO */
            canale_logo.children().eq(0).children('a').attr("href", "canale?c_key=" + canale.id);
            canale_logo.children().eq(0).children('a').children('img').attr("src", canale.logo);
            canale_logo.children().eq(0).children('a').children('img').attr("alt", canale.nome);

            canale_logo.children().eq(1).children('a').attr("href", "canale?c_key=" + canale.id);
            canale_logo.children().eq(1).children('a').children('h5').text(canale.nome);

            canale_logo.children().eq(2).children('small').eq(0).text(canale.start + " - " + canale.end);
            canale_logo.children().eq(2).children('small').eq(1).text(canale.start);
            canale_logo.children().eq(2).children('small').eq(2).text(canale.end);
            /* END CANALE LOGO */

            /* CANALE PROGRAMMAZIONI */
            if (programmazioni !== undefined) {

                for (let z of programmazioni) {

                    let programma = z.programma;
                    let li_element = li.clone();
                    let target_row = li_element.find('.programma');
                    li_element.children('a').attr("href", "programma?p_key=" + programma.id);
                    target_row.children().eq(0).children('img').attr("src", programma.img);
                    target_row.children().eq(0).children('img').attr("alt", programma.titolo.replace(/&#39;/gi, "\'"));

                    let second_row = target_row.children().eq(1).children('div').children('div');
                    second_row.eq(0).children('h5').text(programma.titolo);
                    second_row.eq(1).children('small').text("(" + z.durata + " min)");

                    /* Lista generi */
                    if (programma.generi[0].default) {
                        second_row.eq(2).children('small').text("Questo programma non ha generi");
                    } else {
                        let generi = "";
                        for (let i = 0; i < programma.generi.length; i++) {
                            if ((i + 1) === programma.generi.length) {
                                generi += programma.generi[i].nome;
                            } else {
                                generi += programma.generi[i].nome + ", ";
                            }
                        }
                        second_row.eq(2).children('small').text(generi);
                    }
                    /* End lista generi */

                    second_row.eq(3).children('small').text(z.start_time);
                    lista_programmi.append(li_element);
                }
            } else {
                lista_programmi.empty();
                target.children('div').eq(1).append(
                        `<div class="col-lg-9 text-center align-self-center">
                        <p>Nessuna programmazione</p> 
                    </div>`
                        );
            }

            /* END CANALE PROGRAMMAZIONI */

            target.removeAttr("id");
            target.removeClass("d-none");
            target.hide().appendTo(container).fadeIn(500);
        }
    });
}

function palinsesto_canale(data) {

    let container = $('.canale').children('div');

    container.fadeOut(500, function () {
        $(this).empty().show();
        console.log(data.data[0])
        let canale = data.data[0].canale;
        let programmazioni;

        if (canale.programmazioni !== undefined) {
            programmazioni = canale.programmazioni;
        }

        let lista_programmi = $('#palinsesto_canale_prototype').clone();
        let li_element = lista_programmi.children('li').clone();
        lista_programmi.empty();

        /* CANALE PROGRAMMAZIONI */
        if (programmazioni !== undefined) {

            for (let z of programmazioni) {

                let programma = z.programma;
                let li = li_element.clone();
                li.children('a').attr("href", "programma?p_key=" + programma.id);
                let target_row = li.find('.programma');

                target_row.children().eq(0).children('small').text(z.start_time + " - " + z.end_time);

                target_row.children().eq(1).children('img').attr("src", programma.img);

                let second_row = target_row.children().eq(2);
                second_row.children().eq(0).append("<h5>" + programma.titolo + "</h5>");

                /* Lista generi */
                if (programma.generi[0].default) {
                    second_row.children().eq(0).append("<h6>Questo programma non ha generi</h6>");
                } else {
                    let generi = "";
                    for (let i = 0; i < programma.generi.length; i++) {
                        if ((i + 1) === programma.generi.length) {
                            generi += programma.generi[i].nome;
                        } else {
                            generi += programma.generi[i].nome + ", ";
                        }
                    }
                    second_row.children().eq(0).append("<small>" + generi + "</small>");
                }
                /* End lista generi */

                second_row.children().eq(1).children('small').text("(" + programma.durata + " min)");
                lista_programmi.append(li);
            }
        } else {
            lista_programmi.empty();
            container.append(
                    `<span class="alert alert-warning">Nessuna programmazione corrente</span>`
                    );
        }


        /* END CANALE PROGRAMMAZIONI */

        lista_programmi.removeAttr("id");
        lista_programmi.removeClass("d-none").addClass("lista_programmi");
        lista_programmi.hide().appendTo(container).fadeIn(500);

    });
}


/* Homepage pagination AJAX */

$(document).on('click', '#home_pagination a', function (event) {
    event.preventDefault();
    // console.log(window.location + $(this).attr("href") + "&json=true");
    var container = $('#in_onda');
    var button = $(this);
    $.ajax({
        url: window.location.pathname + $(this).attr("href") + "&json=true",
        method: "GET",
        cache: false,
        success: function (data) {

            var res = JSON.parse(data);
            // console.log(res);

            container.fadeOut(500, function () {
                $(this).empty().show();
                var x;

                for (x of res.data) {
                    let target = $('#home_programma_prototype').clone();
                    let set_bg = target.children().children('.listing__item__pic.set-bg');
                    let item_text = target.children().children('.listing__item__text');

                    if (x.canale.logo.length > 0) {
                        set_bg.children('a').eq(0).find('img').attr('src', x.canale.logo);
                        set_bg.children('a').eq(0).find('img').attr('alt', x.canale.nome + ' logo');
                    }

                    set_bg.children('a').eq(0).attr('href', 'canale?c_key=' + x.canale.id);
                    set_bg.children('a').eq(1).attr('href', 'canale?c_key=' + x.canale.id);
                    set_bg.children('a').eq(1).find('div').text(x.canale.nome);

                    if (x.canale.programmazione) {
                        let programmazione = x.canale.programmazione;
                        let programma = x.canale.programmazione.programma;
                        let text_inside = item_text.children('.listing__item__text__inside');
                        let genres_list = text_inside.find('ul');

                        set_bg.data('setbg', programma.img);
                        set_bg.find('div').find('a').attr('href', 'programma?p_key=' + x.canale.programmazione.programma.id);
                        text_inside.children('a').attr('href', 'programma?p_key=' + programma.id);
                        text_inside.children('a').children('h5').text(programma.titolo.replace(/&#39;/gi, "\'"));
                        text_inside.children('.listing__item__text__rating').children('.listing__item__rating__star').find('h6').text(programmazione.start_time + ' - ' + programmazione.end_time);

                        for (let genere of programma.generi) {
                            if (genere.nome) {
                                genres_list.append(`<li><i class="fa fa-genderless" aria-hidden="true"></i>${genere.nome}</li>`);
                            } else {
                                genres_list.append(`<li><i class="fa fa-genderless" aria-hidden="true"></i>Questo programma non ha generi</li>`);
                            }
                        }

                        genres_list.append(`<li><i class="fa fa-info" aria-hidden="true"></i> <a class="link_ref" href="${programma.link_ref}" target="_blank">Info</a></li>`);

                        if (programma.stagione) {
                            item_text.find('.listing__item__text__info__left').find('span').text('Stagione ' + programma.stagione + ', Puntata ' + programma.episodio + "\u00A0" + "\u00A0");
                            item_text.find('.listing__item__text__info__left').find('span').append('<small>(' + programmazione.durata + ' min)</small>');
                        }

                    } else {
                        let text_inside = item_text.children('.listing__item__text__inside');
                        let genres_list = text_inside.find('ul');
                        set_bg.data('setbg', "img_tv/static/no-image.png");
                        set_bg.find('div').find('a').attr('href', 'canale?c_key=' + x.canale.id);
                        text_inside.children('a').attr('href', 'canale?c_key=' + x.canale.id);
                        text_inside.children('a').children('h5').text("Nessuna programmazione in onda");
                        text_inside.children('.listing__item__text__rating').children('.listing__item__rating__star').find('h6').text('Nessuna programmazione in onda');
                        genres_list.append(`<li><i class="fa fa-genderless" aria-hidden="true"></i>Nessuna programmazione</li>`);
                    }

                    target.removeAttr('id');
                    target.removeClass('d-none');
                    container.append(target);
                    target.hide().appendTo(container).fadeIn(500);
                }

                $('.set-bg').each(function () {
                    var bg = $(this).data('setbg');
                    $(this).css('background-image', 'url(' + bg + ')');
                });


            });

            /* Gestione pagination buttons */
            pagination_buttons(button, res);



        },
        error: function (e) {
            console.log(e);
        }
    });
});



/* Palinsesto Generale AJAX */

$(document).on('click', '#palinsesto_pagination a', function (event) {
    event.preventDefault();
    // console.log(window.location + $(this).attr("href") + "&json=true");

    var button = $(this);
    $.ajax({
        url: $(this).attr("href") + "&json=true",
        method: "GET",
        cache: false,
        success: function (data) {

            var res = JSON.parse(data);


            /* container.fadeOut(500, function () {
             $(this).empty().show();
             
             }); */

            palinsesto_generale(res);

            /* Gestione pagination buttons */
            pagination_buttons(button, res, false);



        },
        error: function (e) {
            console.log(e);
        }
    });
});

$(document).on('click', '.day-number-container a', function (event) {
    event.preventDefault();
    button = $(this);
    // console.log(window.location + $(this).attr("href") + "&json=true");

    var button = $(this);
    $.ajax({
        url: $(this).attr("href") + "&json=true",
        method: "GET",
        cache: false,
        success: function (data) {

            var res = JSON.parse(data);


            /* container.fadeOut(500, function () {
             $(this).empty().show();
             
             }); */

            palinsesto_generale(res);

            $('.day-number').each(function () {
                if ($(this).hasClass('active')) {
                    $(this).removeClass('active');
                }
            });

            button.children('div').addClass('active');

            /* Gestione  buttons */




        },
        error: function (e) {
            console.log(e);
        }
    });
});



$(document).on('click', '.fascia a', function (event) {
    event.preventDefault();
    // console.log(window.location + $(this).attr("href") + "&json=true");
    if ($(event.target).parents('div').hasClass("canale"))
        return false;

    var button = $(this);
    $.ajax({
        url: $(this).attr("href") + "&json=true",
        method: "GET",
        cache: false,
        success: function (data) {

            var res = JSON.parse(data);

            /* container.fadeOut(500, function () {
             $(this).empty().show();
             
             }); */

            palinsesto_generale(res);

            /* Gestione  buttons */

            $('.fascia').each(function () {
                if ($(this).hasClass('active')) {
                    $(this).removeClass('active');
                }
            });

            button.parent().addClass('active');



        },
        error: function (e) {
            console.log(e);
        }
    });
});



$(document).on('click', '.fascia.canale a', function (event) {
    event.preventDefault();
    // console.log(window.location + $(this).attr("href") + "&json=true");

    var button = $(this);
    $.ajax({
        url: $(this).attr("href") + "&json=true",
        method: "GET",
        cache: false,
        success: function (data) {

            var res = JSON.parse(data);

            /* container.fadeOut(500, function () {
             $(this).empty().show();
             
             }); */

            palinsesto_canale(res);

            /* Gestione  buttons */

            $('.fascia').each(function () {
                if ($(this).hasClass('active')) {
                    $(this).removeClass('active');
                }
            });

            button.parent().addClass('active');



        },
        error: function (e) {
            console.log(e);
        }
    });
});

$("#palinsesto_canale_cerca_form").on("submit", function (event) {
    event.preventDefault();
    let data_to_send = $(this).serialize();
    /* let date = null;
     let canale_key = null;
     let pos = data_to_send.indexOf("=");
     if (pos !== -1)
     date = data_to_send.substring(pos + 1, pos + 11);
     pos = data_to_send.lastIndexOf("=");
     if (pos !== -1)
     canale_key = data_to_send.substring(pos + 1, pos + 2);
     */
    $.ajax({
        url: "canale?"+ data_to_send + "&json=true",
        method: "GET",
        cache: false,
        success: function (data) {
            var res = JSON.parse(data);
            palinsesto_canale(res);
        },
        error: function (e) {
            console.log(e);
        }
    });



});