package shadow.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import common.collection.ABoxList;
import common.collection.ABox;
import common.service.SuperService;

@Service
@Transactional(propagation = Propagation.REQUIRED) // 서비스 클래스의 모든 메서드에 트랜잭션을 적용
public class UserServiceImpl extends SuperService implements UserService {
	
	
	@Override
	public ABox test() throws DataAccessException {
		ABox result = new ABox();
		try {

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}


	@Override
	public ABox searchUser(ABox aBox) throws DataAccessException {
		ABox result = new ABox();
		try {
			ABox UserVO = commonDao.select("mybatis.shadow.user.user_mapper.testSQL", aBox);
			result.set("UserVO", UserVO);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	public ABox searchUserList() throws DataAccessException {
		ABox result = new ABox();
		try {
			ABoxList<ABox> user_List = commonDao.selectList("mybatis.shadow.user.user_mapper.selectUserListNWSQL", result);
			result.set("UserVO", user_List);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	public ABox searchUserList(ABox aBox) throws DataAccessException {
		ABox result = new ABox();
		try {
			ABoxList<ABox> user_List = commonDao.selectList("mybatis.shadow.user.user_mapper.selectUserListSQL", aBox);
			result.set("UserVO", user_List);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return result;
	}

	@Override
	public String modifyUser(ABox aBox) throws DataAccessException {
		int result = commonDao.update("mybatis.shadow.user.user_mapper.updateUserSQL", aBox);
		
		if (result==1)
			return "ok"; 
		else
			return "fail";
	}

	@Override
	public String modifyProfileImage(MultipartHttpServletRequest multipartRequest) throws DataAccessException {
		// TODO 작업 필요_사진 수정 
		return null;
	}

	@Override
	public String deleteAccount(ABox aBox) throws DataAccessException {
		int result = commonDao.delete("mybatis.shadow.user.user_mapper.deleteUserSQL", aBox);
		
		if (result==1)
			return "ok"; 
		else
			return "fail";
	}
}
