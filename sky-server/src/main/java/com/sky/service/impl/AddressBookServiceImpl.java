package com.sky.service.impl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBookEntity;
import com.sky.mapper.AddressBookMapper;
import com.sky.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public List<AddressBookEntity> selectList() {
        return new LambdaQueryChainWrapper<>(AddressBookEntity.class).eq(AddressBookEntity::getUserId, BaseContext.getCurrentId()).list();
    }

    @Override
    public AddressBookEntity selectDefaultAddressBook() {
        return new LambdaQueryChainWrapper<>(AddressBookEntity.class).eq(AddressBookEntity::getUserId, BaseContext.getCurrentId()).one();
    }

    @Transactional
    @Override
    public void updateDefaultAddressBook(AddressBookEntity addressBookEntity) {
        List<AddressBookEntity> allAddressList = new LambdaQueryChainWrapper<>(AddressBookEntity.class)
                .eq(AddressBookEntity::getUserId, BaseContext.getCurrentId())
                .list();

        for (AddressBookEntity bookEntity : allAddressList) {
            bookEntity.setIsDefault(0);
            addressBookMapper.updateById(bookEntity);
        }

        AddressBookEntity entity = new LambdaQueryChainWrapper<>(AddressBookEntity.class)
                .eq(AddressBookEntity::getUserId, BaseContext.getCurrentId())
                .eq(AddressBookEntity::getId, addressBookEntity.getId())
                .one();

        if (entity != null) {
            entity.setIsDefault(1);
            addressBookMapper.updateById(entity);
        }

    }

    @Override
    public AddressBookEntity getAddressBookById(Long id) {
        return addressBookMapper.selectById(id);
    }

    @Override
    public void updateAddressBook(AddressBookEntity addressBookEntity) {
        addressBookMapper.updateById(addressBookEntity);
    }

    @Override
    public void deleteAddressBookById(Long id) {
        addressBookMapper.deleteById(id);
    }
}
