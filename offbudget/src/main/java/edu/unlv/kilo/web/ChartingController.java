/**
 *
 * The MIT License
 *
 * Copyright (c) 2011 the original author or authors.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package edu.unlv.kilo.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.googlecode.charts4j.AxisLabels;
import com.googlecode.charts4j.AxisLabelsFactory;
import com.googlecode.charts4j.AxisStyle;
import com.googlecode.charts4j.AxisTextAlignment;
import com.googlecode.charts4j.Color;
import com.googlecode.charts4j.DataUtil;
import com.googlecode.charts4j.Fills;
import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.Line;
import com.googlecode.charts4j.LineChart;
import com.googlecode.charts4j.LineStyle;
import com.googlecode.charts4j.Plots;
import com.googlecode.charts4j.Shape;

import edu.unlv.kilo.domain.MoneyValue;
import static com.googlecode.charts4j.Color.*;

@RequestMapping("/charting/**")
@Controller
/**
 * This controller gets the user's input, sends it off to Projecting
 * which returns graph data to be plotted. A line chart is then produced
 * and displayed on the screen.
 * @author James
 */
public class ChartingController 
{

    @RequestMapping(method = RequestMethod.POST)
    public String post(@Valid ChartingForm form, BindingResult result, Model model, HttpServletRequest request) 
    {	
       	// Returns the form with errors found from the @ checks
        if(result.hasErrors()) 
        {
        	return createForm(model, form);
        }
        
    	Calendar startDate = form.getStartDate();	// Gets the start date from the user
    	Calendar endDate = form.getEndDate();		// Gets the end date from the user
    	
    	// Have to make sure the begin date is actually before the end date
    	// If not, throw an error
    	if (startDate.after(endDate))
    	{
    		result.rejectValue("startDate", "start_date_after_end");
    		return createForm(model, form);
    		//result.rejectValue("startDate", "start_after_end", "The start date must be before the end date.");
    	}
    	
    	int interval = form.getDay_Interval();		// Gets the interval in days from the user
    	
    	// Send the data to projecting and receive the points to plot
    	// List<MoneyValue> data_Points = Projection.getGraphData(startDate, endDate, interval);
    	
    	
    	/*------------TEST CASE------------------------------------------*/
    	List<MoneyValue> data_Points = new ArrayList<MoneyValue>();
    	data_Points.clear();
    	for (long j = 0; j < 10; j++)
    	{
    		MoneyValue abc = new MoneyValue();
    		abc.setAmount(j);
    		data_Points.add(abc);
    	}
    	/*------------TEST CASE------------------------------------------*/
    	
    	// This sets up the start date and end date to be able
    	// to be printed in a nice default format.
    	SimpleDateFormat nice_String = new SimpleDateFormat("MM/dd/yyyy");
    	String begin_Print = null;
    	String end_Print = null;
    	begin_Print = nice_String.format(startDate.getTime());
    	end_Print = nice_String.format(endDate.getTime());
    	
    	
    	/*-------------GRAPHING STARTS HERE-------------*/
    	int number_of_points = data_Points.size();
    	
    	// Holds the points for graphing.
    	// Library dictates it must be a double
    	double[] points = new double[number_of_points];
    	
    	// These values are set so they're sure to be changed
    	double min_Value = 1000000;	// The minimum Money amount in the list
    	double max_Value = 0;		// The maximum Money amount in the list
    	
    	// Put the Money amounts into the array and check for updated
    	// minimum and maximum values.
    	for (int i = 0; i < number_of_points; i++)
    	{
    		points[i] = (double)data_Points.get(i).getAmount();
    		if (min_Value > points[i])
    		{
    			min_Value = points[i];
    		}
    		if (max_Value < points[i])
    		{
    			max_Value = points[i];
    		}
    	}
    	
    	//Line charting_Line = Plots.newLine(Data.newData(points), RED, "Item");
    	Line charting_Line = Plots.newLine(DataUtil.scaleWithinRange(min_Value, max_Value, points), RED, "Item");
    	charting_Line.setLineStyle(LineStyle.newLineStyle(3, 1, 0));
    	charting_Line.addShapeMarkers(Shape.CIRCLE, Color.AQUAMARINE, 8);
    	
    	// Defining the chart
    	LineChart chart = GCharts.newLineChart(charting_Line);
    	chart.setSize(550, 450);
    	chart.setTitle("Budgeting Line Chart", MAROON, 14);
    	chart.setGrid(number_of_points, number_of_points, 3, 2);
    	
    	// Defining the axis information and styles
    	AxisStyle axisStyle = AxisStyle.newAxisStyle(BLACK, 12, AxisTextAlignment.CENTER);
    	AxisLabels xAxis = AxisLabelsFactory.newAxisLabels(begin_Print, "Dates", end_Print);
    	xAxis.setAxisStyle(axisStyle);
    	AxisLabels yAxis = AxisLabelsFactory.newNumericAxisLabels(min_Value, max_Value);
    	yAxis.setAxisStyle(axisStyle);
    	
    	// Adding axis information to the chart
    	chart.addXAxisLabels(xAxis);
    	chart.addYAxisLabels(yAxis);
    	
    	// Defining the background color
    	chart.setBackgroundFill(Fills.newSolidFill(LIGHTBLUE));
    	
    	// The image url to be sent to the jspx
    	String url = chart.toURLString(); 
        
    	// Setting the "url" variable for the jspx
    	model.addAttribute("url", chart.toURLString()); 
    	
    	return "charting/graph";
    }
    
    // Creates a new form for the user.
    public String createForm(Model model, ChartingForm form) 
    {
    	model.addAttribute("form", form);
    	return "charting/index";
    }

    @ModelAttribute("chartingForm")
    public ChartingForm fromBackingObject() 
    {
    	return new ChartingForm();
    }
    
    // This presents the form to the user upon entering the charting page.
    @RequestMapping(method = RequestMethod.GET)
    public String createForm(Model model) 
    {
    	ChartingForm form = new ChartingForm();
    	model.addAttribute("chartData", form);
        return "charting/index";
    }
}
