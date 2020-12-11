package com.whaty.products.controller.information;

import com.whaty.domain.bean.PeManager;
import com.whaty.framework.grid.adapter.controller.TycjGridBaseControllerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通讯录管理
 * @author pingzhihao
 */
@Lazy
@RestController
@RequestMapping("/entity/information/userAddressBook")
public class UserAddressBookController extends TycjGridBaseControllerAdapter<PeManager> {
}
