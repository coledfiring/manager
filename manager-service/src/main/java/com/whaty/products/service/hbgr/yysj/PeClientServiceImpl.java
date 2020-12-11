package com.whaty.products.service.hbgr.yysj;

import com.whaty.common.collection.CollectionUtils;
import com.whaty.constant.CommonConstant;
import com.whaty.dao.GeneralDao;
import com.whaty.domain.bean.hbgr.yysj.PeCheck;
import com.whaty.domain.bean.hbgr.yysj.PeClient;
import com.whaty.framework.exception.UncheckException;
import com.whaty.framework.grid.adapter.service.impl.TycjGridServiceAdapter;
import com.whaty.products.service.common.constant.ComConstant;
import com.whaty.products.service.hbgr.yysj.constant.YysjConstants;
import com.whaty.util.CommonUtils;
import com.whaty.util.QrCodeUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * author weipengsen  Date 2020/7/9
 */
@Lazy
@Service("peClientService")
public class PeClientServiceImpl extends TycjGridServiceAdapter<PeClient> {

    @Resource(name = CommonConstant.GENERAL_DAO_BEAN_NAME)
    private GeneralDao myGeneralDao;

    public void downloadClientQrCode(OutputStream out) throws Exception {
        String filePath = CommonUtils.getRealPath("/incoming/client/客户服务二维码.png");
        File file = new File(filePath);
        if(!file.exists()) {
            QrCodeUtil.generateQrCodes(Collections.singletonMap("客户服务二维码", YysjConstants.CLIENT_QRCODE_WEB_PATH), 500, "/incoming/client/");
        }
        try (FileInputStream input = new FileInputStream(filePath);
             FileChannel channel = input.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(input.available());
            buffer.clear();
            channel.read(buffer);
            out.write(buffer.array());
            buffer.clear();
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new UncheckException(e);
        }
    }
}
