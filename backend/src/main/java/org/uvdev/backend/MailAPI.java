package org.uvdev.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

import org.uvdev.backend.responses.BaseResponse;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Api(name = "mailApi",
        version = "v1")
public class MailAPI {

    private static final String FROM = "from";
    private static final String CONTACT_US = "contact";
    private static final String YUVAL_PERESS = "yuval";
    private static final String MATTHEW_LUNDBERG = "lundberg";
    private static final String MATTHEW_COWDERY = "cowdery";

    private static InternetAddress getAddressFromTarget(String target) {
        switch (target) {
            case FROM:
                return initAddress("no-replay@rowbot-1077.appspotmail.com", "RowBot");
            case CONTACT_US:
            case YUVAL_PERESS:
                return initAddress("yuval.peress@gmail.com", "Yuval Peress");
            case MATTHEW_LUNDBERG:
                return initAddress("lumburgur@yahoo.com", "Matthew Lundberg");
            case MATTHEW_COWDERY:
                return initAddress("mattcowdery@gmail.com", "Matthew Cowdery");
            default:
                return null;
        }
    }
    private static InternetAddress initAddress(String email, String name) {
        try {
            return new InternetAddress(email, name);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @ApiMethod(name = "sendEmail")
    public BaseResponse sendEmail(@Named("target") String target,
            @Named("fromName") String fromName, @Named("fromEmail") String fromEmail,
            @Named("message") String message) {

        InternetAddress from = getAddressFromTarget(FROM);
        InternetAddress to = getAddressFromTarget(target);

        if (from == null || to == null) {
            return new BaseResponse(BaseResponse.STATUS_INTERNAL_ERROR);
        }

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props);

        message += "\nMessage sent from '" + fromName + " <" + fromEmail + ">'";

        try {
            Message email = new MimeMessage(session);
            email.setFrom(from);
            email.addRecipient(Message.RecipientType.TO, to);
            email.setSubject("[UVDev-ContactUs]");
            email.setText(message);
            Transport.send(email);
        } catch (MessagingException e) {
            return new BaseResponse(BaseResponse.STATUS_INTERNAL_ERROR);
        }
        return new BaseResponse(BaseResponse.STATUS_OK);
    }
}
