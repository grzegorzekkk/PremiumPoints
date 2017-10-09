package pl.stillcraft.grzegorzekkk.premiumpoints.payments.sms;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import pl.stillcraft.grzegorzekkk.premiumpoints.configuration.ConfigStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

public class SmsProvider {

    private SmsProvider() {
    }

    /**
     * Check if service sms response code is valid and not used yet.
     *
     * @param code
     * @param service
     * @return
     */
    public static boolean isCodeValid(String code, SmsService service) {
        String responseBody = getProviderResponse(code, service);

        return responseBody.contains(ConfigStorage.getInstance().getValidCodeResponse());
    }

    /**
     * Validates code send by service provider to player
     *
     * @param code
     * @param service
     * @return
     */
    private static String getProviderResponse(String code, SmsService service) {
        HttpClient httpClient = HttpClientBuilder.create().build();
        StringBuilder sb = new StringBuilder();

        try {
            URIBuilder uriBuilder = new URIBuilder(ConfigStorage.getInstance().getProviderURL());

            uriBuilder.addParameter("id", "");
            uriBuilder.addParameter("code", service.getPrefix());
            uriBuilder.addParameter("check", code);

            HttpGet request = new HttpGet(uriBuilder.build().toString());

            HttpResponse response = httpClient.execute(request);

            BufferedReader rd =
                    new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
        } catch (URISyntaxException | IOException e) {
            e.getMessage();
            return null;
        }

        return sb.toString();
    }
}
