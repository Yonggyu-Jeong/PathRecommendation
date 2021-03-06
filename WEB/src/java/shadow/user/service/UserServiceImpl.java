package shadow.user.service;

import java.io.IOException;
import java.util.Comparator;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import common.collection.ABoxList;
import common.clustering.KMeans;
import common.collection.ABox;
import common.service.SuperService;
import common.util.Direction;
import common.util.Weather;
import common.util.mDataFrame;
import smile.clustering.PartitionClustering;
import smile.data.DataFrame;

@Service
@Transactional(propagation = Propagation.REQUIRED) // 서비스 클래스의 모든 메서드에 트랜잭션을 적용
public class UserServiceImpl extends SuperService implements UserService {

	@Override
	public ABox test() throws DataAccessException {
		try {
			for(int i=1; i<4138; i++) {
				commonDao.update("mybatis.shadow.user.user_mapper.updateRate", new ABox().set("idx", i));
			}	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
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

		if (result == 1)
			return "ok";
		else
			return "fail";
	}

	@Override
	public String deleteAccount(ABox aBox) throws DataAccessException {
		int result = commonDao.delete("mybatis.shadow.user.user_mapper.deleteUserSQL", aBox);

		if (result == 1)
			return "ok";
		else
			return "fail";
	}

	@Override
	public ABox insertLocation(ABoxList<ABox> aBoxList) throws DataAccessException {
		ABox resultABox = new ABox();
		int size = aBoxList.size();
		int cnt = commonDao.select("mybatis.shadow.user.user_mapper.selectLocationLabelCountSQL", resultABox).getInt("cnt") + 1;
		String temp = "";
		Random random = new Random();
		ABox aBox = new ABox();

		try {
			for (int i = 0; i < size; i++) {
				aBox = aBoxList.get(i);
				aBox.set("category", "CS01");
				if (aBox.getString("REFINE_WGS84_LOGT").equals("") || aBox.getString("REFINE_WGS84_LAT").equals("")) {
					continue;
				}
				if (commonDao.insert("mybatis.shadow.user.user_mapper.insertLocationSQL", aBox) != 0) {
					aBox.set("doorTag", "DC02");
					aBox.set("locateSeq", cnt);
					for (int j = 1; j < 5; j++) {
						int numA = random.nextInt(7) + 1;
						int numB = 0;
						if (numA == 3 || numA == 4 || numA == 7) {
							numB = random.nextInt(4) + 1;
						} else {
							numB = random.nextInt(9) + 1;
						}
						aBox.set("tag" + j, "TG" + numA + numB);
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

	public ABox insertReview(ABoxList<ABox> aBoxList) throws DataAccessException {
		ABox resultABox = new ABox();
		Random random = new Random();
		ABox aBox = new ABox();
		try {

			for (int i = 0; i < 1000; i++) {
				aBox = new ABox();
				aBox.set("locateSeq", i);
				aBox.set("star", random.nextInt(5) + 1);

				int randomCnt = random.nextInt(15) + 1;
				switch (randomCnt) {
				case 1:
					aBox.set("review", "음식이 맛있어요");
					break;
				case 2:
					aBox.set("review", "매장이 넓어요");
					break;
				case 3:
					aBox.set("review", "주차하기 편해요");
					break;
				case 4:
					aBox.set("review", "친절해요");
					break;
				case 5:
					aBox.set("review", "매장이 청결해요");
					break;
				case 6:
					aBox.set("review", "가성비가 좋아요");
					break;
				case 7:
					aBox.set("review", "양이 많아요");
					break;
				case 8:
					aBox.set("review", "혼밥하기 좋아요");
					break;
				case 9:
					aBox.set("review", "재료가 신선해요");
					break;
				case 10:
					aBox.set("review", "화장실이 깨끗해요");
					break;
				case 11:
					aBox.set("review", "단체모임 하기 좋아요");
					break;
				case 12:
					aBox.set("review", "특별한 메뉴가 있어요");
					break;
				case 13:
					aBox.set("review", "특별한 날 가기 좋아요");
					break;
				case 14:
					aBox.set("review", "뷰가 좋아요");
					break;
				default:
					aBox.set("review", "인테리어가 멋져요");
					break;
				}

				commonDao.insert("mybatis.shadow.user.user_mapper.insertReviewSQL", aBox);
			}
			resultABox.set("check", "ok");

		} catch (Exception ex) {
			resultABox.set("check", "fail");
			ex.printStackTrace();
		}
		return resultABox;
	}

	@SuppressWarnings("finally")
	@Override
	public ABox findLocation(ABox aBox) throws DataAccessException, IOException {
		ABox resultABox = new ABox();

		// KMeans kmeans = null;
		//Weather weather = new Weather();
		try {
			boolean check = false;
			ABox userBox = new ABox();
			userBox.set("id", "hello");
			userBox.set("password", "1");
			ABox locationABox = new ABox();

			resultABox.set("find-error", "4");
			ABoxList<ABox> directList = Direction.getDirection(aBox);
			resultABox.set("directList", directList);

			resultABox.set("find-error", "5");
			ABoxList<ABox> resultDataList = new ABoxList<ABox>();
			
			resultABox.set("find-error", "6");
			//ABox weatherBox = weather.getWeather();
			userBox = commonDao.select("mybatis.shadow.user.user_mapper.selectUserListSQL", userBox);
			resultABox.set("find-error", "7");

			for (int i = 0; i < directList.size(); i++) {
				//ABoxList<ABox> locateDataList = new ABoxList<ABox>();
				ABoxList<ABox> recommendDataList = new ABoxList<ABox>();
				ABoxList<ABox> locateList = commonDao.selectList("mybatis.shadow.user.user_mapper.selectLocateList", directList.get(i));
				locationABox.set("locateList", locateList);
				resultABox.set("find-error", "8");
				if(locateList.size() < 1) {
					check = true;
					resultABox.set("DataSize" + i, resultDataList.size());
					resultABox.set("resultDataList" + i, resultDataList);
					resultABox.set("recommendDataList" + i, recommendDataList);	
					continue;
				}
				
			/*
				for (int j = 0; j < locateList.size(); j++) {
					if ((weatherBox.getString("wf").equals("흐리고 비") || weatherBox.getString("wf").equals("구름많고 비")
							|| weatherBox.getInt("tmn") <= 12 || weatherBox.getInt("tmx") >= 27)
							&& locateList.get(i).get("door_tag").equals("DC01")) {
						locateList.remove(j);
					}
				}

			 */
				ABoxList<ABox> locateIdList = new ABoxList<ABox>();
				for (int j = 0; j < locateList.size(); j++) {
					locateIdList.add(new ABox().set("location", locateList.get(j).getInt("locate_id")));
				}
				resultABox.set("find-error", "9");
				//locateDataList = commonDao.selectList("mybatis.shadow.user.user_mapper.selectLocateDataSQL", new ABox().set("locateIdList", locateIdList));

				//Double[][] dataFrame = mDataFrame.getDoubleFrame(locateDataList, userABox);
				resultABox.set("find-error", "10");
				resultDataList = commonDao.selectList("mybatis.shadow.user.user_mapper.selectLocateList2", new ABox().set("locateIdList", locateIdList));

				resultABox.set("find-error", "11");
				int recommendDataCnt = 0;
				double rate = 4.5;
				do {
					if(rate < 2) {
						rate = 4.5;
						break;
					}
				
					for (int j=0; j<resultDataList.size(); j++) {
						if(recommendDataCnt == 3) {
							break;
						} 
						ABox resultRateBox = resultDataList.get(j);
						if(resultRateBox.getDouble("rate") > rate) {
							recommendDataList.add(resultRateBox);
							resultDataList.remove(j);
							recommendDataCnt++;
						}
					} 
					rate--;
				} while(recommendDataCnt < 2 );
				resultABox.set("find-error", "12");

				resultABox.set("DataSize" + i, resultDataList.size());
				resultABox.set("resultDataList" + i, resultDataList);
				resultABox.set("recommendDataList" + i, recommendDataList);				
			}
			
			resultABox.set("check", "ok");
			return resultABox;

		} catch (Exception ex) {
			resultABox.set("check", "fail");
			ex.printStackTrace();
		} finally {

			return resultABox;
		}
	}

	@Override
	public ABox testLocation(ABox jsonBox) {
		ABox resultABox = new ABox();
		try {
			Random random = new Random();
			ABoxList<ABox> locationList = null;
			ABoxList<ABox> directList = Direction.getDirection(jsonBox);
			
			for(int i=0; i<directList.size(); i++) {
				locationList = commonDao.selectList("mybatis.shadow.user.user_mapper.selectLocateList", directList.get(i));
				for(int j=0; j<locationList.size(); j++) {
					locationList.get(i).set("rate", random.nextInt(5) + 1);
				}
				resultABox.set("locationList"+i, locationList);
			}
			resultABox.set("check", "ok");
			
		} catch (Exception ex) {
			resultABox.set("check", "fail");
			ex.printStackTrace();
		} 
		return resultABox;
	}

	@Override
	public ABox getCategoryList(ABox jsonBox) {
		ABox resultABox = new ABox();
		try {
			ABoxList<ABox> categoryList = commonDao.selectList("mybatis.shadow.user.user_mapper.selectCategoryListSQL", jsonBox);
			resultABox.set("check", "ok");
			resultABox.set("result", categoryList);
			
		} catch (Exception ex) {
			resultABox.set("check", "fail");
			ex.printStackTrace();
		} 
		return resultABox;
	}
	
	@Override
	public ABox getLocation(ABox aBox) {
		ABox resultABox = new ABox();
		try {			
			ABoxList<ABox> locationList = new ABoxList<ABox>();
			for(int i=0; i<3; i++) {
				ABox paramBox = new ABox();
				paramBox.set("maxLng", aBox.getDouble("lng")+(0.01*2));
				paramBox.set("minLng", aBox.getDouble("lng")-(0.01*2));
				paramBox.set("maxLat", aBox.getDouble("lat")+(0.01*2));
				paramBox.set("minLat", aBox.getDouble("lat")-(0.01*2));

				locationList = commonDao.selectList("mybatis.shadow.user.user_mapper.selectLocateList", paramBox);
				if(!locationList.isEmpty()) {
					if(locationList.size() >= 3) {
						break;
					}
				}
				
			}
			for(int i=0; i<locationList.size(); i++) {
				resultABox.set("result_"+(i+1), locationList.get(i));
			}
			resultABox.set("result-size", locationList.size());
			resultABox.set("check", "ok");
			
		} catch (Exception ex) {
			resultABox.set("check", "fail");
			ex.printStackTrace();
		} 
		return resultABox;
	}
}
