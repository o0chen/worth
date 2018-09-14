package com.blackeye.worth.controller;

import com.blackeye.worth.core.customer.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 返回下拉框数据
 */
@RestController
@RequestMapping(value = "/options")
public class OptionsController extends BaseController {

    @Autowired
    private BaseService baseService;


    @ResponseBody
    @RequestMapping(value = "/getByTypes")
    public Map<String, List<Map>> getOptionsByTypes(@RequestBody Map<String, List<Map>> map) {
        for (String code : map.keySet()) {
            map.put(code,getOptionsByCode(code));
        }
        return map;
    }


    private List<Map> getOptionsByCode(String code) {
        List<Map> result = new ArrayList<>();
        switch (code) {
            case "roles":
                result = baseService.queryMapBySql("select id value, role_name label from sys_role where del_flag<>1");
                break;
            case "status":
                break;
            default:
                break;
        }
        return result;
    }
}