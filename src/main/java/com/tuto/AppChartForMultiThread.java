package com.tuto;

import com.tuto.app.multiThread.AppLazyLoadNoThreadSafeMultiThread;
import com.tuto.app.multiThread.AppLazyLoadThreadSafeMultiThread;
import com.tuto.app.multiThread.AppNoLazyLoadMultiThread;
import com.tuto.utils.MemoryUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

public class AppChartForMultiThread extends JFrame {
    public AppChartForMultiThread() throws InterruptedException {
        // MemoryUtils.MemorySnapshot noLazyLoading = AppNoLazyLoadMultiThread.noLazyLoadMultiThreadMemorySnapshot();
        MemoryUtils.MemorySnapshot lazyLoadNoThreadSafe = AppLazyLoadNoThreadSafeMultiThread
                .lazyLoadNoThreadSafeMultiThreadMemorySnapshot();
        MemoryUtils.MemorySnapshot lazyLoadThreadSafe = AppLazyLoadThreadSafeMultiThread
                .lazyLoadThreadSafeMultiThreadMemorySnapshot();


        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // dataset.addValue(noLazyLoading.heapUsed(), "Heap Used", "No Lazy Loading");
        // dataset.addValue(noLazyLoading.timeMillisecond(), "Time Elapsed", "No Lazy Loading");
        dataset.addValue(lazyLoadNoThreadSafe.heapUsed(), "Heap Used", "Lazy Loading No Thread Safe");
        dataset.addValue(lazyLoadNoThreadSafe.timeMillisecond(), "Time Elapsed", "Lazy Loading No Thread Safe");
        dataset.addValue(lazyLoadThreadSafe.heapUsed(), "Heap Used", "Lazy Loading Thread Safe");
        dataset.addValue(lazyLoadThreadSafe.timeMillisecond(), "Time Elapsed", "Lazy Loading Thread Safe");



        JFreeChart chart = ChartFactory.createBarChart(
                "Multi Threaded Lazy Loading Chart",
                "Type",
                "units",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                AppChartForMultiThread example = new AppChartForMultiThread();
                example.setSize(900, 600);
                example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                example.setVisible(true);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
