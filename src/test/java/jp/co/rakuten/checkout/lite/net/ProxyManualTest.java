package jp.co.rakuten.checkout.lite.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.DatatypeConverter;

import org.junit.Ignore;
import org.junit.Test;


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

@Ignore
public class ProxyManualTest extends RpayLiteTest {

    private static final SSLSocketFactory socketFactory = new RpayLiteSSLSocketFactory();
    
    @Test
    public void getViaProxyWithAuth() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException, IOException,
            InvalidApiKeyException {
        RpayLite.setApiKey(TEST_KEY);
        RpayLite.setProxyCredential(new PasswordAuthentication("foo", "bar".toCharArray()));
        RpayLite.setConnectionProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.99.100", 3128)));
        Charge.retrieve(TEST_ID_NORMAL);
    }

    @Test
    public void getViaProxyWithAuthFull() throws AuthenticationException, InvalidRequestException, APIConnectionException, PaymentRequestException,
            APIException, PermissionException, RateLimitException, MethodNotAllowedException, InternalServerException, ServiceException, IOException,
            InvalidApiKeyException {
        RpayLite.setApiKey(TEST_KEY);
        String basic = String.format("Basic %s", DatatypeConverter.printBase64Binary((RpayLite.getApiKey() + ":").getBytes()));        
        RpayLite.setConnectionProxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("192.168.99.100", 3128)));
        RpayLite.setProxyCredential(new PasswordAuthentication("foo", "bar".toCharArray())); 
        String url = RpayLite.getApiBase() + "/charges?id=1800005627-20170623-0000003160";
        //String url = "http://exmaple.com";    
        URL rpayLiteURL = new URL(url);
        
        HttpURLConnection connection;
        if (RpayLite.getConnectionProxy() != null) {
            connection = (HttpURLConnection) rpayLiteURL.openConnection(RpayLite.getConnectionProxy());
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return RpayLite.getProxyCredential();
                }
            });
            if (connection instanceof HttpsURLConnection) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);
        }
        } else {
            connection = (HttpURLConnection) rpayLiteURL.openConnection();
            if (connection instanceof HttpsURLConnection) {
                ((HttpsURLConnection) connection).setSSLSocketFactory(socketFactory);
            }
        } 
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", basic);
        connection.setUseCaches(false);
        connection.setRequestMethod("GET");       
        connection.connect();
        System.out.println(connection.usingProxy());
        InputStream response = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(response));
        StringBuffer sb = new StringBuffer();
        String inputLine = "";
        while ((inputLine = reader.readLine()) != null) {
            sb.append(inputLine);
        }
        String result = sb.toString();
        response.close();
    }
}
