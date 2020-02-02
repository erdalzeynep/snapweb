import com.detectify.screenshot.Application;
import com.detectify.screenshot.repository.ScreenshotRepository;
import com.detectify.screenshot.repository.ScreenshotRequestRepository;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, classes = {
        Application.class})
public class TestBase {


    @Autowired
    ScreenshotRepository screenshotRepository;

    @Autowired
    ScreenshotRequestRepository screenshotRequestRepository;

    private String mServerUri;

    protected Client mClient;

    @Value("${server.port}")
    protected int appPort;

    @PostConstruct
    public void postConstruct() {

        mServerUri = "http://localhost:" + appPort + "/api/1.0";
        mClient = createClient();
    }

    public Builder getBuilder(String path, Object... values) {
        URI uri = UriBuilder.fromUri(mServerUri + path).build(values);

        WebTarget webTarget = mClient.target(uri);
        webTarget = webTarget.register(MultiPartFeature.class);

        return webTarget.request(MediaType.APPLICATION_JSON_TYPE, MediaType.APPLICATION_OCTET_STREAM_TYPE);
    }

    protected Client createClient() {

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();
        return clientBuilder.build();
    }
}
