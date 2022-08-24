package com.lance.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class HttpUtil {

    private static final Logger logger = LogManager.getLogger(HttpUtil.class);

    private static final Charset DEFAULT_ENCODE = StandardCharsets.UTF_8;

    /**
     * Http Post
     *
     * @param url         : URL
     * @param headers     : Customized header
     * @param requestBody : Request body(JSON format)
     * @return CloseableHttpResponse
     */
    public static HttpResult post(String url, Map<String, String> headers, String requestBody) throws Exception {
        HttpResult httpResult;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            logger.info("POST request to: {}", url);
            HttpPost httpPost = new HttpPost(url);
            if (headers != null && !headers.isEmpty())
                for (Map.Entry<String, String> header : headers.entrySet())
                    httpPost.setHeader(header.getKey(), header.getValue());
            StringEntity jsonEntity = new StringEntity(requestBody, DEFAULT_ENCODE);
            httpPost.setEntity(jsonEntity);
            CloseableHttpResponse httpResponse = httpclient.execute(httpPost);
            httpResult = getHttpResult(httpResponse);
        }

        return httpResult;
    }

    /**
     * Http Get
     *
     * @param url:     URL
     * @param headers: Customized header
     * @return HttpResult
     */
    public static HttpResult get(String url, String queryString, Map<String, String> headers) throws Exception {
        HttpResult httpResult;
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            url = url + (queryString != null ? "?" + queryString : "");
            logger.info("GET request to: {}", url);
            HttpGet httpGet = new HttpGet(url);
            if (headers != null && !headers.isEmpty())
                for (Map.Entry<String, String> header : headers.entrySet())
                    httpGet.setHeader(header.getKey(), header.getValue());
            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            httpResult = getHttpResult(httpResponse);
        }
        return httpResult;
    }

    /**
     * Get http result
     *
     * @param httpResponse: CloseableHttpResponse
     * @return HttpResult
     */
    private static HttpResult getHttpResult(CloseableHttpResponse httpResponse) throws Exception {
        HttpResult httpResult = new HttpResult();
        if (httpResponse != null) {
            if (httpResponse.getStatusLine() != null)
                httpResult.setStatusCode(httpResponse.getStatusLine().getStatusCode());
            if (httpResponse.getEntity() != null)
                httpResult.setBody(EntityUtils.toString(httpResponse.getEntity()));
        }
        return httpResult;
    }

    public static class HttpResult {

        private Integer statusCode;
        private String body;

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }
    }

}
