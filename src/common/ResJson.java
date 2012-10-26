package common;

import java.util.ArrayList;
import java.util.List;

// resouce json convert
public class ResJson {
	private List<Integer> video = null /* new ArrayList<Integer>() */;
	public void addVideo(int id) { 
		if (video == null ) {
			video = new ArrayList<Integer>();
	}
		video.add(id);
	}
	// Big horizontal screen, width > 720 and height > 720
	private List<Integer> bh = null /* new ArrayList<Integer>() */ ;
	// Big Vertical screen
	private List<Integer> bv = null /* new ArrayList<Integer>() */;
	
	// Middle horizontal screen, width > 320 and height > 320
	private List<Integer> sh = null /* new ArrayList<Integer>() */;
	// Middle Vertical screen
	private List<Integer> sv = null /* new ArrayList<Integer>() */;
	
	private List<Integer> res = null; // 图片资源，不做他用
	
	/*private int resCnt = 0; // 记录资源个数
	public int getResCnt() { return resCnt; }*/
	
	
	public void addPicture(int id, int width, int height) {
		// resCnt++;
		if ( (width > 720)  && (height > 720)) {
			if (bh == null) {
				bh = new ArrayList<Integer>();
			}
			bh.add(id);
			if (bv == null) {
				bv = new ArrayList<Integer>();
			}
			bv.add(id);
			return ;
		}
		
		if ( (width > 360) && (height > 360) ) {
			if (sh == null) {
				sh = new ArrayList<Integer>();
			}
			sh.add(id);
			if (sv == null) {
				sv = new ArrayList<Integer>();
			}
			sv.add(id);
			return ;
		}
		
		if (res == null)
			res = new ArrayList<Integer>();
		res.add(id);
	}
}
