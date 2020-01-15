package com.fayayo.inte.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author dalizu on 2020/1/13.
 * @version v1.0
 * @desc
 */
@Configuration
public class ElasticSearchRestClient {

    @Value("${elasticsearch.node1}")
    String ipAddress;

    @Bean
    public RestHighLevelClient highLevelClient(){
        String []address=ipAddress.split(":");
        HttpHost httpHost=new HttpHost(address[0],Integer.valueOf(address[1]),"http");
        return new RestHighLevelClient(RestClient.builder(new HttpHost[]{httpHost}));
    }






}
