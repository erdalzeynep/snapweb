import com.detectify.screenshot.dto.ScreenshotRequestDTO;
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
    public void shouldSaveTheGeneratedImagesLocallyAndReturnDownloadUrls() {

        List<String> urlList = new ArrayList<>();
        String webPage1 = "https://www.google.com";
        String webPage2 = "https://www.facebook.com";
        urlList.add(webPage1);
        urlList.add(webPage2);

        Builder screenshotController = getBuilder("/captureScreenshots");

        ScreenshotRequestDTO response = screenshotController.post(Entity.json(urlList), new GenericType<ScreenshotRequestDTO>() {
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
    public void shouldNotAllowCallTheServiceWithEmptyUrlList() {

        List<String> urlList = new ArrayList<>();
        Builder screenshotController = getBuilder("/captureScreenshots");
        Response response = screenshotController.post(Entity.json(urlList));
        assertEquals(400, response.getStatus());
    }

    @Test
    public void shouldNotAppendPreviousScreenshotRequestInTheResults() {

        List<String> urlList1 = new ArrayList<>();
        String webPage1 = "https://www.google.com";
        urlList1.add(webPage1);
        Builder screenshotController = getBuilder("/captureScreenshots");
        ScreenshotRequestDTO response1 = screenshotController.post(Entity.json(urlList1), new GenericType<ScreenshotRequestDTO>() {
        });

        List<String> urlList2 = new ArrayList<>();
        String webPage2 = "https://www.facebook.com";
        urlList2.add(webPage2);
        ScreenshotRequestDTO response2 = screenshotController.post(Entity.json(urlList2), new GenericType<ScreenshotRequestDTO>() {
        });

        assertEquals(1, response2.getResults().size());
    }

    @Test
    public void shouldReturn200ForDownloadRequestWithCorrectRequestIdAndScreenshotId() {
        List<String> urlList = new ArrayList<>();
        String webPage = "https://www.google.com";
        urlList.add(webPage);
        Builder screenshotController = getBuilder("/captureScreenshots");
        ScreenshotRequestDTO responseOfCaptureRequest = screenshotController.post(Entity.json(urlList), new GenericType<ScreenshotRequestDTO>() {
        });

        Long requestId = responseOfCaptureRequest.getRequestId();
        Long screenshotId = responseOfCaptureRequest.getResults().get(webPage).getScreenshotId();

        Response response = getBuilder("/download/{requestId}/{screenshotId}", requestId, screenshotId).get();

        assertEquals(200, response.getStatus());

    }

    @Test
    public void shouldGiveAnErrorForDownloadRequestWithUnmatchedRequestIdAndScreenshotId() {

        List<String> urlList = new ArrayList<>();
        String webPage = "https://www.google.com";
        urlList.add(webPage);
        Builder screenshotController = getBuilder("/captureScreenshots");
        ScreenshotRequestDTO responseOfCaptureRequest = screenshotController.post(Entity.json(urlList), new GenericType<ScreenshotRequestDTO>() {
        });

        Long requestId = responseOfCaptureRequest.getRequestId();
        Long screenshotId = responseOfCaptureRequest.getResults().get(webPage).getScreenshotId();

        Long unknownRequestId = -999L;
        Long unknownScreenshotId = -999L;

        Response response1 = getBuilder("/download/{requestId}/{screenshotId}", unknownRequestId, screenshotId).get();
        Response response2 = getBuilder("/download/{requestId}/{screenshotId}", requestId, unknownScreenshotId).get();
        Response response3 = getBuilder("/download/{requestId}/{screenshotId}", unknownRequestId, unknownScreenshotId).get();

        assertEquals(404, response1.getStatus());
        assertEquals(404, response2.getStatus());
        assertEquals(404, response3.getStatus());
    }

    @Test
    public void shouldFetchRequestDetailsByRequestId() {

        List<String> urlList = new ArrayList<>();
        String webPage = "https://www.google.com";
        urlList.add(webPage);
        Builder screenshotController = getBuilder("/captureScreenshots");
        ScreenshotRequestDTO response = screenshotController.post(Entity.json(urlList), new GenericType<ScreenshotRequestDTO>() {
        });

        Long requestId = response.getRequestId();
        ScreenshotRequestDTO fetchResult = getBuilder("/fetchRequestDetail/{requestId}", requestId).get(new GenericType<ScreenshotRequestDTO>() {
        });

        assertEquals(1, fetchResult.getResults().size());
        assertTrue(fetchResult.getResults().containsKey(webPage));

        String expectedDownloadUrl = response.getResults().get(webPage).getDownloadUrl();
        Long expectedScreenshotId = response.getResults().get(webPage).getScreenshotId();
        String actualDownloadUrl = fetchResult.getResults().get(webPage).getDownloadUrl();
        Long actualScreenshotId = fetchResult.getResults().get(webPage).getScreenshotId();

        assertEquals(expectedDownloadUrl, actualDownloadUrl);
        assertEquals(expectedScreenshotId, actualScreenshotId);

    }

    @Test
    public void shouldGiveNotFoundExceptionWhenFetchingWithInvalidRequestId() {
        Long invalidRequestId = -999L;

        List<String> urlList = new ArrayList<>();
        String webPage = "https://www.google.com";
        urlList.add(webPage);
        Builder screenshotController = getBuilder("/captureScreenshots");
        screenshotController.post(Entity.json(urlList), new GenericType<ScreenshotRequestDTO>() {
        });

        Response response = getBuilder("/fetchRequestDetail/{requestId}", invalidRequestId).get();
        assertEquals(404, response.getStatus());
    }

    private String createDownloadUrl(String webPage, ScreenshotRequestDTO response) {
        return "http://localhost:9443/api/1.0/download/" + response.getRequestId() + "/" + response.getResults().get(webPage).getScreenshotId();
    }
}
