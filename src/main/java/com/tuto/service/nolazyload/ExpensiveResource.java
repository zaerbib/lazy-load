package com.tuto.service.nolazyload;

import com.tuto.utils.EmbeddedPostgres;

import java.util.List;

import static com.tuto.utils.EmbeddedPostgres.Product;
public class ExpensiveResource {

    private final List<Product> products;

    public ExpensiveResource() {
        EmbeddedPostgres embeddedPostgres = new EmbeddedPostgres();
        embeddedPostgres.start();
        this.products = embeddedPostgres.findAllProducts();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void doSomethingElse() {
        // TODO
    }
}
