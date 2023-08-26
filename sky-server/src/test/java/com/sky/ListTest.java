package com.sky;

import com.sky.entity.Dish;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 集合操作
 */
@Slf4j
public class ListTest {

    //1. 集合元素求和
    @Test
    public void test1() {
        List<Integer> list1 = Arrays.asList(1, 2, 3, 4);
        int sum = list1.stream().mapToInt(i -> i).sum();
        log.info("集合元素求和：{}", sum);
    }

    //2. 集合元素拼接
    @Test
    public void test2() {
        List<String> list2 = Arrays.asList("2020-01-01", "2020-01-02", "2020-01-03");
        String join = StringUtils.join(list2, ",");
        log.info("集合元素拼接：{}", join);
    }

    //3. 获取集合中所有元素的某个属性值进行拼接
    @Test
    public void test3() {
        List<Dish> list3 = new ArrayList<>();
        list3.add(Dish.builder().name("鱼香肉丝").build());
        list3.add(Dish.builder().name("水煮鲫鱼").build());
        list3.add(Dish.builder().name("清水蒸鸭").build());

        /*List<String> collect = list3.stream().map(dish -> dish.getName()).collect(Collectors.toList());
        String dishNames = StringUtils.join(collect, ",");*/

        //相当于上面两行代码简写
        String dishNames = list3.stream().map(dish -> dish.getName()).collect(Collectors.joining(","));

        log.info("获取集合中所有元素的某个属性值进行拼接：{}", dishNames);
    }

}
