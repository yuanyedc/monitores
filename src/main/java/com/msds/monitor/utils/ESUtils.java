package com.msds.monitor.utils;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by yuanye on 2016/8/11.
 */
public class ESUtils {

    public static Client getClient() {
        Settings settings = Settings.settingsBuilder().put("cluster.name", "_new_cluster").build();
        Client client = null;
        try {
            client = TransportClient.builder().settings(settings).build()
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.30.12.48"), 9601))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.30.12.48"), 9602))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.30.12.48"), 9603))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.30.12.48"), 9604))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.30.12.48"), 9605))
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.30.12.48"), 9606));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return client;
    }

    public static SearchResponse search(String index, QueryBuilder builder) {
        Client client = getClient();
        SearchResponse response = client.prepareSearch(index)
                .setPostFilter(builder).setExplain(true).execute().actionGet();
        return response;
    }

    public static SearchResponse search(String index, QueryBuilder querybuilder, TermsBuilder termsBuilder) {
        Client client = getClient();
        SearchResponse response = client.prepareSearch(index).setPostFilter(querybuilder).addAggregation(termsBuilder)
                .setExplain(true).execute().actionGet();
        return response;
    }
}
