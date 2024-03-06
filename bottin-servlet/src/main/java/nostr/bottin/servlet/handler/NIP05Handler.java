package nostr.bottin.servlet.handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import nostr.bottin.API;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Data
@AllArgsConstructor
public class NIP05Handler implements Handler<Handler.Result> {

    private final String local;
    private final String domain;

    /**
     * @return
     */
    @Override
    public Result handle() {
        var api = new API();
        var result = new Result();
        var nip05Verification = api.nip05Verify(local, domain);
        result.setResult(Base64.getEncoder().encodeToString(nip05Verification.getBytes(StandardCharsets.UTF_8)));
        //result.setResult(Result.RESULT_SUCCESS);
        return result;
    }
}
