
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


    //console.log('listening');
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

/* Homepage pagination AJAX */

$(document).on('click', '#home_pagination a', function (event) {
    event.preventDefault();
    // console.log(window.location + $(this).attr("href") + "&json=true");
    var container = $('#in_onda');
    var pagination_button = $(this);
    $.ajax({
        url: window.location.pathname + $(this).attr("href") + "&json=true",
        method: "GET",
        cache: false,
        success: function (data) {

            var res = JSON.parse(data);
            console.log(res);

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
                        text_inside.children('a').children('h5').text(programma.titolo);
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

            // Gestione paginazione buttons

            if (pagination_button.text() === 'NEXT') {

                $('#home_pagination a').each(function (index) {
                    console.log($(this))
                    if ((parseInt($(this).attr("href").slice(6))) === (parseInt(pagination_button.attr("href").slice(6))) - 1) {
                        $(this).removeClass('page_active');
                    }
                    if ($(this).attr("href") === pagination_button.attr("href")) {
                        $(this).addClass('page_active');
                        if (parseInt($(this).attr("href").slice(6)) < res.totpages) {
                            pagination_button.attr("href", "?page=" + (parseInt($(this).attr("href").slice(6)) + 1));

                            if (pagination_button.hasClass("d-none")) {
                                pagination_button.removeClass("d-none");
                            }
                        } else if (parseInt($(this).attr("href").slice(6)) == res.totpages) {
                            pagination_button.addClass("d-none");
                        }
                        console.log("aggiungo la classe active a " + $(this).attr("href"))
                        return false;

                    }
                });
            } else {

                if (!pagination_button.hasClass('page_active')) {
                    $('#home_pagination a').each(function () {
                        if ($(this).hasClass('page_active')) {
                            $(this).removeClass('page_active');
                            return false;
                        }
                    });
                    if (parseInt(pagination_button.attr("href").slice(6)) < res.totpages) {
                        $('#home_pagination a:last-child').attr("href", "?page=" + (parseInt(pagination_button.attr("href").slice(6)) + 1));
                        if ($('#home_pagination a:last-child').hasClass("d-none")) {
                            $('#home_pagination a:last-child').removeClass("d-none");
                        }

                    } else if (parseInt(pagination_button.attr("href").slice(6)) == res.totpages) {
                        $('#home_pagination a:last-child').addClass('d-none');
                    }
                    pagination_button.addClass('page_active');
                }
            }

        },
        error: function (e) {
            console.log(e);
        }
    });
});



