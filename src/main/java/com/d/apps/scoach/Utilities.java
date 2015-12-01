package com.d.apps.scoach;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Properties;

import lombok.Getter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utilities {
	private static final Logger LOG = LoggerFactory.getLogger(Utilities.class);
	
	public static final String NAME_MAINFRAME   = "Main Frame";
	public static final String NAME_PROFILEMANAGER = "Profile Manager";
	public static final String NAME_ABOUTDIALOG = "About Counter";
	
	public static final String NAME_COACHESMANAGER = "Manage Coaches";
	public static final String NAME_PROFILESMANAGER= "Manage Profiles";
	public static final String NAME_COUNTERSMANAGER = "Manage Counters";
	
	public static final String NAME_PROFILECOACHES= "Profile Coaches";
	public static final String NAME_ADDCOUNTER = "Add Counter to Coach";
	public static Properties loadApplicationProperties(String propertiesName) {
		Properties ans = new Properties();
		InputStream input = null;

		try {
			input = CounterApp.class.getClassLoader().getResourceAsStream(propertiesName);
    		if (input==null) {
	            LOG.error("Sorry, unable to find " + input);
    		    return null;
    		}

			ans.load(input);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return ans;
	}

	public static String createDateStringRep() {
		String ans = String.format("%s/%s/%s", 
				Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
				Calendar.getInstance().get(Calendar.MONTH)+1,
				Calendar.getInstance().get(Calendar.YEAR));
		return ans;
	}
	
	public enum CounterFunctionType {
	    STEP(0, "Step function"), INPUT(1, "User-Input function");

	    @Getter
	    private final int id;
	    @Getter
	    private final String description;
	    
	    private CounterFunctionType(int id, String description) {	
	    	this.id = id;	
	    	this.description = description;
    	}
	    
	    public static CounterFunctionType forId(int id) {
	        for (CounterFunctionType func : CounterFunctionType.values()) {
	            if (func.getId() == id) {
	                return func;
	            }
	        }
	        return null;
	    }
	    
	    public static CounterFunctionType getId(String descr) {
	        for (CounterFunctionType func : CounterFunctionType.values()) {
	            if (func.getDescription().equals(descr)) {
	                return func;
	            }
	        }
	        return null;
	    }
	}
	
	public enum DataSumType {
	    DAY("per Day"), 
	    MONTH("per Month");

	    @Getter
	    private final String description;
	    
	    private DataSumType(String description) {	
	    	this.description = description;
    	}
	}	

	public enum CounterDimension {
	    D1("1 Dimension"), 
	    D2("2 Dimensions"),
	    D3("3 Dimensions"),
	    TIME("Time dimension");

	    @Getter
	    private final String description;
	    
	    private CounterDimension(String description) {	
	    	this.description = description;
    	}
	    
	    public static CounterDimension getId(String descr) {
	        for (CounterDimension func : CounterDimension.values()) {
	            if (func.getDescription().equals(descr)) {
	                return func;
	            }
	        }
	        return null;
	    }
	}	

	public enum ChartPlotType {
	    SCATTER("Scatter Chart"), 
	    LINE("Line Chart");

	    @Getter
	    private final String description;
	    
	    private ChartPlotType(String description) {	
	    	this.description = description;
    	}
	    
	    public static ChartPlotType getId(String descr) {
	        for (ChartPlotType func : ChartPlotType.values()) {
	            if (func.getDescription().equals(descr)) {
	                return func;
	            }
	        }
	        return null;
	    }
	}	
	
	public enum GraphDimensions {
	    D2GRAPH("2-D Graph"), 
	    D3GRAPH("3-D Graph");

	    @Getter
	    private final String description;
	    
	    private GraphDimensions(String description) {	
	    	this.description = description;
    	}
	    
	    public static GraphDimensions getId(String descr) {
	        for (GraphDimensions func : GraphDimensions.values()) {
	            if (func.getDescription().equals(descr)) {
	                return func;
	            }
	        }
	        return null;
	    }

	}	

	public enum CounterDimensionCombinations {
	    XT("X and Time"), 
	    XY("X and Y"),
	    XZ("X and Z"),
	    YT("Y and Time"),
	    YZ("Y and Z"),
	    ZT("Z and Time"),
	    
	    XYT("X, Y and Time"),
	    YZT("Y, Z and Time"),
	    XZT("X, Z and Time"),
	    XYZ("X, Z and Y"),
	    
	    FULL("X,Y,Z and Time");

	    @Getter
	    private final String description;
	    
	    private CounterDimensionCombinations(String description) {	
	    	this.description = description;
    	}
	    
	    public static CounterDimensionCombinations getId(String descr) {
	        for (CounterDimensionCombinations func : CounterDimensionCombinations.values()) {
	            if (func.getDescription().equals(descr)) {
	                return func;
	            }
	        }
	        return null;
	    }
	}	
}