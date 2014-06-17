package me.joshlarson;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

public class UniverseDateFormat extends DateFormat {
	
	private static final long serialVersionUID = 765473251536038053L;
	
	@Override
	public StringBuffer format(Date date, StringBuffer buf, FieldPosition fieldPosition) {
		long time = date.getTime();
		int milliseconds = (int) (time - (time/1000)*1000);
		time /= 1000;
		int seconds = (int) (time % 60);
		int minutes = (int) ((time-seconds)/60 % 60);
		int hours   = (int) ((time-seconds-minutes*60)/60/60 % 60);
		int days    = (int) ((time-seconds-minutes*60-hours*24)/60/60/24 % 24);
		addZeroes(buf, 2, days);
		buf.append(":");
		addZeroes(buf, 2, hours);
		buf.append(":");
		addZeroes(buf, 2, minutes);
		buf.append(":");
		addZeroes(buf, 2, seconds);
		buf.append(":");
		addZeroes(buf, 3, milliseconds);
		return buf;
	}
	
	@Override
	public Date parse(String source, ParsePosition pos) {
		System.out.println("Date Parse: " + source);
		return null;
	}
	
	private static void addZeroes(StringBuffer buf, int zeroes, long l) {
		if (l <= 0) {
			for (int i = 0; i <= zeroes-1; i++)
				buf.append(l);
		} else {
			int length = (int)(Math.log10(l)+1);
			for (int i = 0; i < zeroes-length; i++)
				buf.append("0");
			buf.append(l);
		}
	}
	
}
