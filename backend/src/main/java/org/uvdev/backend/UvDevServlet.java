package org.uvdev.backend;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UvDevServlet extends HttpServlet {

    private static final InternetAddress YUVAL_PERESS;

    static {
        InternetAddress yuvalPeress;
        try {
            yuvalPeress = new InternetAddress("yuval.peress@gmail.com", "Yuval Peress");
        } catch (UnsupportedEncodingException e) {
            yuvalPeress = null;
        }

        YUVAL_PERESS = yuvalPeress;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getServletPath();


    }
}
