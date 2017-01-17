package com.maomovie.entity;

import java.io.Serializable;
import java.util.List;

/**
 *	影院上映影片信息
 */
public class MoviesOfCinemaEntity implements Serializable{


	/**
	 * movieName : 危城
	 * movieId : 247713
	 * shows : [{"sellPrice":33,"bus":"","park":"","tel":"028-87999481","img":"http://p0.meituan.net/deal/e5038e46db44fcb23d8aa34589897bc4256871.jpg","addr":"郫县创智东二路58号绿地缤纷城四楼（原华大影院）","lng":103.90188,"deals":[{"title":"38.1元，可乐（中）1杯+爆米花（小）1份+衍生品1个","price":38.1,"dealId":38906255,"dealUrl":"http://www.meituan.com/deal/38906255.html"}],"ct":"成都","areaId":510124,"dri":"","id":10994,"referencePrice":0,"area":"郫县","dealPrice":24.9,"deal":1,"sell":1,"suw":"","dis":"","plist":[{"dt":"2016-08-29","tm":"22:45","seqNo":"201608290067545","tp":"3D","ticketStatus":0,"showId":67545,"lang":"国语","embed":0,"th":"3号厅","reservedMin":15,"seatUrl":"http://maoyan.meituan.com/xuanzuo/shop/5131933/show/201608290067545/seats","sell":true,"sellPr":"33.0","showClosedSeat":1,"pr":"90.0"}],"feature":"","imax":0,"note":"","lat":30.788548,"nm":"成都华昕影院(原华大影院)","brd":"其它","ctid":59}]
	 */

	private DataBean data;

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public static class DataBean implements Serializable{
		private String movieName;
		private int movieId;
		/**
		 * sellPrice : 33
		 * bus :
		 * park :
		 * tel : 028-87999481
		 * img : http://p0.meituan.net/deal/e5038e46db44fcb23d8aa34589897bc4256871.jpg
		 * addr : 郫县创智东二路58号绿地缤纷城四楼（原华大影院）
		 * lng : 103.90188
		 * deals : [{"title":"38.1元，可乐（中）1杯+爆米花（小）1份+衍生品1个","price":38.1,"dealId":38906255,"dealUrl":"http://www.meituan.com/deal/38906255.html"}]
		 * ct : 成都
		 * areaId : 510124
		 * dri :
		 * id : 10994
		 * referencePrice : 0
		 * area : 郫县
		 * dealPrice : 24.9
		 * deal : 1
		 * sell : 1
		 * suw :
		 * dis :
		 * plist : [{"dt":"2016-08-29","tm":"22:45","seqNo":"201608290067545","tp":"3D","ticketStatus":0,"showId":67545,"lang":"国语","embed":0,"th":"3号厅","reservedMin":15,"seatUrl":"http://maoyan.meituan.com/xuanzuo/shop/5131933/show/201608290067545/seats","sell":true,"sellPr":"33.0","showClosedSeat":1,"pr":"90.0"}]
		 * feature :
		 * imax : 0
		 * note :
		 * lat : 30.788548
		 * nm : 成都华昕影院(原华大影院)
		 * brd : 其它
		 * ctid : 59
		 */

		private List<ShowsBean> shows;

		public String getMovieName() {
			return movieName;
		}

		public void setMovieName(String movieName) {
			this.movieName = movieName;
		}

		public int getMovieId() {
			return movieId;
		}

		public void setMovieId(int movieId) {
			this.movieId = movieId;
		}

		public List<ShowsBean> getShows() {
			return shows;
		}

		public void setShows(List<ShowsBean> shows) {
			this.shows = shows;
		}

		public static class ShowsBean implements Serializable{
			private int sellPrice;
			private String bus;
			private String park;
			private String tel;
			private String img;
			private String addr;
			private double lng;
			private String ct;
			private int areaId;
			private String dri;
			private int id;
			private int referencePrice;
			private String area;
			private double dealPrice;
			private int deal;
			private int sell;
			private String suw;
			private String dis;
			private String feature;
			private int imax;
			private String note;
			private double lat;
			private String nm;
			private String brd;
			private int ctid;
			/**
			 * title : 38.1元，可乐（中）1杯+爆米花（小）1份+衍生品1个
			 * price : 38.1
			 * dealId : 38906255
			 * dealUrl : http://www.meituan.com/deal/38906255.html
			 */

			private List<DealsBean> deals;
			/**
			 * dt : 2016-08-29
			 * tm : 22:45
			 * seqNo : 201608290067545
			 * tp : 3D
			 * ticketStatus : 0
			 * showId : 67545
			 * lang : 国语
			 * embed : 0
			 * th : 3号厅
			 * reservedMin : 15
			 * seatUrl : http://maoyan.meituan.com/xuanzuo/shop/5131933/show/201608290067545/seats
			 * sell : true
			 * sellPr : 33.0
			 * showClosedSeat : 1
			 * pr : 90.0
			 */

			private List<PlistBean> plist;

			public int getSellPrice() {
				return sellPrice;
			}

			public void setSellPrice(int sellPrice) {
				this.sellPrice = sellPrice;
			}

			public String getBus() {
				return bus;
			}

			public void setBus(String bus) {
				this.bus = bus;
			}

			public String getPark() {
				return park;
			}

			public void setPark(String park) {
				this.park = park;
			}

			public String getTel() {
				return tel;
			}

			public void setTel(String tel) {
				this.tel = tel;
			}

			public String getImg() {
				return img;
			}

			public void setImg(String img) {
				this.img = img;
			}

			public String getAddr() {
				return addr;
			}

			public void setAddr(String addr) {
				this.addr = addr;
			}

			public double getLng() {
				return lng;
			}

			public void setLng(double lng) {
				this.lng = lng;
			}

			public String getCt() {
				return ct;
			}

			public void setCt(String ct) {
				this.ct = ct;
			}

			public int getAreaId() {
				return areaId;
			}

			public void setAreaId(int areaId) {
				this.areaId = areaId;
			}

			public String getDri() {
				return dri;
			}

			public void setDri(String dri) {
				this.dri = dri;
			}

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public int getReferencePrice() {
				return referencePrice;
			}

			public void setReferencePrice(int referencePrice) {
				this.referencePrice = referencePrice;
			}

			public String getArea() {
				return area;
			}

			public void setArea(String area) {
				this.area = area;
			}

			public double getDealPrice() {
				return dealPrice;
			}

			public void setDealPrice(double dealPrice) {
				this.dealPrice = dealPrice;
			}

			public int getDeal() {
				return deal;
			}

			public void setDeal(int deal) {
				this.deal = deal;
			}

			public int getSell() {
				return sell;
			}

			public void setSell(int sell) {
				this.sell = sell;
			}

			public String getSuw() {
				return suw;
			}

			public void setSuw(String suw) {
				this.suw = suw;
			}

			public String getDis() {
				return dis;
			}

			public void setDis(String dis) {
				this.dis = dis;
			}

			public String getFeature() {
				return feature;
			}

			public void setFeature(String feature) {
				this.feature = feature;
			}

			public int getImax() {
				return imax;
			}

			public void setImax(int imax) {
				this.imax = imax;
			}

			public String getNote() {
				return note;
			}

			public void setNote(String note) {
				this.note = note;
			}

			public double getLat() {
				return lat;
			}

			public void setLat(double lat) {
				this.lat = lat;
			}

			public String getNm() {
				return nm;
			}

			public void setNm(String nm) {
				this.nm = nm;
			}

			public String getBrd() {
				return brd;
			}

			public void setBrd(String brd) {
				this.brd = brd;
			}

			public int getCtid() {
				return ctid;
			}

			public void setCtid(int ctid) {
				this.ctid = ctid;
			}

			public List<DealsBean> getDeals() {
				return deals;
			}

			public void setDeals(List<DealsBean> deals) {
				this.deals = deals;
			}

			public List<PlistBean> getPlist() {
				return plist;
			}

			public void setPlist(List<PlistBean> plist) {
				this.plist = plist;
			}

			public static class DealsBean implements Serializable{
				private String title;
				private double price;
				private int dealId;
				private String dealUrl;

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

				public double getPrice() {
					return price;
				}

				public void setPrice(double price) {
					this.price = price;
				}

				public int getDealId() {
					return dealId;
				}

				public void setDealId(int dealId) {
					this.dealId = dealId;
				}

				public String getDealUrl() {
					return dealUrl;
				}

				public void setDealUrl(String dealUrl) {
					this.dealUrl = dealUrl;
				}
			}

			public static class PlistBean implements Serializable{
				private String dt;
				private String tm;
				private String seqNo;
				private String tp;
				private int ticketStatus;
				private int showId;
				private String lang;
				private int embed;
				private String th;
				private int reservedMin;
				private String seatUrl;
				private boolean sell;
				private String sellPr;
				private int showClosedSeat;
				private String pr;

				public String getDt() {
					return dt;
				}

				public void setDt(String dt) {
					this.dt = dt;
				}

				public String getTm() {
					return tm;
				}

				public void setTm(String tm) {
					this.tm = tm;
				}

				public String getSeqNo() {
					return seqNo;
				}

				public void setSeqNo(String seqNo) {
					this.seqNo = seqNo;
				}

				public String getTp() {
					return tp;
				}

				public void setTp(String tp) {
					this.tp = tp;
				}

				public int getTicketStatus() {
					return ticketStatus;
				}

				public void setTicketStatus(int ticketStatus) {
					this.ticketStatus = ticketStatus;
				}

				public int getShowId() {
					return showId;
				}

				public void setShowId(int showId) {
					this.showId = showId;
				}

				public String getLang() {
					return lang;
				}

				public void setLang(String lang) {
					this.lang = lang;
				}

				public int getEmbed() {
					return embed;
				}

				public void setEmbed(int embed) {
					this.embed = embed;
				}

				public String getTh() {
					return th;
				}

				public void setTh(String th) {
					this.th = th;
				}

				public int getReservedMin() {
					return reservedMin;
				}

				public void setReservedMin(int reservedMin) {
					this.reservedMin = reservedMin;
				}

				public String getSeatUrl() {
					return seatUrl;
				}

				public void setSeatUrl(String seatUrl) {
					this.seatUrl = seatUrl;
				}

				public boolean isSell() {
					return sell;
				}

				public void setSell(boolean sell) {
					this.sell = sell;
				}

				public String getSellPr() {
					return sellPr;
				}

				public void setSellPr(String sellPr) {
					this.sellPr = sellPr;
				}

				public int getShowClosedSeat() {
					return showClosedSeat;
				}

				public void setShowClosedSeat(int showClosedSeat) {
					this.showClosedSeat = showClosedSeat;
				}

				public String getPr() {
					return pr;
				}

				public void setPr(String pr) {
					this.pr = pr;
				}
			}
		}
	}
}
