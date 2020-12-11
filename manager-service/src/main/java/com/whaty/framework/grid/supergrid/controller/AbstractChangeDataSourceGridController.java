package com.whaty.framework.grid.supergrid.controller;

import com.whaty.core.bean.AbstractBean;
import com.whaty.core.framework.api.domain.ParamsDataModel;
import com.whaty.core.framework.api.domain.ResultDataModel;
import com.whaty.core.framework.api.exception.ApiException;
import com.whaty.core.framework.grid.bean.GridConfig;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 更改数据源的controller
 *
 * @param <T>
 *
 * @author weipengsen
 */
public abstract class AbstractChangeDataSourceGridController<T extends AbstractBean> extends TycjGridBaseControllerAdapter<T> {


    @Override
    public GridConfig initGrid(ParamsDataModel<T> paramsData) {
        this.changeDataSource(paramsData);
        return super.initGrid(paramsData);
    }

    @Override
    public void initParams(ParamsDataModel<T> paramsData) {
        this.changeDataSource(paramsData);
    }

    @Override
    @RequestMapping({"/abstractList"})
    public ResultDataModel abstractList(@RequestBody ParamsDataModel<T> paramsData) {
        this.changeDataSource(paramsData);
        return super.abstractList(paramsData);
    }

    @Override
    @RequestMapping({"/abstractAdd"})
    public ResultDataModel abstractAdd(@RequestBody ParamsDataModel<T> paramsData) {
        this.changeDataSource(paramsData);
        return super.abstractAdd(paramsData);
    }

    @Override
    @RequestMapping({"/abstractUpdate"})
    public ResultDataModel abstractUpdate(@RequestBody ParamsDataModel<T> paramsData) {
        this.changeDataSource(paramsData);
        return super.abstractUpdate(paramsData);
    }

    @Override
    @RequestMapping({"/abstractDelete"})
    public ResultDataModel abstractDelete(@RequestBody ParamsDataModel<T> paramsData) throws ApiException {
        this.changeDataSource(paramsData);
        return super.abstractDelete(paramsData);
    }

    @Override
    @RequestMapping({"/abstractDetail"})
    public ResultDataModel abstractDetail(@RequestBody ParamsDataModel<T> paramsData) throws ApiException {
        this.changeDataSource(paramsData);
        return super.abstractDetail(paramsData);
    }

    /**
     * 根据传参切换数据源
     * @param paramsData
     */
    protected abstract void changeDataSource(ParamsDataModel<T> paramsData);

}
