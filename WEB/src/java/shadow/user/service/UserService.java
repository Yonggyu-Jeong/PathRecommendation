package shadow.user.service;

import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import common.collection.ABox;

public interface UserService {

	public ABox test() throws DataAccessException; 
	
	public ABox searchUser(ABox aBox) throws DataAccessException; //특정 사용자의 회원 정보 조회
	public ABox searchUserList() throws DataAccessException; //모든 사용자의 회원 정보 리스트 조회
	public ABox searchUserList(ABox aBox) throws DataAccessException; //조건으로 사용자 리스트 검색
	public String modifyUser(ABox aBox) throws DataAccessException; //회원 정보 수정
	public String modifyProfileImage(MultipartHttpServletRequest multipartRequest) throws DataAccessException; // 프로필 사진 변	
	public String deleteAccount(ABox aBox) throws DataAccessException; //회원탈퇴
	//해당 옷장이랑 옷 데이터베이스, 사진 파일들 전부 같이 삭제해야 함.
	
}
