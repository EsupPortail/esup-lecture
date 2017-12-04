package org.esupportail.lecture.domain.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * Created by jgribonvald on 17/05/17.
 */
public class CustomHttpClientConfig {

    private SSLConnectionSocketFactory sslConnectionSocketFactory;

    private RequestConfig requestConfig;

    private int maxConnPerRoute = 50;

    private int maxConnTotal = 20;


    public CustomHttpClientConfig() {
        super();
    }

    public HttpClient customize() {
        return HttpClientBuilder.create()
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setDefaultRequestConfig(requestConfig)
                .setMaxConnPerRoute(maxConnPerRoute)
                .setMaxConnTotal(maxConnTotal)
                .build();
    }

    public void setSslConnectionSocketFactory(SSLConnectionSocketFactory sslConnectionSocketFactory) {
        this.sslConnectionSocketFactory = sslConnectionSocketFactory;
    }

    public void setRequestConfig(RequestConfig requestConfig) {
        this.requestConfig = requestConfig;
    }

    public void setMaxConnPerRoute(int maxConnPerRoute) {
        this.maxConnPerRoute = maxConnPerRoute;
    }

    public void setMaxConnTotal(int maxConnTotal) {
        this.maxConnTotal = maxConnTotal;
    }
}
