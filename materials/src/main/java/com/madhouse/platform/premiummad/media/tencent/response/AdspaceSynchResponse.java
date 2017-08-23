package com.madhouse.platform.premiummad.media.tencent.response;

import java.util.List;

/**
 * 广告位信息同步响应
 */
public class AdspaceSynchResponse extends TencentResponse {

	private Msg ret_msg;

	public Msg getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(Msg ret_msg) {
		this.ret_msg = ret_msg;
	}

	public class Msg {
		private Integer total;
		private Integer count;
		private List<Record> records;

		public Msg() {
		}

		public Integer getTotal() {
			return total;
		}

		public void setTotal(Integer total) {
			this.total = total;
		}

		public Integer getCount() {
			return count;
		}

		public void setCount(Integer count) {
			this.count = count;
		}

		public List<Record> getRecords() {
			return records;
		}

		public void setRecords(List<Record> records) {
			this.records = records;
		}

		public class Record {
			private String location_id;
			private String location_name;
			private String style;
			private Double cpm_start_price;
			private List<String> block_vocation;
			private List<String> allow_file;
			private String enable;
			private String screen;
			private String review_pic;
			private String loc_quality;
			private String product;
			private String product_name;

			public String getLocation_id() {
				return location_id;
			}

			public void setLocation_id(String location_id) {
				this.location_id = location_id;
			}

			public String getLocation_name() {
				return location_name;
			}

			public void setLocation_name(String location_name) {
				this.location_name = location_name;
			}

			public String getStyle() {
				return style;
			}

			public void setStyle(String style) {
				this.style = style;
			}

			public Double getCpm_start_price() {
				return cpm_start_price;
			}

			public void setCpm_start_price(Double cpm_start_price) {
				this.cpm_start_price = cpm_start_price;
			}

			public String getEnable() {
				return enable;
			}

			public void setEnable(String enable) {
				this.enable = enable;
			}

			public String getProduct() {
				return product;
			}

			public void setProduct(String product) {
				this.product = product;
			}

			public String getProduct_name() {
				return product_name;
			}

			public void setProduct_name(String product_name) {
				this.product_name = product_name;
			}

			public String getBlock_vocation() {
				return block_vocation.isEmpty() ? "" : block_vocation.toString();
			}

			public void setBlock_vocation(List<String> block_vocation) {
				this.block_vocation = block_vocation;
			}

			public String getAllow_file() {
				return allow_file.isEmpty() ? "" : allow_file.toString();
			}

			public void setAllow_file(List<String> allow_file) {
				this.allow_file = allow_file;
			}

			public String getScreen() {
				return screen;
			}

			public void setScreen(String screen) {
				this.screen = screen;
			}

			public String getReview_pic() {
				return review_pic;
			}

			public void setReview_pic(String review_pic) {
				this.review_pic = review_pic;
			}

			public String getLoc_quality() {
				return loc_quality;
			}

			public void setLoc_quality(String loc_quality) {
				this.loc_quality = loc_quality;
			}

		}
	}
}
