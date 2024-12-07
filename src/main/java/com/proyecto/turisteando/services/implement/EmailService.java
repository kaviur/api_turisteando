package com.proyecto.turisteando.services.implement;

import com.proyecto.turisteando.dtos.requestDto.ReservationRequestDto;
import com.proyecto.turisteando.dtos.requestDto.UserRequestDto;
import com.proyecto.turisteando.entities.ReservationEntity;
import com.proyecto.turisteando.entities.TouristPlanEntity;
import com.proyecto.turisteando.entities.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
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

    public void sendHtmlMessage(String toEmail, String userName, String lastName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // 'true' indica contenido HTML

        String confirmationLink = getConfirmLink();
        String subjectEmail = "Confirmación de cuenta de Turisteando";
        String htmlContent = """
                    <html>
                    <body>
                        <p>Estimado(a) %s %s,</p>
                        <p>Gracias por registrarte en <strong>Turisteando</strong>. Estamos encantados de tenerte a bordo.</p>
                        <p>Para completar tu registro, por favor confirma tu cuenta haciendo clic en el botón a continuación:</p>
                        <a href="%s" style="display:inline-block;padding:10px 20px;color:white;background-color:#ff0178;text-decoration:none;border-radius:5px;font-weight:bold;">Confirmar Cuenta</a>
                        <p>Si no te registraste en Turisteando, puedes ignorar este correo.</p>
                    </body>
                    </html>
                """.formatted(userName, lastName, confirmationLink);

        helper.setFrom("noreply@turisteando.com");
        helper.setTo(toEmail);
        helper.setSubject(subjectEmail);
        helper.setText(htmlContent, true);

        javaMailSender.send(message);
    }

    public void sendRegisterConfirmationEmail(String toEmail, String userName, String lastName) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String confirmationLink = getConfirmLink();
        ByteArrayResource logo = getLogoImage();
        String subjectEmail = "Confirmación de cuenta de Turisteando";

        String htmlContent = """
                    <!DOCTYPE html>
                    <html lang="es">
                    <head>
                        <meta charset="UTF-8" />
                        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                        <title>Bienvenido a Turisteando</title>
                    </head>
                    <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;">
                        <div style="width: 100%%; max-width: 550px; margin: 0 auto; padding: 16px; text-align: center;">
                            <div style="border-radius: 8px; background-color: #ffffff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); overflow: hidden;">
                                <div style="padding: 16px; background-color: #fff; text-align: center; border-bottom: 1px solid #e6e6e6;">
                                    <img src="cid:logoImage" alt="Logo Turisteando" style="height: 60px;" />
                                </div>
                                <div style="padding: 24px;">
                                    <h1 style="font-size: 24px; font-weight: bold; color: #333333; text-align: center;">¡Bienvenido(a), %s %s!</h1>
                                    <p style="font-size: 16px; color: #666666; text-align: center; line-height: 1.5;">
                                        Gracias por registrarte en <strong>Turisteando</strong>. Estamos encantados de tenerte a bordo.
                                        Para completar tu registro, por favor confirma tu cuenta haciendo clic en el siguiente botón.
                                    </p>
                                    <a href="%s" style="display: block; width: 100%%; max-width: 192px; padding: 12px; font-size: 16px; color: #ffffff !important; background-color: #ff0178; text-align: center; border-radius: 5px; text-decoration: none; margin: 16px auto 0;">Confirmar Cuenta</a>
                                    <p style="font-size: 12px; color: #999999; text-align: center; margin-top: 20px;">
                                        Si no creó una cuenta, puede ignorar este correo electrónico con seguridad.
                                    </p>
                                </div>
                                <div style="padding: 16px; background-color: #f8f9fa; text-align: center;">
                                    <p style="font-size: 12px; color: #999999;">© 2024 Turisteando. Todos los derechos reservados.</p>
                                    <a href="#" style="margin: 0 8px; font-size: 12px; color: #ff0178; text-decoration: none;">Privacy Policy</a>
                                    <a href="#" style="margin: 0 8px; font-size: 12px; color: #ff0178; text-decoration: none;">Terms of Service</a>
                                </div>
                            </div>
                        </div>
                    </body>
                    </html>
                """.formatted(userName, lastName, confirmationLink);

        helper.setFrom("noreply@turisteando.com");
        helper.setTo(toEmail);
        helper.setSubject(subjectEmail);
        helper.setText(htmlContent, true);

        helper.addInline("logoImage", logo, "image/png");

        javaMailSender.send(message);
    }

    public void sendReservationConfirmationEmail(ReservationEntity reservation, UserEntity user, TouristPlanEntity touristPlan) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        String subjectEmail = "Confirmación de Reserva de Turisteando";
        ByteArrayResource logo = getLogoImage();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        String htmlContent = """
                 <!DOCTYPE html>
                 <html lang="es">
                 <head>
                     <meta charset="UTF-8" />
                     <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                     <title>Confirmación de Reserva</title>
                 </head>
                 <body style="font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 0;">
                        <div style="width: 100%%; max-width: 550px; margin: 0 auto; padding: 16px;">
                            <div style="border-radius: 8px; background-color: #ffffff; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); max-width:550px;">
                                <div style="padding: 16px; background-color: #fff; text-align: center; border-bottom: 1px solid #e6e6e6;">
                                    <img src="cid:logoImage" alt="Logo Turisteando" style="height: 60px;" />
                                </div>
                                <div style="max-width: 550px; margin: 0 auto; padding: 10px 24px; background-color: #ffffff; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);">
                                     <h2 style="text-align: center; color: #333;">Resumen de la Reserva</h2>
                                     <p>Gracias por reservar con Turisteando, a continuación
                                      encontrarás los detalles de tu reserva:</p>
                                    \s
                                     <h3>Datos del Usuario</h3>
                                     <p><strong>Nombre:</strong> %s</p>
                                     <p><strong>Apellido:</strong> %s</p>
                                     <p style="padding-bottom: 20px;"><strong>Email:</strong> %s</p>
                                    \s
                                     <h3>Detalles de la Reserva</h3>
                                     <p><strong>Desde:</strong> %s</p>
                                     <p><strong>Hasta:</strong> %s</p>
                                     <p><strong>Número de personas:</strong> %s</p>
                                     <p><strong>Precio por persona:</strong> S/. %s</p>
                                     <p style="padding-bottom: 20px;"><strong>Precio total:</strong> S/. %s</p>
                                    \s
                                     <h3>Detalles del Plan Turístico</h3>
                                     <p><strong>Plan Turístico:</strong> %s</p>
                                     <p><strong>Empresa:</strong> %s</p>
                                     <p><strong>Categoría:</strong> %s</p>
                                </div>
                                <div style="padding: 16px; background-color: #f8f9fa; text-align: center;">
                                    <p style="font-size: 12px; color: #999999;">© 2024 Turisteando. Todos los derechos reservados.</p>
                                    <a href="#" style="margin: 0 8px; font-size: 12px; color: #ff0178; text-decoration: none;">Privacy Policy</a>
                                    <a href="#" style="margin: 0 8px; font-size: 12px; color: #ff0178; text-decoration: none;">Terms of Service</a>
                                </div>
                            </div>
                        </div>
                    </body>
                 </html>
                \s""".formatted(
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                reservation.getStartDate().format(formatter),
                reservation.getEndDate().format(formatter),
                reservation.getPeopleCount(),
                touristPlan.getPrice(),
                reservation.getTotalPrice(),
                touristPlan.getTitle(),
                touristPlan.getSeller(),
                touristPlan.getCategory().getName()
        );

        helper.setFrom("noreply@turisteando.com");
        helper.setTo(user.getEmail());
        helper.setSubject(subjectEmail);
        helper.setText(htmlContent, true);
        helper.addInline("logoImage", logo, "image/png");

        javaMailSender.send(message);
    }

    public ByteArrayResource getLogoImage() throws IOException {
        ClassPathResource imageResource = new ClassPathResource("static/images/logo.png");
        return new ByteArrayResource(imageResource.getInputStream().readAllBytes());
    }

}
