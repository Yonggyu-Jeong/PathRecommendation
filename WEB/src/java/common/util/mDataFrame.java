package common.util;

import java.util.ArrayList;

import common.collection.ABox;
import common.collection.ABoxList;
import smile.data.DataFrame;
import smile.data.IndexDataFrame;

public class mDataFrame {
	public Double[][] getDoubleFrame(ABoxList<ABox> aBoxList, ABox userABox) {
		Double[][] dataFrame;

		ArrayList<Integer> locateTagList = null;
		ArrayList<Integer> userTagList = null;
				
		final int SIZE = 5;
		try {
			dataFrame = new Double[aBoxList.size()][SIZE];
			for(int i=0; i<aBoxList.size(); i++) {
				userTagList = new ArrayList<Integer>();
				locateTagList = new ArrayList<Integer>();
				
				for(int j=1; j<SIZE; j++) {
					locateTagList.add(Integer.parseInt(aBoxList.get(i).getString("tag"+j).substring(2)));
					userTagList.add(Integer.parseInt(userABox.getString("tag"+j).substring(2)));										
				}
				



						
						
						
		//		dataFrame[i][1] = aBoxList.get(i).get("");
			//	dataFrame[i][2] = aBoxList.get(i).get("");
			//	dataFrame[i][3] = aBoxList.get(i).get("");
		//		dataFrame[i][4] = aBoxList.get(i).get("");
			}
			
		


		} catch (Exception e) {
			return null;
		}
		return dataFrame;

	}

}
