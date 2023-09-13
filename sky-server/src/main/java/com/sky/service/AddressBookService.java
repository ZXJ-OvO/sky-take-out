package com.sky.service;

import com.sky.entity.AddressBookEntity;

import java.util.List;

/**
 * @author zxj
 */
public interface AddressBookService {

    void insert(AddressBookEntity addressBookEntity);

    List<AddressBookEntity> selectList();

    AddressBookEntity selectDefaultAddressBook();

    void updateDefaultAddressBook(AddressBookEntity addressBookEntity);

    AddressBookEntity getAddressBookById(Long id);

    void updateAddressBook(AddressBookEntity addressBookEntity);

    void deleteAddressBookById(Long id);
}
