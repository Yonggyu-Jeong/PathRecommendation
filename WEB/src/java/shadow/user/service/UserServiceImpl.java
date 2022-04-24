package shadow.user.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import common.collection.ABoxList;
import common.collection.ABox;
import common.service.SuperService;
import common.util.Direction;
import common.util.weather;

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
			ABoxList<ABox> user_List = commonDao.selectList("mybatis.shadow.user.user_mapper.selectUserListSQL", result);
			result.set("UserVO", user_List);

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


	@Override
	public ABox insertLocation(ABoxList<ABox> aBoxList) throws DataAccessException {
		ABox resultABox = new ABox();
		int size = aBoxList.size();
		int cnt = commonDao.select("mybatis.shadow.user.user_mapper.selectLocationLabelCountSQL", resultABox).getInt("cnt")+1;
		String temp = "";
		Random random = new Random();
		ABox aBox = new ABox();

		try { 
			for(int i=0; i<size; i++) {
				aBox = aBoxList.get(i);
				aBox.set("category", "CS01");
				if(aBox.getString("REFINE_WGS84_LOGT").equals("") || aBox.getString("REFINE_WGS84_LAT").equals("") ) {
					continue;
				}
				if (commonDao.insert("mybatis.shadow.user.user_mapper.insertLocationSQL", aBox) != 0) {
					aBox.set("doorTag", "DC02");
					aBox.set("locateSeq", cnt);
					for(int j=1; j<5; j++) {
						int numA = random.nextInt(7)+1;
						int numB = 0;
						if(numA == 3 || numA == 4 || numA == 7) {
							numB = random.nextInt(4)+1;
						} else {
							numB = random.nextInt(9)+1;
						}					
						aBox.set("tag"+j, "TG"+numA+numB);
					}
					if (commonDao.insert("mybatis.shadow.user.user_mapper.insertLocationLabelSQL", aBox) != 0) {	
						cnt++;
					}
				}
			}
			resultABox.set("check", cnt);

		} catch (Exception ex) {
			resultABox.set("check", "fail");
			ex.printStackTrace();
		}		
		return resultABox;
	}


	@Override
	public ABox findLocation(ABoxList<ABox> aBoxList) throws DataAccessException {
		ABox resultABox = new ABox();
		try { 
			ABoxList<ABox> locateList = new ABoxList<ABox>();			
			ABoxList<ABox> directList = Direction.getDirection(aBoxList);

			weather weather = new weather();
			String temp = weather.weather();
			resultABox.set("directList", temp);

			
			for(int i=0; i<directList.size(); i++) {
				resultABox.set("list"+i, commonDao.selectList("mybatis.shadow.user.user_mapper.selectLocateList", directList.get(i)));				
			}

			
			resultABox.set("check", "ok");

		} catch (Exception ex) {
			resultABox.set("check", "fail");
			ex.printStackTrace();
		}		
		return resultABox;		

	}
}
