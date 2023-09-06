package com.sky.controller.user;

import com.sky.entity.AddressBookEntity;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端-地址簿接口")
@Slf4j
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    @PostMapping
    public Result<String> addAddressBook(@RequestBody AddressBookEntity addressBookEntity) {
        addressBookService.insert(addressBookEntity);
        return Result.success();
    }

//    @PostMapping
//    public Result<String> addAddressBook() {
//
//    }
//
//    @PostMapping
//    public Result<String> addAddressBook() {
//
//    }
//
//    @PostMapping
//    public Result<String> addAddressBook() {
//
//    }
//
//    @PostMapping
//    public Result<String> addAddressBook() {
//
//    }
//
//    @PostMapping
//    public Result<String> addAddressBook() {
//
//    }
//
//    @PostMapping
//    public Result<String> addAddressBook() {
//
//    }
}
