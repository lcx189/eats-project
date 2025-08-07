package com.eats.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.wechat")
@Data
public class WeChatProperties {

    private String appid; //ミニプログラムのappid
    private String secret; //ミニプログラムのシークレットキー
    private String mchid; //事業者番
    private String mchSerialNo; //事業者API証明書のシリアル番号
    private String privateKeyFilePath; //事業者秘密鍵ファイル
    private String apiV3Key; //証明書復号化キー
    private String weChatPayCertFilePath; //プラットフォーム証明
    private String notifyUrl; //支払い成功時のコールバックアドレ
    private String refundNotifyUrl; //返金成功時のコールバックアドレス

}
