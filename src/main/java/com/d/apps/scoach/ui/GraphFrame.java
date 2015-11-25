package com.d.apps.scoach.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.Utilities.DataSumType;
import com.d.apps.scoach.db.model.CoachGraph;
import com.d.apps.scoach.db.model.CoachInstance;
import com.d.apps.scoach.db.model.Counter;

public class GraphFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private CoachGraph graph;
	private DataSumType sumType =  DataSumType.DAY;
	
	private JFreeChart chart;
	
	public GraphFrame(CoachGraph graph) {
		super();
		
		this.graph = graph;
		chart = ChartFactory.createXYLineChart(
				graph.getName(),
                "Time - "+sumType.name(),
                "Counter",
                createDataset(),
                PlotOrientation.VERTICAL,
                true,
                true,
                false
                );
		
		XYPlot plot = chart.getXYPlot();
		NumberAxis axis = (NumberAxis) plot.getDomainAxis();
		axis.setTickUnit(new NumberTickUnit(1));
		ChartFrame frame = new ChartFrame("Results", chart);
		
		DateAxis axis2 = new DateAxis(sumType.name());
		
		plot.setDomainAxis(axis2);
		frame.pack();
		frame.setVisible(true);
	}
	
	private XYDataset createDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();

	    for (Counter counter : graph.getCounters()) {
	    	XYSeries series = new XYSeries(counter.getName());
	    	List<Object[]> moredata = (List<Object[]>)CounterApp.DBServices.getCounterDataSummed(counter.getId(), sumType);
	    	for (Object[] objects : moredata) {
	    		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
	    		Date d;
				try {
					d = f.parse(objects[0].toString());
		    		double milliseconds = d.getTime();
		    		
				    series.add(
				    		milliseconds,
				    		Double.parseDouble(objects[1].toString()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		    dataset.addSeries(series);
	    }
	    
 	   	return dataset;
	}
}