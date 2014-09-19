package net.stackoverflow.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "row")
@XmlAccessorType(XmlAccessType.FIELD)
public class Users {

  @XmlAttribute(name = "Id")
  private Integer id;

  @XmlAttribute(name = "Reputation")
  private Integer reputation;

  @XmlAttribute(name = "CreationDate")
  private String creationDate;

  @XmlAttribute(name = "DisplayName")
  private String displayName;

  @XmlAttribute(name = "LastAccessDate")
  private String lastAccessDate;

  @XmlAttribute(name = "Location")
  private String location;

  @XmlAttribute(name = "WebsiteUrl")
  private String websiteUrl;

  @XmlAttribute(name = "AboutMe")
  private String aboutMe;

  @XmlAttribute(name = "Age")
  private Integer age;

  @XmlAttribute(name = "UpVotes")
  private Integer upVotes;

  @XmlAttribute(name = "DownVotes")
  private Integer downVotes;

  @XmlAttribute(name = "Views")
  private Integer views;

  @XmlAttribute(name = "EmailHash")
  private String emailHash;

  @XmlAttribute(name = "AccountId")
  private Integer accountId;

  public Integer getAccountId() {
    return accountId;
  }

  public void setAccountId(Integer accountId) {
    this.accountId = accountId;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getReputation() {
    return reputation;
  }

  public void setReputation(Integer reputation) {
    this.reputation = reputation;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getLastAccessDate() {
    return lastAccessDate;
  }

  public void setLastAccessDate(String lastAccessDate) {
    this.lastAccessDate = lastAccessDate;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getWebsiteUrl() {
    return websiteUrl;
  }

  public void setWebsiteUrl(String websiteUrl) {
    this.websiteUrl = websiteUrl;
  }

  public String getAboutMe() {
    return aboutMe;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  public Integer getUpVotes() {
    return upVotes;
  }

  public void setUpVotes(Integer upVotes) {
    this.upVotes = upVotes;
  }

  public Integer getDownVotes() {
    return downVotes;
  }

  public void setDownVotes(Integer downVotes) {
    this.downVotes = downVotes;
  }

  public Integer getViews() {
    return views;
  }

  public void setViews(Integer views) {
    this.views = views;
  }

  public String getEmailHash() {
    return emailHash;
  }

  public void setEmailHash(String emailHash) {
    this.emailHash = emailHash;
  }

}
