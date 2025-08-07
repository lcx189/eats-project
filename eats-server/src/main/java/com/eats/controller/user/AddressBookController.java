package com.eats.controller.user;

import com.eats.context.BaseContext;
import com.eats.entity.AddressBook;
import com.eats.result.Result;
import com.eats.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Api(tags = "C側アドレス帳API")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 現在ログインしているユーザーのすべてのアドレス情報を照会
     *
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("現在ログインしているユーザーのすべてのアドレス情報を照会")
    public Result<List<AddressBook>> list() {
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * アドレスを追
     *
     * @param addressBook
     * @return
     */
    @PostMapping
    @ApiOperation("アドレスを追加")
    public Result save(@RequestBody AddressBook addressBook) {
        addressBookService.save(addressBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    @ApiOperation("IDに基づいてアドレスを照会")
    public Result<AddressBook> getById(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * IDに基づいてアドレスを編集
     *
     * @param addressBook
     * @return
     */
    @PutMapping
    @ApiOperation("IDに基づいてアドレスを編集")
    public Result update(@RequestBody AddressBook addressBook) {
        addressBookService.update(addressBook);
        return Result.success();
    }

    /**
     * デフォルトアドレスを設定
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("デフォルトアドレスを設定")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * IDに基づいてアドレスを削除
     *
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("IDに基づいてアドレスを削除")
    public Result deleteById(Long id) {
        addressBookService.deleteById(id);
        return Result.success();
    }

    /**
     * デフォルトアドレスを照会
     */
    @GetMapping("default")
    @ApiOperation("デフォルトアドレスを照会")
    public Result<AddressBook> getDefault() {
        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = new AddressBook();
        addressBook.setIsDefault(1);
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("デフォルトアドレスが見つかりませんでした");
    }

}
