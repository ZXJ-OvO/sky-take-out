package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.entity.AddressBookEntity;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zxj
 */
@Service
@Slf4j
public class AddressBookServiceImpl implements AddressBookService {

    @Resource
    private AddressBookMapper addressBookMapper;


    @Override
    public void insert(AddressBookEntity addressBookEntity) {
        addressBookEntity.setUserId(BaseContext.getCurrentId());
        addressBookMapper.insert(addressBookEntity);
    }
}
