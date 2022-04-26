 package com.example.shareonfoot.HTTP.Model;


 import com.google.gson.annotations.SerializedName;

 import java.io.Serializable;

 // 회원 테이블
public class UserVO implements Serializable {
	

	@SerializedName(value = "publicId")
	// 닉네임
	private String publicId;

	@SerializedName(value = "phoneNum")
	// 연락처
	private String phoneNum;

	@SerializedName(value = "email")
	private String email;

	@SerializedName(value = "pwd")
	// 비밀번호
	private String pwd;

	@SerializedName(value = "introduce")
	// 간단 자기소개
	private String introduce;

	@SerializedName(value = "mbti")
	// mbti
	private String mbti;

	@SerializedName(value = "genderSt")
	// 성별유형(Gender Type) GT01 : 남자 GT02 : 여자
	private String genderSt;

	@SerializedName(value = "publicSt")
	// 회원상태(Public Status) String PS01 : 정상 String PS02 : 휴면 String PS03 : 탈퇴 String PS04: 심사중  String PS05: 사진 반려  String PS06: 프로필 반려 String PS06: 인증 반려
	private String publicSt;

	@SerializedName(value = "publicBadSt")
	// 악성회원상태(Bad Status) BS01 : 주의 BS02 : 경고 BS03 : 악성 BS04 : 영정 BS05 : 정상
	private String publicBadSt;

	@SerializedName(value = "badComment")
	// 내용
	private String badComment;

	@SerializedName(value = "secessionType")
	// 탈퇴유형(sEcessionType) ET01 : 상품 불만족 ET02 : 사이트 이용 불편 ET03 : 서비스 불만족 ET04 : 기타
	private String secessionType;

	@SerializedName(value = "secessionComment")
	// 내용
	private String secessionComment;

	@SerializedName(value = "pushRecvSt")
	// 수신상태(푸쉬 알림) IS01 : 수신동의 IS02 : 수신거부
	private String pushRecvSt;

	@SerializedName(value = "smsRecvSt")
	// 수신상태(SMS) IS01 : 수신동의 IS02 : 수신거부
	private String smsRecvSt;

	@SerializedName(value = "ratingRecvSt")
	// 수신상태(방문자,고평가) IS01 : 수신동의 IS02 : 수신거부
	private String ratingRecvSt;

	@SerializedName(value = "askRecvSt")
	// 수신상태(대화신청) IS01 : 수신동의 IS02 : 수신거부
	private String askRecvSt;

	@SerializedName(value = "recentLoginDt")
	// 최근로그인일시
	private String recentLoginDt;

	@SerializedName(value = "seartchTime")
	// 무료매칭 가능 시간

	private String seartchTime;

	@SerializedName(value = "joinDate")
	// 가입일
	private String joinDate;

	@SerializedName(value = "ratingGrade")
	// 평균점수
	private Float ratingGrade;

	@SerializedName(value = "personalCode")
	// 개인 추천 코드
	private String personalCode;

	public String getPublicId() {
		return publicId;
	}

	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getIntroduce() {
		return introduce;
	}

	public void setIntroduce(String introduce) {
		this.introduce = introduce;
	}

	public String getMbti() {
		return mbti;
	}

	public void setMbti(String mbti) {
		this.mbti = mbti;
	}

	public String getGenderSt() {
		return genderSt;
	}

	public void setGenderSt(String genderSt) {
		this.genderSt = genderSt;
	}

	public String getPublicSt() {
		return publicSt;
	}

	public void setPublicSt(String publicSt) {
		this.publicSt = publicSt;
	}

	public String getPublicBadSt() {
		return publicBadSt;
	}

	public void setPublicBadSt(String publicBadSt) {
		this.publicBadSt = publicBadSt;
	}

	public String getBadComment() {
		return badComment;
	}

	public void setBadComment(String badComment) {
		this.badComment = badComment;
	}

	public String getSecessionType() {
		return secessionType;
	}

	public void setSecessionType(String secessionType) {
		this.secessionType = secessionType;
	}

	public String getSecessionComment() {
		return secessionComment;
	}

	public void setSecessionComment(String secessionComment) {
		this.secessionComment = secessionComment;
	}

	public String getPushRecvSt() {
		return pushRecvSt;
	}

	public void setPushRecvSt(String pushRecvSt) {
		this.pushRecvSt = pushRecvSt;
	}

	public String getSmsRecvSt() {
		return smsRecvSt;
	}

	public void setSmsRecvSt(String smsRecvSt) {
		this.smsRecvSt = smsRecvSt;
	}

	public String getRatingRecvSt() {
		return ratingRecvSt;
	}

	public void setRatingRecvSt(String ratingRecvSt) {
		this.ratingRecvSt = ratingRecvSt;
	}

	public String getAskRecvSt() {
		return askRecvSt;
	}

	public void setAskRecvSt(String askRecvSt) {
		this.askRecvSt = askRecvSt;
	}

	public String getRecentLoginDt() {
		return recentLoginDt;
	}

	public void setRecentLoginDt(String recentLoginDt) {
		this.recentLoginDt = recentLoginDt;
	}

	public String getSeartchTime() {
		return seartchTime;
	}

	public void setSeartchTime(String seartchTime) {
		this.seartchTime = seartchTime;
	}

	public String getJoinDate() {
		return joinDate;
	}

	public void setJoinDate(String joinDate) {
		this.joinDate = joinDate;
	}

	public Float getRatingGrade() {
		return ratingGrade;
	}

	public void setRatingGrade(Float ratingGrade) {
		this.ratingGrade = ratingGrade;
	}

	public String getPersonalCode() {
		return personalCode;
	}

	public void setPersonalCode(String personalCode) {
		this.personalCode = personalCode;
	}

	// TbUser 모델 복사
	public void CopyData(UserVO param)
	{
		this.publicId = param.getPublicId();
		this.phoneNum = param.getPhoneNum();
		this.email = param.getEmail();
		this.pwd = param.getPwd();
		this.introduce = param.getIntroduce();
		this.mbti = param.getMbti();
		this.genderSt = param.getGenderSt();
		this.publicSt = param.getPublicSt();
		this.publicBadSt = param.getPublicBadSt();
		this.badComment = param.getBadComment();
		this.secessionType = param.getSecessionType();
		this.secessionComment = param.getSecessionComment();
		this.pushRecvSt = param.getPushRecvSt();
		this.smsRecvSt = param.getSmsRecvSt();
		this.ratingRecvSt = param.getRatingRecvSt();
		this.askRecvSt = param.getAskRecvSt();
		this.recentLoginDt = param.getRecentLoginDt();
		this.seartchTime = param.getSeartchTime();
		this.joinDate = param.getJoinDate();
		this.ratingGrade = param.getRatingGrade();
		this.personalCode = param.getPersonalCode();
	}

	public UserVO() {

	}

}