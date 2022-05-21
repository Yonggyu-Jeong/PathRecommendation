package shadow.user.service;

import java.io.IOException;

import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import common.collection.ABox;
import common.collection.ABoxList;

public interface UserService {

	public ABox test() throws DataAccessException; 
	
	public ABox searchUserList(ABox aBox) throws DataAccessException; //특정 사용자의 회원 정보 조회
	public String modifyUser(ABox aBox) throws DataAccessException; //회원 정보 수정
	public String deleteAccount(ABox aBox) throws DataAccessException; //회원탈퇴
	//해당 옷장이랑 옷 데이터베이스, 사진 파일들 전부 같이 삭제해야 함.
	
	public ABox insertLocation(ABoxList<ABox> aBoxList) throws DataAccessException;

	public ABox findLocation(ABox aBox) throws DataAccessException, IOException;

	public Object insertReview(ABoxList<ABox> jsonBoxList) throws DataAccessException;

	public ABox testLocation(ABox jsonBox);

	public ABox getCategoryList(ABox jsonBox);

	public ABox getLocation(ABox aBox);

}
