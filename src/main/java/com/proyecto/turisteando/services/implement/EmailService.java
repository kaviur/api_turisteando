package com.proyecto.turisteando.services.implement;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${FRONTEND_URLS}")
    private String frontendUrls;

    public void sendEmail(String toEmail, String userName) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED, "UTF-8");

        String subjectEmail = "Confirmación de cuenta de Turisteando";
        String confirmLink = getConfirmLink();
        String htmlContent = getHtmlTemplate(confirmLink, userName);

        helper.setTo(toEmail);
        helper.setFrom("noreply@turisteando.com", "Turisteando");
        helper.setSubject(subjectEmail);
        helper.setText(htmlContent, true);

//        URL url = new URL("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        helper.addInline("logoImage", new ClassPathResource("static/images/logo.png"));
//        Resource resource = new InputStreamResource()
        helper.addInline("emailStyles", new File("src/main/resources/static/css/styles.css"));

        javaMailSender.send(message);
    }

    private String getHtmlTemplate(String confirmLink, String userName) throws IOException {
        String templateContent = new String(Files.readAllBytes(
                Paths.get("src/main/resources/templates/account-confirmation.html")
        ));

        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{{ confirmLink }}", confirmLink);
        placeholders.put("{{ user }}", userName);

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            templateContent = templateContent.replace(entry.getKey(), entry.getValue());
        }
        return templateContent;
    }

    private String getConfirmLink() {
        List<String> urls = List.of(frontendUrls.split(","));
        String url = urls.size() > 1 && !urls.get(1).isEmpty() ? urls.get(1) : urls.get(0);
        return url + "/login";
    }
}
