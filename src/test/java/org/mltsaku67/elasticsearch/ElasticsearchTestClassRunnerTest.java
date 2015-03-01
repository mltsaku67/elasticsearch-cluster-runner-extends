package org.mltsaku67.elasticsearch;

import static org.junit.Assert.*;

import org.codelibs.elasticsearch.runner.ElasticsearchClusterRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mltsaku67.elasticsearch.annotations.ClusterRunnerSetting;
import org.mltsaku67.elasticsearch.annotations.ClusterSetting;

@RunWith(ElasticsearchTestClassRunner.class)
@ClusterRunnerSetting( //
clusterSetting = @ClusterSetting("elasticsearch.yml"), //
indexSettings = {}, //
typeSettings = {})
public class ElasticsearchTestClassRunnerTest {

    private static ElasticsearchClusterRunner runner;

    @BeforeClass
    public static void setup() {
        System.out.println("setup");
    }

    @Before
    public void init() {
        System.out.println("init");
    }

    @After
    public void cleanup() {
        System.out.println("cleanup");
    }

    @AfterClass
    public static void teardown() {
        System.out.println("teardown");
    }

    @Test
    public void test() {
        fail("Not yet implemented");
    }
}
