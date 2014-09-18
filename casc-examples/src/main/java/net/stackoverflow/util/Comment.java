package net.stackoverflow.util;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="row")
@XmlAccessorType(XmlAccessType.FIELD)
class Comment {
	

	@XmlAttribute(name="Id")
	private Integer Id;
	
	@XmlAttribute(name="PostId")
	private Integer postId;
	
	@XmlAttribute(name="Score")
	private Integer score;
	
	@XmlAttribute(name="Text")
	private String text;
	
	
	@XmlAttribute(name="CreationDate")
	private String creationDate;
	
	@XmlAttribute(name="UserId")
	private Integer userId;

	public Integer getId() {
		return Id;
	}

	public void setId(Integer id) {
		Id = id;
	}

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Comments [Id=" + Id + ", postId=" + postId + ", score=" + score
				+ ", text=" + text + ", creationDate=" + creationDate
				+ ", userId=" + userId + "]";
	}
	
	
	
}
