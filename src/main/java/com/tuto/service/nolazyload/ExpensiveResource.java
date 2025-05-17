package com.tuto.service.nolazyload;

import com.tuto.utils.EmbeddedPostgres;

import java.util.List;

import static com.tuto.utils.EmbeddedPostgres.Product;
public class ExpensiveResource {

    private final List<Product> products;
    private static int nbUseOfConnection = 0;

    public ExpensiveResource() {
        EmbeddedPostgres embeddedPostgres = new EmbeddedPostgres();
        embeddedPostgres.start();
        this.products = embeddedPostgres.findAllProducts();
        nbUseOfConnection++;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void doSomethingElse() {
        // TODO
    }

    public int getNbUseOfConnection() {
        return nbUseOfConnection;
    }
}
