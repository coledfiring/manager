package com.whaty.framework.api.oltrain.onlineexam;

import com.whaty.core.framework.util.RequestUtils;
import com.whaty.framework.aop.operatelog.annotation.OperateRecord;
import com.whaty.framework.aop.operatelog.constant.OperateRecordModuleConstant;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 同步学生在线考试成绩controller
 *
 * @author suoqiangqiang
 */
@Lazy
@RestController("olOpenSyncOnlineExamScoreController")
@RequestMapping("/open/olTrain/onlineExam")
public class OLOpenSyncOnlineExamScoreController {

    @Resource(name = "olSyncOnlineExamScoreService")
    private OLSyncOnlineExamScoreService olSyncOnlineExamScoreService;

    /**
     * 成绩回传接口
     *
     * @param request
     * @return
     */
    @RequestMapping({"/syncOnlineExamScore"})
    @OperateRecord(value = "成绩回传结果", moduleCode = OperateRecordModuleConstant.PAY_MODULE_CODE, isImportant = true)
    public Map<String, String> syncOnlineExamScore(HttpServletRequest request) {
        Map<String, String> requestParameterMap = RequestUtils.getRequestMap(request);
        return olSyncOnlineExamScoreService.doSyncOnlineExamScore(requestParameterMap);
    }
}
