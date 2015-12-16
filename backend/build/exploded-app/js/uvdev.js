var CLIENT_READY = false;

function validateEmail(email) {
    var re = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
    return re.test(email);
}

function clientInit() {
    var ROOT = 'https://rowbot-1077.appspot.com/_ah/api';
    gapi.client.load('mailApi', 'v1', function() {
        CLIENT_READY = true;
    }, ROOT);
}

function showDialog(dialog) {
    // Hide all dialogs first.
    $('.uvdev-dialog').removeClass('is-visible');
    dialog.trigger('clearData');
    dialog.addClass('is-visible');
}

$(document).ready(function() {
    $('.uvdev-dialog .mdl-card__actions').each(function(index) {
        $(this).find('.uvdev-button-positive').insertBefore($(this).find('.uvdev-button-negative'));
    });
    $('.uvdev-dialog[for]').each(function(index) {
        var dialog = $(this);
        $('#' + $(this).attr('for')).click(function() {
            showDialog(dialog);
        });
    });
    $('.uvdev-dialog').click(function(index) {
        if (!$(this).hasClass('modal')) {
            $(this).removeClass('is-visible');
        }
    }).children().click(function(index) {
        return false;
    });

    $('[mailtoTarget]:not(#send_mail)').click(function() {
        var data = JSON.parse($(this).attr('mailtoTarget'));
        $('#send_mail').attr('mailtoTarget', data.target);
        $('#email_dialog .mdl-card__title-text').html(data.title);
        $('#email_dialog_footnote').html(data.footnote);
        showDialog($('#email_dialog'));
    });
    $('#email_dialog').on('clearData', function() {
        $('#email_dialog input,#email_dialog textarea').val('');
    });
    $('#send_mail').click(function() {
        var data = {
            'target': $(this).attr('mailtoTarget'),
            'fromName': $('#email_dialog input[name="fromName"]').val(),
            'fromEmail': $('#email_dialog input[name="fromEmail"]').val(),
            'message': $('#email_dialog textarea[name="message"]').val(),
        };
        if (!data.fromName) {
            $('#email_dialog_footnote').html("Sorry, your name is required so we can respond.");
            return;
        }
        if (!validateEmail(data.fromEmail)) {
            $('#email_dialog_footnote').html("Sorry, that doesn't appear to be a valid email address.");
            return;
        }
        if (!data.message) {
            $('#email_dialog_footnote').html("Sorry, you seem to be missing a message.");
            return;
        }
        gapi.client.mailApi.sendEmail(data).execute(function (resp) {
            alert('response code = ' + resp.status);
        });
    });
});