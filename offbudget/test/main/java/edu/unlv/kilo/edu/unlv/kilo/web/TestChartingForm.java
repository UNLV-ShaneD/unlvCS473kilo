package edu.unlv.kilo.web;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

import edu.unlv.kilo.web.ChartingForm;

public class TestChartingForm 
{

	ChartingForm test_Form = new ChartingForm();
	
	@Test
	public void testStartDateBeforeEndDate() 
	{
		Calendar start, end;
		start = Calendar.getInstance();
		end = Calendar.getInstance();
		start.set(2012, 3, 6);
		end.set(2012, 4, 6);
		
		test_Form.setStartDate(start);
		test_Form.setEndDate(end);
		assertTrue("Start date is not before end date", test_Form.getStartDate().before(test_Form.getEndDate()));
	}
}
