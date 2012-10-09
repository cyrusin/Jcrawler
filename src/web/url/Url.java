package web.url;

import java.sql.Timestamp;
import java.util.*;

public class Url {
	//原始URL，主机部分是域名
	private String oriUrl;
	//URL的值，主机部分是IP
	private String url;
	
	private int urlNO;
	
	private int statusCode;
	//URL被其他页面引用的次数
	private int hitNum;
	
	private String charSet;
	
	private String abstractText;
	private String author;
	private int weight;
	private String description;
	private int fileSize;
	//最后更新时间
	private Timestamp lastUpdateTime;
	//过期时间
	private Date timeToLive;
	private String title;
	private Url[] urlReferences;
	private int layer;
	//get,set方法
	public String getOriUrl() {
		return oriUrl;
	}
	public void setOriUrl(String oriUrl) {
		this.oriUrl = oriUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getUrlNO() {
		return urlNO;
	}
	public void setUrlNO(int urlNO) {
		this.urlNO = urlNO;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public int getHitNum() {
		return hitNum;
	}
	public void setHitNum(int hitNum) {
		this.hitNum = hitNum;
	}
	public String getCharSet() {
		return charSet;
	}
	public void setCharSet(String charSet) {
		this.charSet = charSet;
	}
	public String getAbstractText() {
		return abstractText;
	}
	public void setAbstractText(String abstractText) {
		this.abstractText = abstractText;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getFileSize() {
		return fileSize;
	}
	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}
	public Timestamp getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Timestamp lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public Date getTimeToLive() {
		return timeToLive;
	}
	public void setTimeToLive(Date timeToLive) {
		this.timeToLive = timeToLive;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Url[] getUrlReferences() {
		return urlReferences;
	}
	public void setUrlReferences(Url[] urlReferences) {
		this.urlReferences = urlReferences;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	
	

}
