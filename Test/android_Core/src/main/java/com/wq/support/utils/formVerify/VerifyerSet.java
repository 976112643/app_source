package com.wq.support.utils.formVerify;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * 验证器集合
 * Created by WQ on 2017/1/24.
 */
public class VerifyerSet {

    public static class ConfirmVerifyer implements  IVerifyer{

        @Override
        public void verify(String text) throws FormatException {

        }
    }

    /**
     * 正则验证
     */
    public static class RegularVerifyer implements IVerifyer {
        private String tipTxt="内容格式不正确",regular;

        /**
         * @param tipTxt  错误提示文字
         * @param regular 正则表达式
         */
        public RegularVerifyer(String tipTxt,String regular) {
            this.tipTxt = tipTxt;
            this.regular=regular;
        }

        /**
         * @param regular 正则表达式
         */
        public RegularVerifyer(String regular) {
            this.regular = regular;
        }
        @Override
        public void verify(String text) throws FormatException {
            if(String.valueOf(text).matches(regular))throw new FormatException(tipTxt);
        }
    }
    /**
     * 内容长度的验证
     * Created by WQ on 2017/1/24.
     */
    public static class LengthVerifyer implements IVerifyer {
        private String tipTxt="内容字数不正确";
        private int min,max;

        /**
         * @param tipTxt 错误提示文字
         * @param min    最小字数
         * @param max    最大字数
         */
        public LengthVerifyer(String tipTxt,int min,int max) {
            this.tipTxt = tipTxt;
            this.min=min;
            this.max=max;
        }
        public LengthVerifyer(int min,int max) {
        }
        @Override
        public void verify(String text) throws FormatException {
            if(text==null ||text.length()<min ||text.length()>max)throw new FormatException(tipTxt);
        }
    }
    /**
     * 邮箱格式的验证
     * Created by WQ on 2017/1/24.
     */
    public static class EmailVerifyer implements IVerifyer {
        private String tipTxt="邮箱格式不正确";

        public EmailVerifyer() {
        }

        public EmailVerifyer(String tipTxt) {
            this.tipTxt = tipTxt;
        }
        @Override
        public void verify(String text) throws FormatException {
            if(!FormatCheckUtil.isEmail(text))throw new FormatException(tipTxt);
        }
    }
    /**
     * 手机号格式的验证
     * Created by WQ on 2017/1/24.
     */
    public static class PhoneVerifyer implements IVerifyer {
        private String tipTxt="手机号格式不正确";

        public PhoneVerifyer() {
        }

        public PhoneVerifyer(String tipTxt) {
            this.tipTxt = tipTxt;
        }
        @Override
        public void verify(String text) throws FormatException {
            if(!FormatCheckUtil.isMobileNO(text))throw new FormatException(tipTxt);
        }
    }
    /**
     * 数据为空的验证
     * Created by WQ on 2017/1/24.
     */
    public static class EmptyVerifyer implements IVerifyer {
        private String tipTxt;
        private  boolean isForce=true;

        public EmptyVerifyer() {
        }

        /**
         * @param tipTxt
         * @param isForce 是否去除首尾空白字符
         */
        public EmptyVerifyer(String tipTxt, boolean isForce) {
            this.tipTxt = tipTxt;
            this.isForce = isForce;
        }

        public EmptyVerifyer(String tipTxt) {
            this.tipTxt = tipTxt;
        }
        @Override
        public void verify(String text) throws FormatException {
            if(isForce&&(text==null||text.trim().isEmpty()))throw new FormatException(tipTxt);
            if(text.length()==0)throw new FormatException(tipTxt);
        }
    }
    public static  final String verfiyerPackName=VerifyerSet.class.getPackage().getName();

    /**
     * 根据格式检查器名字获取其实例
     * @param verifyerName
     * @param tipTxt
     * @return
     */
    public static IVerifyer getVerifyer(String verifyerName,String tipTxt){
        try {
            Class<?> clazz=IVerifyer.class.getClassLoader().loadClass(verfiyerPackName+verifyerName);
            if(tipTxt==null){
                return (IVerifyer) clazz.newInstance();
            }else {
                Constructor constructor = clazz.getConstructor(String.class);
                return (IVerifyer) constructor.newInstance(tipTxt);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
