package com.example.demo.UserRegistrationService;

import java.math.BigDecimal;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.Repository.ItemRepository;
import com.example.demo.Repository.ReferralRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Repository.WalletRepository;
import com.example.demo.items.Item;
import com.example.demo.referrals.Referral;
import com.example.demo.users.Users;
import com.example.demo.wallet.Wallet;
import com.example.demo.service.SendGridEmailService;

import jakarta.transaction.Transactional;

@Service
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private ReferralRepository referralRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SendGridEmailService sendGridEmailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private Map<String, Users> pendingUsers = new HashMap<>();
    private Map<String, String> otpStore = new HashMap<>();

    private static final BigDecimal REFERRAL_BONUS = BigDecimal.valueOf(50);

    // Generate unique reference code
    private String generateReferenceCode() {
        String code;
        do {
            int number = 100000 + new Random().nextInt(900000);
            code = "REF" + number;
        } while (userRepository.findByReferenceCode(code).isPresent());
        return code;
    }

    // Send OTP to email using SMTP
    public void sendOtp(String email) {
        try {
            String otp = String.format("%06d", new Random().nextInt(999999));
            otpStore.put(email, otp);

            // Try SendGrid first, then fallback to SMTP
            try {
                sendGridEmailService.sendOtpEmail(email, otp);
                System.out.println("✅ SendGrid email sent successfully to: " + email);
            } catch (Exception e) {
                System.err.println("❌ SendGrid failed for: " + email + ". Error: " + e.getMessage());
                System.err.println("🔄 Falling back to SMTP...");
                
                // Fallback to SMTP
                try {
                    SimpleMailMessage message = new SimpleMailMessage();
                    message.setTo(email);
                    message.setSubject("Your OTP for Verification");
                    message.setText("Dear user,\n\nYour OTP is: " + otp + "\n\nPlease verify within 5 minutes.");

                    mailSender.send(message);
                    System.out.println("✅ SMTP fallback email sent successfully to: " + email);
                } catch (Exception smtpError) {
                    System.err.println("❌ SMTP fallback also failed for: " + email + ". Error: " + smtpError.getMessage());
                    System.err.println("⚠️ Email delivery failed, but OTP is stored for testing");
                }
            }
        } catch (Exception e) {
            System.err.println("Error in sendOtp method: " + e.getMessage());
            // Don't throw exception, just log the error
        }
    }

    // Register new user or send OTP
    public String registerOrSendOtp(Users user) {
        try {
            // Validate input
            if (user == null) {
                throw new IllegalArgumentException("User object cannot be null");
            }
            
            if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
                throw new IllegalArgumentException("Email cannot be null or empty");
            }
            
            if (user.getName() == null || user.getName().trim().isEmpty()) {
                throw new IllegalArgumentException("Name cannot be null or empty");
            }
            
            if (user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty()) {
                throw new IllegalArgumentException("Phone number cannot be null or empty");
            }

            Optional<Users> existingUser = userRepository.findByEmail(user.getEmail());

            if (existingUser.isPresent()) {
                sendOtp(existingUser.get().getEmail());
                return "OTP sent to existing user.";
            }

            if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("❌ Password cannot be null or empty for new user.");
            }

            user.setStatus("PENDING");
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setReferenceCode(generateReferenceCode());

            pendingUsers.put(user.getEmail(), user);
            sendOtp(user.getEmail());

            return "OTP sent for new user registration. Please verify.";
        } catch (Exception e) {
            System.err.println("Error in registerOrSendOtp: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Registration failed: " + e.getMessage());
        }
    }

  
    public Users verifyOtpAndRegister(String email, String otp) {
               
    	
    	String storedOtp = otpStore.get(email);
        if (storedOtp == null || !storedOtp.equals(otp)) {
            throw new RuntimeException("Invalid OTP for email: " + email);
        }
        otpStore.remove(email);

       
        Users user = pendingUsers.remove(email);
        if (user == null) {
            user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }

      
        user.setStatus("ACTIVE");
        user = userRepository.save(user);

  
        Wallet wallet = walletRepository.findByUserPhoneNumber(user.getPhoneNumber());
        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user);
            wallet.setWalletBalance(BigDecimal.ZERO);
            wallet = walletRepository.save(wallet);
            user.setWallet(wallet);
        }

        
        
        Item item = itemRepository.findByUserPhoneNumber(user.getPhoneNumber()).orElse(null);
        if (item == null) {
            item = new Item();
            item.setUserPhoneNumber(user.getPhoneNumber());
            List<String> defaultItems = (user.getItemNames() != null && !user.getItemNames().isEmpty())
                    ? user.getItemNames()
                    : Collections.emptyList();
            item.setItemNamesList(defaultItems);
            item.setStatus("PENDING");
            item = itemRepository.save(item);
        }

        
        if (user.getReferredByCode() != null && !user.getReferredByCode().isEmpty()) {
            Optional<Users> referrerOpt = userRepository.findByReferenceCode(user.getReferredByCode());
            if (referrerOpt.isPresent()) {
                Users referrer = referrerOpt.get();

                // Apply referral bonus when user successfully verifies OTP
                // No need to check item status for OTP verification

                Wallet refWallet = walletRepository.findByUserPhoneNumber(referrer.getPhoneNumber());
                if (refWallet == null) {
                    refWallet = new Wallet();
                    refWallet.setUser(referrer);
                    refWallet.setWalletBalance(BigDecimal.ZERO);
                    refWallet = walletRepository.save(refWallet);
                }

                refWallet.setWalletBalance(refWallet.getWalletBalance().add(REFERRAL_BONUS));
                walletRepository.save(refWallet);

                Referral referral = new Referral();
                referral.setReferrerPhoneNumber(referrer.getPhoneNumber());
                referral.setBonusAmount(REFERRAL_BONUS);
                referralRepository.save(referral);
            }
        }

   
        int referralCount = referralRepository.countByReferrerPhoneNumber(user.getPhoneNumber());
        user.setReferralCount(referralCount);

        return user;
    }

    // Get OTP for testing purposes
    public String getOtpForEmail(String email) {
        return otpStore.get(email);
    }
}
