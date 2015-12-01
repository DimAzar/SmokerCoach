package com.d.apps.scoach.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.d.apps.scoach.CounterApp;
import com.d.apps.scoach.Utilities.ChartPlotType;
import com.d.apps.scoach.Utilities.CounterFunctionType;
import com.d.apps.scoach.Utilities.DataSumType;
import com.d.apps.scoach.db.model.CoachGraph;
import com.d.apps.scoach.db.model.Counter;

public class GraphFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(GraphFrame.class);
	
	private CoachGraph graph;
	private DataSumType sumType =  DataSumType.DAY;
	
	private JFreeChart chart;
	
	public GraphFrame(CoachGraph graph) {
		super(graph.getTitle());
		this.graph = graph;
		
		chart = createGraph(graph.getPlotType());

		ChartPanel chartPanel = new ChartPanel(chart, false);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        chartPanel.setMouseZoomable(true, false);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setHorizontalAxisTrace(true);
        chartPanel.setVerticalAxisTrace(true);
        
        setContentPane(chartPanel);
		pack();
		RefineryUtilities.centerFrameOnScreen(this);

		setVisible(true);
	}
	
	private XYDataset createXYDataset() {
		XYSeriesCollection dataset = new XYSeriesCollection();

	    for (Counter counter : graph.getCounters()) {
	    	XYSeries series = new XYSeries(counter.getName());
	    	
	    	List<Object[]> moredata = null; 
	    	String dateFormat = "yyyy-MM-dd hh:mm:ss.SSS";
	    	if (counter.getType() == CounterFunctionType.STEP) {
	    		moredata = (List<Object[]>)CounterApp.DBServices.getCounterDataSummed(counter.getId(), sumType);
	    		dateFormat = "yyyy-MM-dd";
	    	} else {
		    	moredata = (List<Object[]>)CounterApp.DBServices.getCounterData(counter.getId(), graph.getXyAxisDataFetch());
	    	}
	    	//INITIAL ZERO VALUE
	    	if (moredata.size() == 1) {
	    		Calendar cldr = Calendar.getInstance();
	    		cldr.set(Calendar.HOUR_OF_DAY, 0);
	    		cldr.set(Calendar.MINUTE, 0);
	    		cldr.set(Calendar.SECOND, 0);
	    		cldr.add(Calendar.DAY_OF_MONTH, -1);
			    series.add(
			    		cldr.getTime().getTime(),
			    		0.0);
	    	}
	    	for (Object[] objects : moredata) {
				try {
				    series.add(
				    		new SimpleDateFormat(dateFormat).parse(objects[1].toString()).getTime(),
				    		Double.parseDouble(objects[0].toString()));
				} catch (ParseException e) {
					LOG.error(e.getMessage());
				}
			}
		    dataset.addSeries(series);
	    }
	    
 	   	return dataset;
	}
	
	private JFreeChart createGraph(ChartPlotType type) {
		JFreeChart ans = null;
		String title  = (graph.getTitle().equals("")) ? graph.getName() : graph.getTitle();
		String xtitle = (graph.getXAxisTitle().equals("")) ? "Time - "+sumType.name() : graph.getXAxisTitle();	
		String ytitle = (graph.getYAxisTitle().equals("")) ? "Counter" : graph.getYAxisTitle();
		
		XYDataset dataset = createXYDataset();
		boolean legend = graph.isShowLegend();
		boolean tooltips = graph.isShowTooltips();
		boolean urls = false;
		PlotOrientation orientation = PlotOrientation.VERTICAL;

		switch (type) {
			case LINE:
				ans = ChartFactory.createXYLineChart(title, xtitle, ytitle, dataset, orientation , legend, tooltips, urls);
				break;
			case SCATTER:
				ans = ChartFactory.createScatterPlot(title, xtitle, ytitle, dataset, orientation, legend, tooltips, urls);
				break;
			default:
				String msg = "UNIMPLEMENTED CHART TYPE :"+type;
				LOG.error(msg);
				throw new RuntimeException(msg);			
		}

		XYPlot plot = ans.getXYPlot();
		DateAxis axis2 = new DateAxis(xtitle);
		axis2.setTickUnit(new DateTickUnit(DateTickUnitType.DAY, 1, new SimpleDateFormat("dd-MM-yy")));
		axis2.setVerticalTickLabels(true);
		plot.setDomainAxis(axis2);
		plot.setDomainCrosshairVisible(true);
        plot.setRangeCrosshairVisible(true);
		
		return ans;
	}
}

/*
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import javax.swing.AbstractAction;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;
import org.jfree.data.xy.XYDataset;

public class ChartPanelDemo {

    private static final String title = "Return On Investment";
    private ChartPanel chartPanel = createChart();

    public ChartPanelDemo() {
        JFrame f = new JFrame(title);
        f.setTitle(title);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout(0, 5));
        f.add(chartPanel, BorderLayout.CENTER);
        chartPanel.setMouseWheelEnabled(true);
        chartPanel.setHorizontalAxisTrace(true);
        chartPanel.setVerticalAxisTrace(true);

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.add(createTrace());
        panel.add(createDate());
        panel.add(createZoom());
        f.add(panel, BorderLayout.SOUTH);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private JComboBox createTrace() {
        final JComboBox trace = new JComboBox();
        final String[] traceCmds = {"Enable Trace", "Disable Trace"};
        trace.setModel(new DefaultComboBoxModel(traceCmds));
        trace.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (traceCmds[0].equals(trace.getSelectedItem())) {
                    chartPanel.setHorizontalAxisTrace(true);
                    chartPanel.setVerticalAxisTrace(true);
                    chartPanel.repaint();
                } else {
                    chartPanel.setHorizontalAxisTrace(false);
                    chartPanel.setVerticalAxisTrace(false);
                    chartPanel.repaint();
                }
            }
        });
        return trace;
    }

    private JComboBox createDate() {
        final JComboBox date = new JComboBox();
        final String[] dateCmds = {"Horizontal Dates", "Vertical Dates"};
        date.setModel(new DefaultComboBoxModel(dateCmds));
        date.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JFreeChart chart = chartPanel.getChart();
                XYPlot plot = (XYPlot) chart.getPlot();
                DateAxis domain = (DateAxis) plot.getDomainAxis();
                if (dateCmds[0].equals(date.getSelectedItem())) {
                    domain.setVerticalTickLabels(false);
                } else {
                    domain.setVerticalTickLabels(true);
                }
            }
        });
        return date;
    }

    private JButton createZoom() {
        final JButton auto = new JButton(new AbstractAction("Auto Zoom") {

            @Override
            public void actionPerformed(ActionEvent e) {
                chartPanel.restoreAutoBounds();
            }
        });
        return auto;
    }

    private ChartPanel createChart() {
        XYDataset roiData = createDataset();
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            title, "Date", "Value", roiData, true, true, false);
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer renderer =
            (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setBaseShapesVisible(true);
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        currency.setMaximumFractionDigits(0);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(currency);
        return new ChartPanel(chart);
    }

    private XYDataset createDataset() {
        TimeSeriesCollection tsc = new TimeSeriesCollection();
        tsc.addSeries(createSeries("Projected", 200));
        tsc.addSeries(createSeries("Actual", 100));
        return tsc;
    }

    private TimeSeries createSeries(String name, double scale) {
        TimeSeries series = new TimeSeries(name);
        for (int i = 0; i < 6; i++) {
            series.add(new Year(2005 + i), Math.pow(2, i) * scale);
        }
        return series;
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                ChartPanelDemo cpd = new ChartPanelDemo();
            }
        });
    }
} 
 */
