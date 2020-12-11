package com.whaty.framework.learnspace;

import com.caucho.hessian.client.HessianProxyFactory;
import com.whaty.products.learning.webservice.LearningSpaceStandardWebService;

/**
 * hessian请求测试类
 *
 * @author suoqiangqiang
 */
public class TestLearnSpaceLogin {

    public static void main(String[] args) throws Exception {
        MyThread myThread = new MyThread();
        for (int i = 0; i < 50; i++) {
            new Thread(myThread, String.valueOf(i)).start();
        }
    }

    public static class MyThread implements Runnable {
        @Override
        public void run() {
            String url = "http://whatypx.cep.webtrn.cn/webService/LearningSpaceStandardWebService";
            HessianProxyFactory factory = new HessianProxyFactory();
            try {
                LearningSpaceStandardWebService learningSpaceStandardWebService = (LearningSpaceStandardWebService) factory.create(LearningSpaceStandardWebService.class, url);
                for (int i = 0; i < 15600; i++) {
                    Thread.sleep(3000);
                    boolean flag = learningSpaceStandardWebService.validateUserOnlineStatus("whatysqq", "training");
                    if (!flag) {
                        System.out.println("**************************");
                        System.out.println(flag);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
