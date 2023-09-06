package com.sky.controller.user;

import com.sky.entity.AddressBookEntity;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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

    @GetMapping("/list")
    public Result<List<AddressBookEntity>> addAddressBook() {

        return Result.success(addressBookService.selectList());
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
}
