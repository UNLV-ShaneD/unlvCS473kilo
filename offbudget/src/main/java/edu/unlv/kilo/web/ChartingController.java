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

import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.googlecode.charts4j.GCharts;
import com.googlecode.charts4j.PieChart;
import com.googlecode.charts4j.Slice;
import static com.googlecode.charts4j.Color.*;

@RequestMapping("/charting/**")
@Controller
public class ChartingController 
{

    @RequestMapping(method = RequestMethod.POST)
    public String post(@Valid ChartingForm form, BindingResult result, Model model, HttpServletRequest request) 
    {
    	Calendar startDate = form.getStartDate();
    	Calendar endDate = form.getEndDate();
    	int interval = form.getDay_Interval();
    	// CALL DANE
    	
    	Slice s1 = Slice.newSlice(interval, YELLOW, "Ms. Pac-Man");         
    	Slice s2 = Slice.newSlice(100 - interval, RED, "Red Lips");          
    	PieChart chart = GCharts.newPieChart(s1, s2);         
    	chart.setTitle("2D Pie Chart", BLACK, 16);         
    	chart.setSize(500, 200);         
    	String url = chart.toURLString(); 
    	
    	//Your really great chart.
        //final Plot plot = Plots.newPlot(Data.newData(0, 66.6, 33.3, 100));
        //final LineChart chart = GCharts.newLineChart(plot);
        //chart.setTitle("My Really Great Chart");
        //Passing the chart to the JSP for rendering.
        model.addAttribute("url", chart.toURLString()); 
    	
    	return "charting/graph";
    }

    @ModelAttribute("chartingForm")
    public ChartingForm fromBackingObject() 
    {
    	return new ChartingForm();
    }
    
    @RequestMapping(method = RequestMethod.GET)
    public String createForm(Model model) 
    {
    	ChartingForm form = new ChartingForm();
    	model.addAttribute("chartData", form);
        return "charting/index";
    }
}
