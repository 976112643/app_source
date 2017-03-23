package com.wq.bean;

import com.wq.support.utils.EmptyDeal;

/**
 * 登录信息
 * 
 * @author WQ 上午10:31:08
 * 
 */
public class LoginInfo extends SerializableBean {
	
	
	/**
	 * {
        "id": "5",            //用户id
        "type": "1",          //用户类型 1 - 普通会员 2 - 商家
        "sex": "2",           //性别  0-表示未设置  1-女  2-男
        "age": "0",                     //年龄
        "headimg": "",                  //头像图片全路径
        "email": "",                    //邮箱
        "mobile": "13377850432",        //注册手机号
        "nickname": "aaa",              //昵称  
        "lat": "0.0000000000",
        "lng": "0.0000000000",
        "user_key": "67aa79d1325e74c350430a9e1a31acd2"
    }
	 *  */
	private String type;

	private String sex;

	private String age;

	private String headimg;

	private String email;

	private String mobile;

	private String nickname="";

	private String lat;

	private String lng;

	private String user_key;
	private String member_id;
	private String birthday;
	private String description;
	public String signature;//个性签名
	public int sound_on_off;
	public int shake_on_off;
	public int system_message_on_off;
	public int night_mode_on_off;
	public boolean isSOUND() {
		return sound_on_off==1;
	}

	public boolean isVIBRATE() {
		return shake_on_off==1;
	}

	public boolean isNofity() {
		return system_message_on_off==1;
	}

	public boolean isNight() {
		return night_mode_on_off==1;
	}
	public void setSound_on_off(boolean sound_on_off) {
		this.sound_on_off = sound_on_off?1:0;
	}

	public void setShake_on_off(boolean shake_on_off) {
		this.shake_on_off = shake_on_off?1:0;
	}

	public void setSystem_message_on_off(boolean system_message_on_off) {
		this.system_message_on_off = system_message_on_off?1:0;
	}

	public void setNight_mode_on_off(boolean night_mode_on_off) {
		this.night_mode_on_off = night_mode_on_off?1:0;
	}

	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public boolean isShangJia(){
		return "2".equals(type);
	}
	public String getMember_id() {
		return member_id;
	}



	public void setMember_id(String member_id) {
		this.member_id = member_id;
	}



	public boolean hasNickname(){
		return !EmptyDeal.isEmpy(nickname);
	}
	


	public String getBirthday() {
		return birthday;
	}





	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}





	public String getDescription() {
		return description==null?signature:description;
	}





	public void setDescription(String description) {
		this.description = description;
	}




	public void setId(String id) {
		if(id==null && member_id==null){
			member_id=this.id;
		}
		this.id = id;
	}
	public String getId() {
		return isValid()?id:member_id;
	}





	public String getType() {
		return type;
	}





	public void setType(String type) {
		this.type = type;
	}





	public String getSex() {
		return sex;
	}

	/**
	 * 是否是男的
	 * @return
	 */
	public boolean isMan(){
		return "2".equals(sex)
				;
	}




	public void setSex(String sex) {
		this.sex = sex;
	}





	public String getAge() {
		return age;
	}





	public void setAge(String age) {
		this.age = age;
	}





	public String getHeadimg() {
		return headimg;
	}





	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}





	public String getEmail() {
		return email;
	}





	public void setEmail(String email) {
		this.email = email;
	}





	public String getMobile() {
		return mobile;
	}





	public void setMobile(String mobile) {
		this.mobile = mobile;
	}





	public String getNickname() {
		return nickname;
	}





	public void setNickname(String nickname) {
		this.nickname = nickname;
	}





	public String getLat() {
		return lat;
	}





	public void setLat(String lat) {
		this.lat = lat;
	}





	public String getLng() {
		return lng;
	}





	public void setLng(String lng) {
		this.lng = lng;
	}





	public String getUser_key() {
		return user_key;
	}





	public void setUser_key(String user_key) {
		this.user_key = user_key;
	}





	public boolean isValid() {
		// return uid != null && tokey != null;
		return !EmptyDeal.isEmpy(id);
	}
}
