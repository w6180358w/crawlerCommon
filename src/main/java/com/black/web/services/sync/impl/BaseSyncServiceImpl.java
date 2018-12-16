package com.black.web.services.sync.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.black.web.Logger.Logger;
import com.black.web.base.utils.CommonUtil;
import com.black.web.bean.GoodsEntity;
import com.black.web.services.sync.SyncService;

public abstract class BaseSyncServiceImpl implements SyncService{

	protected boolean shutdown;
	
	@Override
	public void sync(List<GoodsEntity> data,String key,Integer count) throws Exception{
		System.setProperty("webdriver.chrome.driver",getProperty("driverPath"));
		String url = getUrl();
		WebDriver driver = null;
    	try {
    		ChromeOptions options = new ChromeOptions();
    		if(getProperty("headless",Boolean.class)) {
    			options.addArguments("headless");
    		}
        	//options.addArguments("--proxy-server=127.0.0.1:8001");
        	options.addArguments("no-sandbox");
        	//options.addArguments("--start-maximized");
        	driver = new ChromeDriver(options);
        	driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        	//获取页面
    		driver.get(url);
    		//注入js 反屏蔽
    		setJs(driver);
    		//采集
			doSync(driver,data, key, count);
		} catch (Exception e) {
			Logger.error("采集失败!",e);
			throw e;
		} finally {
			if(driver!=null) {
				driver.close();
		        driver.quit();
			}
		}
	}
	
	protected abstract void doSync(WebDriver driver, List<GoodsEntity> data,String key,Integer count) throws Exception;
	

	@Override
	public void shutdown() {
		this.shutdown = true;
	}

	protected abstract String getUrl();
	
	protected String getProperty(String key) {
		return CommonUtil.getApplicationContext().getEnvironment().getProperty("collect."+key);
	}
	
	protected <T>T getProperty(String key,Class<T> clazz) {
		return CommonUtil.getApplicationContext().getEnvironment().getProperty("collect."+key,clazz);
	}
	
	protected void setJs(WebDriver driver) {
		((JavascriptExecutor) driver).executeScript(
    			"    Object.defineProperties(navigator,{" + 
    			"        webdriver:{" + 
    			"            get:() => false" + 
    			"        }" + 
    			"    });");
        ((JavascriptExecutor) driver).executeScript("window.navigator.chrome = { runtime: {},  };");
        ((JavascriptExecutor) driver).executeScript(
        		"    Object.defineProperties(navigator,{" + 
        				"        languages:{" + 
        				"            get:() => ['en-US', 'en']" + 
        				"        }" + 
        		"    });");
        ((JavascriptExecutor) driver).executeScript(
        		"    Object.defineProperties(navigator,{" + 
        				"        plugins:{" + 
        				"            get:() => [1, 2, 3, 4, 5,6]" + 
        				"        }" + 
        		"    });");
	}
}
