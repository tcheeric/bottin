package nostr.bottin.servlet.handler;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

public interface Handler<T> {

    T handle() throws Exception;

    @Data
    @NoArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    class Result {

        public static final String RESULT_ERROR = "ERROR";
        public static final String RESULT_SUCCESS = "SUCCESS";

        private String npub;
        private String hashedPassword;
        private String nsec;
        private String password;
        private String result;
        private String error;
    }
}
