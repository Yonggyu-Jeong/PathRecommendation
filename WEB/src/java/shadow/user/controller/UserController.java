package shadow.user.controller;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import common.collection.ABox;
import common.collection.ABoxList;
import common.util.NaverMap;
import shadow.user.service.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/user/test", method = RequestMethod.GET, produces = "application/json; charset=utf8")
	public ResponseEntity<String> test() throws Exception {
		String json = "{\"goal\":\"126.73697,37.36263,name=\\\"안산종합여객자동차터미널\\\"\",\"start\":\"126.73882379220626,37.3627151749158,name=\\\"오이도역 수인분당선\\\"\",\"waypoints\":\"126.7582752593,37.350593833,name=생금집:126.8229310131,37.319893151,name=안산패션일번가:126.8541872996,37.3204546056,name=노적봉 공원\"}";
		String result = "";
		ABox jsonBox = new ABox();
		ABox jsonBox2 = new ABox();
		ABox jsonBox3 = new ABox();
		ABox resultBox = new ABox();
		ABoxList<ABox> jsonBoxList = new ABoxList<ABox>();
		NaverMap naverMap = new NaverMap();
		
		jsonBox.setJson(json);
		try {
			jsonBox2.setJson(naverMap.sendNaverMap(jsonBox));
			if (jsonBox2.getString("message").equals("길찾기를 성공하였습니다.")) {

				jsonBox2.setJson(jsonBox2.get("route").toString());
				jsonBoxList.setJson(jsonBox2.get("traoptimal").toString()); // 추가 기능
				jsonBox3 = jsonBoxList.get(0);
				String parsingJson = jsonBox3.get("path").toString();

				result = parsingJson.substring(1, parsingJson.length() - 1).replaceAll("\\],\\[", "\\]&\\[");
				resultBox.set("result", result);
				if (result.length() > 0) {
					resultBox.set("check", "ok");
				} else {
					resultBox.set("check", "fail");
				}
				result = resultBox.aBoxToJsonObject().toString();
			} else {
				resultBox.set("check", "fail");
			}

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/test2", method = RequestMethod.GET, produces = "application/json; charset=utf8")
	public ResponseEntity<String> test2() throws Exception {
		String result = "";
		try {
			ABox jsonBox = new ABox();
			ABox jsonBox2 = new ABox();
			ABox resultBox = new ABox();

			ABoxList<ABox> jsonBoxList = new ABoxList<ABox>();

			String json = "{\"code\":0,\"message\":\"길찾기를 성공하였습니다.\",\"currentDateTime\":\"2022-06-01T12:51:01\",\"route\":{\"traoptimal\":[{\"summary\":{\"start\":{\"location\":[126.7388230,37.3627145]},\"goal\":{\"location\":[126.7369697,37.3626295],\"dir\":1,\"distance\":4066,\"duration\":536802,\"pointIndex\":234},\"waypoints\":[{\"location\":[126.7582752,37.3505930],\"dir\":2,\"distance\":3582,\"duration\":426800,\"pointIndex\":114}],\"distance\":7648,\"duration\":963602,\"etaServiceType\":1,\"departureTime\":\"2022-06-01T12:51:01\",\"bbox\":[[126.7361839,37.3505930],[126.7600814,37.3663637]],\"tollFare\":0,\"taxiFare\":8100,\"fuelPrice\":1100},\"path\":[[126.7390619,37.3626511],[126.7391380,37.3628355],[126.7392575,37.3630832],[126.7393130,37.3631746],[126.7394839,37.3633587],[126.7396275,37.3634570],[126.7398311,37.3636476],[126.7399904,37.3638640],[126.7401392,37.3641128],[126.7402383,37.3642568],[126.7403970,37.3644165],[126.7404571,37.3644998],[126.7405374,37.3645994],[126.7406242,37.3647325],[126.7406621,37.3648914],[126.7407248,37.3651748],[126.7408418,37.3655559],[126.7409020,37.3656248],[126.7409806,37.3656695],[126.7410357,37.3656942],[126.7410564,37.3656601],[126.7411619,37.3656130],[126.7412866,37.3655660],[126.7416226,37.3655051],[126.7423915,37.3652982],[126.7427768,37.3652736],[126.7429365,37.3652332],[126.7433368,37.3651780],[126.7434735,37.3651690],[126.7436871,37.3651541],[126.7438204,37.3651541],[126.7443250,37.3651726],[126.7449364,37.3652333],[126.7479116,37.3656245],[126.7486865,37.3657241],[126.7493000,37.3658073],[126.7494883,37.3658329],[126.7499992,37.3658992],[126.7500624,37.3659077],[126.7505146,37.3659728],[126.7508315,37.3660189],[126.7511022,37.3660504],[126.7514816,37.3660537],[126.7518319,37.3660280],[126.7519462,37.3660107],[126.7521724,37.3659779],[126.7524985,37.3658889],[126.7530179,37.3656713],[126.7531654,37.3655938],[126.7534991,37.3653165],[126.7536753,37.3651986],[126.7539939,37.3649537],[126.7541249,37.3648400],[126.7542001,37.3647711],[126.7542240,37.3647496],[126.7544011,37.3645425],[126.7544481,37.3644743],[126.7570348,37.3607481],[126.7571080,37.3606476],[126.7572374,37.3604664],[126.7579744,37.3594750],[126.7582187,37.3592043],[126.7583637,37.3590439],[126.7584082,37.3590000],[126.7585040,37.3589059],[126.7590045,37.3584142],[126.7591220,37.3582978],[126.7592234,37.3581984],[126.7597116,37.3578192],[126.7599098,37.3575284],[126.7599559,37.3574377],[126.7599616,37.3574260],[126.7599990,37.3572937],[126.7600065,37.3571108],[126.7597733,37.3555653],[126.7597425,37.3553587],[126.7597441,37.3551965],[126.7597490,37.3550496],[126.7597681,37.3548279],[126.7597684,37.3546774],[126.7597571,37.3545674],[126.7596874,37.3542992],[126.7595696,37.3538785],[126.7594147,37.3533268],[126.7593554,37.3531632],[126.7592844,37.3530303],[126.7592122,37.3529082],[126.7590716,37.3527351],[126.7589006,37.3525610],[126.7587463,37.3524104],[126.7585818,37.3522597],[126.7583379,37.3520256],[126.7582516,37.3519638],[126.7581123,37.3518881],[126.7578996,37.3518165],[126.7575243,37.3517492],[126.7574916,37.3517436],[126.7573868,37.3517249],[126.7572244,37.3517059],[126.7570372,37.3516804],[126.7569324,37.3516563],[126.7568323,37.3516232],[126.7567199,37.3515711],[126.7565516,37.3514583],[126.7564923,37.3514101],[126.7564386,37.3513584],[126.7563795,37.3512841],[126.7567460,37.3511035],[126.7570191,37.3510024],[126.7573932,37.3508434],[126.7576176,37.3507637],[126.7577782,37.3507314],[126.7578707,37.3507374],[126.7579113,37.3507493],[126.7579763,37.3507984],[126.7580073,37.3508554],[126.7580208,37.3509817],[126.7580049,37.3511033],[126.7579578,37.3511832],[126.7579105,37.3512811],[126.7578986,37.3513514],[126.7579194,37.3514092],[126.7579507,37.3514508],[126.7580159,37.3514747],[126.7580306,37.3514775],[126.7581399,37.3514962],[126.7581881,37.3515353],[126.7582819,37.3516386],[126.7583129,37.3516992],[126.7583202,37.3517641],[126.7583057,37.3518533],[126.7582516,37.3519638],[126.7583379,37.3520256],[126.7585818,37.3522597],[126.7587463,37.3524104],[126.7589006,37.3525610],[126.7590716,37.3527351],[126.7592122,37.3529082],[126.7592844,37.3530303],[126.7593554,37.3531632],[126.7594147,37.3533268],[126.7595696,37.3538785],[126.7596874,37.3542992],[126.7597571,37.3545674],[126.7597684,37.3546774],[126.7597681,37.3548279],[126.7597490,37.3550496],[126.7597441,37.3551965],[126.7597425,37.3553587],[126.7600065,37.3571108],[126.7600814,37.3575295],[126.7599689,37.3576000],[126.7597116,37.3578192],[126.7592234,37.3581984],[126.7591220,37.3582978],[126.7585040,37.3589059],[126.7584082,37.3590000],[126.7583637,37.3590439],[126.7582187,37.3592043],[126.7579744,37.3594750],[126.7572374,37.3604664],[126.7571080,37.3606476],[126.7570348,37.3607481],[126.7544481,37.3644743],[126.7544011,37.3645425],[126.7542240,37.3647496],[126.7542001,37.3647711],[126.7541249,37.3648400],[126.7539939,37.3649537],[126.7536753,37.3651986],[126.7534991,37.3653165],[126.7531654,37.3655938],[126.7530179,37.3656713],[126.7524985,37.3658889],[126.7524962,37.3658898],[126.7521724,37.3659779],[126.7518319,37.3660280],[126.7514816,37.3660537],[126.7511022,37.3660504],[126.7508315,37.3660189],[126.7500624,37.3659077],[126.7499992,37.3658992],[126.7494883,37.3658329],[126.7493000,37.3658073],[126.7486865,37.3657241],[126.7479116,37.3656245],[126.7449364,37.3652333],[126.7443250,37.3651726],[126.7438204,37.3651541],[126.7436871,37.3651541],[126.7434735,37.3651690],[126.7433368,37.3651780],[126.7429365,37.3652332],[126.7427768,37.3652736],[126.7427078,37.3652912],[126.7424145,37.3653704],[126.7422922,37.3654057],[126.7412780,37.3657480],[126.7399715,37.3661885],[126.7394638,37.3663637],[126.7393616,37.3661909],[126.7387728,37.3654507],[126.7386971,37.3653457],[126.7386303,37.3652470],[126.7384467,37.3649736],[126.7383867,37.3648831],[126.7383311,37.3647998],[126.7379873,37.3642829],[126.7378905,37.3641471],[126.7376989,37.3638808],[126.7375978,37.3637161],[126.7373367,37.3632998],[126.7371299,37.3629713],[126.7370199,37.3628002],[126.7369342,37.3626690],[126.7368620,37.3625576],[126.7367920,37.3624436],[126.7367130,37.3623241],[126.7364207,37.3618616],[126.7361839,37.3614923],[126.7363256,37.3614374],[126.7364197,37.3614082],[126.7368026,37.3612809],[126.7369268,37.3612844],[126.7370629,37.3613376],[126.7371915,37.3614646],[126.7373661,37.3617253],[126.7374173,37.3618077],[126.7377052,37.3622512],[126.7377350,37.3623217],[126.7377263,37.3623982],[126.7377123,37.3624423],[126.7372096,37.3625761],[126.7370793,37.3626149],[126.7369819,37.3626512]],\"section\":[{\"pointIndex\":19,\"pointCount\":50,\"distance\":2152,\"name\":\"봉화로\",\"congestion\":2,\"speed\":31},{\"pointIndex\":113,\"pointCount\":38,\"distance\":817,\"name\":\"마전로\",\"congestion\":2,\"speed\":20},{\"pointIndex\":150,\"pointCount\":50,\"distance\":2352,\"name\":\"봉화로\",\"congestion\":2,\"speed\":39}],\"guide\":[{\"pointIndex\":19,\"type\":3,\"instructions\":\"'봉화로' 방면으로 우회전\",\"distance\":389,\"duration\":65608},{\"pointIndex\":68,\"type\":3,\"instructions\":\"'정왕동, 시흥스마트허브' 방면으로 우회전\",\"distance\":2152,\"duration\":249356},{\"pointIndex\":106,\"type\":2,\"instructions\":\"좌회전\",\"distance\":882,\"duration\":88737},{\"pointIndex\":114,\"type\":87,\"instructions\":\"경유지\",\"distance\":159,\"duration\":23099},{\"pointIndex\":131,\"type\":3,\"instructions\":\"우회전\",\"distance\":152,\"duration\":31228},{\"pointIndex\":150,\"type\":2,\"instructions\":\"'오이도역, 정왕IC' 방면으로 좌회전\",\"distance\":657,\"duration\":106221},{\"pointIndex\":190,\"type\":56,\"instructions\":\"정왕지하차도에서 '배곧신도시, 오이도역, 시흥철도차량사업소' 방면으로 지하차도 진입\",\"distance\":1971,\"duration\":175845},{\"pointIndex\":199,\"type\":2,\"instructions\":\"'오이도역' 방면으로 좌회전\",\"distance\":381,\"duration\":37702},{\"pointIndex\":219,\"type\":2,\"instructions\":\"'오이도역' 방면으로 좌회전\",\"distance\":614,\"duration\":120453},{\"pointIndex\":231,\"type\":2,\"instructions\":\"좌회전\",\"distance\":222,\"duration\":49858},{\"pointIndex\":234,\"type\":88,\"instructions\":\"목적지\",\"distance\":69,\"duration\":15495}]}]}}";
			jsonBox.setJson(json);
			jsonBox.setJson(jsonBox.get("route").toString());
			jsonBoxList.setJson(jsonBox.get("traoptimal").toString());
			jsonBox2 = jsonBoxList.get(0);

			String parsingJson = jsonBox2.get("path").toString();
			result = parsingJson.substring(1, parsingJson.length() - 1).replaceAll("\\],\\[", "\\]&\\[");
			resultBox.set("result", result);
			if (result.length() > 0) {
				resultBox.set("check", "ok");
			} else {
				resultBox.set("check", "fail");
			}
			result = resultBox.aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/insert/locate", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> insertLocate(@RequestBody String json) throws Exception {
		String result = "";
		ABoxList<ABox> jsonBoxList = new ABoxList<ABox>();
		jsonBoxList.setJson(json);
		try {
			result = userService.insertLocation(jsonBoxList).toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);
		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/get-location", produces = "application/json; charset=utf8", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> getLocation(@RequestBody String json) throws Exception {
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.setJson(json);
		try {
			result = userService.getLocation(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/find", produces = "application/json; charset=utf8", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> findLocation(@RequestBody String json) throws Exception {
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.setJson(json);

		try {
			result = userService.findLocation(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/testData", method = RequestMethod.GET)
	public ResponseEntity<String> testLocation() throws Exception {
		String result = "";
		String trash = "{\"goal\":{\"lng\":\"126.73882379220626\",\"lat\":\"37.3627151749158\"},\"start\":{\"lng\":\"126.9784147336912\",\"lat\":\"37.56667197782809\"},\"user\":{\"password\":\"1\",\"id\":\"hello\"}}";
		ABox jsonBox = new ABox();
		jsonBox.setJson(trash);

		try {
			result = userService.findLocation(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}

	@RequestMapping(value = "/path", produces = "application/json; charset=utf8", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> getPathList(@RequestBody String json) throws Exception {
		String result = "";
		String parsingJson = "";
		ABox jsonBox = new ABox();
		ABox jsonBox2 = new ABox();
		ABox jsonBox3 = new ABox();
		ABox resultBox = new ABox();
		ABoxList<ABox> jsonBoxList = new ABoxList<ABox>();
		NaverMap naverMap = new NaverMap();

		jsonBox.setJson(json);
		try {
			if (jsonBox.containsKey("start") && jsonBox.containsKey("goal")) {
				try {
					jsonBox2.setJson(naverMap.sendNaverMap(jsonBox));
					if (jsonBox2.getString("message").equals("길찾기를 성공하였습니다.")) {
						jsonBox2.setJson(jsonBox2.get("route").toString());
						jsonBoxList.setJson(jsonBox2.get("traoptimal").toString()); // 추가 기능
						jsonBox3 = jsonBoxList.get(0);
						parsingJson = jsonBox3.get("path").toString();

						result = parsingJson.substring(1, parsingJson.length() - 1).replaceAll("\\],\\[", "\\]&\\[");
						resultBox.set("result", result);
						if (result.length() > 0) {
							resultBox.set("check", "ok");
						} else {
							resultBox.set("check", "fail");
							resultBox.set("input", jsonBox.toString());
							resultBox.set("check_message", "error1");
							resultBox.set("message", jsonBox2.getString("message"));
						}

					} else {
						resultBox.set("check", "fail");
						resultBox.set("input", jsonBox.toString());
						resultBox.set("check_message", "error2");
						resultBox.set("message", jsonBox2.getString("message"));
					}
				} catch (Exception e) {
					resultBox.set("check", "fail");
					resultBox.set("input", jsonBox.toString());
					resultBox.set("check_message", "error3");
					resultBox.set("message", jsonBox2.getString("message"));
					e.printStackTrace();
				}
			} else {
				resultBox.set("check", "fail");
				resultBox.set("input", jsonBox.toString());
				resultBox.set("check_message", "error4");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(resultBox.aBoxToJsonObject().toString(), HttpStatus.SERVICE_UNAVAILABLE);

		} finally {
			result = null;
			parsingJson = null;
			jsonBox = null;
			jsonBox2 = null;
			jsonBox3 = null;
			naverMap = null;
			jsonBoxList = null;
		}
		return new ResponseEntity<String>(resultBox.aBoxToJsonObject().toString(), HttpStatus.OK);
	}

	@RequestMapping(value = "/category/list", produces = "application/json; charset=utf8", method = RequestMethod.POST, headers = "Content-Type=application/json;utf-8")
	public ResponseEntity<String> getCategoryList(@RequestBody String json) throws Exception {
		String result = "";
		ABox jsonBox = new ABox();
		jsonBox.setJson(json);

		try {
			result = userService.getCategoryList(jsonBox).aBoxToJsonObject().toString();

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(result, HttpStatus.SERVICE_UNAVAILABLE);

		}
		return new ResponseEntity<String>(result, HttpStatus.OK);
	}
}
