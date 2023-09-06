package com.sky.controller.user;

import com.sky.entity.AddressBookEntity;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zxj
 */
@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C端-地址簿接口")
@Slf4j
public class AddressBookController {

    @Resource
    private AddressBookService addressBookService;

    @PostMapping
    public Result<String> insert(@RequestBody AddressBookEntity addressBookEntity) {
        addressBookService.insert(addressBookEntity);
        return Result.success();
    }

    @GetMapping("/list")
    public Result<List<AddressBookEntity>> getList() {
        return Result.success(addressBookService.selectList());
    }

    @GetMapping("/default")
    public Result<AddressBookEntity> getDefault() {
        return Result.success(addressBookService.selectDefaultAddressBook());
    }

    @PutMapping("/default")
    public Result<String> putDefault(@RequestBody AddressBookEntity addressBookEntity) {
        addressBookService.updateDefaultAddressBook(addressBookEntity);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<AddressBookEntity> getById(@PathVariable Long id) {
        return Result.success(addressBookService.getAddressBookById(id));
    }

    @PutMapping
    public Result<String> update(@RequestBody AddressBookEntity addressBookEntity) {
        addressBookService.updateAddressBook(addressBookEntity);
        return Result.success();
    }

    @DeleteMapping
    public Result<String> delById(@RequestParam Long id) {
        addressBookService.deleteAddressBookById(id);
        return Result.success();
    }
}
