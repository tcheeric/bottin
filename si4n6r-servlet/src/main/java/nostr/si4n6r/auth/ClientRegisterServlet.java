package nostr.si4n6r.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.java.Log;
import nostr.base.PublicKey;
import nostr.si4n6r.core.impl.AccountProxy;
import nostr.si4n6r.core.impl.ApplicationProxy;
import nostr.si4n6r.core.impl.BaseActorProxy;
import nostr.si4n6r.core.impl.Principal;
import nostr.si4n6r.storage.Vault;
import nostr.si4n6r.util.EncryptionUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ServiceLoader;

/**
 * @author eric
 */
@WebServlet(name = "ClientRegisterServlet", urlPatterns = {"/register"})
@Log
public class ClientRegisterServlet extends HttpServlet {

    private static Vault getVault(@NonNull String entity) {
        return ServiceLoader
                .load(Vault.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .filter(v -> entity.equals(v.getEntityName()))
                .findFirst()
                .get();
    }

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

        try {
            var registerResult = handleRegistration(request);
            sendResponse(response, registerResult, HttpServletResponse.SC_OK);
        } catch (Exception ex) {
            var registerResult = new RegisterResult();
            registerResult.setError(ex.getMessage());
            registerResult.setResult(ClientAuthServlet.AuthResult.RESULT_ERROR);
            sendResponse(response, registerResult, HttpServletResponse.SC_FORBIDDEN);
        }
    }

    private RegisterResult handleRegistration(HttpServletRequest request) throws Exception {
        var npub = request.getParameter("npub");
        var nsec = request.getParameter("nsec");
        var password = request.getParameter("password");
        var appName = request.getParameter("appName");
        var appPubKey = request.getParameter("appPubKey");

        Principal principal = Principal.getInstance(new PublicKey(npub), password);
        nostr.si4n6r.core.impl.SecurityManager securityManager = nostr.si4n6r.core.impl.SecurityManager.getInstance();
        securityManager.addPrincipal(principal);

        var app = new ApplicationProxy(appPubKey);
        var decodedAppName = URLDecoder.decode(appName, StandardCharsets.UTF_8.toString());
        app.setName(decodedAppName);

        var account = new AccountProxy();
        account.setPrivateKey(nsec);
        account.setPublicKey(npub);
        account.setId(System.currentTimeMillis());
        account.setApplication(app);

        Vault<AccountProxy> vault = getVault();
        vault.store(account);

        var registerResult = new RegisterResult();
        registerResult.setNpub(npub);
        registerResult.setNsec(EncryptionUtil.hashSHA256(nsec));
        registerResult.setPassword(EncryptionUtil.hashSHA256(password));
        registerResult.setResult(ClientAuthServlet.AuthResult.RESULT_SUCCESS);

        return registerResult;
    }

    private void sendResponse(HttpServletResponse response, RegisterResult registerResult, int status) throws IOException {
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

    private Vault<AccountProxy> getVault() {
        return getVault(BaseActorProxy.VAULT_ACTOR_ACCOUNT);
    }

    @Data
    @NoArgsConstructor
    static class RegisterResult {

        private String npub;
        private String nsec;
        private String password;
        private String result;
        private String error;
    }

}
