package com.whaty.function;

import lombok.AllArgsConstructor;

import java.io.Serializable;

/**
 * 二元组
 *
 * @author weipengsen
 */
@AllArgsConstructor
public class Tuple<T, R> implements Serializable {

    private static final long serialVersionUID = 2905996901882067031L;

    public T t0;

    public R t1;

}
