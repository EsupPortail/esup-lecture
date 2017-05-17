package org.esupportail.lecture.domain.utils;

import org.apache.http.client.config.RequestConfig;

/**
 * Created by jgribonvald on 17/05/17.
 */
public class CustomRequestConfig {

    private int connectionRequestTimeout = -1;
    private int connectTimeout = -1;
    private int socketTimeout = -1;

    public CustomRequestConfig() {
        super();
    }

    public RequestConfig customize() {
        return RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(socketTimeout)
                .setConnectTimeout(connectTimeout)
                .build();
    }

    public void setConnectionRequestTimeout(int connectionRequestTimeout) {
        this.connectionRequestTimeout = connectionRequestTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

}
