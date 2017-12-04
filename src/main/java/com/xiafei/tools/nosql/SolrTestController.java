package com.xiafei.tools.nosql;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * <P>Description: solr测试. </P>
 * <P>CALLED BY:   齐霞飞 </P>
 * <P>UPDATE BY:   齐霞飞 </P>
 * <P>CREATE DATE: 2017/12/2</P>
 * <P>UPDATE DATE: 2017/12/2</P>
 *
 * @author 齐霞飞
 * @version 1.0
 * @since java 1.7.0
 */
@RestController
@RequestMapping("/test")
public class SolrTestController {

    @Resource
    private SolrClient solrClient;

    @RequestMapping("/solr/add")
    public void testSolrAdd() throws IOException, SolrServerException {
        final SolrInputDocument solrInputFields = new SolrInputDocument();
        solrInputFields.setField("id", "1");
        solrInputFields.setField("name", "齐霞飞");
        solrInputFields.setField("password", "qxf");
        solrClient.add(solrInputFields);
        solrClient.commit();
    }


    @RequestMapping("/solr/query")
    public String testSolrQuery() throws IOException, SolrServerException {
        final SolrQuery query = new SolrQuery();
        query.set("q", "id:1");
        return solrClient.query(query).getResults().toString();
    }
}
