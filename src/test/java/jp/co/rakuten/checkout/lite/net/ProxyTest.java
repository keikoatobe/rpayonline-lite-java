package jp.co.rakuten.checkout.lite.net;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.URL;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.Test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import jp.co.rakuten.checkout.lite.RpayLite;
import jp.co.rakuten.checkout.lite.RpayLiteTest;
import jp.co.rakuten.checkout.lite.exception.APIConnectionException;
import jp.co.rakuten.checkout.lite.exception.APIException;
import jp.co.rakuten.checkout.lite.exception.AuthenticationException;
import jp.co.rakuten.checkout.lite.exception.InternalServerException;
import jp.co.rakuten.checkout.lite.exception.InvalidApiKeyException;
import jp.co.rakuten.checkout.lite.exception.InvalidRequestException;
import jp.co.rakuten.checkout.lite.exception.MethodNotAllowedException;
import jp.co.rakuten.checkout.lite.exception.PaymentRequestException;
import jp.co.rakuten.checkout.lite.exception.PermissionException;
import jp.co.rakuten.checkout.lite.exception.RateLimitException;
import jp.co.rakuten.checkout.lite.exception.ServiceException;
import jp.co.rakuten.checkout.lite.model.Charge;
import jp.co.rakuten.checkout.lite.net.APIResource.RequestMethod;
import jp.co.rakuten.checkout.lite.net.APIResource.RequestType;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.options;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;

public class ProxyTest extends RpayLiteTest {

    @ClassRule
    public static WireMockRule proxyServer = new WireMockRule(options().proxyVia("http://otherhost.com", 8092));

    private String targetServiceBaseUrl;
    private String targetServiceBaseHttpsUrl;

    WireMockServer targetService;
    WireMock targetServiceAdmin;

    WireMockServer proxyingService;
    WireMock proxyingServiceAdmin;

    String apiError = "{\"errors\": [{\"type\": \"api_error\",\"code\": \"invalid_request\",\"message\": \"api error\"}]}";

    RpayLiteResponseGetter responseGetter = new LiveRpayLiteResponseGetter();

    @Before
    public void setUpAll() throws InvalidApiKeyException, UnsupportedEncodingException, FileNotFoundException {
        RpayLite.setApiKey(TEST_KEY);
        RpayLite.overrideApiBase("http://localhost:8092");
        RpayLite.setConnectionProxy(null);
    }

    void init(WireMockConfiguration proxyingServiceOptions) {
        targetService = new WireMockServer(wireMockConfig().port(8092).dynamicHttpsPort());
        targetService.start();
        targetServiceAdmin = new WireMock("localhost", 8092);

        targetServiceBaseUrl = "http://localhost:" + "8092";
        targetServiceBaseHttpsUrl = "https://localhost:" + targetService.httpsPort();

        proxyingServiceOptions.dynamicPort();
        proxyingService = new WireMockServer(proxyingServiceOptions);
        proxyingService.start();
        proxyingServiceAdmin = new WireMock(proxyingService.port());
        WireMock.configureFor(8092);
    }

    void initWithDefaultConfig() {
        init(wireMockConfig());
    }

    void initWithDocker() {
        targetService = new WireMockServer(wireMockConfig().port(8092).dynamicHttpsPort());
        targetService.start();
        targetServiceAdmin = new WireMock("localhost", 8092);

        targetServiceBaseUrl = "http://localhost:" + "8092";
        targetServiceBaseHttpsUrl = "https://localhost:" + targetService.httpsPort();

        wireMockConfig().port(3128).bindAddress("192.168.99.100");
        proxyingService = new WireMockServer(wireMockConfig());
        proxyingService.start();
        proxyingServiceAdmin = new WireMock(proxyingService.port());
        WireMock.configureFor(8092);
    }

    @Test
    public void getViaProxyTest() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException, IOException {
        initWithDefaultConfig();

        targetServiceAdmin.register(
                get(urlEqualTo("/proxied")).willReturn(aResponse().withStatus(200).withHeader("Content-Type", "text/plain").withBody("{}")));

        proxyingServiceAdmin.register(get(urlEqualTo("/proxied")).atPriority(10)
                .willReturn(aResponse().withHeader("Proxy-Authorization", "Basic dXNlcm5hbWU6cGFzc3dvcmQ=").proxiedFrom(targetServiceBaseUrl)));

        RpayLite.setConnectionProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyingService.port())));
        responseGetter.request(RequestMethod.GET, RpayLite.getApiBase() + "/proxied", null, this.getClass(), RequestType.NORMAL, null);
        URL url = new URL(RpayLite.getApiBase() + "/proxied");
        HttpURLConnection connection;
        if (RpayLite.getConnectionProxy() != null) {
            connection = (HttpURLConnection) url.openConnection(RpayLite.getConnectionProxy());
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return RpayLite.getProxyCredential();
                }
            });
        } else {
            connection = (HttpURLConnection) url.openConnection();
        }
        connection.setRequestProperty("Accept-Charset", "UTF-8");
        connection.setDoOutput(true);
        connection.setRequestMethod("GET");
        InputStream response = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(response));
        StringBuffer sb = new StringBuffer();
        String inputLine = "";
        while ((inputLine = reader.readLine()) != null) {
            sb.append(inputLine);
        }
        String result = sb.toString();
        assertEquals("{}", result);
        response.close();
    }


}
