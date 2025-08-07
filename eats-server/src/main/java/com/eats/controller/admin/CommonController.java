package com.eats.controller.admin;

import com.eats.constant.MessageConstant;
import com.eats.result.Result;
import com.eats.utils.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

/**
 * 共通インターフェー�?
 */
@RestController
@RequestMapping("/admin/common")
@Api(tags = "共通インターフェー�?")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;

    /**
     * ファイルアップロード
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ApiOperation("ファイルアップロード")
    public Result<String> upload(MultipartFile file){
        log.info("ファイルアップロード：{}",file);

        try {
            //元のファイル�?
            String originalFilename = file.getOriginalFilename();
            //元のファイル名の拡張子を切り取る dfdfdf.png
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            //新しいファイル名を構築
            String objectName = UUID.randomUUID().toString() + extension;

            //ファイルのリクエストパス
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (IOException e) {
            log.error("ファイルアップロードに失敗しました：{}", e);
        }

        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
