package com.proyecto.turisteando.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * This class is responsible for scheduling tasks to wake up the server at specific times.
 */
@Component
@EnableScheduling
public class ServerWakeUpScheduler {

    @Value("${SERVER_URL}")
    private String serverUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * This method is scheduled to run every 13 minutes (780,000 milliseconds).
     * It wakes up the server by sending a GET request to the URL configured in the env.properties file.
     * If the request is successful, it prints a message indicating that the server has woken up.
     * If an exception occurs, it prints an error message with the exception details.
     */
    @Scheduled(fixedRate = 600000, initialDelay = 600000)
    public void wakeUpServer() {
        String url = serverUrl + "/health";
//        String url = "";
        try {
            String result = restTemplate.getForObject(url, String.class);
            System.out.println("Server woke up: " + result);
        } catch (Exception e) {
            System.out.println("Failed to wake up the server: " + e.getMessage());
        }
    }
}
