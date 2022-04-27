package common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import common.collection.ABox;
import common.collection.ABoxList;
import smile.data.DataFrame;
import smile.data.IndexDataFrame;

public class mDataFrame {
	public static Double[][] getDoubleFrame(ABoxList<ABox> aBoxList, ABox userABox) {

		ABox result = new ABox();
		Double[][] dataFrame = null;

		ABoxList<ArrayList> tagList = new ABoxList<ArrayList>();
		tagList.add(new ArrayList(Arrays.asList("00", "01", "02", "03", "04", "05", "06", "07", "08", "09")));
		tagList.add(new ArrayList(Arrays.asList("10", "11", "12", "13", "14", "15", "16", "17", "18", "19")));
		tagList.add(new ArrayList(Arrays.asList("20", "21", "22", "23", "24", "25", "26", "27", "28", "29")));
		tagList.add(new ArrayList(Arrays.asList("30", "31", "32", "33", "34", "35", "36", "37", "38", "39")));
		tagList.add(new ArrayList(Arrays.asList("40", "41", "42", "43", "44", "45", "46", "47", "48", "49")));
		tagList.add(new ArrayList(Arrays.asList("50", "51", "52", "53", "54", "55", "56", "57", "58", "59")));
		tagList.add(new ArrayList(Arrays.asList("60", "61", "62", "63", "64", "65", "66", "67", "68", "69")));
		tagList.add(new ArrayList(Arrays.asList("70", "71", "72", "73", "74", "75", "76", "77", "78", "79")));
		tagList.add(new ArrayList(Arrays.asList("80", "81", "82", "83", "84", "85", "86", "87", "88", "89")));
		tagList.add(new ArrayList(Arrays.asList("90", "91", "92", "93", "94", "95", "96", "97", "98", "99")));
		
		ArrayList<String> locateTagList = null;
		ArrayList<String> userTagList = null;
				
		final int SIZE = 5;
		try {
			dataFrame = new Double[aBoxList.size()][SIZE+1];
			for(int i=0; i<aBoxList.size(); i++) {
				userTagList = new ArrayList<String>();
				locateTagList = new ArrayList<String>();
				dataFrame[i][0] = (double) aBoxList.get(i).getInt("label_seq");
				dataFrame[i][SIZE] = (double) aBoxList.get(i).getDouble("star"); 
				
				for(int j=1; j<SIZE; j++) {
					dataFrame[i][j] = 0.1;
					locateTagList.add(aBoxList.get(i).getString("tag"+j).substring(2));
					userTagList.add(userABox.getString("tag"+j).substring(2));									
				}
				for(int j=1; j<5; j++) {
					if(locateTagList.contains(userTagList.get(j-1))) {
						dataFrame[i][j] += 1.0;
						dataFrame[i][SIZE] += 1.0;
						
					} else {
						for(int k=0; k<4; k++) {
							if(locateTagList.get(j-1).substring(0, 0).equals(userTagList.get(k).substring(0, 0))) {
								dataFrame[i][j] += 0.5;
								dataFrame[i][SIZE] += 0.5;
							}
						}	
					}
				}
			}
			result.set("data", dataFrame);
		} catch (Exception e) {
			e.printStackTrace();
			return dataFrame;
		}
		return dataFrame;

	}

}
