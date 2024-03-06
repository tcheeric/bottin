package nostr.bottin.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.java.Log;
import nostr.bottin.servlet.handler.Handler;
import nostr.bottin.servlet.handler.NIP05Handler;

import java.io.IOException;
import java.util.logging.Level;

@WebServlet(name = "NIP05Servlet", urlPatterns = {"/nip05"})
@Log
public class NIP05Servlet extends HttpServlet {

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

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");

        String local = request.getParameter("localpart");
        String domain = request.getParameter("domain");

        try {
            var handler = new NIP05Handler(local, domain);
            var nip05Result = handler.handle();
            log.log(Level.INFO, "NIP05 result: {0}", nip05Result);
            sendResponse(response, nip05Result, HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            var authResult = new Handler.Result();
            authResult.setError(ex.getMessage());
            authResult.setResult(Handler.Result.RESULT_ERROR);
            sendResponse(response, authResult, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void sendResponse(HttpServletResponse response, Handler.Result registerResult, int status) throws IOException {
        var objectMapper = new ObjectMapper();
        var result = objectMapper.writeValueAsString(registerResult);
        response.setStatus(status);
        response.getWriter().write(result);
    }


}
