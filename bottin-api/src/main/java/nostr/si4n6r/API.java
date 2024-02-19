package nostr.si4n6r;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Hello world!
 *
 */
public class API {
    private final WebModule rest;
    private final WebModule servlet;

    public API() {
        this.rest = new WebModule("Bottin Rest", "localhost", 6060);
        this.servlet = new WebModule("Bottin Servlet", "localhost", 8080);
    }


    public void registerApp(
            @NonNull String publicKey,
            @NonNull String name,
            @NonNull String url,
            @NonNull String description,
            @NonNull String[] icons)
    {
        try {
            URL servletUrl = new URL(servlet.getURL() + "/app");
            HttpURLConnection conn = (HttpURLConnection) servletUrl.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String iconsString = String.join(",", icons);
            String body = String.format("publicKey=%s&name=%s&url=%s&description=%s&icons=%s",
                    publicKey, name, url, description, iconsString);

            try (var os = conn.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code != 200) {
                throw new RuntimeException("Failed to register app: HTTP error code : " + code);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to register app", e);
        }
    }

    public void authenticate(@NonNull String name, @NonNull String npub, @NonNull String password) {
        try {
            URL servletUrl = new URL(servlet.getURL() + "/auth");
            HttpURLConnection conn = (HttpURLConnection) servletUrl.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String body = String.format("name=%s&npub=%s&password=%s", name, npub, password);

            try (var os = conn.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code != 200) {
                throw new RuntimeException("Failed to authenticate: HTTP error code : " + code);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to authenticate", e);
        }
    }

    public void register(
            @NonNull String npub,
            @NonNull String nsec,
            @NonNull String name,
            @NonNull String password,
            @NonNull String appName,
            @NonNull String appPubKey)
    {
        try {
            URL servletUrl = new URL(servlet.getURL() + "/identity");
            HttpURLConnection conn = (HttpURLConnection) servletUrl.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String body = String.format("npub=%s&nsec=%s&name=%s&password=%s&appName=%s&appPubKey=%s",
                    npub, nsec, name, password, appName, appPubKey);

            try (var os = conn.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code != 200) {
                throw new RuntimeException("Failed to register: HTTP error code : " + code);
            }

        } catch (IOException e) {
            throw new RuntimeException("Failed to register", e);
        }
    }

    public String nip05Verify(@NonNull String localPart, @NonNull String domain) {
        try {
            URL servletUrl = new URL(rest.getURL() + "/nip05");
            HttpURLConnection conn = (HttpURLConnection) servletUrl.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            String body = String.format("localpart=%s&domain=%s", localPart, domain);

            try (var os = conn.getOutputStream()) {
                byte[] input = body.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int code = conn.getResponseCode();
            if (code != 200) {
                throw new RuntimeException("Failed to verify nip05: HTTP error code : " + code);
            }

            return conn.getResponseMessage();
        } catch (IOException e) {
            throw new RuntimeException("Failed to verify nip05", e);
        }
    }

    @Data
    @AllArgsConstructor
    private static class WebModule {
        private final String name;
        private final String hostname;
        private final int port;

        public String getURL() {
            return "http://" + hostname + ":" + port;
        }
    }

}
