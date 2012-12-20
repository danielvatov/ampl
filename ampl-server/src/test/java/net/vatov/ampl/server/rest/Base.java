/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.server.rest;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class Base {

    protected HttpResponse executeGet(String context) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://localhost:8080/" + context);
        return client.execute(get);
    }
    
    protected HttpResponse executePut(String context, String body) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPut put = new HttpPut("http://localhost:8080/" + context);
        HttpEntity entity = new StringEntity(body);
        put.setEntity(entity);
        return client.execute(put);
    }
    
    protected HttpResponse executePost(String context, String body) throws ClientProtocolException, IOException {
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost("http://localhost:8080/" + context);
        HttpEntity entity = new StringEntity(body);
        post.setEntity(entity);
        return client.execute(post);
    }
}
