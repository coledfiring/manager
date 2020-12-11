package com.whaty.util;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 集合分割器
 * 传入两个集合C1，C2，分割器处理为
 * delete集：C1与C2的差集中为C1的真子集的集合部分
 * add集：C1与C2的差集中为C2的真子集的集合部分
 * same集，C1与C2的交集
 * <p>
 * 注意：传入的集合会自动去重并排序
 *
 * @author weipengsen
 */
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class CollectionDivider<T> {

    private final TreeSet<T> c1;

    private final TreeSet<T> c2;

    private final DivideData<T> target;

    public CollectionDivider(Collection<T> c1, Collection<T> c2) {
        this.c1 = CollectionUtils.isNotEmpty(c1) ? new TreeSet<>(c1) : new TreeSet<>();
        this.c2 = CollectionUtils.isNotEmpty(c2) ? new TreeSet<>(c2) : new TreeSet<>();
        this.target = this.divide();
    }

    /**
     * 分割
     *
     * @return
     */
    private DivideData<T> divide() {
        Set<T> same = this.c1.stream().filter(this.c2::contains).collect(Collectors.toSet());
        same.forEach(e -> {
            this.c1.remove(e);
            this.c2.remove(e);
        });
        return new DivideData<>(this.c1, this.c2, same);
    }

    public Set<T> getDelete() {
        return this.target.getDelete();
    }

    public Set<T> getAdd() {
        return this.target.getAdd();
    }

    public Set<T> getSame() {
        return this.target.getSame();
    }

    /**
     * 分割器返回结果
     *
     * @param <R>
     */
    @Getter
    @AllArgsConstructor
    @ToString
    private class DivideData<R> implements Serializable {

        private static final long serialVersionUID = 8463538662786370335L;

        private Set<R> delete;

        private Set<R> add;

        private Set<R> same;

    }

}
