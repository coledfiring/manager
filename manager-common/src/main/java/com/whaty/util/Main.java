package com.whaty.util;


import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import com.whaty.handler.SiemensS7NetHandler;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.util.LinkedHashtable;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) throws Exception {
        //System.out.println((byte)Integer.parseInt("70") / 2.5625);
        Map map = new HashMap();
        new SiemensS7NetHandler("220.194.141.5", 20080, e -> {
            map.put("w2ktGy", Math.abs(e.ReadFloat("DB1.600").Content)  + "Mpa");
            map.put("w2ktHy", Math.abs(e.ReadFloat("DB1.604").Content)  + "Mpa");
            map.put("w2ktGW", Math.abs(e.ReadFloat("DB1.608").Content));
            map.put("w2ktHw", Math.abs(e.ReadFloat("DB1.612").Content));
            map.put("w2dnGy", Math.abs(e.ReadFloat("DB1.600").Content) + "Mpa");
            map.put("w2dnHy", Math.abs(e.ReadFloat("DB1.604").Content) + "Mpa");
            map.put("w2dnGw", Math.abs(e.ReadFloat("DB1.608").Content));
            map.put("w2dnHw", Math.abs(e.ReadFloat("DB1.612").Content));
        }).handle();
        System.out.println(map);
       new SiemensS7NetHandler("220.194.141.5", 20080, e-> {
           System.out.println(e.ReadFloat("DB1.600").Content);
           System.out.println(e.ReadFloat("DB1.624").Content);
           System.out.println(keepDigit(e.ReadDouble("DB1.624").Content, 2));
          // e.Write("DB1.352", Byte.parseByte(String.valueOf(Math.round(Double.parseDouble("60") / 2.5625))));
           //System.out.println(getBinaryStrFromByte(e.ReadByte("M14").Content));
           /*System.out.println(Byte.parseByte("66"));
           System.out.println(Byte.parseByte("61"));
           System.out.println(Short.parseShort("100"));
           System.out.println(Float.parseFloat("50.0"));
           System.out.println(Float.parseFloat("19.0"));
           System.out.println(Float.parseFloat("45.0"));
          System.out.println("一网泵手动给定频率值" + e.ReadByte("DB1.172").Content);
           System.out.println("一网供回水实际压差" + e.ReadByte("DB1.384").Content);
           System.out.println("一网泵切泵时间间隔" + e.ReadInt16("DB1.74").Content);
           System.out.println("一网泵频率上限" + e.ReadFloat("DB1.184").Content);
           System.out.println("一网泵频率下限" + e.ReadFloat("DB1.188").Content);
           System.out.println("一网泵增泵频率" + e.ReadFloat("DB1.192").Content);
           System.out.println("一网泵减泵频率" + e.ReadFloat("DB1.196").Content);
           System.out.println("一网供回水设定压差" + e.ReadFloat("DB1.176").Content);
           System.out.println("一网供回水压差死区" + e.ReadFloat("DB1.180").Content);
           System.out.println("一网频率增量" + e.ReadFloat("DB1.8").Content);
           System.out.println("一网增量周期" + e.ReadInt16("DB1.72").Content);*/
           /*System.out.println("一网泵手动给定频率值" + e.ReadByte("DB1.150").Content.toString());
           System.out.println("一网供回水实际压差" + e.ReadFloat("DB1.508").Content.toString());
           System.out.println("一网泵切泵时间间隔" + e.ReadInt16("DB1.118").Content.toString());
           System.out.println("一网泵频率上限" + e.ReadFloat("DB1.48").Content.toString());
           System.out.println("一网泵频率下限" + e.ReadFloat("DB1.52").Content.toString());
           System.out.println("一网泵增泵频率" + e.ReadFloat("DB1.56").Content.toString());
           System.out.println("一网泵减泵频率" + e.ReadFloat("DB1.60").Content.toString());
           System.out.println("一网供回水设定压差" + e.ReadFloat("DB1.100").Content.toString());
           System.out.println("一网供回水压差死区" + e.ReadFloat("DB1.104").Content.toString());
           System.out.println("一网频率增量" + e.ReadFloat("DB1.64").Content.toString());
           System.out.println("一网增量周期" + e.ReadInt16("DB1.116").Content.toString());*/
          /* System.out.println("水箱液位高限量程设定" + e.ReadFloat("DB1.68").Content.toString());
           System.out.println("水箱液位低限量程设定" + e.ReadFloat("DB1.76").Content.toString());
           System.out.println("膨胀水箱液位高限量程设定" + e.ReadFloat("DB1.84").Content.toString());
           System.out.println("膨胀水箱液位低限量程设定" + e.ReadFloat("DB1.88").Content.toString());*/
        }).handle();
       /* SiemensS7Net siemens_net = new SiemensS7Net(SiemensPLCS.S200Smart,"220.194.141.5");
        siemens_net.setPort(10080);
        OperateResult connect=siemens_net.ConnectServer();
        if(connect.IsSuccess){
            System.out.println("connect success!");
        }else{
            System.out.println("failed:"+connect.Message);
        }
       byte b = siemens_net.ReadByte("M17").Content;
        byte b1 = siemens_net.ReadByte("I0").Content;
        System.out.println(getBinaryStrFromByte((byte)-64));
        System.out.println(b1);
        //-------------------------------------锅炉--------------------------------------
        System.out.println(siemens_net.ReadDouble("DB1.600").Content);
        float gl1_hy=siemens_net.ReadFloat("DB1.200").Content;//1#炉进压（回水）
        float gl1_gy=siemens_net.ReadFloat("DB1.204").Content;//1#炉出压（供水）
        float gl2_hy=siemens_net.ReadFloat("DB1.208").Content;//2#炉进压（回水）
        float gl2_gy=siemens_net.ReadFloat("DB1.212").Content;//2#炉出压（供水）
        float gl3_hy=siemens_net.ReadFloat("DB1.216").Content;//3#炉进压（回水）
        float gl3_gy=siemens_net.ReadFloat("DB1.220").Content;//3#炉出压（供水）
        float gl4_hy=siemens_net.ReadFloat("DB1.224").Content;//4#炉进压（回水）
        float gl4_gy=siemens_net.ReadFloat("DB1.228").Content;//4#炉出压（供水）
        float gl5_hy=siemens_net.ReadFloat("DB1.232").Content;//5#炉进压（回水）
        float gl5_gy=siemens_net.ReadFloat("DB1.236").Content;//5#炉出压（供水）
        float gl6_hy=siemens_net.ReadFloat("DB1.240").Content;//6#炉进压（回水）
        float gl6_gy=siemens_net.ReadFloat("DB1.244").Content;//6#炉出压（供水）
        float gl7_hy=siemens_net.ReadFloat("DB1.248").Content;//7#炉进压（回水）
        float gl7_gy=siemens_net.ReadFloat("DB1.252").Content;//7#炉出压（供水）
        float w1_gy=siemens_net.ReadFloat("DB1.256").Content;//一网供压
        float w1_hy=siemens_net.ReadFloat("DB1.260").Content;//一网回压
        float w1_gw=siemens_net.ReadFloat("DB1.320").Content;//一网供温
        float w1_hw=siemens_net.ReadFloat("DB1.324").Content;//一网回温
        float w1_b1_zsfk=siemens_net.ReadFloat("DB1.336").Content;//一网1#泵频率反馈
        float w1_b2_zsfk=siemens_net.ReadFloat("DB1.340").Content;//一网2#泵频率反馈
        float w1_b3_zsfk=siemens_net.ReadFloat("DB1.344").Content;//一网3#泵频率反馈
        float w1_b4_zsfk=siemens_net.ReadFloat("DB1.348").Content;//一网4#泵频率反馈
        float w1_b1_zsgd=siemens_net.ReadFloat("DB1.352").Content;//一网1#泵频率给定
        float w1_b2_zsgd=siemens_net.ReadFloat("DB1.356").Content;//一网2#泵频率给定
        float w1_b3_zsgd=siemens_net.ReadFloat("DB1.360").Content;//一网3#泵频率给定
        float w1_b4_zsgd=siemens_net.ReadFloat("DB1.364").Content;//一网4#泵频率给定
        float gl1_gw=siemens_net.ReadFloat("DB1.268").Content;//1#锅炉出（供）水温度
        float gl1_hw=siemens_net.ReadFloat("DB1.264").Content;//1#锅炉进（回）水温度
        float gl2_gw=siemens_net.ReadFloat("DB1.276").Content;//2#锅炉出（供）水温度
        float gl2_hw=siemens_net.ReadFloat("DB1.272").Content;//2#锅炉进（回）水温度
        float gl3_gw=siemens_net.ReadFloat("DB1.284").Content;//3#锅炉出（供）水温度
        float gl3_hw=siemens_net.ReadFloat("DB1.280").Content;//3#锅炉进（回）水温度
        float gl4_gw=siemens_net.ReadFloat("DB1.292").Content;//4#锅炉出（供）水温度
        float gl4_hw=siemens_net.ReadFloat("DB1.288").Content;//4#锅炉进（回）水温度
        float gl5_gw=siemens_net.ReadFloat("DB1.300").Content;//5#锅炉出（供）水温度
        float gl5_hw=siemens_net.ReadFloat("DB1.296").Content;//5#锅炉进（回）水温度
        float gl6_gw=siemens_net.ReadFloat("DB1.308").Content;//6#锅炉出（供）水温度
        float gl6_hw=siemens_net.ReadFloat("DB1.304").Content;//6#锅炉进（回）水温度
        float gl7_gw=siemens_net.ReadFloat("DB1.316").Content;//7#锅炉出（供）水温度
        float gl7_hw=siemens_net.ReadFloat("DB1.312").Content;//7#锅炉进（回）水温度
        //------------------------------------------------------------------------------
        //-----------------------------输出获取结果-------------------------------------
        System.out.println("1#炉进压（回水）"+gl1_hy);
        System.out.println("1#炉出压（供水）"+gl1_gy);
        System.out.println("2#炉进压（回水）"+gl2_hy);
        System.out.println("2#炉出压（供水）"+gl2_gy);
        System.out.println("3#炉进压（回水）"+gl3_hy);
        System.out.println("3#炉出压（供水）"+gl3_gy);
        System.out.println("4#炉进压（回水）"+gl4_hy);
        System.out.println("4#炉出压（供水）"+gl4_gy);
        System.out.println("5#炉进压（回水）"+gl5_hy);
        System.out.println("5#炉出压（供水）"+gl5_gy);
        System.out.println("6#炉进压（回水）"+gl6_hy);
        System.out.println("6#炉出压（供水）"+gl6_gy);
        System.out.println("7#炉进压（回水）"+gl7_hy);
        System.out.println("7#炉出压（供水）"+gl7_gy);
        System.out.println("一网供压"+w1_gy);
        System.out.println("一网回压"+w1_hy);
        System.out.println("一网供温"+w1_gw);
        System.out.println("一网回温"+w1_hw);
        System.out.println("一网1#泵频率反馈"+w1_b1_zsfk);
        System.out.println("一网2#泵频率反馈"+w1_b2_zsfk);
        System.out.println("一网3#泵频率反馈"+w1_b3_zsfk);
        System.out.println("一网4#泵频率反馈"+w1_b4_zsfk);
        System.out.println("一网1#泵频率给定"+w1_b1_zsgd);
        System.out.println("一网2#泵频率给定"+w1_b2_zsgd);
        System.out.println("一网3#泵频率给定"+w1_b3_zsgd);
        System.out.println("一网4#泵频率给定"+w1_b4_zsgd);
        System.out.println("1#锅炉出（供）水温度"+gl1_gw);
        System.out.println("1#锅炉进（回）水温度"+gl1_hw);
        System.out.println("2#锅炉出（供）水温度"+gl2_gw);
        System.out.println("2#锅炉进（回）水温度"+gl2_hw);
        System.out.println("3#锅炉出（供）水温度"+gl3_gw);
        System.out.println("3#锅炉进（回）水温度"+gl3_hw);
        System.out.println("4#锅炉出（供）水温度"+gl4_gw);
        System.out.println("4#锅炉进（回）水温度"+gl4_hw);
        System.out.println("5#锅炉出（供）水温度"+gl5_gw);
        System.out.println("5#锅炉进（回）水温度"+gl5_hw);
        System.out.println("6#锅炉出（供）水温度"+gl6_gw);
        System.out.println("6#锅炉进（回）水温度"+gl6_hw);
        System.out.println("7#锅炉出（供）水温度"+gl7_gw);
        System.out.println("7#锅炉进（回）水温度"+gl7_hw);
        //------------------------------------------------------------------------------*/

    }

    public static String getBinaryStrFromByte(byte b){
        String result ="";
        byte a = b; ;
        for (int i = 0; i < 8; i++){
            byte c=a;
            a=(byte)(a>>1);//每移一位如同将10进制数除以2并去掉余数。
            a=(byte)(a<<1);
            if(a==c){
                result="0"+result;
            }else{
                result="1"+result;
            }
            a=(byte)(a>>1);
        }
        return StringUtils.reverse(result);
    }

    public void getData(){
        SiemensS7Net siemens_net = new SiemensS7Net(SiemensPLCS.S200Smart,"192.168.1.111");
        OperateResult connect=siemens_net.ConnectServer();
        if(connect.IsSuccess){
            System.out.println("connect success!");
        }else{
            System.out.println("failed:"+connect.Message);
        }
        //---------------------------�����յ�����ˮ-------------------------------------
        float w2_1_gy=siemens_net.ReadFloat("DB1.600").Content;
        float w2_1_hy=siemens_net.ReadFloat("DB1.604").Content;
        float w2_1_gw=siemens_net.ReadFloat("DB1.608").Content;
        float w2_1_hw=siemens_net.ReadFloat("DB1.612").Content;
        float sw_wd=siemens_net.ReadFloat("DB1.616").Content;
        float bsx_yw=siemens_net.ReadFloat("DB1.620").Content;
        float pzsx_yw=siemens_net.ReadFloat("DB1.624").Content;
        float w2_1_bsyl=siemens_net.ReadFloat("DB1.644").Content;
        float w2_b1_zsfk=siemens_net.ReadFloat("DB1.628").Content;
        float w2_b2_zsfk=siemens_net.ReadFloat("DB1.632").Content;
        float w2_b3_zsfk=siemens_net.ReadFloat("DB1.636").Content;
        float w2_b4_zsfk=siemens_net.ReadFloat("DB1.640").Content;
        float w2_b1_zsgd=siemens_net.ReadFloat("DB1.680").Content;
        float w2_b2_zsgd=siemens_net.ReadFloat("DB1.684").Content;
        float w2_b3_zsgd=siemens_net.ReadFloat("DB1.688").Content;
        float w2_b4_zsgd=siemens_net.ReadFloat("DB1.692").Content;
        //-------------------------------------------------------------------------------

    }

    @Test
    public void getData113() {
        SiemensS7Net siemens_net = new SiemensS7Net(SiemensPLCS.S200Smart,"220.194.141.5");//外网ip
        // SiemensS7Net siemens_net = new SiemensS7Net(SiemensPLCS.S200Smart,"192.168.1.113");//局域网ip
        try {
            siemens_net.setPort(30080);//外网端口号
            //siemens_net.setPort(102);
            OperateResult connect=siemens_net.ConnectServer();
            if(connect.IsSuccess){
                System.out.println("connect success!");
            }else{
                System.out.println("failed:"+connect.Message);
            }
            //---------------------------------地暖--------------------------------------
            byte w2_2_gy=siemens_net.ReadByte("DB1.600").Content;//二网地暖供压
            byte w2_2_hy=siemens_net.ReadByte("DB1.604").Content;//二网地暖回压
            byte w2_2_gw=siemens_net.ReadByte("DB1.608").Content;//二网地暖供温
            byte w2_2_hw=siemens_net.ReadByte("DB1.612").Content;//二网地暖回温
            float w2_2_bsyl=siemens_net.ReadFloat("DB1.622").Content;//二网地暖补水侧压力
            double sb_ljl=siemens_net.ReadFloat("DB1.532").Content;//总耗水量
            byte RLB_DN_RLJ=siemens_net.ReadByte("DB1.556").Content;//地暖日消耗量
            byte RLB_KT_RLJ=siemens_net.ReadByte("DB1.548").Content;//地暖日消耗量
            double sb_ljl1=siemens_net.ReadFloat("DB1.564").Content;//总耗水量

            //---------------------------------------------------------------------------
            //-----------------------------输出获取结果------------------------------------
            System.out.println("二网地暖供压"+w2_2_gy);
            System.out.println("二网地暖回压"+w2_2_hy);
            System.out.println("二网地暖供温"+w2_2_gw);
            System.out.println("二网地暖回温"+w2_2_hw);
            System.out.println("二网地暖补水侧压力"+w2_2_bsyl);
            System.out.println("总耗水量"+sb_ljl);
            System.out.println("地暖日消耗量"+RLB_DN_RLJ);
            System.out.println("空调日消耗量"+RLB_KT_RLJ);
            System.out.println("水日消耗量"+sb_ljl1);
            //-----------------------------------------------------------------------------
            siemens_net.ConnectClose();//关闭连接
        } catch (Exception e) {
            siemens_net.ConnectClose();//关闭连接
        }
    }

    @Test
    public void test1() {
            System.out.println(new Date().getTime());
            byte[] nums={1,2,4,8,16,32,64,-128};
            byte sum=106;
            for(int a=0;a<nums.length;a++){
                byte aa=nums[a];
                for(int b=0;b<a;b++){
                    byte bb=nums[b];
                    for(int c=0;c<b;c++){
                        byte cc=nums[c];
                        for(int d=0;d<c;d++){
                            byte dd=nums[d];
                            if((aa+bb+cc+dd)==sum){
                                System.out.println("aa="+aa);
                                System.out.println("bb="+bb);
                                System.out.println("cc="+cc);
                                System.out.println("dd="+dd);
                                System.out.println("一号"+d);
                                System.out.println("二号"+c);
                                System.out.println("三号"+b);
                                System.out.println("四号"+a);
                            }

                        }
                    }
                }
            }
            System.out.println(new Date().getTime());
    }

    @Test
    public void testGuZhang(){
        System.out.println(new Date().getTime());
        int[] nums={-128,1,2,4,8,16,32,64};
        int sum=106;
        if(sum>0){
            a: for(int a=nums.length-1;a>0;a--){
                int aa=nums[a];
                if(Math.abs(sum)>aa){
                    sum=sum-aa;
                    for(int b=a;b>0;b--){
                        int bb=nums[b];
                        if(Math.abs(sum)>bb){
                            sum=sum-bb;
                            for(int c=b;c>0;c--){
                                int cc=nums[c];
                                if(Math.abs(sum)>cc){
                                    int dd=sum-cc;
                                    int d=(int) (Math.log(dd)/Math.log(2));
                                    System.out.println("aa="+aa);
                                    System.out.println("bb="+bb);
                                    System.out.println("cc="+cc);
                                    System.out.println("dd="+dd);
                                    System.out.println("一号"+d);
                                    System.out.println("二号"+(c-1));
                                    System.out.println("三号"+(b-1));
                                    System.out.println("四号"+(a-1));
                                    break a;
                                }
                            }
                        }
                    }
                }
            }
        }else{
            sum=sum+128;
            a: for(int a=nums.length-1;a>0;a--){
                int aa=nums[a];
                if(Math.abs(sum)>aa){
                    sum=sum-aa;
                    for(int b=a;b>0;b--){
                        int bb=nums[b];
                        if(Math.abs(sum)>bb){
                            int cc=sum-bb;
                            int c=(int) (Math.log(cc)/Math.log(2));
                            System.out.println("aa="+aa);
                            System.out.println("bb="+bb);
                            System.out.println("cc="+cc);
                            System.out.println("dd="+-128);
                            System.out.println("一号"+c);
                            System.out.println("二号"+(b-1));
                            System.out.println("三号"+(a-1));
                            System.out.println("四号"+7);
                            break a;
                        }
                    }
                }
            }
        }

        System.out.println(new Date().getTime());
    }

    public static double keepDigit(double originNumber, int digit) {
        if(StringUtils.isBlank(String.valueOf(originNumber))) {
            return 0.0;
        }
        BigDecimal bg = new BigDecimal(originNumber);
        return bg.setScale(digit, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
}
