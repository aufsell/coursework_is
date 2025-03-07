package com.itmo.is.lz.pipivo.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class ReCaptchaService {

    @Value("${google.recaptcha.secret-key}")
    private String secretKey;

    private static final String RECAPTCHA_VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    public boolean verifyRecaptcha(String recaptchaResponse) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> requestMap = new LinkedMultiValueMap<>();
        requestMap.add("secret", secretKey);
        requestMap.add("response", recaptchaResponse);

        ReCaptchaResponse response = restTemplate.postForObject(RECAPTCHA_VERIFY_URL, requestMap, ReCaptchaResponse.class);

        if (response == null) {
            throw new RuntimeException("Failed to verify reCAPTCHA: No response from Google API");
        }

        if (!response.isSuccess()) {
            System.out.println("reCAPTCHA verification failed. Error codes: " + response.success);
            return false;
        }

        return true;
    }

    @Getter
    @Setter
    private static class ReCaptchaResponse {
        private boolean success;
        private String challenge_ts;
        private String hostname;

    }
}
