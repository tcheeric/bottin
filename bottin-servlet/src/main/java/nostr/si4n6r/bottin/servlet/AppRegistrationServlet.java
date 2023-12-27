package nostr.si4n6r.bottin.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nostr.si4n6r.bottin.servlet.handler.AppRegistrationHandler;
import nostr.si4n6r.bottin.servlet.handler.Handler;
import nostr.si4n6r.core.impl.ApplicationProxy;

import java.io.IOException;
import java.util.Arrays;

@WebServlet(name = "AppRegistrationServlet", urlPatterns = {"/app"})
public class AppRegistrationServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String publicKey = request.getParameter("publicKey");
        ApplicationProxy.ApplicationTemplate template = getTemplate(request);

        var handler = new AppRegistrationHandler(publicKey, template);
        var result = handler.handle();

        response.setContentType("application/json");
        sendResponse(response, result);
    }

    private ApplicationProxy.ApplicationTemplate getTemplate(HttpServletRequest request) {
        ApplicationProxy.ApplicationTemplate template = new ApplicationProxy.ApplicationTemplate();
        template.setName(request.getParameter("name"));
        template.setUrl(request.getParameter("url"));
        template.setDescription(request.getParameter("description"));
        String[] icons = request.getParameterValues("icons");
        if (icons != null) {
            template.setIcons(Arrays.asList(icons));
        }
        return template;
    }

    private void sendResponse(HttpServletResponse response, Handler.Result authResult) throws IOException {
        var objectMapper = new ObjectMapper();
        var result = objectMapper.writeValueAsString(authResult);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(result);
    }
}