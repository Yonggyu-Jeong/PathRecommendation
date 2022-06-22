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
			ABox startDirectionBox = new ABox();
			ABox centerDirectionBox = new ABox();
			ABox goalDirectionBox = new ABox();

			startBox.setJson(aBox.get("start").toString());
			goalBox.setJson(aBox.get("goal").toString());

			double lng_start = startBox.getDouble("lng");
			double lat_start = startBox.getDouble("lat");
			double lng_goal = goalBox.getDouble("lng");
			double lat_goal = goalBox.getDouble("lat");
			double lng = lng_goal - lng_start;
			double lat = lat_goal - lat_start;	
			
			if (lat > 0) {
				startDirectionBox.set("minLat", df.format(lat_start - lat/4));
				startDirectionBox.set("maxLat", df.format(lat_start + lat/4));
				goalDirectionBox.set("minLat",  df.format(lat_goal - lat/4));
				goalDirectionBox.set("maxLat",  df.format(lat_goal + lat/4));
				centerDirectionBox.set("minLat",  df.format((lat_start + lat/4)+0.0000001));
				centerDirectionBox.set("maxLat",  df.format((lat_goal - lat/4)-0.0000001));
				
			} else {
				startDirectionBox.set("minLat", df.format(lat_start + lat/4));
				startDirectionBox.set("maxLat", df.format(lat_start - lat/4));
				goalDirectionBox.set("minLat",  df.format(lat_goal + lat/4));
				goalDirectionBox.set("maxLat",  df.format(lat_goal - lat/4));
				centerDirectionBox.set("minLat",  df.format((lat_goal - lat/4)+0.0000001));
				centerDirectionBox.set("maxLat",  df.format((lat_start + lat/4)-0.0000001));
			} 
			
			if (lng > 0) {
				startDirectionBox.set("minLng", df.format(lng_start - lng/4));
				startDirectionBox.set("maxLng", df.format(lng_start + lng/4));
				goalDirectionBox.set("minLng",  df.format(lng_goal - lng/4));
				goalDirectionBox.set("maxLng",  df.format(lng_goal + lng/4));
				centerDirectionBox.set("minLng",  df.format((lng_start + lng/4)+0.0000001));
				centerDirectionBox.set("maxLng",  df.format((lng_goal - lng/4)-0.0000001));

			} else {
				startDirectionBox.set("minLng", df.format(lng_start + lng/4));
				startDirectionBox.set("maxLng", df.format(lng_start - lng/4));
				goalDirectionBox.set("minLng",  df.format(lng_goal + lng/4));
				goalDirectionBox.set("maxLng",  df.format(lng_goal - lng/4));
				centerDirectionBox.set("minLng",  df.format((lng_goal - lng/4)+0.0000001));
				centerDirectionBox.set("maxLng",  df.format((lng_start + lng/4)-0.0000001));
			} 	
			
			resultBoxList.add(startDirectionBox);
			resultBoxList.add(centerDirectionBox);
			resultBoxList.add(goalDirectionBox);

		} catch (Exception e) {
			e.printStackTrace();
			return resultBoxList;
		}
		return resultBoxList;
	}

}
