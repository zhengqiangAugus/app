package com.zq.app.util;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressLint("DefaultLocale")
public class CommUtil {
	public static boolean isNull(Object obj){
		if(obj!=null&&!"null".equals(obj)){
			if(obj.toString().trim().length()>0){
				return false;
			}
		}
		return true;
	}
	
	public static boolean checkIdCard(String idCard){
		boolean result = true;
		if(isNull(idCard) || !(idCard.trim().length() == 15 || idCard.trim().length() == 18)){
			result = false;
		}else{
			//var city= {11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",21:"辽宁",22:"吉林",23:"黑龙江 ",31:"上海",32:"江苏",33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",42:"湖北 ",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",51:"四川",52:"贵州",53:"云南",54:"西藏 ",61:"陕西",62:"甘肃",63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外 "};
			String city = "11,12,13,14,15,21,22,23,31,32,33,34,35,36,37,41,42,43,44,45,46,50,51,52,53,54,61,62,63,64,65,71,81,82,91";
			List<String> citys = Arrays.asList(city.split(","));
			String start_two = idCard.substring(0,2);
			if(!citys.contains(start_two)){
				result = false;
			}else{
				if(idCard.length()==18){
					List<String> code = new ArrayList<String>();
					for(int i =0;i<idCard.length();i++){
						code.add(idCard.charAt(i)+"");
					}
					//∑(ai×Wi)(mod 11)
		            //加权因子
		            int[] factor = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		            //校验位
		            String[] parity = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
		            int sum = 0;
		            int ai = 0;
		            int wi = 0;
		            for (int i = 0; i < 17; i++){
		                ai = Integer.parseInt(code.get(i));
		                wi = factor[i];
		                sum += ai * wi;
		            }
		            String last = parity[sum % 11];
		            if(!last.equals(code.get(17))){
		            	result = false;
		            }
				}
			}
		}
		return result;
	}
	
	public final static String MD5(String s) {
        char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};       
        try {
            byte[] btInput = s.getBytes();
            // 获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            // 使用指定的字节更新摘要
            mdInst.update(btInput);
            // 获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str).toLowerCase();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
	
	public static Bitmap drawable2Bitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof NinePatchDrawable) {
            Bitmap bitmap = Bitmap  .createBitmap(drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight(),drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  : Bitmap.Config.RGB_565);  
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());  
            drawable.draw(canvas);
            return bitmap;
        } else {
            return null;
        }
    }
    
	public static InputStream Bitmap2InputStream(Bitmap bm, int quality) {  
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos); 
        InputStream is = new ByteArrayInputStream(baos.toByteArray());  
        return is;  
    }
	
	public static byte[] bitmap2ByteArray(Bitmap bm, int quality){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, quality, baos); 
        return baos.toByteArray();
	}
}
