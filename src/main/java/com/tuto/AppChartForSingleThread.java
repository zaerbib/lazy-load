package com.tuto;

import com.tuto.app.singleThread.AppLazyLoadNoThreadSafeSingleThread;
import com.tuto.app.singleThread.AppLazyLoadThreadSafeSingleThread;
import com.tuto.app.singleThread.AppNoLazyLoad;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.swing.*;

import static com.tuto.utils.MemoryUtils.MemorySnapshot;

public class AppChartForSingleThread extends JFrame{
    public AppChartForSingleThread() {
        // MemorySnapshot noLazyLoading = AppNoLazyLoad.noLazyLoadSingleThreadMemorySnapshot();;
        MemorySnapshot lazyLoadNoThreadSafe = AppLazyLoadNoThreadSafeSingleThread
                .lazyLoadNoThreadSafeSingleThreadMemorySnapshot();
        MemorySnapshot lazyLoadThreadSafe =  AppLazyLoadThreadSafeSingleThread
                .lazyLoadThreadSafeSingleThreadMemorySnapshot();


        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        // dataset.addValue(noLazyLoading.heapUsed(), "Heap Used", "No Lazy Loading");
        // dataset.addValue(noLazyLoading.timeMillisecond(), "Time Elapsed", "No Lazy Loading");
        dataset.addValue(lazyLoadNoThreadSafe.heapUsed(), "Heap Used", "Lazy Loading No Thread Safe");
        dataset.addValue(lazyLoadNoThreadSafe.timeMillisecond(), "Time Elapsed", "Lazy Loading No Thread Safe");
        dataset.addValue(lazyLoadThreadSafe.heapUsed(), "Heap Used", "Lazy Loading Thread Safe");
        dataset.addValue(lazyLoadThreadSafe.timeMillisecond(), "Time Elapsed", "Lazy Loading Thread Safe");



        JFreeChart chart = ChartFactory.createBarChart(
                "Single Threaded Lazy Loading Chart",
                "Type",
                "units",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppChartForSingleThread example = new AppChartForSingleThread();
            example.setSize(900, 600);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
