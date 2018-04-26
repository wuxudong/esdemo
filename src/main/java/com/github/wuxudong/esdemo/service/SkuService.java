package com.github.wuxudong.esdemo.service;

import com.github.wuxudong.esdemo.model.Sku;
import com.github.wuxudong.esdemo.repository.SkuRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
public class SkuService {
    @Autowired
    private SkuRepository skuRepository;

    Logger logger = Logger.getLogger(getClass().getName());

    public void index(Sku sku) {
        skuRepository.save(sku);
    }

    public Page<Sku> search(String query, Pageable pageable) {
//        FunctionScoreQueryBuilder.FilterFunctionBuilder title = new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("title", query), ScoreFunctionBuilders.weightFactorFunction(1));
//        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{title});
//        FunctionScoreQueryBuilder.FilterFunctionBuilder detail = new FunctionScoreQueryBuilder.FilterFunctionBuilder(QueryBuilders.matchQuery("detail", query), ScoreFunctionBuilders.weightFactorFunction(100));


        // Function Score Query
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(QueryBuilders.matchQuery("title", query));

        // 创建搜索 DSL 查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();

        logger.info(functionScoreQueryBuilder.toString());

        return skuRepository.search(searchQuery);

    }

    public Page<Sku> plainSearch(String query, Pageable pageable) {
        // 创建搜索 DSL 查询
        MatchQueryBuilder title = QueryBuilders.matchQuery("title", query);
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withPageable(pageable)
                .withQuery(title).build();

        logger.info(title.toString());

        return skuRepository.search(searchQuery);

    }

}
