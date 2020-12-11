package com.whaty.handler;

import HslCommunication.Core.Types.OperateResult;
import HslCommunication.Profinet.Siemens.SiemensPLCS;
import HslCommunication.Profinet.Siemens.SiemensS7Net;
import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SiemensS7NetHandler {

    private SiemensS7Net siemens_net;

    private final Consumer<SiemensS7Net> consumer;

    public SiemensS7NetHandler(String ip, int port, Consumer<SiemensS7Net> consumer) throws Exception {
        siemens_net = new SiemensS7Net(SiemensPLCS.S200Smart,ip);
        this.consumer = consumer;
        siemens_net.setPort(port);
        OperateResult connect = siemens_net.ConnectServer();
        if(!connect.IsSuccess) {
          //  throw new Exception("连接 " + ip + ":" + port + "异常");
        }
    }

    public void handle() {
        try {
           if (this.consumer != null) {
                this.consumer.accept(siemens_net);
            }
            siemens_net.ConnectClose();
        } catch (Exception e) {
            e.printStackTrace();
            siemens_net.ConnectClose();
        }
    }
}
