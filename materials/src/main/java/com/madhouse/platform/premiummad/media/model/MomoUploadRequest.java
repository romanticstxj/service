package com.madhouse.platform.premiummad.media.model;

import java.util.List;

public class MomoUploadRequest {

    /**
     * dspid : 1234
     * cid : 1234
     * adid : 1234
     * crid : 1234
     * advertiser_id : 1234
     * advertiser_name : 路易威登
     * quality_level : 1
     * display_labels : ["购物"]
     * cat : ["101100","116100"]
     * native_creative : {"native_format":"FEED_APP_ANDROID_LARGE_IMG","desc":"自我发现之旅","logo":{"url":"http://www.immomo.com/static/w5/img/website/map.jpg","width":250,"height":250},"image":[{"url":"http://www.immomo.com/static/w5/img/website/map.jpg","width":520,"height":260}],"landingpage_url":"http://a.b.com","app_download_url":"http://a.b.com/apk","app_intro_url":"http://a.b.com/intro","app_ver":"1.0","app_size":1}
     * uptime : 1458108615
     * expiry_date : 2016-04-20 24:00:00
     * sign : 7B2254C6C491B71A687D989C91B6657E
     */
    private String dspid;
    private String cid;
    private String adid;
    private String crid;
    private String advertiser_id;
    private String advertiser_name;
    private int quality_level;
    
    /**
     * native_format : FEED_APP_ANDROID_LARGE_IMG
     * desc : 自我发现之旅
     * logo : {"url":"http://www.immomo.com/static/w5/img/website/map.jpg","width":250,"height":250}
     * image : [{"url":"http://www.immomo.com/static/w5/img/website/map.jpg","width":520,"height":260}]
     * landingpage_url : http://a.b.com
     * app_download_url : http://a.b.com/apk
     * app_intro_url : http://a.b.com/intro
     * app_ver : 1.0
     * app_size : 1
     */
    private NativeCreativeBean native_creative;
    private long uptime;
    private String expiry_date;
    private String sign;
    private List<String> display_labels;
    private List<String> cat;

	public String getDspid() {
		return dspid;
	}

	public void setDspid(String dspid) {
		this.dspid = dspid;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getAdid() {
		return adid;
	}

	public void setAdid(String adid) {
		this.adid = adid;
	}

	public String getCrid() {
		return crid;
	}

	public void setCrid(String crid) {
		this.crid = crid;
	}

	public String getAdvertiser_id() {
		return advertiser_id;
	}

	public void setAdvertiser_id(String advertiser_id) {
		this.advertiser_id = advertiser_id;
	}

	public String getAdvertiser_name() {
		return advertiser_name;
	}

	public void setAdvertiser_name(String advertiser_name) {
		this.advertiser_name = advertiser_name;
	}

	public int getQuality_level() {
		return quality_level;
	}

	public void setQuality_level(int quality_level) {
		this.quality_level = quality_level;
	}

	public NativeCreativeBean getNative_creative() {
		return native_creative;
	}

	public void setNative_creative(NativeCreativeBean native_creative) {
		this.native_creative = native_creative;
	}

	public long getUptime() {
		return uptime;
	}

	public void setUptime(long uptime) {
		this.uptime = uptime;
	}

	public String getExpiry_date() {
		return expiry_date;
	}

	public void setExpiry_date(String expiry_date) {
		this.expiry_date = expiry_date;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public List<String> getDisplay_labels() {
		return display_labels;
	}

	public void setDisplay_labels(List<String> display_labels) {
		this.display_labels = display_labels;
	}

	public List<String> getCat() {
		return cat;
	}

	public void setCat(List<String> cat) {
		this.cat = cat;
	}

    public static class NativeCreativeBean {
        private String native_format;
        private String desc;
        private String title;
        
        /**
         * url : http://www.immomo.com/static/w5/img/website/map.jpg
         * width : 250
         * height : 250
         */
        private LogoBean logo;
        private String landingpage_url;
        private String app_download_url;
        private String app_intro_url;
        private String app_ver;
        private int app_size;
        private String card_title;
        private String card_desc;
        private ImageBean card_img;

        public String getCard_title() {
			return card_title;
		}

		public void setCard_title(String card_title) {
			this.card_title = card_title;
		}

		public String getCard_desc() {
			return card_desc;
		}

		public void setCard_desc(String card_desc) {
			this.card_desc = card_desc;
		}

		public ImageBean getCard_img() {
			return card_img;
		}

		public void setCard_img(ImageBean card_img) {
			this.card_img = card_img;
		}

		public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        /**
         * url : http://www.immomo.com/static/w5/img/website/map.jpg
         * width : 520
         * height : 260
         */

        private List<ImageBean> image;
        
        private List<VideoBean> video;
        
        public List<VideoBean> getVideo() {
			return video;
		}

		public void setVideo(List<VideoBean> video) {
			this.video = video;
		}

		public String getNative_format() {
            return native_format;
        }

        public void setNative_format(String native_format) {
            this.native_format = native_format;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public LogoBean getLogo() {
            return logo;
        }

        public void setLogo(LogoBean logo) {
            this.logo = logo;
        }

        public String getLandingpage_url() {
            return landingpage_url;
        }

        public void setLandingpage_url(String landingpage_url) {
            this.landingpage_url = landingpage_url;
        }

        public String getApp_download_url() {
            return app_download_url;
        }

        public void setApp_download_url(String app_download_url) {
            this.app_download_url = app_download_url;
        }

        public String getApp_intro_url() {
            return app_intro_url;
        }

        public void setApp_intro_url(String app_intro_url) {
            this.app_intro_url = app_intro_url;
        }

        public String getApp_ver() {
            return app_ver;
        }

        public void setApp_ver(String app_ver) {
            this.app_ver = app_ver;
        }

        public int getApp_size() {
            return app_size;
        }

        public void setApp_size(int app_size) {
            this.app_size = app_size;
        }

        public List<ImageBean> getImage() {
            return image;
        }

        public void setImage(List<ImageBean> image) {
            this.image = image;
        }

        public static class LogoBean {
            private String url;
            private int width;
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            @Override
            public String toString() {
                return "LogoBean{" +
                        "url='" + url + '\'' +
                        ", width=" + width +
                        ", height=" + height +
                        '}';
            }
        }

        public static class ImageBean {
            private String url;
            private int width;
            private int height;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }

            @Override
            public String toString() {
                return "ImageBean{" +
                        "url='" + url + '\'' +
                        ", width=" + width +
                        ", height=" + height +
                        '}';
            }
        }
        
        public static class VideoBean {
            private String url;
            private ImageBean cover_img;

            public String getUrl() {
				return url;
			}
			public void setUrl(String url) {
				this.url = url;
			}
			public ImageBean getCover_img() {
				return cover_img;
			}
			public void setCover_img(ImageBean cover_img) {
				this.cover_img = cover_img;
			}

			@Override
			public String toString() {
				return "VideoBean [url=" + url + ", cover_img=" + cover_img
						+ "]";
			}
			
        }
        

        @Override
        public String toString() {
            return "NativeCreativeBean{" +
                    "native_format='" + native_format + '\'' +
                    ", desc='" + desc + '\'' +
                    ", logo=" + logo +
                    ", landingpage_url='" + landingpage_url + '\'' +
                    ", app_download_url='" + app_download_url + '\'' +
                    ", app_intro_url='" + app_intro_url + '\'' +
                    ", app_ver='" + app_ver + '\'' +
                    ", app_size=" + app_size +
                    ", image=" + image +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "MomoUploadRequest{" +
                "dspid='" + dspid + '\'' +
                ", cid='" + cid + '\'' +
                ", adid='" + adid + '\'' +
                ", crid='" + crid + '\'' +
                ", advertiser_id='" + advertiser_id + '\'' +
                ", advertiser_name='" + advertiser_name + '\'' +
                ", quality_level=" + quality_level +
                ", native_creative=" + native_creative +
                ", uptime=" + uptime +
                ", expiry_date='" + expiry_date + '\'' +
                ", sign='" + sign + '\'' +
                ", display_labels=" + display_labels +
                ", cat=" + cat +
                '}';
    }
}
