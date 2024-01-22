package nostr.si4n6r.bottin.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import nostr.si4n6r.bottin.servlet.handler.AuthenticationHandler;
import nostr.si4n6r.bottin.servlet.handler.Handler;

import java.io.IOException;
import java.util.logging.Level;

@WebServlet(name = "AuthenticationServlet", urlPatterns = {"/auth"})
@Log
public class AuthenticationServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException      if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String npub = request.getParameter("npub");
        String password = request.getParameter("password");

        try {
            var handler = new AuthenticationHandler(npub, password);
            var authResult = handler.handle();
            log.log(Level.INFO, "Authentication result: {0}", authResult);
            sendResponse(response, authResult, HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            var authResult = new Handler.Result();
            authResult.setError(ex.getMessage());
            authResult.setResult(Handler.Result.RESULT_ERROR);
            sendResponse(response, authResult, HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private void sendResponse(HttpServletResponse response, Handler.Result authResult, int status) throws IOException {
        var objectMapper = new ObjectMapper();
        var result = objectMapper.writeValueAsString(authResult);
        response.setStatus(status);
        response.getWriter().write(result);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request  servlet request
     * @param response servlet response
     * @throws IOException      if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        processRequest(request, response);
    }
}
