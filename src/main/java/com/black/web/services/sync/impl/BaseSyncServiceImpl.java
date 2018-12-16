package com.black.web.services.sync.impl;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.black.web.Logger.Logger;
import com.black.web.base.service.POIService;
import com.black.web.base.utils.CommonUtil;
import com.black.web.base.utils.mail.Mail;
import com.black.web.bean.BaseEntity;
import com.black.web.bean.ThreadBean;
import com.black.web.services.sync.SyncService;

public abstract class BaseSyncServiceImpl implements SyncService{

	protected boolean shutdown;
	
	@Override
	public void sync(List<BaseEntity> data,String key,Integer count) throws Exception{
		System.setProperty("webdriver.chrome.driver",getProperty("driverPath"));
		String url = getUrl();
		WebDriver driver = null;
    	try {
    		ChromeOptions options = new ChromeOptions();
    		if(getProperty("headless",Boolean.class)) {
    			options.addArguments("--headless");
    			options.addArguments("--disable-gpu");
    		}
        	//options.addArguments("--proxy-server=127.0.0.1:8001");
        	options.addArguments("--no-sandbox");
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
			e.printStackTrace();
			throw e;
		} finally {
			if(driver!=null) {
				driver.close();
		        driver.quit();
			}
		}
	}
	
	protected abstract void doSync(WebDriver driver, List<BaseEntity> data,String key,Integer count) throws Exception;
	

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
	
	@Override
	public void handleData(ThreadBean bean) throws Exception {
		String now = bean.getNow();
		StringBuffer sb = new StringBuffer();
		if(bean.getSuccess()) {
			sb.append("采集成功!\n");
			bean.getData().forEach(d->{
				sb.append(d.toString()).append("\n");
			});
		}else {
			sb.append("采集失败!\n采集开始时间【"+now+"】,任务编码【"+bean.getName()+"】\n请联系管理员查看文档!");
		}
		
		String[] dataFields = new String[]{"name","price","source","detail","url","subjectName"};
		String[] columns = new String[]{"商品名称","价格","来源","商品详情","商品url","厂家名称"};
		
		@SuppressWarnings("unchecked")
		POIService<BaseEntity> poiService = CommonUtil.getApplicationContext().getBean(POIService.class);
		InputStream in = poiService.exportExcelInputStream(bean.getData(), dataFields, columns, "yyyy-MM-dd");
		
		Mail mail = new Mail(this.getProperty("collect.mail.host"),
				this.getProperty("collect.mail.port"),
				this.getProperty("collect.mail.username"),
				this.getProperty("collect.mail.password"));
		mail.setMailFrom("xyzhang@qysoft.cn");
		mail.setMailTo(new String[]{bean.getTarget()}, "to");
		mail.setSubject("采集数据文档!");
		mail.addTextContext(sb.toString());
		mail.addAttachmentFrombyte(in, "采集["+this.getType()+"-"+bean.getS()+"]数据-"+now+".xlsx");
		mail.sendMail();
		Logger.info("-------------采集线程["+bean.getName()+"]发送邮件成功,目标["+bean.getTarget()+"]------------");
	}
}
