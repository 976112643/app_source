package com.widget.banner;

import java.util.List;


/**
 * 热门数据实体�?
 * 
 * @author WQ 上午10:05:29
 */
public class Rem {

	public List<BANNER> banner;// 轮播�?

	public static class BANNER {
		
		public String banner_id;
		public String image;
		public String type;
		public String title;
		public String name;

		
		
		
		
		
		public BANNER(String image) {
			super();
			this.image = image;
		}

		@Override
		public String toString() {
			return image;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}


		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	
}
