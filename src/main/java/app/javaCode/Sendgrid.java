package app.javaCode;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

import java.io.IOException;

public class Sendgrid {

    public void sendGridAction(String email, String name, String template) throws IOException {
        Email from = new Email("mav.solver@hotmail.com");
        from.setName("Johannes Fog Byggemarked");

        Mail mail = new Mail();
        mail.setFrom(from);

        String API_KEY = System.getenv("SENDGRID_API_KEY");

        Personalization personalization = new Personalization();

        personalization.addTo(new Email(email));
        personalization.addDynamicTemplateData("name", name);
        //personalization.addDynamicTemplateData("email", "anders@and.dk");
        //personalization.addDynamicTemplateData("zip", "2100");
        mail.addPersonalization(personalization);

        mail.addCategory("carportapp");

        SendGrid sg = new SendGrid(API_KEY);
        Request request = new Request();
        try
        {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");

            // indsæt dit skabelonid herunder
            mail.templateId = template;
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        }
        catch (IOException ex)
        {
            System.out.println("Error sending mail");
            throw ex;
        }

    }
}
