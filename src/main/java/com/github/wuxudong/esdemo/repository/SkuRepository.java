package com.github.wuxudong.esdemo.repository;

import com.github.wuxudong.esdemo.model.Sku;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SkuRepository extends ElasticsearchRepository<Sku,Long> {
}
