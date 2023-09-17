package com.common.retryProcess.service;

import com.common.retryProcess.configuration.EmailDetails;

import java.util.Map;

public interface NotificationService {

    public void sendEmail(String item, EmailDetails emailDetails);
    public void sendEmailReceipt(Map<String, Integer> item, EmailDetails emailDetails, int amount) ;

    }
