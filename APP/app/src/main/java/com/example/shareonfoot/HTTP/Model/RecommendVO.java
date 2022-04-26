package com.example.shareonfoot.HTTP.Model;

// 친구추천 테이블
public class RecommendVO {

	// 순번
	private Integer id;

	// 추천 자
	private Integer recommendUserId;

	// tlksrb가입자
	private Integer joinUserId;

	// 날짜
	private String regdate;

	// RC01 : OK
	private String replCode;

	// 입력코드
	private String code;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRecommendUserId() {
		return recommendUserId;
	}

	public void setRecommendUserId(Integer recommendUserId) {
		this.recommendUserId = recommendUserId;
	}

	public Integer getJoinUserId() {
		return joinUserId;
	}

	public void setJoinUserId(Integer joinUserId) {
		this.joinUserId = joinUserId;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public String getReplCode() {
		return replCode;
	}

	public void setReplCode(String replCode) {
		this.replCode = replCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	// TbRecommend 모델 복사
	public void CopyData(RecommendVO param)
	{
		this.id = param.getId();
		this.recommendUserId = param.getRecommendUserId();
		this.joinUserId = param.getJoinUserId();
		this.regdate = param.getRegdate();
		this.replCode = param.getReplCode();
		this.code = param.getCode();
	}
}