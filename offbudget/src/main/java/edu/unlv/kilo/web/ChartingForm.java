package edu.unlv.kilo.web;

import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * The is the backend of the form the user fills out
 * for charting data.
 * 
 * @author James
 *
 */
public class ChartingForm 
{
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
	private Calendar startDate;		// The starting date to begin the chart
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
	@Future
	private Calendar endDate;		// The ending date to stop charting
	
	@NotNull
	@Min(1)
	private int day_Interval;		// How often points are displayed on the graph
	
	public Calendar getStartDate() 
	{
		return startDate;
	}

	public void setStartDate(Calendar startDate) 
	{
		this.startDate = startDate;
	}

	public Calendar getEndDate() 
	{
		return endDate;
	}

	public void setEndDate(Calendar endDate) 
	{
		this.endDate = endDate;
	}

	public int getDay_Interval() 
	{
		return day_Interval;
	}

	public void setDay_Interval(int day_Interval) 
	{
		this.day_Interval = day_Interval;
	}
}
