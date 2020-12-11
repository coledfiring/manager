package com.whaty.handler;

import org.apache.commons.collections.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 分批执行处理类
 * @param <R>
 * @author weipengsen
 */
public class DivideWorkHandler<R> {

    private final int divideNumber;

    private final Consumer<List<R>> consumer;

    private final Consumer<List<R>> rollback;

    public DivideWorkHandler(int divideNumber, Consumer<List<R>> consumer, Consumer<List<R>> rollback) {
        this.divideNumber = divideNumber;
        this.consumer = consumer;
        this.rollback = rollback;
    }

    public void handle(List<R> data) {
        List<R> handledData = new ArrayList<>();
        try {
            for (List<R> divideDatum : this.divideData(data)) {
                if (this.consumer != null) {
                    this.consumer.accept(divideDatum);
                    handledData.addAll(divideDatum);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (this.rollback != null && CollectionUtils.isNotEmpty(handledData)) {
                this.rollback.accept(handledData);
            }
            throw e;
        }
    }

    private List<List<R>> divideData(List<R> data) {
        List<List<R>> divideData = new ArrayList<>();
        if (data.size() > this.divideNumber) {
            int temp = data.size() / this.divideNumber + 1;
            for (int i = 0; i < temp; i++) {
                List<R> elemData = new ArrayList<>();
                for (int j = (i) * this.divideNumber; j < ((i + 1) * this.divideNumber > data.size() ?
                        data.size() :
                        (i + 1) * this.divideNumber); j++) {
                    elemData.add(data.get(j));
                }
                if (elemData.size() > 0) {
                    divideData.add(elemData);
                }
            }
        } else {
            divideData.add(data);
        }
        return divideData;
    }
}
