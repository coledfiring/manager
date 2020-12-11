package com.whaty.handler.tree;

import lombok.Data;

import java.io.Serializable;
import java.util.*;

/**
 * 树节点
 *
 * @author weipengsen
 */
@Data
public class TreeNode implements Serializable, Comparable<TreeNode> {

    private static final long serialVersionUID = 1800633344371050717L;

    private String id;

    private String label;

    private Set<TreeNode> children;

    private transient String parentId;

    @Override
    public int compareTo(TreeNode o) {
        if (Objects.isNull(o)) {
            return 1;
        }
        return this.label.compareTo(o.label);
    }

    public void putChild(TreeNode child) {
        if (Objects.isNull(this.children)) {
            this.children = new TreeSet<>();
        }
        this.children.add(child);
    }

    /**
     * 获取当前节点所有子节点的id值并放入参数ids中
     *
     * @return
     */
    public List<String> getAllTreeNodeIds(){
        List<String> ids;
        if (null == this.children || this.children.isEmpty()) {
            ids = new ArrayList<>(1);
            ids.add(this.id);
            return ids;
        }

        ids = new ArrayList<>(1 + this.children.size() * 2);
        ids.add(this.id);
        Iterator<TreeNode> iterator = this.children.iterator();
        while (iterator.hasNext()) {
            ids.addAll(iterator.next().getAllTreeNodeIds());
        }
        return ids;
    }

    /**
     * 根据指定id获取当前节点或子节点
     *
     * @param id
     * @return
     */
    public TreeNode getTreeNodeById(String id) {
        if (id.equals(this.id)) {
            return this;
        }
        if (null == this.children || this.children.isEmpty()) {
            return null;
        }
        Iterator<TreeNode> iterator = this.children.iterator();
        TreeNode next;
        while (iterator.hasNext()) {
            next = iterator.next();
            if ((next = next.getTreeNodeById(id)) != null) {
                return next;
            }
        }
        return null;
    }

    /**
     * 判断当前节点以及子节点是否存在指定的id节点
     *
     * @param id
     * @return
     */
    public boolean hasTreeNodeById(String id) {
        if (id.equals(this.id)) {
            return true;
        }
        if (null == this.children || this.children.isEmpty()) {
            return false;
        }
        Iterator<TreeNode> iterator = this.children.iterator();
        TreeNode next;
        while (iterator.hasNext()) {
            next = iterator.next();
            if (next.hasTreeNodeById(id)) {
                return true;
            }
        }
        return false;
    }
}
