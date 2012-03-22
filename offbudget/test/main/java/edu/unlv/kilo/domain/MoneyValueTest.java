package domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.unlv.kilo.domain.MoneyValue;

public class MoneyValueTest {

	@Test
	public void centsTest() {
		MoneyValue value = new MoneyValue(123);
		String out = value.toPrintable();
		assertTrue("123 cents doesn't print $1.23 - prints " + out, ("$1.23").compareTo(out) == 0);
	}
	
	@Test
	public void hundredCentsTest() {
		MoneyValue value = new MoneyValue(100);
		String out = value.toPrintable();
		assertTrue("100 cents doesn't print $1.00 - prints " + out, ("$1.00").compareTo(out) == 0);
	}
	
	@Test
	public void zeroCentsTest() {
		MoneyValue value = new MoneyValue(0);
		String out = value.toPrintable();
		assertTrue("0 cents doesn't print $0.00 - prints " + out, ("$0.00").compareTo(out) == 0);
	}
	
	@Test
	public void largeTest() {
		MoneyValue value = new MoneyValue(100000);
		String out = value.toPrintable();
		assertTrue("100000 cents doesn't print $1000.00 - prints " + out, ("$1000.00").compareTo(out) == 0);
	}
	
	@Test
	public void largeNegativeTest() {
		MoneyValue value = new MoneyValue(-100000);
		String out = value.toPrintable();
		assertTrue("-100000 cents doesn't print ($1000.00) - prints " + out, ("($1000.00)").compareTo(out) == 0);
	}

}
