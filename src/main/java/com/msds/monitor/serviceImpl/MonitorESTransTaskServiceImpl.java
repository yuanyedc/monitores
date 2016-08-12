package com.msds.monitor.serviceImpl;

import com.msds.monitor.enums.BankgatewayInfoEnum;
import com.msds.monitor.service.MonitorESTransTaskService;
import com.msds.monitor.utils.DateTimeUtils;
import com.msds.monitor.utils.ESUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.indexedscripts.put.PutIndexedScriptResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MonitorESTransTaskServiceImpl implements MonitorESTransTaskService {


    public void monitorProcessingByMinute() {
        long currentTime = System.currentTimeMillis();
        long oneminbefore = currentTime - 60 * 60 * 1000;

        String time = DateTimeUtils.getDateTimeToString(DateTimeUtils.getNowDate(),
                DateTimeUtils.DATE_FORMAT_YYYYPOINTMMPOINTDD);
        String index = "bankgateway-" + time;

        TermsBuilder tranTypeBuilder = AggregationBuilders.terms("tranTypeAgg").field("tranType");
        TermsBuilder channelBuilder = AggregationBuilders.terms("payChannelCodeAgg").field("payChannelCode");
        TermsBuilder bankBuilder = AggregationBuilders.terms("bankAgg").field("bankCode");
        TermsBuilder stateBuilder = AggregationBuilders.terms("stateAgg").field("state");

        tranTypeBuilder.subAggregation(channelBuilder);
        channelBuilder.subAggregation(bankBuilder);
        bankBuilder.subAggregation(stateBuilder);
        QueryBuilder queryTotalbuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.rangeQuery(BankgatewayInfoEnum.ITEM_STATERTIME.item).gt(oneminbefore));

        SearchResponse response = ESUtils.search(index, queryTotalbuilder, tranTypeBuilder);

        Map<String, Aggregation> map = response.getAggregations().asMap();

        StringTerms tranTypeTerms = (StringTerms) map.get("tranTypeAgg");
        List<Bucket> tranTypeList = tranTypeTerms.getBuckets();
        Map<String, Long> statisMap = new HashMap<String, Long>();
        for (Bucket bucket : tranTypeList) {
            log.info("交易类型：" + bucket.getKey().toString() + "=总数：" + bucket.getDocCount());
            statisMap.put(bucket.getKey().toString(), bucket.getDocCount());
            StringTerms channelTerm = (StringTerms) bucket.getAggregations().asMap().get("payChannelCodeAgg");
            List<Bucket> channelList = channelTerm.getBuckets();
            for (Bucket channelBucket : channelList) {
                log.info("渠道：" + channelBucket.getKey().toString() + "=总数：" + channelBucket.getDocCount());
                statisMap.put(channelBucket.getKey().toString(), channelBucket.getDocCount());
                StringTerms bankTerm = (StringTerms) channelBucket.getAggregations().asMap().get("bankAgg");
                List<Bucket> bankList = bankTerm.getBuckets();
                for (Bucket bankBucket : bankList) {
                    String channelBankKey = channelBucket.getKey().toString() + "-" + bankBucket.getKey().toString();
                    statisMap.put(channelBankKey, bankBucket.getDocCount());
                    StringTerms stateTerm = (StringTerms) bankBucket.getAggregations().asMap().get("stateAgg");
                    List<Bucket> stateist = stateTerm.getBuckets();
                    for (Bucket stateBucket : stateist) {
                        log.info(bucket.getKey() + ":" + channelBucket.getKey() + ":" + bankBucket.getKey() + ":"
                                + stateBucket.getKey() + "===" + stateBucket.getDocCount());
                        Map<String,Map<String,Map<String,Map<String,Long>>>> smap = new HashMap<>();

                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();
        log.info("查询耗时" + (endTime - currentTime) + "ms");
    }

    public static void main(String[] args) {
        long currentTime = System.currentTimeMillis();
        long oneminbefore = currentTime - 60 * 60 * 1000;

        Client client = ESUtils.getClient();
        PutIndexedScriptResponse builder = client.preparePutIndexedScript("mustache", "template_gender", "{\"index\":[\"monitor_bankgateway_log_collector-2016.08.12\"],\"search_type\":\"count\",\"ignore_unavailable\":true}\n" +
                "{\"size\":0,\"query\":{\"filtered\":{\"query\":{\"query_string\":{\"query\":\"*\",\"analyze_wildcard\":true}},\"filter\":{\"bool\":{\"must\":[{\"range\":{\"@timestamp\":{\"gte\":1470984022865,\"lte\":1470987622865,\"format\":\"epoch_millis\"}}}],\"must_not\":[]}}}},\"aggs\":{\"2\":{\"terms\":{\"field\":\"payChannelCode.raw\",\"size\":100,\"order\":{\"_count\":\"desc\"}},\"aggs\":{\"3\":{\"terms\":{\"field\":\"payChannelName.raw\",\"size\":100,\"order\":{\"_count\":\"desc\"}},\"aggs\":{\"4\":{\"terms\":{\"field\":\"state.raw\",\"size\":6,\"order\":{\"_count\":\"desc\"}}}}}}}}}").get();
        System.out.print(builder.getContext());
    }
}
