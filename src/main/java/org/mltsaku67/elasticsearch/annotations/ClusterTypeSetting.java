package org.mltsaku67.elasticsearch.annotations;


public @interface ClusterTypeSetting {

    String index();

    String type();

    String setting();
}
