/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package nostr.si4n6r.registration;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.extern.java.Log;
import nostr.si4n6r.core.impl.AccountProxy;
import nostr.si4n6r.core.impl.BaseActorProxy;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * @author eric
 */
@Data
@Log
@EqualsAndHashCode(callSuper = true)
public class AccountRegistration extends AbstractBaseRegistration<AccountProxy> {

    private final String password;

    private static final String URL_PATH = "/si4n6r-servlet-1.0-SNAPSHOT";
    private static final String URL_RESOURCE_REGISTER = "/register";
    private static final String URL_RESOURCE_LOGIN = "/login";

    public AccountRegistration(@NonNull String password) {
        super(BaseActorProxy.VAULT_ACTOR_ACCOUNT);
        this.password = password;
    }

    @Override
    public void register(@NonNull AccountProxy proxy) {
        HttpURLConnection connection = null;
        try {
            var registerUrl = buildRegisterUrl();
            var parameters = buildParameters(proxy);
            var urlString = buildUrlWithParameters(registerUrl, parameters);
            log.log(Level.INFO, "URL: {0}", urlString);
            var url = new URI(urlString).toURL();
            connection = (HttpURLConnection) url.openConnection();
            setupConnection(connection);
            String responseContent = readResponse(connection);
            log.log(Level.INFO, "Response Content: {0}", responseContent);
        } catch (IOException | URISyntaxException e) {
            log.log(Level.SEVERE, "An error has occurred: ", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private Map<String, String> buildParameters(AccountProxy proxy) throws UnsupportedEncodingException {
        String encodedAppName = URLEncoder.encode(proxy.getApplication().getName(), StandardCharsets.UTF_8.toString());
        return Map.of(
                "npub", proxy.getPublicKey(),
                "nsec", proxy.getPrivateKey(),
                "password", password,
                "appPubKey", proxy.getApplication().getPublicKey(),
                "appName", encodedAppName
        );
    }

    private void setupConnection(HttpURLConnection connection) throws IOException {
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Accept", "application/json");
    }

    private String readResponse(HttpURLConnection connection) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder responseContent = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            responseContent.append(line);
        }
        reader.close();
        return responseContent.toString();
    }

    private String buildRegisterUrl() {
        var env = System.getProperty("env") == null ? "dev" : System.getProperty("env");
        Properties properties = new Properties();
        try (var input = getClass().getClassLoader().getResourceAsStream("auth_client_" + env + ".properties")) {
            if (input == null) {
                throw new FileNotFoundException("Unable to find properties file: auth_client_" + env + ".properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            log.log(Level.SEVERE, "An error has occurred while reading the properties file: ", ex);
        }

        String host = properties.getProperty("si4n6r.auth.server.host", "localhost");
        int port = Integer.parseInt(properties.getProperty("si4n6r.auth.server.port", "8080"));
        return "http://" + host + ":" + port + URL_PATH + URL_RESOURCE_REGISTER;
    }

    private String buildUrlWithParameters(String url, Map<String, String> parameters) {
        String paramString = parameters.entrySet().stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        return url + "?" + paramString;
    }
}