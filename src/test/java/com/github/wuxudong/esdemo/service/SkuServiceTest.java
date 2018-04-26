package com.github.wuxudong.esdemo.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.wuxudong.esdemo.Configuration;
import com.github.wuxudong.esdemo.ESDemoLauncher;
import com.github.wuxudong.esdemo.model.Sku;
import org.apache.commons.lang.time.DateUtils;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStreamReader;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ESDemoLauncher.class, Configuration.class})
public class SkuServiceTest {
    @Autowired
    SkuService skuService;

    @org.junit.Test
    public void index() throws Exception {
        Sku sku = new Sku();
        sku.setId(1l);
        sku.setTitle("Have a nice day, boy");
        sku.setDetail("today is rainy");
        sku.setRank(100);
        sku.setCheckTime(new Date());

        skuService.index(sku);

    }

    @org.junit.Test
    public void initData() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);

        InputStreamReader reader = new InputStreamReader(getClass().getResourceAsStream("/data.json"), "utf-8");
        JsonNode jsonNode = objectMapper.readTree(reader);

        for(JsonNode node: jsonNode.get("data")) {
            Sku sku = new Sku();
            sku.setId(node.get("id").asLong());
            sku.setTitle(node.get("title").asText());
            sku.setDetail(node.get("detail").asText());
            sku.setRank(node.get("rank").asInt());
            if (node.get("check_time").isTextual()) {
                sku.setCheckTime(DateUtils.parseDate(node.get("check_time").asText(), new String[]{"yyyy-MM-dd HH:mm:ss"}));
            }

            skuService.index(sku);
        }
    }

    @org.junit.Test
    public void search() throws Exception {
        Page<Sku> page = skuService.search("耳机", PageRequest.of(0, 5));
        System.out.println(page.getTotalElements());
        System.out.println(page.getContent());
    }

}