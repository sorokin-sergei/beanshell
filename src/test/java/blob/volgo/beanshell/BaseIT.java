package blob.volgo.beanshell;

import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
public class BaseIT {
	private static final Logger log = LoggerFactory.getLogger(BaseIT.class);

    protected static TestRestTemplate testRestTemplate;
    protected static ObjectMapper objectMapper = new ObjectMapper();

 // proto+host in test.properties, may be passed as maven arg
    @Value("${VB_SERVICE_URL}")
    public String VB_SERVICE_URL;
    
    @Test
	public void testBeanshellScript() {
        // curl http://localhost:18080/beanshell/script -d 'file=/tmp/test.bsh'
		HttpHeaders headers = getFormHeaders();
		String bsURL = VB_SERVICE_URL+"/beanshell/script";
		log.info("URL = " + bsURL);
		
        HttpEntity<String> entity = new HttpEntity<String>("file=/tmp/test.bsh", headers);
        ResponseEntity<String> response = testRestTemplate.exchange(bsURL, HttpMethod.POST, entity, String.class);
        String body = response.getBody();
		log.info("RESPONSE = \n" + body );
	}

    @Test
	public void testBeanshellCommand() {
        // curl http://localhost:18080/beanshell/command -d 'command=ls'
		HttpHeaders headers = getFormHeaders();
		String bsURL = VB_SERVICE_URL+"/beanshell/command";
		log.info("URL = " + bsURL);
		
        HttpEntity<String> entity = new HttpEntity<String>("command=ls", headers);
        ResponseEntity<String> response = testRestTemplate.exchange(bsURL, HttpMethod.POST, entity, String.class);
        String body = response.getBody();
		log.info("RESPONSE = \n" + body );
	}
    
	private HttpHeaders getFormHeaders() {
		HttpHeaders headers = new HttpHeaders();
	    headers.add("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
	    headers.add("Accept", "application/json;charset=utf-8");
		return headers;
	}
	
    @SuppressWarnings("rawtypes")
    @BeforeClass
    public static void setup() {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setRedirectsEnabled(false).setCookieSpec("easy").build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(defaultRequestConfig)
                .setUserAgent("VolgoBlob Integration test").build();
        RestTemplateBuilder builder = new RestTemplateBuilder();
        HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory(httpClient);
        BufferingClientHttpRequestFactory brf = new BufferingClientHttpRequestFactory(rf);
        builder.requestFactory(() -> brf);
        testRestTemplate = new TestRestTemplate(builder);
        for (HttpMessageConverter converter : testRestTemplate.getRestTemplate().getMessageConverters()) {
            if (converter instanceof StringHttpMessageConverter) {
                ((StringHttpMessageConverter) converter).setWriteAcceptCharset(false);
            }
        }
    }
}
