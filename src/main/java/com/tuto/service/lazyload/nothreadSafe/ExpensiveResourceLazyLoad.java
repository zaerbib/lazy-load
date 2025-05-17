package com.tuto.service.lazyload.nothreadSafe;

import com.tuto.utils.EmbeddedPostgres;

import java.util.List;

import static com.tuto.utils.EmbeddedPostgres.Product;

public class ExpensiveResourceLazyLoad {

    private List<Product> products;
    private static int nbUseOfConnection = 0;

    public ExpensiveResourceLazyLoad() {
        products = null;
    }

    public List<Product> getProducts() {
        if(products == null) {
            EmbeddedPostgres embeddedPostgres = new EmbeddedPostgres();
            embeddedPostgres.start();
            products = embeddedPostgres.findAllProducts();
            nbUseOfConnection++;
        }
        return this.products;
    }

    public void doSomethingElse() {
        // TODO
    }

    public int getNbUseOfConnection() {
        return nbUseOfConnection;
    }
}
