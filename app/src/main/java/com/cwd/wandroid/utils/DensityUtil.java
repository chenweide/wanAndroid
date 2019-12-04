package com.cwd.wandroid.utils;

import android.content.Context;

/**
 * Created by Administrator on 2016/1/25.
 */
public class DensityUtil {

  public static   DensityUtil densityUtil ;

    public  synchronized  static DensityUtil getInstance(){
        if (densityUtil==null){
            densityUtil = new DensityUtil();
        }
        return densityUtil;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * convert px to its equivalent sp
     *
     * 将px转换为sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * convert sp to its equivalent px
     *
     * 将sp转换为px
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


    private static final double EARTH_RADIUS = 6378137.0;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，
     *
     * @param lat1 经度
     * @param lng1 维度
     * @param lat2 经度
     * @param lng2 维度
     * @return 距离：单位为米
     */
    public static String DistanceOfTwoPoints(double lat1, double lng1,
                                             double lat2, double lng2) {

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        String result;
        if(s > 1000.0){
            if(Math.floor(s / 1000) > 500){
                result = ">" + 500 + "km";
            }else{
                result = Math.floor(s / 1000) + "km";
            }
        }else{
            result = s + "m";
        }
        return result;
    }
//转换成金钱格式
    public static String alterMoney(String money_s) {
        if (!money_s.equals("0")) {
            StringBuffer money = new StringBuffer();
            String money_1 = Float.parseFloat(money_s) / 100 + "" ;
            String money_ary[] =money_1.split("\\.");
            String int_money = money_ary[0];
            money.append(int_money + "");
            for (int i = money.length() - 3; i > 0; i = i - 3) {
                money.insert(i, ",");
            }
            return  "" + money + "." + (money_ary[1].length() >= 2 ? money_ary[1] : (money_ary[1] + 0)) ;
        }else {
            return "0.00";
        }
    }

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，
     *
     * @param lat1 经度
     * @param lng1 维度
     * @param lat2 经度
     * @param lng2 维度
     * @return 距离：单位为米
     */
    public static double DistanceOfTwoPoints1(double lat1, double lng1,
                                             double lat2, double lng2) {

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;

        return s;
    }

}
