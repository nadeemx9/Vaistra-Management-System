package com.vaistra.services;

import com.vaistra.logindb.entities.User;

public interface EmailService {
    void sendSimpleMailMessage(String name, String to, String token);

    void sendResetPasswordLink(User user, String newPassword, String token);
}
