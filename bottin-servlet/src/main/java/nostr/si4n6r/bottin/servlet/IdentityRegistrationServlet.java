package nostr.si4n6r.bottin.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import nostr.si4n6r.bottin.servlet.handler.Handler;
import nostr.si4n6r.bottin.servlet.handler.IdentityRegistrationHandler;

import java.io.IOException;

/**
 * @author eric
 */
@WebServlet(name = "IdentityRegistrationServlet", urlPatterns = {"/identity"})
@Log
public class IdentityRegistrationServlet extends HttpServlet {

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

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
        String nsec = request.getParameter("nsec");
        String password = request.getParameter("password");
        String appName = request.getParameter("appName");
        String appPubKey = request.getParameter("appPubKey");

        try {
            var handler = new IdentityRegistrationHandler(npub, nsec, password, appName, appPubKey);
            var registerResult = handler.handle();
            log.info("Registration result: " + registerResult);
            sendResponse(response, registerResult, HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            var registerResult = new Handler.Result();
            registerResult.setError(ex.getMessage());
            registerResult.setResult(Handler.Result.RESULT_ERROR);
            sendResponse(response, registerResult, HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private void sendResponse(HttpServletResponse response, Handler.Result registerResult, int status) throws IOException {
        var objectMapper = new ObjectMapper();
        var result = objectMapper.writeValueAsString(registerResult);
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
