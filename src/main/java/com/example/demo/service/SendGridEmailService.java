package com.example.demo.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SendGridEmailService {

    @Autowired
    private com.sendgrid.SendGrid sendGrid;

    public void sendOtpEmail(String toEmail, String otp) throws IOException {
        Email from = new Email("j.arun80964@gmail.com"); // Use your verified email
        String subject = "Your OTP for Verification";
        Email to = new Email(toEmail);
        
        String emailContent = String.format(
            "Dear user,\n\n" +
            "Your OTP for verification is: %s\n\n" +
            "Please use this OTP to complete your registration.\n" +
            "This OTP is valid for 5 minutes.\n\n" +
            "If you didn't request this OTP, please ignore this email.\n\n" +
            "Best regards,\n" +
            "MLM Team", 
            otp
        );
        
        Content content = new Content("text/plain", emailContent);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sendGrid.api(request);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("✅ SendGrid email sent successfully to: " + toEmail);
            } else {
                System.err.println("❌ SendGrid email failed. Status: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
                throw new IOException("SendGrid email failed with status: " + response.getStatusCode());
            }
        } catch (IOException ex) {
            System.err.println("❌ SendGrid API error: " + ex.getMessage());
            throw ex;
        }
    }

    public void sendForgotPasswordOtpEmail(String toEmail, String otp) throws IOException {
        Email from = new Email("j.arun80964@gmail.com"); // Use your verified email
        String subject = "OTP for Password Reset";
        Email to = new Email(toEmail);
        
        String emailContent = String.format(
            "Dear user,\n\n" +
            "Your OTP for password reset is: %s\n\n" +
            "Please use this OTP to reset your password.\n" +
            "This OTP is valid for 5 minutes.\n\n" +
            "If you didn't request this password reset, please ignore this email.\n\n" +
            "Best regards,\n" +
            "MLM Team", 
            otp
        );
        
        Content content = new Content("text/plain", emailContent);
        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            
            Response response = sendGrid.api(request);
            
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("✅ SendGrid forgot password email sent successfully to: " + toEmail);
            } else {
                System.err.println("❌ SendGrid forgot password email failed. Status: " + response.getStatusCode());
                System.err.println("Response body: " + response.getBody());
                throw new IOException("SendGrid forgot password email failed with status: " + response.getStatusCode());
            }
        } catch (IOException ex) {
            System.err.println("❌ SendGrid forgot password API error: " + ex.getMessage());
            throw ex;
        }
    }
}
