package ru.netology;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Request {
    protected String method;
    protected String path;
    protected String protocol;
    protected List<String> headers;
    protected String body;
    protected List<NameValuePair> params = new CopyOnWriteArrayList<>();

    public Request(String method, String path, String protocol) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
    }

    public Request(String method, String path, String protocol, List<String> headers) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.headers = headers;
    }

    public Request(String method, String path, String protocol, List<String> headers, String body) {
        this.method = method;
        this.path = path;
        this.protocol = protocol;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getProtocol() {
        return protocol;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public List<NameValuePair> getQueryParams() {
        params = URLEncodedUtils.parse(path, StandardCharsets.UTF_8);
        return params;
    }

    public String getQueryParam(String name) {
        params = URLEncodedUtils.parse(path, StandardCharsets.UTF_8);
        String param = null;
        for (NameValuePair paramsPair : params) {
            if (paramsPair.getName().equals(name)) {
                param = paramsPair.getValue();
                return param;
            }
        }
        return param;
    }
}
