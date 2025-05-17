package com.tuto;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.statistics.HistogramDataset;

import javax.swing.*;
import java.util.Random;

public class AppChart extends JFrame{
    public AppChart() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        dataset.addValue(35, "Product A", "Q1");
        dataset.addValue(45, "Product A", "Q2");
        dataset.addValue(60, "Product A", "Q3");
        dataset.addValue(30, "Product B", "Q1");
        dataset.addValue(25, "Product B", "Q2");
        dataset.addValue(40, "Product B", "Q3");

        JFreeChart chart = ChartFactory.createBarChart(
                "Quarterly Product Sales",
                "Quarter",
                "Sales (in units)",
                dataset
        );

        ChartPanel panel = new ChartPanel(chart);
        setContentPane(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppChart example = new AppChart();
            example.setSize(800, 600);
            example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            example.setVisible(true);
        });
    }
}
