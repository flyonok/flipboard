package video;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Video {
	
	private String videoid;
	public String getVideoid() { return videoid; }
	public void setVideoid(String videoid) { this.videoid = videoid; }
	
	private String date;
	public String getDate() { return date; }
	public void setDate(String date) { this.date = date; }
	
	private String pic;
	public String getPic() { return pic; }
	public void setPic(String pic) { this.pic = pic; }
	
	private String title;
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	
	private List<Integer> args= new ArrayList<Integer>();
	// private int index = 0;
	public void setArgs(List<Integer> args) {this.args = args;}
	// public String getArgs() { return args.toArray().toString(); }
	public void addArg(int arg) { args.add(arg);}
	
	private String test = null;

	@Override
	public String toString() {
		return videoid + ":" + date + ":" + pic + ":" + title;
	}
		
	public static void main(String[] args) {
		Gson gson = new Gson();
		List<Video> videos = new ArrayList<Video>();
		List<Integer> resList = new ArrayList<Integer>();
		for (int i = 0; i < 10; i++) {
			Video video = new Video();
			video.setVideoid("video"+ i);
			video.setTitle("title" + i);
			video.setDate("date" + i);
			video.setPic("pic" + i);
			videos.add(video);
			for (int j = 0; j < i; j++)
			{
				video.addArg(j);
			}
			resList.add(i);
		}
		System.out.println(resList.toString());
		String str = gson.toJson(videos);
		System.out.println(str);
		String resStr = gson.toJson(resList);
		System.out.println(resStr);
		List<Integer> list = gson.fromJson(resStr, new TypeToken<List<Integer>>(){}.getType());
		for (int i : list){
			System.out.println(i);
		}
	}
	
}
