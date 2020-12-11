package com.whaty.handler.tree;

import com.alibaba.fastjson.JSON;
import com.whaty.handler.recursive.RecursiveInvoker;
import com.whaty.handler.recursive.TailRecursion;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 树工具
 *
 * @author weipengsen
 */
public class TreeUtils {

    /**
     * 构建树
     * @param origin
     * @return
     */
    public static Set<TreeNode> buildTree(List<Map<String, Object>> origin) {
        Map<String, TreeNode> nodeHash = new HashMap<>(16);
        Set<TreeNode> rootNodes = new TreeSet<>();
        origin.stream().map(TreeUtils::convert).forEach(e -> {
            Optional.ofNullable(e)
                    .filter(n -> StringUtils.isBlank(n.getParentId())).ifPresent(rootNodes::add);
            nodeHash.put(e.getId(), e);
        });
        if (origin.size() == rootNodes.size()) {
            return rootNodes;
        }
        nodeHash.values().stream().filter(e -> Objects.nonNull(e.getParentId()))
                .forEach(e -> nodeHash.get(e.getParentId()).putChild(e));
        return rootNodes;
    }

    /**
     * 转换数据类型
     * @param origin
     * @return
     */
    private static TreeNode convert(Map<String, Object> origin) {
        return JSON.parseObject(JSON.toJSONString(origin), TreeNode.class);
    }

    /**
     * 构建有效树
     * @param origin
     * @param activeNodeId
     * @return
     */
    public static Set<TreeNode> buildActiveTree(List<Map<String, Object>> origin, List<String> activeNodeId) {
        return buildTree(extractActiveNode(origin, activeNodeId));
    }

    /**
     * 提取有效节点
     * @param origin
     * @param activeNodeId
     * @return
     */
    public static List<Map<String, Object>> extractActiveNode(List<Map<String, Object>> origin,
                                                              List<String> activeNodeId) {
        if (CollectionUtils.isEmpty(activeNodeId)) {
            return origin;
        }
        Map<String, Map<String, Object>> originHash = origin.stream().collect(Collectors
                .toMap(e -> (String) e.get("id"), e -> e));
        return activeNodeId.stream().map(originHash::get)
                .map(e -> recursiveParentNode(new ArrayList<>(), originHash, originHash.get(e)).invoke())
                .flatMap(Collection::stream).collect(Collectors.toList());
    }

    /**
     * 尾递归查找父级节点
     * @param target
     * @param originHash
     * @param node
     * @return
     */
    private static TailRecursion<List<Map<String, Object>>> recursiveParentNode(List<Map<String, Object>> target,
                                                                 Map<String, Map<String, Object>> originHash,
                                                                 Map<String, Object> node) {
        target.add(node);
        if (Objects.isNull(node.get("parentId"))) {
            return RecursiveInvoker.done(target);
        }
        return RecursiveInvoker.call(() -> recursiveParentNode(target, originHash,
                originHash.get(node.get("parentId"))));
    }

}
