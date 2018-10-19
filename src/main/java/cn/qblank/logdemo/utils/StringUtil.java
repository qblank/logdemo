package cn.qblank.logdemo.utils;

import java.security.MessageDigest;
import java.util.Random;


import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

public class StringUtil { 

	private final static byte[] hex = "0123456789ABCDEF".getBytes();
	
	private static final String LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	/**
	 * 
	 */
	private static final String ACCNO_LETTERS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-";
	
	public static String quote(String s) {
		return new StringBuffer("'").append(s).append("'").toString();
	}

	public static final String getMD5(String key) throws Exception {
		String password = null;
		byte[] data;
		if (StringUtils.isEmpty(key)||"".equals(key.trim())) {
			return null;
		}
		MessageDigest msg = MessageDigest.getInstance("MD5");
		data = key.trim().getBytes("UnicodeLittleUnmarked");
		byte[] datas = msg.digest(data);
		password = new BASE64Encoder().encode(datas);
		return password.toUpperCase();
	}

	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str){
		 for (int i = str.length(); --i >= 0;){   
			 if (!Character.isDigit(str.charAt(i))){
				 return false;
			 }
		 }
		 return true;
	}
	
	public static String bytesToHexString(byte[] b) {
		byte[] buff = new byte[2 * b.length];
		for (int i = 0; i < b.length; i++) {
			buff[2 * i] = hex[(b[i] >> 4) & 0x0f];
			buff[2 * i + 1] = hex[b[i] & 0x0f];
		}
		return new String(buff);
	}
	
	/**
	 *
	 * 
	 * @param length
	 * @return
	 */
	public static String getNoString(int length) {
		Random random = new Random();
		int upLimit = 10;
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < length; i++) {
			buffer.append(Integer.toString(random.nextInt(upLimit)));
		}
		return buffer.toString();
	}
	
	/**
	 * 
	 * @param length
	 * @return
	 */
	public static String getRandomString(int length) {
		StringBuffer code = new StringBuffer();
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			int index = random.nextInt(LETTERS.length());
			code.append(LETTERS.charAt(index));
		}
		return code.toString();
	}
	
	/**
	 * 
	 * @param accNo
	 * @return
	 */
	public static boolean isCorrectAccNo(String accNo) {
		if (accNo == null) {
			return false;
		}
		for (int i = 0; i < accNo.length(); i++) {
			if (ACCNO_LETTERS.indexOf(accNo.charAt(i)) == -1) {
				return false;
			}
		}
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println(StringUtil.getMD5("123456"));
		System.out.println(DigestUtils.md5DigestAsHex("123456".getBytes()));
		System.out.println(new BASE64Encoder().encode(DigestUtils.md5DigestAsHex("123456".trim().getBytes("UnicodeLittleUnmarked")).getBytes()));
	}

}
