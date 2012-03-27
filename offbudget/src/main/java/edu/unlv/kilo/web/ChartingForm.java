package edu.unlv.kilo.web;

import java.util.Calendar;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

public class ChartingForm 
{
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
	private Calendar startDate;
	
	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(style = "M-")
	private Calendar endDate;
	
	@NotNull
	@Min(1L)
	private int day_Interval;

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
