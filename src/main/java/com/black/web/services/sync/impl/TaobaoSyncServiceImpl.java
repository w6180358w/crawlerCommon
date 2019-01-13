package com.black.web.services.sync.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.black.web.Logger.Logger;
import com.black.web.base.service.impl.BaseSyncServiceImpl;
import com.black.web.bean.BaseEntity;

public class TaobaoSyncServiceImpl extends BaseSyncServiceImpl<BaseEntity>{

	@Override
	public void doSync(WebDriver driver, List<BaseEntity> data,String key,Integer count) throws Exception {
		//点击登陆页签
		driver.findElement(By.xpath("//*[@id=\"J_LoginBox\"]/div[1]/div[1]")).click();
		//用户名
    	WebElement username = driver.findElement(By.xpath("//*[@id=\"TPL_username_1\"]"));
        username.sendKeys(this.getProperty("taobao.username"));
        Thread.sleep(1000);
        //密码
        WebElement password = driver.findElement(By.xpath("//*[@id=\"TPL_password_1\"]"));
        password.sendKeys(this.getProperty("taobao.password"));
        
        Thread.sleep(1000);
        
    	//移动滑块
        move(driver);
        //登录
        driver.findElement(By.xpath("//*[@id=\"J_SubmitStatic\"]")).click();
        //输入搜索条件
        driver.findElement(By.xpath("//*[@id=\"q\"]")).sendKeys(key);
        Thread.sleep(500);
        //点击搜索按钮
        driver.findElement(By.xpath("//*[@id=\"J_TSearchForm\"]/div[1]/button")).click();
        //获取所有商品节点  解析
        List<WebElement> items = driver.findElement(By.xpath("//*[@id=\"mainsrp-itemlist\"]/div/div/div[1]")).findElements(By.className("item"));
        items.forEach(item->{
        	//如果结束标记未结束  并且当前数据量小于规定数据量
        	if(!shutdown && data.size() < count ) {
        		WebElement ctx = item.findElement(By.className("ctx-box"));
            	String price = ctx.findElement(By.className("price")).getText();
            	WebElement titleTag = ctx.findElement(By.className("title")).findElement(By.tagName("a"));
            	String url = titleTag.getAttribute("href");
            	String title = titleTag.getText();
            	
            	WebElement sub = ctx.findElement(By.className("row-3")).findElement(By.className("shop")).findElement(By.tagName("a"));
            	String subjectName = sub.getText();
            	BaseEntity entity = new BaseEntity();
            	entity.setSource("taobao");
            	entity.setPrice(price);
            	entity.setSubjectName(subjectName);
            	entity.setSubjectUrl(sub.getAttribute("href"));
            	entity.setName(title);
            	entity.setUrl(url);
            	data.add(entity);
        	}
        });
        
        //driver.findElement(By.xpath("//*[@id=\"J_Itemlist_TLink_522997440305\"]")).click();
	}
	
	private void move(WebDriver driver) throws Exception {
		
		doMove(driver);
		
        try {
        	driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
			WebElement refresh = driver.findElement(By.xpath("//*[@id=\"nocaptcha\"]/div/span/a"));
			refresh.click();
			doMove(driver);
		} catch (Exception e) {
			Logger.info("滑块验证成功!");
		}
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        return;
    }

	private void doMove(WebDriver driver) throws Exception {
		WebElement drop = driver.findElement(By.xpath("//*[@id=\"nc_1_n1z\"]"));
        Actions actions = new Actions(driver);
        actions.clickAndHold(drop).perform();
        Thread.sleep(1000);
        actions.moveByOffset(drop.getLocation().getX()+50, drop.getLocation().getY()).perform();
        Thread.sleep(500);
	}
	
	@Override
	protected String getUrl() {
		return "https://login.taobao.com/member/login.jhtml?spm=a21bo.2017.754894437.1.5af911d9bFsMVv&f=top&redirectURL=https%3A%2F%2Fwww.taobao.com%2F";
	}

	@Override
	public String getType() {
		return "淘宝";
	}
}
