package com.black.web.base.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.springframework.context.ApplicationContext;

public class CommonUtil {
    public final static String CH_ZN_TIME = "yyyy-MM-dd HH:mm:ss";
    
    private static ApplicationContext applicationContext = null;
    

    public static String curDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(CH_ZN_TIME);
        return sdf.format(new Date());
    }

    public static Date parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
        Date result = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        return result;
    }

	public static boolean notNull(String code) {
		if(code!=null && !"".equals(code))
			return true;
		return false;
	}
	
	public static boolean listNotNull(List<?> list) {
		if(list != null && list.size() > 0) {
			return true;
		}  else {
			return false;
		}
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static void setApplicationContext(ApplicationContext applicationContext) {
		CommonUtil.applicationContext = applicationContext;
	}
}
