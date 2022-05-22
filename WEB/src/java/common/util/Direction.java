package common.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import common.collection.ABox;
import common.collection.ABoxList;

public class Direction {
	public static ABoxList<ABox> getDirection(ABox aBox) {
		ABoxList<ABox> resultBoxList = new ABoxList<ABox>();
		DecimalFormat df = new DecimalFormat("0.0000000");
		try {
			ABox startBox = new ABox();
			ABox goalBox = new ABox();
			
			startBox.setJson(aBox.get("start").toString());
			goalBox.setJson(aBox.get("goal").toString());

			double distance = 0.0;
			double lng_start = startBox.getDouble("lng");
			double lat_start = startBox.getDouble("lat");
			double lng_goal = goalBox.getDouble("lng");
			double lat_goal = goalBox.getDouble("lat");

			double lng = lng_start - lng_goal;
			double lat = lat_start - lat_goal;
			double lng_standard = lng_start;
			double lat_standard = lat_start;
			double lng_standard2 = 0;
			double lat_standard2 = 0;

			distance = Math.sqrt((lng * lng) + (lat * lat));
			distance = distance / 3;

			for (int i = 0; i < 3; i++) {
				ABox directionBox = new ABox();
				if (i != 0) {
					lng_standard = lng_standard2;
					lat_standard = lat_standard2;
				}
				lng_standard2 = lng_standard - lng / 3;
				lat_standard2 = lat_standard - lat / 3;
				distance = Math.sqrt(((lng_standard - lng_standard2) * (lng_standard - lng_standard2))
						+ ((lat_standard - lat_standard2) * (lat_standard - lat_standard2)));

				double lng_new = (lng_standard - (lng_standard - lng_standard2));
				double lat_new = (lat_standard - (lat_standard - lat_standard2));

				double lng_new2 = lng_new + distance / 2;
				double lat_new2 = lat_new + distance / 2;

				double lng_new3 = lng_new - distance / 2;
				double lat_new3 = lat_new - distance / 2;

				directionBox.set("minLng",
						df.format(Collections.min(Arrays.asList(lng_new2, lng_new3, lng_standard, lng_standard2))));
				directionBox.set("maxLng",
						df.format(Collections.max(Arrays.asList(lng_new2, lng_new3, lng_standard, lng_standard2))));
				directionBox.set("minLat",
						df.format(Collections.min(Arrays.asList(lat_new2, lat_new3, lat_standard, lat_standard2))));
				directionBox.set("maxLat",
						df.format(Collections.max(Arrays.asList(lat_new2, lat_new3, lat_standard, lat_standard2))));
//				System.out.println(directionBox.getString("minLng")+"/"+directionBox.getString("maxLng")+"/"+directionBox.getString("minLat")+"/"+directionBox.getString("maxLat"));
				
				resultBoxList.set(directionBox);
			}


		} catch (Exception e) {
			e.printStackTrace();
			return resultBoxList;
		}
		return resultBoxList;
	}

}
