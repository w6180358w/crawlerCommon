package com.black.web.services.sync.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.black.web.Logger.Logger;
import com.black.web.base.dao.BaseDao;
import com.black.web.base.utils.CommonUtil;
import com.black.web.bean.BaseEntity;
import com.black.web.bean.LiknedinFollowGroupEntity;
import com.black.web.bean.LinkedinAchievementEntity;
import com.black.web.bean.LinkedinCompanyEntity;
import com.black.web.bean.LinkedinEntity;
import com.black.web.bean.LinkedinFollowEntity;
import com.black.web.bean.LinkedinFriendEntity;
import com.black.web.bean.LinkedinRecommendEntity;
import com.black.web.bean.LinkedinRecommendGroupEntity;
import com.black.web.bean.LinkedinSchoolEntity;
import com.black.web.bean.LinkedinSkillEntity;
import com.black.web.bean.LinkedinSkillGroupEntity;
import com.black.web.bean.LinkedinjobEntity;
import com.black.web.bean.ThreadBean;
import com.google.gson.Gson;

public class LinkedinSyncServiceImpl extends BaseSyncServiceImpl{

	@Override
	public String getType() {
		return "领英";
	}

	@Override
	protected void doSync(WebDriver driver, List<BaseEntity> data, String key, Integer count) throws Exception {
		LinkedinEntity user = new LinkedinEntity();
		data.add(user);
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		WebElement unEl = driver.findElement(By.xpath("//*[@id=\"session_key-login\"]"));
		WebElement pwdEl = driver.findElement(By.xpath("//*[@id=\"session_password-login\"]"));
		//登陆
		String username = this.getProperty("linkedin.username");
		unEl.click();
		unEl.sendKeys(username);
		Thread.sleep(1000);
		String password = this.getProperty("linkedin.password");
		pwdEl.click();
		pwdEl.sendKeys(password);
		Thread.sleep(200);
		pwdEl.sendKeys(Keys.ENTER);
		
		//搜索
		driver.navigate().to("https://www.linkedin.com/in/"+key);
		
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		//设置基础信息
		setDetail(driver, user);
		//设置公司
		setCompany(driver, user);
		//设置院校
		setSchool(driver, user);
		//设置技能认可
		setSkill(driver, user);
		//设置推荐信
		setCommend(driver, user);
		//成就信息
		setAchievement(driver, user);
		//设置关注信息
		setFollow(driver, user);
	}
	/**
	 * 设置基础信息
	 * @param driver
	 * @param user
	 * @throws Exception
	 */
	private void setDetail(WebDriver driver,LinkedinEntity user) throws Exception {
		WebElement userView = driver.findElement(By.cssSelector("div.mt4.display-flex.ember-view"));
		//姓名
		setProperty("userName", ".pv-top-card-section__name.inline.t-24.t-black.t-normal", userView, user);
		//简介
		setProperty("desc", ".pv-top-card-section__headline.mt1.t-18.t-black.t-normal", userView, user);
		//地区
		setProperty("address", ".pv-top-card-section__location.t-16.t-black--light.t-normal.mt1.inline-block", userView, user);
		//现单位
		setProperty("location", ".pv-top-card-v2-section__entity-name.pv-top-card-v2-section__company-name.text-align-left.ml2.t-14.t-black.t-bold.lt-line-clamp.lt-line-clamp--multi-line.ember-view", userView, user);
		//最高学历
		setProperty("education", ".pv-top-card-v2-section__entity-name.pv-top-card-v2-section__school-name.text-align-left.ml2.t-14.t-black.t-bold.lt-line-clamp.lt-line-clamp--multi-line.ember-view", userView, user);
		//好友数
		setProperty("friendStr", ".pv-top-card-v2-section__entity-name.pv-top-card-v2-section__connections.ml2.t-14.t-black.t-normal", userView, user);
		
		
		//点击联系方式
		userView.findElement(By.cssSelector(".pv-top-card-v2-section__entity-name.pv-top-card-v2-section__contact-info.ml2.t-14.t-black.t-bold")).click();
		
		WebElement modal = driver.findElement(By.tagName("artdeco-modal"));
		List<WebElement> contactsList = modal.findElement(By.tagName("artdeco-modal-content")).findElements(By.tagName("a"));
		//所有联系方式
		List<String> contacts = contactsList.stream().map(con->con.getAttribute("href")).collect(Collectors.toList());
		
		modal.findElement(By.className("artdeco-dismiss")).click();
		
		scrollTo(driver,500);
		
		//文章和动态的关注量 
		setProperty("callFollow", ".align-self-center.pv-recent-activity-section__follower-count ", driver, user);
		user.setContacts(contacts);
		
	}
	/**
	 * 设置工作和教育经历
	 * @param driver
	 * @param user
	 * @throws Exception
	 */
	private void setCompany(WebDriver driver,LinkedinEntity user) throws Exception {
		List<WebElement> views = new ArrayList<>();
		List<LinkedinCompanyEntity> companys = new ArrayList<>();
		try {
			scrollTo(driver,"oc-background-section",null);
			//获取所有公司节点
			views = driver.findElements(By.cssSelector("li.pv-profile-section__card-item-v2.pv-profile-section.pv-position-entity.ember-view"));
			views.forEach(view->{
				LinkedinCompanyEntity company = new LinkedinCompanyEntity();
				List<LinkedinjobEntity> jobs = new ArrayList<>();
				try {
					
					//通过节点报错判断是公司下挂岗位还是公司详情  两种html模板
					//岗位职责的li集合  如果能查询到 说明是公司下挂岗位的模板
					view.findElement(By.cssSelector("ul.pv-entity__position-group.mt2.ember-view"));
					List<WebElement> lis = view.findElements(By.cssSelector("li.pv-entity__position-group-role-item"));
					Logger.info("公司信息，下挂岗位模板");
					//公司名称
					setProperty("name", "h3.t-16.t-black.t-bold > span:nth-child(2)", view, company);
					//在职时长
					setProperty("duration", "h4.t-14.t-black.t-normal > span:nth-child(2)", view, company);
					//循环岗位
					lis.forEach(li->{
						LinkedinjobEntity job = new LinkedinjobEntity();
						//岗位名称
						setProperty("name", "h3.t-14.t-black.t-bold > span:nth-child(2)", li, job);
						//职位开始时间
						setProperty("entry", "h4.pv-entity__date-range.t-14.t-black.t-normal > span:nth-child(2)", li, job);
						//在职时长
						setProperty("duration", "span.pv-entity__bullet-item-v2", li, job);
						//根据报错判断是否有详细岗位详情  两种不同html模板
						try {
							//获取
							WebElement detailView = li.findElement(By.cssSelector("div.pv-entity__extra-details.ember-view"));
							//点击更多按钮
							try {
								detailView.findElement(By.className("lt-line-clamp__more")).click();
							} catch (Exception e) {
								Logger.info("岗位详情没有更多按钮");
							}
							String detail = detailView.findElement(By.tagName("p")).getText();
							if(detail.indexOf("收起")>0) {
								detail = detail.replaceAll("收起", "");
							}
							job.setDetail(detail);
						} catch (Exception e) {
							setProperty("detail", "h4.pv-entity__location.t-14.t-black--light.t-normal.block > span:nth-child(2)", li, job);
						}
						jobs.add(job);
					});
					company.setJobs(jobs);
					
				} catch (Exception e) {
					Logger.info("公司信息，没有岗位");
					//点击更多
					try {
						view.findElement(By.cssSelector("a.lt-line-clamp__more")).click();
					} catch (Exception e1) {
						Logger.error("点击公司信息更多失败!");
					}
					//没有岗位的html模板解析
					setProperty("name", "h4.t-16.t-black.t-normal > span:nth-child(2)", view, company);
					setProperty("entry", "h4.pv-entity__date-range.t-14.t-black--light.t-normal > span:nth-child(2)", view, company);
					setProperty("duration", "span.pv-entity__bullet-item-v2", view, company);
					setProperty("address", "h4.pv-entity__location.t-14.t-black--light.t-normal.block > span:nth-child(2)", view, company);
					
					StringBuffer detail = new StringBuffer();
					try {
						List<WebElement> details = view.findElement(By.cssSelector("div.pv-entity__extra-details")).findElements(By.cssSelector("span.lt-line-clamp__line"));
						details.forEach(det->{
							String text = det.getText();
							if(text.indexOf("收起")>0) {
								text = text.replaceAll("收起", "");
							}
							detail.append(text);
							detail.append("\n");
						});
					} catch (Exception e1) {
						Logger.error("获取公司详情失败!");
					}
					company.setDetail(detail.toString());
				}
				companys.add(company);
			});
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("公司信息未找到，不设置!");
		}
		
		user.setCompanys(companys);
	}
	/**
	 * 设置学校
	 * @param driver
	 * @param user
	 * @throws Exception
	 */
	private void setSchool(WebDriver driver,LinkedinEntity user) throws Exception {
		List<LinkedinSchoolEntity> schools = new ArrayList<>();
		try {
			scrollTo(driver,"education-section",null);
			//取前两位院校
			List<WebElement> views = driver.findElement(By.id("education-section")).findElements(By.tagName("li"));
			views.forEach(schoolData->{
				if(schools.size()<2) {
					LinkedinSchoolEntity school = new LinkedinSchoolEntity();
					setProperty("name", "h3.pv-entity__school-name.t-16.t-black.t-bold", schoolData, school);
					setProperty("degree", "span.pv-entity__comma-item", schoolData, school);
					setProperty("duration", "p.pv-entity__dates.t-14.t-black--light.t-normal > span:nth-child(2)", schoolData, school);
					schools.add(school);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("学校信息未找到!");
		}
		user.setSchools(schools);
		
		//设置滚动条到技能认可
		try {
			WebElement gzjyView = driver.findElement(By.cssSelector("section.pv-profile-section.pv-profile-section--reorder-enabled.background-section.artdeco-container-card.ember-view"));
			scrollTo(driver,gzjyView.getAttribute("id"),"after");
		} catch (Exception e) {
			Logger.info("设置滚动条到技能认可失败!");
		}
	}
	/**
	 * 设置认技能认可信息
	 * @param driver
	 * @param user
	 * @throws Exception
	 */
	private void setSkill(WebDriver driver,LinkedinEntity user) throws Exception {
		List<LinkedinSkillGroupEntity> skillGroups = new ArrayList<>();
		try {
			WebElement skillView = driver.findElement(By.cssSelector("section.pv-profile-section.pv-skill-categories-section.artdeco-container-card.ember-view"));
			
			try {
				skillView.findElement(By.cssSelector("button.pv-profile-section__card-action-bar.pv-skills-section__additional-skills.artdeco-container-card-action-bar")).click();
			} catch (Exception e) {
				Logger.info("技能认可更多按钮未找到!");
			}
			//默认 技能认可的分组
			LinkedinSkillGroupEntity top = new LinkedinSkillGroupEntity();
			setProperty("name", "h2.pv-profile-section__card-heading.t-20.t-black.t-normal", skillView, top);
			setSkillList(skillView.findElement(By.cssSelector("ol.pv-skill-categories-section__top-skills.pv-profile-section__section-info.section-info.pb4")), top,true,driver);
			skillGroups.add(top);
			//其他分组
			List<WebElement> otherGroupViews = skillView.findElements(By.cssSelector("div.pv-skill-category-list.pv-profile-section__section-info.mb6.ember-view"));
			for (WebElement other : otherGroupViews) {
				LinkedinSkillGroupEntity group = new LinkedinSkillGroupEntity();
				setProperty("name", "h3.pb2.t-16.t-black--light.t-normal.pv-skill-categories-section__secondary-skill-heading", other, group);
				setSkillList(other.findElement(By.cssSelector("ol.pv-skill-category-list__skills_list.list-style-none")), group,false,driver);
				skillGroups.add(group);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.error("技能信息未找到!");
		}
		user.setSkillGroups(skillGroups);
	}
	private void setSkillList(WebElement ol,LinkedinSkillGroupEntity group,Boolean top,WebDriver driver) {
		try {
			List<LinkedinSkillEntity> topSkills = new ArrayList<>();
			//默认 技能认可分组的技能认可li
			List<WebElement> list = null;
			if(top) {
				list = ol.findElements(By.cssSelector("li.pv-skill-category-entity__top-skill.pv-skill-category-entity.pb3.pt4.pv-skill-endorsedSkill-entity.relative.ember-view"));
			}else {
				list = ol.findElements(By.cssSelector("li.pv-skill-category-entity.pv-skill-category-entity--secondary.pt4.pv-skill-endorsedSkill-entity.relative.ember-view"));
			}
			for (WebElement sv : list) {
				LinkedinSkillEntity skill = new LinkedinSkillEntity();
				//技能名称
				setProperty("name", "span.pv-skill-category-entity__name-text.t-16.t-black.t-bold", sv, skill);
				//认可数量
				setProperty("count", "span.pv-skill-category-entity__endorsement-count.t-14.t-black--light.t-normal", sv, skill);
				if(top) {
					//认可数量字符串
					setProperty("countStr", "a.pv-skill-entity__highlight-link.t-14.t-black--light.t-normal.ember-view", sv, skill);
				}
				//添加认可技能人
				setAccessUser(skill, sv, driver);
				
				topSkills.add(skill);
			}
			group.setSkills(topSkills);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("技能【{}】的列表不存在!",group.getName());
		}
	}
	/**
	 * 添加认可人
	 * @param skill	技能
	 * @param li	技能视图对象
	 * @param driver	全局视图对象
	 */
	private void setAccessUser(LinkedinSkillEntity skill,WebElement li,WebDriver driver) {
		try {
			List<LinkedinFriendEntity> accesss = new ArrayList<>();
			//点击认可人 弹出窗口
			WebElement a = li.findElement(By.cssSelector("a.display-block.full-width.ember-view"));
			//滚动全局滚动条到技能标签
			scrollTo(driver, a.getAttribute("id"),null);
			a.click();
			WebElement modalView = driver.findElement(By.id("artdeco-modal-outlet"));
			
			WebElement div = modalView.findElement(By.tagName("artdeco-modal-content")).findElement(By.cssSelector("div.pv-profile-detail__content.ph4.ember-view"));
			
			//循环滚动到末尾  加载全部数据
			List<WebElement> lis =null;
			Integer i = 0,lisCount = 0;
			while(true) {
				lis = div.findElements(By.cssSelector("li.pv-endorsement-entity.pv3.ember-view"));
				Logger.info("采集认可人窗口数据，技能名称{}，滚动条滚动次数:{}",skill.getName(),i++);
				if(lisCount == lis.size()) {
					Logger.info("采集认可人窗口数据加载完毕,技能名称{}，退出循环,共{}条数据",skill.getName(),lis.size());
					break;
				}
				lisCount = lis.size();
				scrollTo(driver, div.getAttribute("id"));
			}
			//循环添加认可人
			for (WebElement acc : lis) {
				LinkedinFriendEntity frinds = new LinkedinFriendEntity();
				setProperty("name", "span.pv-endorsement-entity__name--has-hover", acc, frinds);
				setProperty("detail", "div.pv-endorsement-entity__headline.t-14.t-black--light.t-normal", acc, frinds);
				try {
					frinds.setIcon(acc.findElement(By.tagName("img")).getAttribute("src"));
				} catch (Exception e) {
					Logger.info("认可人[{}]设置头像失败!",frinds.getName());
				}
				accesss.add(frinds);
			}
			
			skill.setAccepts(accesss);
			modalView.findElement(By.cssSelector("button.artdeco-dismiss")).click();
		} catch (Exception e) {
			Logger.info("添加认可人出错!",e);
			e.printStackTrace();
		}
	}
	/**
	 * 添加推荐信信息
	 * @param driver
	 * @param user
	 */
	private void setCommend(WebDriver driver,LinkedinEntity user) {
		List<LinkedinRecommendGroupEntity> group = new ArrayList<>();
		LinkedinRecommendGroupEntity in = new LinkedinRecommendGroupEntity();
		LinkedinRecommendGroupEntity out = new LinkedinRecommendGroupEntity();
		
		try {
			WebElement content = driver.findElement(By.cssSelector("section.pv-profile-section.pv-recommendations-section.artdeco-container-card.ember-view"));
			//已收到和已发出
			List<WebElement> tabList = content.findElements(By.tagName("artdeco-tab"));
			//循环两个tab
			for (WebElement tab : tabList) {
				//推荐人的集合
				List<LinkedinRecommendEntity> dataList = new ArrayList<>();
				//滚动到顶部
				scrollTo(driver, content.getAttribute("id"),null);
				//点击推荐人 使对应tab面板显示出来
				tab.click();
				//tab面板
				WebElement tabPanel = content.findElement(By.cssSelector("artdeco-tabpanel[aria-labelledby='"+tab.getAttribute("id")+"']"));
				
				//更多按钮在数据加载完后会销毁  这里通过报错判断是否加载完成
				while(true) {
					try {
						//更多按钮  循环加载数据
						tabPanel.findElement(By.cssSelector("button.pv-profile-section__see-more-inline.pv-profile-section__text-truncate-toggle.link")).click();
					} catch (Exception e) {
						Logger.info("推荐信展开按钮不存在,推荐人信息已全部加载完");
						break;
					}
				}
				//推荐人的节点集合
				List<WebElement> lis = tabPanel.findElements(By.cssSelector("li.pv-recommendation-entity.ember-view"));
				//循环采集数据
				for (WebElement com : lis) {
					LinkedinRecommendEntity bean = new LinkedinRecommendEntity();
					setProperty("name", "h3.t-16.t-black.t-bold", com, bean);
					setProperty("detail", "p.pv-recommendation-entity__headline.t-14.t-black.t-normal.pb1", com, bean);
					setProperty("relationship", "p.t-12.t-black--light.t-normal", com, bean);
					setProperty("content", "blockquote.pv-recommendation-entity__text.relative", com, bean);
					String time = "";
					try {
						if(StringUtils.isNotEmpty(bean.getRelationship())) {
							time = bean.getRelationship().split("\\(")[1].replaceAll("\\)", "");
						}
					} catch (Exception e) {
						Logger.info("推荐信与本人关系时间截取错误!");
					}
					bean.setTime(time);
					dataList.add(bean);
				}
				
				String type = tab.getText();
				if(type.indexOf("已收到")>-1) {
					in.setType(type);
					in.setUsers(dataList);
				}else {
					out.setType(type);
					out.setUsers(dataList);
				}
			}
		
		} catch (Exception e) {
			Logger.info("推荐信信息不存在!");
			e.printStackTrace();
		}
		group.add(in);
		group.add(out);
		user.setTjGroup(group);
	}
	/**
	 * 添加成就信息
	 * @param driver
	 * @param user
	 */
	private void setAchievement(WebDriver driver,LinkedinEntity user) {
		List<LinkedinAchievementEntity> list = new ArrayList<>();
		try {
			WebElement content = driver.findElement(By.cssSelector("section.pv-profile-section.pv-accomplishments-section.artdeco-container-card.ember-view"));
			scrollTo(driver, content.getAttribute("id"),null);
			List<WebElement> views = content.findElements(By.cssSelector("section.accordion-panel.pv-profile-section.pv-accomplishments-block"));
			views.forEach(achs->{
				LinkedinAchievementEntity ach = new LinkedinAchievementEntity();
				setProperty("count", "h3.pv-accomplishments-block__count.t-32.t-black.t-normal.pr3 > span:nth-child(2)", achs, ach);
				setProperty("name", "h3.pv-accomplishments-block__title", achs, ach);
				setProperty("content", "ul.pv-accomplishments-block__summary-list.t-14.t-black--light.t-normal", achs, ach);
				list.add(ach);
			});
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("成就信息不存在!");
		}
		user.setAchievements(list);
	}
	/**
	 * 添加关注信息
	 * @param driver
	 * @param user
	 */
	private void setFollow(WebDriver driver,LinkedinEntity user) {
		List<LiknedinFollowGroupEntity> groups = new ArrayList<>();
		try {
			WebElement content = driver.findElement(By.cssSelector("section.pv-profile-section.pv-interests-section.artdeco-container-card.ember-view"));
			//利用报错来判断是否有查看全部
			try {
				//获取弹窗标签  滚动到标签坐标
				WebElement a = content.findElement(By.cssSelector("a.pv-profile-section__card-action-bar.artdeco-container-card-action-bar.ember-view"));
				scrollTo(driver, a.getAttribute("id"),null);
				//点击标签 弹出窗口
				a.click();
				
				try {
					//获取弹窗头 弹窗内容  标签列表
					WebElement header = null;
					Logger.info("开始循环获取关注信息窗口");
					Integer num = 0;
					while(header==null) {
						try {
							header = driver.findElement(By.cssSelector("artdeco-modal-header.ember-view"));
						} catch (Exception e) {
							Logger.info("查询关注信息窗口错误,当前次数：{}",num++);
							if(num==10) {
								Logger.info("查询关注信息窗口错误已到10次，推出采集关注信息",num);
								throw new Exception();
							}
						}
					}
					Logger.info("获取关注信息窗口成功,退出循环!");
					
					List<WebElement> tagList = header.findElements(By.cssSelector("a.pv-profile-detail__nav-link"));
					//循环标签列表
					tagList.forEach(tagEl->{
						//对应类别的关注组对象
						LiknedinFollowGroupEntity group = new LiknedinFollowGroupEntity();
						group.setType(tagEl.getText());
						//关注对象集合
						List<LinkedinFollowEntity> datas = new ArrayList<>();
						//当前tab面板里的数据循环次数  已有数据次数
						Integer count = 0,size=0;
						
						try {
							//滚动到标签的位置  防止第一个标签循环滚动到低点后点击第二个标签失效
							scrollTo(driver, tagEl.getAttribute("id"));
							//点击标签 刷新内容面板
							tagEl.click();
							
							List<WebElement> dataList = null;
							//循环当前tab面板里的数据
							while(true) {
								Logger.info("关注列表窗口数据加载，当前第{}次",count++);
								//数据集合的视图 带滚动条
								//必须放在循环里  不然会有节点刷新后  节点失效的bug
								WebElement dataView = driver.findElement(By.cssSelector("artdeco-modal-content.pv-profile-detail__modal-content.ember-view")).findElement(By.cssSelector("div.entity-all.pv-interests-list.ember-view"));
								String id = dataView.getAttribute("id");
								dataList = dataView.findElements(By.cssSelector("li.entity-list-item"));
								//当滚动条滚动后  如果数据不加载  当前数据和上一次数据的数量是相同的  表示数据加载完毕
								if(size == dataList.size()) {
									Logger.info("关注列表窗口数据加载完毕，关闭窗口，供{}条数据",dataList.size());
									size = dataList.size();
									break;
								}
								size = dataList.size();
								//滚动到最底部
								scrollTo(driver, id);
							}
							
							for (WebElement fold : dataList) {
								LinkedinFollowEntity follow = new LinkedinFollowEntity();
								setProperty("name", "span.pv-entity__summary-title-text", fold, follow);
								setProperty("count", "p.pv-entity__follower-count.t-14.t-black--light.t-normal", fold, follow);
								datas.add(follow);
							}
						} catch (Exception e) {
							Logger.info("关注类型为{}的关注列表获取失败!",group.getType());
							e.printStackTrace();
						}
						group.setCount(size+"");//设置总数量
						group.setFollows(datas);//设置数据集合
						groups.add(group);
					});
				} catch (Exception e) {
					Logger.info("获取弹窗关注数据失败!");
					e.printStackTrace();
				}
				
			} catch (Exception e) {
				Logger.info("关注信息，查看按钮不存在，采集页面上的关注信息!");
				List<WebElement> lis = content.findElements(By.cssSelector("li.pv-interest-entity.pv-profile-section__card-item.ember-view"));
				LiknedinFollowGroupEntity group = new LiknedinFollowGroupEntity();
				group.setType("公司");
				List<LinkedinFollowEntity> follows = new ArrayList<>();
				lis.forEach(folView->{
					LinkedinFollowEntity fol = new LinkedinFollowEntity();
					setProperty("name", "span.pv-entity__summary-title-text", folView, fol);
					setProperty("count", "p.pv-entity__follower-count t-14.t-black--light.t-normal", folView, fol);
					follows.add(fol);
				});
				group.setFollows(follows);
				groups.add(group);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Logger.info("关注信息不存在!");
		}
		
		user.setFollowGroups(groups);
	}
	
	private void setProperty(String col,String css,WebElement driver,Object user){
		String value = "";
		try {
			value = driver.findElement(By.cssSelector(css)).getText();
		} catch (Exception e) {
			Logger.info("{}节点不存在，设置为空",col);
		}
		setValue(col, user, value);
	}
	
	private void setProperty(String col,String css,WebDriver driver,Object user){
		String value = "";
		try {
			value = driver.findElement(By.cssSelector(css)).getText();
		} catch (Exception e) {
			Logger.info("{}节点不存在，设置为空",col);
		}
		setValue(col, user, value);
	}
	
	private void setValue(String col,Object user,Object value) {
		try {
			Field field = user.getClass().getDeclaredField(col);
			field.setAccessible(true);
			field.set(user, value);
			field.setAccessible(false);
		} catch (Exception e) {
			Logger.error("设置值出错!");
		}
	}
	//根据tag和ID滚动全局滚动调到ID对应节点的节点头或末尾
	private void scrollTo(WebDriver driver,String id,String tag) {
		StringBuffer js = new StringBuffer("function getOffsetTopByBody (el) {        "+
				"	  var offsetTop = 0;                    "+
				"	  while (el && el.tagName !== 'BODY') {"+
				"	    offsetTop += el.offsetTop;          "+
				"	    el = el.offsetParent;               "+
				"	  }                                    ");
		if("after".equals(tag)) {
			js.append("	window.scrollTo(0,offsetTop+document.getElementById('"+id+"').offsetHeight);}  getOffsetTopByBody (document.getElementById('"+id+"'));    ");
		}else {
			js.append("	window.scrollTo(0,offsetTop-100);}  getOffsetTopByBody (document.getElementById('"+id+"'));    ");
		}
		
		((JavascriptExecutor) driver).executeScript(js.toString());
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//根据所传ID  滚动ID所对应节点的滚动条到节点末尾
	private void scrollTo(WebDriver driver,String id) {
		((JavascriptExecutor) driver).executeScript("document.getElementById('"+id+"').scrollTo(0,document.getElementById('"+id+"').scrollHeight)");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//滚动全局滚动条到指定高度
	private void scrollTo(WebDriver driver,Integer top) {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0,"+top+")");
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void handleData(ThreadBean bean) throws Exception {
		CommonUtil.getApplicationContext().getBean(BaseDao.class).save(bean.getData().get(0));
		System.out.println(new Gson().toJson(bean.getData().get(0)));
	}

	@Override
	protected String getUrl() {
		return "https://www.linkedin.com/uas/login";
	}
}
