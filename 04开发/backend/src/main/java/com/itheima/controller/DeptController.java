package com.itheima.controller;


import com.itheima.anno.Log;
import com.itheima.pojo.Dept;
import com.itheima.pojo.Result;
import com.itheima.service.DeptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

    //@RequestMapping(value = "/depts",method = RequestMethod.GET)//method：指定请求方式
    @GetMapping
    public Result list(){
        System.out.println("查询全部部门数据");
        List<Dept> deptList = deptService.findAll();
        return Result.success(deptList);
    }

//    /**
//     * 删除部门-第一种方式：基于 HttpServletRequest
//     */
//
//    @DeleteMapping("/depts")
//    public Result delete(HttpServletRequest request){
//        String idStr = request.getParameter("id");
//        int id = Integer.parseInt(idStr);
//        System.out.println("删除部门： "+id);
//        return Result.success();
//    }
//
//    /**
//     * 删除部门-第二种方式：基于 @RequestParam
//     * 注意事项：一旦声明@RequestParam注解，那么改参数在请求时必须传递，否则会报错
//     * @param deptId
//     * @return
//     */
//    @DeleteMapping("/depts")
//    public Result delete(@RequestParam("id") Integer deptId){
//        System.out.println("删除部门： "+ deptId);
//        return Result.success();
//    }

    @Log
    @DeleteMapping
    public Result delete(@RequestParam Integer id){
        log.info("根据 ID 删除部门： {}", id);
//      调用 Service
        deptService.deleteById(id);
        return Result.success();
    }

    @Log
    @PostMapping
    public Result add(@RequestBody Dept dept){
        log.info("添加部门： {}", dept);
        deptService.add(dept);//不需要返回数据
        return Result.success();
    }

    /**
     * 根据 id 查询部门
     */
    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        log.info("查询部门： {}", id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }

    /**
     * 修改部门
     */
    @Log
    @PutMapping
    public Result update(@RequestBody Dept dept){
        log.info("修改部门： {}", dept);
        deptService.update(dept);
        return Result.success();
    }
}
