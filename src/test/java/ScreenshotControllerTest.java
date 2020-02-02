import com.detectify.screenshot.DTO.CaptureScreenshotDTO;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
public class ScreenshotControllerTest extends TestBase {

    @Test
    public void shouldSaveTheGeneratedImagesLocallyAndReturnTracingUrls() {

        List<String> urlList = new ArrayList<>();
        String webPage1 = "https://www.google.com";
        String webPage2 = "https://www.facebook.com";
        urlList.add(webPage1);
        urlList.add(webPage2);

        Builder screenshotController = getBuilder("/captureScreenshots");

        CaptureScreenshotDTO response = screenshotController.post(Entity.json(urlList), new GenericType<CaptureScreenshotDTO>() {
        });

        long persistedScreenshotCount = screenshotRepository.count();
        assertEquals(urlList.size(), persistedScreenshotCount);
        assertNotNull(response.getRequestId());
        assertEquals(urlList.size(), response.getResults().size());
        assertTrue(response.getResults().containsKey(webPage1));
        assertTrue(response.getResults().containsKey(webPage2));
        assertNotNull(response.getResults().get(webPage1).getScreenshotId());
        assertNotNull(response.getResults().get(webPage2).getScreenshotId());
        assertEquals(createDownloadUrl(webPage1, response), response.getResults().get(webPage1).getDownloadUrl());
        assertEquals(createDownloadUrl(webPage2, response), response.getResults().get(webPage2).getDownloadUrl());
    }

    @Test
    public void shouldNotAllowCallTheServiceWithEmptyUrlList(){

        List<String> urlList = new ArrayList<>();
        Builder screenshotController = getBuilder("/captureScreenshots");
        Response response = screenshotController.post(Entity.json(urlList));
        assertEquals(400, response.getStatus());
    }

    @Test
    public void shouldNotAppendPreviousScreenshotRequestInTheResults(){

        List<String> urlList1 = new ArrayList<>();
        String webPage1 = "https://www.google.com";
        urlList1.add(webPage1);
        Builder screenshotController = getBuilder("/captureScreenshots");
        CaptureScreenshotDTO response1 = screenshotController.post(Entity.json(urlList1), new GenericType<CaptureScreenshotDTO>() {
        });

        List<String> urlList2 = new ArrayList<>();
        String webPage2 = "https://www.facebook.com";
        urlList2.add(webPage2);
        CaptureScreenshotDTO response2 = screenshotController.post(Entity.json(urlList2), new GenericType<CaptureScreenshotDTO>() {
        });

        assertEquals(1, response2.getResults().size());
    }

    private String createDownloadUrl(String webPage, CaptureScreenshotDTO response) {
        return "http://localhost:9443/app/api/1.0/download/" + response.getRequestId() + "/" + response.getResults().get(webPage).getScreenshotId();
    }
}
