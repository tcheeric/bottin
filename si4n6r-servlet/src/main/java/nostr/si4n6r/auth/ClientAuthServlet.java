package nostr.si4n6r.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import nostr.base.PublicKey;
import nostr.si4n6r.core.impl.Principal;
import nostr.si4n6r.util.EncryptionUtil;

import java.io.IOException;

/**
 * @author eric
 */
@Log
@WebServlet(name = "ClientAuthServlet", urlPatterns = {"/login"})
public class ClientAuthServlet extends HttpServlet {

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

        try {
            var authResult = handleAuthentication(request);
            sendResponse(response, authResult, HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            var authResult = new AuthResult();
            authResult.setError(ex.getMessage());
            authResult.setResult(AuthResult.RESULT_ERROR);
            sendResponse(response, authResult, HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private AuthResult handleAuthentication(HttpServletRequest request) throws Exception {
        var npub = request.getParameter("npub");
        var password = request.getParameter("password");

        var authResult = new AuthResult();
        authResult.setNpub(npub);
        authResult.setHashedPassword(EncryptionUtil.hashSHA256(password));

        var principal = Principal.getInstance(new PublicKey(npub), password);
        principal.decryptNsec();

        authResult.setResult(AuthResult.RESULT_SUCCESS);
        return authResult;
    }

    private void sendResponse(HttpServletResponse response, AuthResult authResult, int status) throws IOException {
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

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    @Data
    @NoArgsConstructor
    static class AuthResult {

        static final String RESULT_ERROR = "ERROR";
        static final String RESULT_SUCCESS = "SUCCESS";

        private String npub;
        private String hashedPassword;
        private String result;
        private String error;
    }
}
