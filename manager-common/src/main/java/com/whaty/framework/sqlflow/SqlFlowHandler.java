package com.whaty.framework.sqlflow;

import com.whaty.framework.sqlflow.constant.SqlFlowConstant;
import com.whaty.framework.sqlflow.constant.SqlFlowValidateConstant;
import com.whaty.framework.sqlflow.exception.IllegalDynamicSqlException;
import com.whaty.util.CommonUtils;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;

/**
 * sql流程处理器
 *
 * @author weipengsen
 */
public class SqlFlowHandler {

    /**
     * 需要处理的sql
     */
    private String sql;
    /**
     * 需要进行流程控制的参数
     */
    private Map<String, Object> params;

    public SqlFlowHandler(String sql, Map<String, Object> params) {
        if (StringUtils.isBlank(sql)) {
            throw new IllegalArgumentException("sql is blank");
        }
        this.sql = sql;
        this.params = params;
    }

    /**
     * 处理sql中的流程控制
     *
     * @return
     */
    public void handle() throws InvocationTargetException, IllegalAccessException {
        // 处理流程控制
        this.setSql(this.handleFlow(this.getSql(), this.getParams()));
    }

    /**
     * 处理流程控制字符串
     *
     * @param params
     * @param originString
     */
    protected String handleFlow(String originString, Map<String, Object> params) throws InvocationTargetException,
            IllegalAccessException {
        // 处理流程标签
        originString = this.handleFlowInString(originString, params);
        // 处理不在流程标签内的表达式
        originString = this.handleExpressionInString(originString, params);
        return originString;
    }

    /**
     * 处理字符串中的流程控制符
     *
     * @param originString
     * @param params
     */
    private String handleFlowInString(String originString, Map<String, Object> params) throws
            InvocationTargetException, IllegalAccessException {
        List<String> flowStringList = this.searchFlowString(originString);
        // 处理流程标签
        for (String flowStr : flowStringList) {
            this.validateFlowTag(flowStr);
            String handledStr = this.handleFlowStr(flowStr, params);
            originString = originString.replace(flowStr, handledStr);
        }
        return originString;
    }

    /**
     * 寻找字符串中的流程控制标签
     * @param originString
     * @return
     */
    private List<String> searchFlowString(String originString) {
        // 寻找标签组
        List<String> flowStringList = new LinkedList<>();
        Stack<String> openTagStack = new Stack<>();
        Matcher m = SqlFlowValidateConstant.SQL_FLOW_OPEN_OR_CLOSE_TAG_FORMAT_PATTERN.matcher(originString);
        int start = 0;
        int index = 0;
        boolean isStart = true;
        while (m.find()) {
            String findStr = m.group();
            findStr = findStr.trim();
            if (isStart) {
                start = m.start();
                isStart = false;
            }
            // 如果是闭标签，查看栈顶是否是对应开标签
            if (findStr.startsWith(SqlFlowConstant.SQL_FLOW_TAG_CLOSE_TAG_PREFIX)) {
                if (index == 0) {
                    throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nclose tag '"
                            + findStr + "' not found open tag");
                }
                String flowType = findStr.trim().substring(2, findStr.trim().length() - 1).trim();
                // 出栈对应开标签
                this.removeTagForCloseTag(openTagStack, flowType);
                if (openTagStack.size() == 0) {
                    int end = m.end() == originString.length() ? m.end() : m.end() + 1;
                    flowStringList.add(originString.substring(start, end).trim());
                    isStart = true;
                }
            } else {
                // 如果是开标签，判断是否是寻找的标签且是否栈只有一个元素，不是压栈
                String flowType = findStr.trim().substring(1, findStr.indexOf(" ")).trim();
                openTagStack.push(flowType);
            }
            index++;
        }
        return flowStringList;
    }

    /**
     * 处理表达式调用
     */
    private String handleExpressionInString(String originString, Map<String, Object> params) throws
            InvocationTargetException, IllegalAccessException {
        // 处理表达式标签
        Matcher m = SqlFlowValidateConstant.SQL_ARG_SIGN_FORMAT_PATTERN.matcher(originString);
        while (m.find()) {
            String findStr = m.group();
            String expression = findStr.substring(2, findStr.length() - 1);
            Object target = this.handleExpression(expression, params);
            originString = originString.replace(findStr, target == null ? "" : target.toString());
        }
        return originString;
    }

    /**
     * 校验流程标签是否符合要求
     *
     * @param flowStr
     */
    private void validateFlowTag(String flowStr) {
        String openFlowType = flowStr.substring(1, flowStr.indexOf(" "));
        String closeFlowType = flowStr.substring(flowStr.lastIndexOf("/") + 1, flowStr.length() - 1).trim();
        if (!openFlowType.equals(closeFlowType)) {
            throw new IllegalDynamicSqlException("error sql :" + this.sql + "\ntag '" + openFlowType + "' not found close tag");
        }
    }

    /**
     * 处理流程控制字符串
     *
     * @param flowStr
     * @param params
     */
    private String handleFlowStr(String flowStr, Map<String, Object> params) throws InvocationTargetException,
            IllegalAccessException {
        // 拿到流程类型
        String flowType = flowStr.substring(1, flowStr.indexOf(" "));
        switch (flowType) {
            case SqlFlowConstant.FLOW_TYPE_IF:
                return this.handleIfFlowStr(flowStr, params);
            case SqlFlowConstant.FLOW_TYPE_FOR:
                return this.handleForFlowStr(flowStr, params);
            default:
                throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nnot known flow type : '" + flowType + "'");
        }
    }

    /**
     * 处理if控制
     *
     * @param flowStr
     * @param params
     */
    private String handleIfFlowStr(String flowStr, Map<String, Object> params) throws
            InvocationTargetException, IllegalAccessException {
        boolean testValue = this.handleTagHasTest(flowStr, params);
        if (testValue) {
            flowStr = this.cutIfTag(flowStr);
            return this.handleFlow(flowStr, params);
        }
        // 查看是否有elseif部分，及elseif部分是否有test
        String target = this.handleIfTagCutToTag(flowStr, SqlFlowConstant.FLOW_TYPE_ELSE_IF);
        if (StringUtils.isNotBlank(target)) {
            flowStr = target;
            testValue = this.handleTagHasTest(flowStr, params);
            if (testValue) {
                flowStr = this.cutIfTag(flowStr);
                return this.handleFlow(flowStr, params);
            }
        }
        // 查看是否有else部分
        target = this.handleIfTagCutToTag(flowStr, SqlFlowConstant.FLOW_TYPE_ELSE);
        if (StringUtils.isNotBlank(target)) {
            flowStr = target;
            flowStr = this.cutTag(flowStr);
            return this.handleFlow(flowStr, params);
        }
        return "";
    }

    /**
     * 清楚if标签的包围标签
     * @param flowStr
     * @return
     */
    private String cutIfTag(String flowStr) {
        Stack<String> openTagStack = new Stack<>();
        Matcher m = SqlFlowValidateConstant.SQL_FLOW_IF_TAG_FORMAT_PATTERN.matcher(flowStr);
        while (m.find()) {
            String findStr = m.group().trim();
            // 如果是闭标签，查看栈顶是否是对应开标签
            if (findStr.startsWith(SqlFlowConstant.SQL_FLOW_TAG_CLOSE_TAG_PREFIX)) {
                String flowType = findStr.substring(2, findStr.length() - 1).trim();
                // 判断栈顶是否是对应元素
                this.removeTagForCloseTag(openTagStack, flowType);
            } else {
                // 如果是开标签，判断是否是寻找的标签且是否栈只有一个元素，不是压栈
                String flowType;
                if (findStr.contains(" ")) {
                    flowType = findStr.substring(1, findStr.indexOf(" ")).trim();
                } else {
                    flowType = findStr.substring(1, findStr.length() - 1).trim();
                }
                // 判断是否是需要寻找的元素且栈容量为1
                boolean searched = (SqlFlowConstant.FLOW_TYPE_ELSE_IF.equals(flowType)
                        || SqlFlowConstant.FLOW_TYPE_ELSE.equals(flowType)) && openTagStack.size() == 1;
                if (searched) {
                    String needCutString = flowStr.substring(m.start(),
                            flowStr.lastIndexOf(SqlFlowConstant.SQL_FLOW_TAG_CLOSE_TAG_PREFIX));
                    flowStr = flowStr.replace(needCutString, "");
                    break;
                }
                openTagStack.push(flowType);
            }
        }
        return this.cutTag(flowStr);
    }

    /**
     * 截取字符串的外标签
     *
     * @param flowStr
     * @return
     */
    private String cutTag(String flowStr) {
        return flowStr.substring(flowStr.indexOf(SqlFlowConstant.SQL_FLOW_RIGHT_LIMIT_SIGN) + 1,
                flowStr.lastIndexOf(SqlFlowConstant.SQL_FLOW_LEFT_LIMIT_SIGN)).trim();
    }

    /**
     * 将if流程字符串截取到下一个指定的并行标签，if判断为false寻找同级的elseif或else
     *
     * @param flowStr
     * @param tag
     * @return
     */
    private String handleIfTagCutToTag(String flowStr, String tag) {
        Stack<String> openTagStack = new Stack<>();
        Matcher m = SqlFlowValidateConstant.SQL_FLOW_IF_TAG_FORMAT_PATTERN.matcher(flowStr);
        while (m.find()) {
            String findStr = m.group().trim();
            // 如果是闭标签，查看栈顶是否是对应开标签
            if (findStr.startsWith(SqlFlowConstant.SQL_FLOW_TAG_CLOSE_TAG_PREFIX)) {
                String flowType = findStr.substring(2, findStr.length() - 1).trim();
                // 判断栈顶是否是对应元素
                this.removeTagForCloseTag(openTagStack, flowType);
            } else {
                // 如果是开标签，判断是否是寻找的标签且是否栈只有一个元素，不是压栈
                String flowType;
                if (findStr.contains(" ")) {
                    flowType = findStr.substring(1, findStr.indexOf(" ")).trim();
                } else {
                    flowType = findStr.substring(1, findStr.length() - 1).trim();
                }
                if (flowType.equals(tag) && openTagStack.size() == 1) {
                    return flowStr.substring(m.start());
                } else {
                    openTagStack.push(flowType);
                }
            }
        }
        return "";
    }

    /**
     * 删除指定闭标签的开标签
     *
     * @param closeTag
     * @param stack
     */
    private void removeTagForCloseTag(Stack<String> stack, String closeTag) {
        String openTag;
        try {
            openTag = stack.peek();
        } catch (EmptyStackException e) {
            throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nclose tag '" + closeTag + "' not found open tag");
        }
        if (openTag.equals(closeTag)) {
            stack.removeElementAt(stack.size() - 1);
        } else if (closeTag.equals(SqlFlowConstant.FLOW_TYPE_IF)) {
            switch (openTag) {
                case SqlFlowConstant.FLOW_TYPE_IF:
                    stack.removeElementAt(stack.size() - 1);
                    break;
                case SqlFlowConstant.FLOW_TYPE_ELSE_IF:

                case SqlFlowConstant.FLOW_TYPE_ELSE:
                    stack.removeElementAt(stack.size() - 1);
                    if (stack.size() != 0) {
                        this.removeTagForCloseTag(stack, closeTag);
                    }
                    break;
                default:
                    throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nclose tag '" + closeTag + "' not found open tag");
            }
        } else {
            throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nclose tag '" + closeTag + "' not found open tag");
        }
    }

    /**
     * 处理拥有test属性的标签，得到test的值
     *
     * @param flowStr
     * @param params
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private boolean handleTagHasTest(String flowStr, Map<String, Object> params) throws InvocationTargetException,
            IllegalAccessException {
        String tagStr = flowStr.substring(0, flowStr.indexOf(SqlFlowConstant.SQL_FLOW_RIGHT_LIMIT_SIGN) + 1);
        Map<String, String> properties = this.getPropertiesForTag(tagStr);
        if (!properties.containsKey(SqlFlowConstant.FLOW_PROPERTY_KEY_TEST)) {
            throw new IllegalDynamicSqlException("error sql :" + this.sql + "\n '" + flowStr +
                    "' not found 'test' property");
        }
        // 获取if判断的if部分test的值是否为真
        String testExpression = properties.get(SqlFlowConstant.FLOW_PROPERTY_KEY_TEST);
        return this.handleLogicExpression(testExpression, params);
    }

    /**
     * 处理逻辑表达式
     *
     * @param expression
     * @param params
     * @return
     */
    private boolean handleLogicExpression(String expression, Map<String, Object> params) throws
            InvocationTargetException, IllegalAccessException {
        if (StringUtils.isBlank(expression.trim())) {
            return false;
        }
        // 判断是否有逻辑运算符
        String[] expressions = expression.split(SqlFlowValidateConstant.FLOW_EXPRESSION_LOGIC_SIGN_FORMAT);
        Boolean flag = null;
        for (String exp : expressions) {
            Object objValue = this.handleExpression(exp, params);
            boolean value = objValue == null ? false : (Boolean) objValue;
            if (expressions.length == 1) {
                return value;
            }
            if (expression.contains(SqlFlowConstant.FLOW_EXPRESSION_BIT_AND_SIGN)
                    || expression.contains(SqlFlowConstant.FLOW_EXPRESSION_LOGIC_AND_SIGN)) {
                if (!value) {
                    return false;
                }
                flag = true;
            } else if (expression.contains(SqlFlowConstant.FLOW_EXPRESSION_BIT_OR_SIGN)
                    || expression.contains(SqlFlowConstant.FLOW_EXPRESSION_LOGIC_OR_SIGN)) {
                if (value) {
                    return true;
                }
                flag = false;
            }
        }
        return flag == null ? false : flag;
    }

    /**
     * 处理for控制
     *
     * @param flowStr
     * @param params
     */
    private String handleForFlowStr(String flowStr, Map<String, Object> params) throws InvocationTargetException,
            IllegalAccessException {
        String tagStr = flowStr.substring(0, flowStr.indexOf(SqlFlowConstant.SQL_FLOW_RIGHT_LIMIT_SIGN));
        Map<String, String> properties = this.getPropertiesForTag(tagStr);
        if (!properties.containsKey(SqlFlowConstant.FLOW_PROPERTY_KEY_ITEMS)) {
            throw new IllegalDynamicSqlException("error sql :" + this.sql + "\n '" + flowStr +
                    "' not found 'items' property");
        }
        if (!properties.containsKey(SqlFlowConstant.FLOW_PROPERTY_KEY_ELEMENT)) {
            throw new IllegalDynamicSqlException("error sql :" + this.sql + "\n '" + flowStr +
                    "' not found 'element' property");
        }
        String elementName = properties.get(SqlFlowConstant.FLOW_PROPERTY_KEY_ELEMENT);
        String indexName = properties.get(SqlFlowConstant.FLOW_PROPERTY_KEY_INDEX);
        String itemsStr = properties.get(SqlFlowConstant.FLOW_PROPERTY_KEY_ITEMS);
        Object itemsObj = this.handleExpression(itemsStr, params);
        if (itemsObj == null) {
            return "";
        }
        Map<String, Object> scopeParams = new HashMap<>(params);
        // 循环将不同形式的item解析并拼接到一起
        StringBuilder target = new StringBuilder();
        if (itemsObj instanceof Iterable) {
            Iterator iterator = ((Iterable) itemsObj).iterator();
            int index = 0;
            while (iterator.hasNext()) {
                scopeParams.put(elementName, iterator.next());
                if (StringUtils.isBlank(indexName)) {
                    scopeParams.put(indexName, index);
                    index++;
                }
                target.append(this.handleForTagSingleContent(this.cutTag(flowStr), scopeParams));
            }
        } else if (itemsObj.getClass().isArray()) {
            Object[] items = (Object[]) itemsObj;
            for (int i = 0; i < items.length; i++) {
                scopeParams.put(elementName, items[i]);
                if (StringUtils.isBlank(indexName)) {
                    scopeParams.put(indexName, i);
                }
                target.append(this.handleForTagSingleContent(this.cutTag(flowStr), scopeParams));
            }
        } else if (itemsObj instanceof Map) {
            int index = 0;
            for (Object key : ((Map) itemsObj).keySet()) {
                scopeParams.put(elementName, key);
                if (StringUtils.isBlank(indexName)) {
                    scopeParams.put(indexName, index);
                    index++;
                }
                target.append(this.handleForTagSingleContent(this.cutTag(flowStr), scopeParams));
            }
        }
        return target.toString();
    }

    /**
     * 处理for循环中的单次循环的内容
     *
     * @param originStr
     * @param params
     * @return
     */
    private String handleForTagSingleContent(String originStr, Map<String, Object> params) throws
            InvocationTargetException, IllegalAccessException {
        return this.handleFlow(originStr, params);
    }

    /**
     * 处理表达式
     *
     * @param expression
     * @param params
     * @return
     */
    private Object handleExpression(String expression, Map<String, Object> params) throws
            InvocationTargetException, IllegalAccessException {
        Object target;
        if (expression.contains(SqlFlowConstant.FLOW_EXPRESSION_METHOD_INVOKE_SIGN)) {
            boolean logicNo = expression.startsWith(SqlFlowConstant.FLOW_EXPRESSION_LOGIC_NO);
            if (logicNo) {
                expression = expression.substring(1);
            }
            target = this.handleExpressionForMethodInvoke(expression, params);
            if (logicNo) {
                if (!(target instanceof Boolean)) {
                    throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nexpression '" + expression + "' is not a logic expression");
                }
                target = !((Boolean) target);
            }
        } else if (expression.matches(SqlFlowValidateConstant.FLOW_EXPRESSION_HAVE_LOGIC_COMPUTE_FORMAT)) {
            target = this.handleExpressionForLogicCompute(expression, params);
        } else {
            boolean logicNo = expression.startsWith(SqlFlowConstant.FLOW_EXPRESSION_LOGIC_NO);
            if (logicNo) {
                expression = expression.substring(1);
            }
            target = this.handleExpressionInParam(expression, params);
            if (logicNo) {
                if (!(target instanceof Boolean)) {
                    throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nexpression '" + expression + "' is not a logic expression");
                }
                target = !((Boolean) target);
            }
        }
        return target;
    }

    /**
     * 处理包含逻辑计算的表达式
     *
     * @param expression
     * @param params
     * @return
     */
    private boolean handleExpressionForLogicCompute(String expression, Map<String, Object> params)
            throws InvocationTargetException, IllegalAccessException {
        String[] computeNumber = expression.split(SqlFlowValidateConstant.FLOW_EXPRESSION_LOGIC_COMPUTE_FORMAT);
        Object leftNumberObj = this.handleExpressionInParam(computeNumber[0], params);
        Object rightNumberObj = this.handleExpressionInParam(computeNumber[1], params);
        String operateSign = expression.replace(computeNumber[0], "").replace(computeNumber[1], "").trim();
        return this.compute(leftNumberObj, rightNumberObj, operateSign);
    }

    /**
     * 进行布尔值运算
     *
     * @param number1
     * @param number2
     * @param operateSign
     * @return
     */
    private boolean compute(Object number1, Object number2, String operateSign) {/**/
        switch (operateSign) {
            case SqlFlowConstant.FLOW_EXPRESSION_LOGIC_COMPUTED_EQUAL:
                return number1.equals(number2);
            case SqlFlowConstant.FLOW_EXPRESSION_LOGIC_COMPUTED_NO_EQUAL:
                return number1 != number2;
            case SqlFlowConstant.FLOW_EXPRESSION_LOGIC_COMPUTED_GREAT:
                return Float.parseFloat(String.valueOf(number1)) > Float.parseFloat(String.valueOf(number2));
            case SqlFlowConstant.FLOW_EXPRESSION_LOGIC_COMPUTED_GREAT_EQUAL:
                return Float.parseFloat(String.valueOf(number1)) >= Float.parseFloat(String.valueOf(number2));
            case SqlFlowConstant.FLOW_EXPRESSION_LOGIC_COMPUTED_LESS:
                return Float.parseFloat(String.valueOf(number1)) < Float.parseFloat(String.valueOf(number2));
            case SqlFlowConstant.FLOW_EXPRESSION_LOGIC_COMPUTED_LESS_EQUAL:
                return Float.parseFloat(String.valueOf(number1)) <= Float.parseFloat(String.valueOf(number2));
            default:
                throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nillegal operate sign '" + operateSign + "'");
        }
    }

    /**
     * 处理方法调用表达式
     *
     * @param expression
     * @param params
     * @return
     */
    private Object handleExpressionForMethodInvoke(String expression, Map<String, Object> params) throws
            InvocationTargetException, IllegalAccessException {
        try {
            return this.handleExpressionForStaticMethodInvoke(expression, params);
        } catch (ClassNotFoundException e) {
            return this.handleExpressionForDynamicMethodInvoke(expression, params);
        }
    }

    /**
     * 处理动态方法调用
     *
     * @param expression
     * @param params
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object handleExpressionForDynamicMethodInvoke(String expression, Map<String, Object> params) throws
            InvocationTargetException, IllegalAccessException {
        String[] exp = expression.split(SqlFlowConstant.FLOW_EXPRESSION_METHOD_INVOKE_SIGN);
        Object invoker;
        try {
            invoker = this.handleExpressionInParam(exp[0], params);
        } catch (IllegalDynamicSqlException e1) {
            throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nexpression '" + exp[0] + "' unknown");
        }
        Class clazz = invoker.getClass();
        String argStr = exp[1].substring(exp[1].indexOf(SqlFlowConstant.FLOW_METHOD_INVOKE_ARG_WRAPPER_LEFT_SIGN) + 1,
                exp[1].indexOf(SqlFlowConstant.FLOW_METHOD_INVOKE_ARG_WRAPPER_RIGHT_SIGN));
        String methodName = exp[1].substring(0, exp[1].indexOf(SqlFlowConstant.FLOW_METHOD_INVOKE_ARG_WRAPPER_LEFT_SIGN));
        return this.invokeMethod(invoker, clazz, methodName, argStr, params);
    }

    /**
     * 静态方法调用
     *
     * @param expression
     * @param params
     * @return
     */
    private Object handleExpressionForStaticMethodInvoke(String expression, Map<String, Object> params) throws
            ClassNotFoundException, InvocationTargetException, IllegalAccessException {
        expression = expression.substring(1);
        String[] exp = expression.split(SqlFlowConstant.FLOW_EXPRESSION_METHOD_INVOKE_SIGN);
        Class clazz = Class.forName(exp[0]);
        String argStr = exp[1].substring(exp[1].indexOf(SqlFlowConstant.FLOW_METHOD_INVOKE_ARG_WRAPPER_LEFT_SIGN) + 1,
                exp[1].indexOf(SqlFlowConstant.FLOW_METHOD_INVOKE_ARG_WRAPPER_RIGHT_SIGN));
        String methodName = exp[1].substring(0, exp[1].indexOf(SqlFlowConstant.FLOW_METHOD_INVOKE_ARG_WRAPPER_LEFT_SIGN));
        return this.invokeMethod(null, clazz, methodName, argStr, params);
    }

    /**
     * 方法调用
     *
     * @param invoker
     * @param methodName
     * @param invokerClass
     * @param argStr
     * @param params
     * @return
     */
    private Object invokeMethod(Object invoker, Class invokerClass, String methodName, String argStr,
                                Map<String, Object> params) throws InvocationTargetException, IllegalAccessException {
        Object[] args;
        Class[] argTypes;
        if (StringUtils.isBlank(argStr.trim())) {
            args = new Object[0];
            argTypes = new Class[0];
        } else {
            String[] argStrArr = argStr.split(SqlFlowConstant.FLOW_METHOD_INVOKE_ARG_SPLIT_STR);
            args = new Object[argStrArr.length];
            argTypes = new Class[argStrArr.length];
            for (int i = 0; i < argStrArr.length; i++) {
                args[i] = this.handleExpression(argStrArr[i].trim(), params);
                argTypes[i] = args[i].getClass();
            }
        }
        Method m = this.getMethodForArgs(invokerClass, methodName, argTypes);
        if (m == null) {
            throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nmethod '" + methodName + "' not found");
        }
        return m.invoke(invoker, args);
    }

    /**
     * 拿出类中指定参数类型的方法
     *
     * @param clazz
     * @param argTypes
     * @return
     */
    private Method getMethodForArgs(Class clazz, String methodName, Class[] argTypes) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Class[] paramTypes = method.getParameterTypes();
            if (method.getName().equals(methodName)
                    && paramTypes.length == argTypes.length) {
                boolean paramMatch = true;
                for (int i = 0; i < argTypes.length; i++) {
                    if (!this.classEqual(paramTypes[i], argTypes[i])) {
                        paramMatch = false;
                        break;
                    }
                }
                if (paramMatch) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * 对比target是否是origin的子类或者是否相等
     *
     * @param origin
     * @param target
     * @return
     */
    private boolean classEqual(Class origin, Class target) {
        if (origin.equals(target)) {
            return true;
        }
        if (target == null) {
            return false;
        }
        // 判断所有的父类
        Class superClass = target.getSuperclass();
        if (this.classEqual(origin, superClass)) {
            return true;
        }
        // 判断所有的接口
        Class[] interfaces = target.getInterfaces();
        for (Class inter : interfaces) {
            if (this.classEqual(origin, inter)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 处理参数引用表达式
     *
     * @param expression
     * @param params
     * @return
     */
    private Object handleExpressionInParam(String expression, Map<String, Object> params) throws
            InvocationTargetException, IllegalAccessException {
        if (SqlFlowConstant.STRING_NULL.equals(expression.toLowerCase())) {
            return null;
        }
        if (expression.matches(SqlFlowValidateConstant.FLOW_EXPRESSION_STRING_FORMAT)) {
            return expression.substring(1, expression.length() - 1);
        } else if (expression.matches(SqlFlowValidateConstant.FLOW_EXPRESSION_INTEGER_FORMAT)) {
            return Integer.parseInt(expression);
        } else if (expression.matches(SqlFlowValidateConstant.FLOW_EXPRESSION_FLOAT_FORMAT)) {
            return Float.parseFloat(expression);
        }
        String[] exp = expression.split(SqlFlowConstant.FLOW_EXPRESSION_REFERENCE_SIGN);
        Object value = null;
        for (int i = 0; i < exp.length; i++) {
            String currentExp;
            // 判断表达式是否是集合索引形式
            if (exp[i].contains(SqlFlowConstant.FLOW_EXPRESSION_INDEX_WRAPPER_LEFT_SIGN)) {
                currentExp = exp[i].substring(0, exp[i].indexOf(SqlFlowConstant.FLOW_EXPRESSION_INDEX_WRAPPER_LEFT_SIGN));
            } else {
                currentExp = exp[i];
            }
            // 获取值
            if (i == 0) {
                if (!params.containsKey(currentExp)) {
                    return null;
                }
                value = params.get(currentExp);
            } else {
                if (value == null) {
                    throw new NullPointerException();
                }
                if (value instanceof Map) {
                    value = ((Map) value).get(currentExp);
                } else {
                    try {
                        value = CommonUtils.getValueByReflect(value, currentExp);
                    } catch (NoSuchMethodException e) {
                        return null;
                    }
                }
            }
            // 如果存在集合索引形式，则拿出索引中的值
            if (!currentExp.equals(exp[i])) {
                int index = Integer.parseInt(exp[i]
                        .substring(exp[i].indexOf(SqlFlowConstant.FLOW_EXPRESSION_INDEX_WRAPPER_LEFT_SIGN) + 1,
                                exp[i].indexOf(SqlFlowConstant.FLOW_EXPRESSION_INDEX_WRAPPER_RIGHT_SIGN)));
                if (value instanceof List) {
                    value = ((List) value).get(index);
                } else if (value.getClass().isArray()) {
                    value = ((Object[]) value)[index];
                } else {
                    throw new IllegalDynamicSqlException("error sql :" + this.sql + "\nproperty '" + currentExp + "' is not Array or List");
                }
            }
        }
        return value;
    }

    /**
     * 获取控制标签的属性
     *
     * @param tag
     * @return
     */
    private Map<String, String> getPropertiesForTag(String tag) {
        Matcher m = SqlFlowValidateConstant.SQL_FLOW_PROPERTY_FORMAT_PATTERN.matcher(tag);
        Map<String, String> properties = new HashMap<>(16);
        while (m.find()) {
            String property = m.group();
            String key = property.substring(0, property.indexOf(SqlFlowConstant.FLOW_EXPRESSION_PROPERTY_EQUAL)).trim();
            String valueWithWrapper = property.substring(property
                    .indexOf(SqlFlowConstant.FLOW_EXPRESSION_PROPERTY_EQUAL) + 1).trim();
            String value = valueWithWrapper.substring(1, valueWithWrapper.length() - 1).trim();
            properties.put(key, value);
        }
        return properties;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
