package com.github.wuxudong.esdemo.service;

import com.github.wuxudong.esdemo.model.Sku;
import com.github.wuxudong.esdemo.repository.SkuRepository;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Component;

import java.util.Date;
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
        FunctionScoreQueryBuilder.FilterFunctionBuilder rankScore = new FunctionScoreQueryBuilder.FilterFunctionBuilder(ScoreFunctionBuilders.fieldValueFactorFunction("rank"));
        FunctionScoreQueryBuilder.FilterFunctionBuilder checkTimeScore = new FunctionScoreQueryBuilder.FilterFunctionBuilder(ScoreFunctionBuilders.gaussDecayFunction("checkTime", new Date().getTime(), 24 * 3600 * 1000));

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("title", query).operator(Operator.AND)).should(QueryBuilders.matchQuery("detail", query).operator(Operator.AND));


        // Function Score Query
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilder
                , new FunctionScoreQueryBuilder.FilterFunctionBuilder[]{rankScore, checkTimeScore}
        );

        // 创建搜索 DSL 查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withIndices("eshop")
                .withPageable(pageable)
                .withQuery(functionScoreQueryBuilder).build();

//        logger.info(functionScoreQueryBuilder.toString());

        return skuRepository.search(searchQuery);

    }


}
