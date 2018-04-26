package com.github.wuxudong.esdemo;

import org.springframework.context.annotation.Bean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;

@org.springframework.context.annotation.Configuration
@EnableElasticsearchRepositories
public class Configuration {
}
