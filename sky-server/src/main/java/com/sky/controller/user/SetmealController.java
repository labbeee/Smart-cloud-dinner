package com.sky.controller.user;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.mapper.SetmealMapper;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.utils.BloomFilterUtil;
import com.sky.vo.DishItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端-套餐浏览接口")
public class SetmealController {

    // 预期插入数量
    static long expectedInsertions = 200L;
    // 误判率
    static double falseProbability = 0.01;

    // 非法请求所返回的JSON
    static String illegalJson = "[\"com.company.springboot.entity.User\",{\"id\":null,\"userName\":\"null\",\"sex\":null,\"age\":null}]";

    private RBloomFilter<Long> bloomFilter = null;

    @Autowired
    private BloomFilterUtil bloomFilterUtil;
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private SetmealService setmealService;
    @Autowired
    private SetmealMapper setmealMapper;

    ArrayList list = new ArrayList();

    @PostConstruct
    public void init(){
        List<Setmeal> setmealList = setmealMapper.listAll();
        bloomFilter = bloomFilterUtil.create("idWhiteList", expectedInsertions, falseProbability);
        for(Setmeal setmeal : setmealList){
            bloomFilter.add(setmeal.getCategoryId());
        }
    }

    /**
     * 条件查询
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据分类id查询套餐")
    @Cacheable(cacheNames = "setmealCache",key = "#categoryId", unless = "#result == null") //key: setmealCache::100
    public Result<List<Setmeal>> list(Long categoryId) {
        //bloomFilter中不存在该Key,为非法访问
        if(!bloomFilter.contains(categoryId)){
            System.out.println("非法key!!!");
            redissonClient.getBucket("setmeal::" + categoryId, new StringCodec()).set(illegalJson, new Random().nextInt(200) + 300, TimeUnit.SECONDS);
            return Result.error("非法Key!!!");
        }
        //合法访问， 访问数据库
        System.out.println("数据库中可以得到数据...");
        Setmeal setmeal = new Setmeal();
        setmeal.setCategoryId(categoryId);
        setmeal.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = setmealService.list(setmeal);
        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的菜品列表
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    @ApiOperation("根据套餐id查询包含的菜品列表")
    public Result<List<DishItemVO>> dishList(@PathVariable("id") Long id) {
        List<DishItemVO> list = setmealService.getDishItemById(id);
        return Result.success(list);
    }
}
