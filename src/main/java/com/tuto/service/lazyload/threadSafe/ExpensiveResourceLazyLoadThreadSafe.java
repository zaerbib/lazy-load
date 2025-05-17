package com.tuto.service.lazyload.threadSafe;

import com.tuto.utils.EmbeddedPostgres;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class ExpensiveResourceLazyLoadThreadSafe {

    private List<EmbeddedPostgres.Product> products;
    private static int nbUseOfConnection = 0;
    private static ReentrantLock lock = new ReentrantLock();

    public ExpensiveResourceLazyLoadThreadSafe() {
        products = null;
    }

    public List<EmbeddedPostgres.Product> getProducts() {
        lock.lock();
        try {
            if(products == null) {
                EmbeddedPostgres embeddedPostgres = new EmbeddedPostgres();
                embeddedPostgres.start();
                products = embeddedPostgres.findAllProducts();
                nbUseOfConnection++;
            }
            return this.products;
        } finally {
            lock.unlock();
        }
    }

    public void doSomethingElse() {
        // TODO
    }

    public int getNbUseOfConnection() {
        return nbUseOfConnection;
    }
}
