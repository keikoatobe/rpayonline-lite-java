Java bindings for Rakuten Pay Online LITE
===================================
[![Build Status](https://travis-ci.org/rpayonline/rpayonline-lite-java.svg?branch=master)](https://travis-ci.org/rpayonline/rpayonline-lite-java)
[![Coverage Status](https://coveralls.io/repos/github/rpayonline/rpayonline-lite-java/badge.svg?branch=master)](https://coveralls.io/github/rpayonline/rpayonline-lite-java?branch=master)

[楽天ペイ（オンライン決済 LITE版）のAPI](https://lite.checkout.rakuten.co.jp/manual)をJavaで利用するためのSDKです。

動作環境
----------------------------------
Java 8

インストール
----------------------------------

Gradle
----------------------------------
build.gradleに以下のように設定を追加して下さい。

```groovy
repositories {
    jcenter()
    maven {
        url "https://rpayonline.bintray.com/rpayonline"
    }
}

dependencies {
    implementation 'jp.co.rakuten.checkout.lite:rpayonline-lite-java:1.0.0'
}

```

Maven
----------------------------------
pom.xmlに下記のように設定を追加して下さい。

```xml
<repositories>
    <repository>
        <id>rpayonline</id>
        <url>https://rpayonline.bintray.com/rpayonline</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>jp.co.rakuten.checkout.lite</groupId>
        <artifactId>rpayonline-lite-java</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

使い方
==================================

お支払いボタンの作成方法 
-----------------------------------

```java
public static void main(String args[]) {
    try{
        // 秘密鍵を設定
        RpayLite.setApiKey("***");
        // お支払ボタンのオブジェクトを作成
        Button b = new Button();
        // 公開鍵を設定
        b.setPublicKey("***");
        // お支払ボタンにItemオブジェクトを追加
        b.addItem("item-01", "item-name", 100, 2);
        // カートIDを設定
        b.setCartId("cart-id-1");
        // callback関数を設定
        b.setCallback("callback");
        // data-sigを設定
        b.setSignature(true);
        // お支払ボタンの<script>タグの出力
        System.out.println(b.build());
    } catch (RpayLiteException e){
        e.printStackTrace();
    }
}
```

結果例

```html
<script 
    src="https://lite.checkout.rakuten.co.jp/s/js/checkout-lite-v1.js" 
    class="checkout-lite-button" 
    data-key="***" 
    data-cart-id="cart-id-1"
    data-callback="callback" 
    data-item-id-1="item-01" 
    data-item-name-1="item-name" 
    data-item-unit-price-1="100" 
    data-item-quantity-1="2" 
    data-sig="###">
</script>
```

Charge objects
-----------------------------------

### 決済の取得

```java
public static void main(String args[]) {
    try{
        // 秘密鍵を設定
        RpayLite.setApiKey("***");
        // 注文番号を指定してChargeオブジェクトを取得
        Charge ch = Charge.retrieve("1250000255-20150623-0000168715");
        System.out.println(ch);
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```

結果例

```json
<jp.co.rakuten.checkout.lite.model.Charge@*** id=1250000255-20150623-0000168715> JSON: {
  "object": "charge",
  "open_id": "https://myid.rakuten.co.jp/openid/user/h65MxxxxxxxQxn0wJENoHHsalseDD==", 
  "id": "1250000255-20150623-0000168715",
  "cipher": "7d2558bba9a49d22a4788dca9395c21289b953571a92388891da8bb6b210a12d",
  "livemode": false,
  "currency": "jpy",
  "amount": 5000,
  "point": 1000,
  "cart_id": "cart_id1",
  "paid": true,
  "captured": true,
  "status": "succeeded",
  "refunded": false,
  "items": [
    {
      "id": "item_id1",
      "name": "商品名",
      "quantity": 10,
      "unit_price": 100
    },
    {
      "id": "item_id2",
      "name": "商品名",
      "quantity": 20,
      "unit_price": 200
    }
  ],
  "address": {
    "country": "JP",
    "first_name": "太郎",
    "first_name_kana": "タロウ",
    "last_name": "楽天",
    "last_name_kana": "ラクテン",
    "address_zip": "158-0094",
    "address_state": "東京都",
    "address_city": "世田谷区",
    "address_line": "玉川1-14-1",
    "tel": "000-0000-0000"
  },
  "created": 1433862000,
  "updated": 1433948400
}
```

### 決済の確定

```java
public static void main(String args[]) {
    try {
        // 秘密鍵を設定
        RpayLite.setApiKey("***");
        // 注文番号を指定してChargeオブジェクトを取得
        Charge ch = Charge.retrieve("1250000255-20150623-0000168715");
        System.out.println("ID:" + ch.getId() + "\n"
                       + "Amount:" + ch.getAmount() + "\n"
                       + "Captured:" + ch.getCaptured()
                       );
        // 確定処理を実行
        ch = ch.capture();
        System.out.println("ID:" + ch.getId() + "\n"
                       + "Amount:" + ch.getAmount() + "\n"
                       + "Captured:" + ch.getCaptured()
                       );
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```

結果例

```
ID:1250000255-20150623-0000168715
Amount:10000
Captured:false
ID:1250000255-20150623-0000168715
Amount:10000
Captured:true
```

### 決済の払い戻し

```java
public static void main(String args[]) {
    try{
        // 秘密鍵を設定
        RpayLite.setApiKey("***");
        // 注文番号を指定してChargeオブジェクトを取得
        Charge ch = Charge.retrieve("1250000255-20150623-0000168715");
        System.out.println("ID:" + ch.getId() + "\n"
                       + "Amount:" + ch.getAmount() + "\n"
                       + "Refunded:" + ch.getRefunded()
                       );
        // 払い戻しを実行
        ch = ch.refund();
        System.out.println("ID:" + ch.getId() + "\n"
                       + "Amount:" + ch.getAmount() + "\n"
                       + "Refunded:" + ch.getRefunded()
                       );
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```

結果例

```
ID:1250000255-20150623-0000168715
Amount:10000
Refunded:false
ID:1250000255-20150623-0000168715
Amount:10000
Refunded:true
```

### 決済の金額変更

```java
public static void main(String args[]) {
    try{
        // 秘密鍵を設定
        RpayLite.setApiKey("***");
        // 注文番号を指定してChargeオブジェクトを取得
        Charge ch = Charge.retrieve("1250000255-20150623-0000168715");
        System.out.println("ID:" + ch.getId() + "\n"
                       + "Amount:" + ch.getAmount()
                       );
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("item_id_1", "itemId-001");
        params.put("item_name_1", "item1");
        params.put("item_quantity_1", "5");
        params.put("item_unit_price_1", "1000");
        // 金額変更を実行
        ch = ch.update(params);
        System.out.println("ID:" + ch.getId() + "\n"
                       + "Amount:" + ch.getAmount()
                       );
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```
結果例

```
ID:1250000255-20150623-0000168715
Amount:10000
ID:1250000255-20150623-0000168715
Amount:5000
```


### 決済リストの取得

 * 3件の注文を取得するサンプル

ChargeオブジェクトとChargeCollectionを定義する。
list関数を呼ぶことで、APIにアクセスすることができる。

```java
public static void main(String args[]) {
    try{
        // 秘密鍵を設定
        RpayLite.setApiKey("***");
        Map<String, Object> chargeParams = new HashMap<String, Object>();
        chargeParams.put("limit",3);
        // Chargeオブジェクトのリストを取得
        ChargeCollection charges = Charge.list(chargeParams);
        for (Charge ch : charges.getData()) {
            System.out.println(
                "ID:" + ch.getId() + "\n" +
                "Amount:" + ch.getAmount()
            );
        }
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```

結果例

```
ID:1250000255-20150623-0000168715
Amount:10000
ID:1250000255-20150623-0000168716
Amount:10000
ID:1250000255-20150623-0000168717
Amount:10000
```

Notification objects
-----------------------------------

### メールの送信

```java
public static void main(String args[]) {
    try{
        // 秘密鍵を設定
        RpayLite.setApiKey("***");
        Map<String, Object> params = new HashMap<String, Object>();
        // メールのCharge番号を指定して内容を入力
        params.put("charge", "1250000255-20150623-0000168715");
        params.put("subject", "件名");
        params.put("body", "商品を発送しました。\\n#CUSTOMER_NAME# 様のまたのご利用をお待ちしております。");
        // メールを送信する
        Notification n = Notification.send(params);
        System.out.println(n);
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```

結果例

```json
<jp.co.rakuten.checkout.lite.model.Notification@*** id=ntfn_77cc7ad01dee4772a2f4ed4c9778da89> JSON: {
  "object": "notification",
  "id": "ntfn_77cc7ad01dee4772a2f4ed4c9778da89",
  "charge": "1250000255-20150623-0000168715",
  "type": "notification.mail",
  "subject": "件名",
  "body": "商品を発送しました。\\n#CUSTOMER_NAME# 様のまたのご利用をお待ちしております。",
  "created": 1456126606
}
```

### メール送信済み情報の取得

```java
public static void main(String args[]) {
    try{
        // 秘密鍵を設定
        RpayLite.setApiKey("***");
        // Charge オブジェクトのIDに紐づく、特定のIDを持ったメールの情報を取得。
        Notification n = Notification.retrieve("1250000255-20150623-0000168715", "ntfn_77cc7ad01dee4772a2f4ed4c9778da89");
        System.out.println("Charge:" + n.getCharge() + "\n"
                       + "Subject:" + n.getSubject() + "\n"
                       + "Body:" + n.getBody());
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```
結果例

```
Charge:1250000255-20150623-0000168715
Subject:件名
Body:商品を発送しました。\n#CUSTOMER_NAME# 様のまたのご利用をお待ちしております。
```

### メール送信済リストの取得

```java
public static void main(String args[]) {
    try{
        // 秘密鍵を設定
        RpayLite.setApiKey("***");
        // Charge オブジェクトのIDに紐づく全てのメールの情報を取得
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("charge", "1250000255-20150623-0000168715");
        NotificationCollection nc = Notification.list(params);
        for (Notification n : nc.getData()) {
            System.out.println("Charge:" + n.getCharge() + "\n"
                           + "Subject:" + n.getSubject() + "\n"
                           + "Body:" + n.getBody());
        }
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```
結果例

```
Charge:1250000255-20150623-0000168715
Subject:通知件名3
Body:通知本文3
Charge:1250000255-20150623-0000168715
Subject:通知件名2
Body:通知本文2
Charge:1250000255-20150623-0000168715
Subject:通知件名1
Body:通知本文1
```

Webhook
-----------------------------------

```java
// 事前にDashboardより、WebhookのSignatureを入手し、設定して下さい。
RpayLite.setWebhookSignature("webhook_xxxxxxxxxx");
 
// Httpサーバで、Webhookのリクエストヘッダを受け取って下さい。
String sigHeader = headers.get(RpayLite.SIGNATURE_HEADER);
// Httpサーバで、WebhookのリクエストボディをJSON形式で受け取って下さい
String payload = "{\"object\":\"event\", \"id\":\"evt_xxxxxxxxx\", \"type\":\"charge.check\", \"data\": \"...\"}";
 
// Event オブジェクトを生成
Event ev = Webhook.constructEvent(payload, sigHeader, RpayLite.getWebhookSignature());
//OR
Event ev = Webhook.constructEvent(payload, SigHeader);
```

Proxy Connection
-----------------------------------
```java
RpayLite.setConnectionProxy(<Proxy>);
RpayLite.setProxyCredential(<PasswordAuthentication>);
```

Proxy Authentication
-----------------------------------
（JRE8 Update111 以後のリビジョンをインストールしている方のみ）

### Basic 認証が必要な Proxy サーバ経由の通信の場合、以下の修正が必要です：

__以下ファイルを開き、対象行を削除もしくはコメントアウト（行の左端に「#」を追加）してください。__

+ ファイル（32bitOS の場合） ：C:\Program Files\Java\jre1.8.0_111\lib\net.properties

+ ファイル（64bitOS の場合） ：C:\Program Files (x86)\Java\jre1.8.0_111\lib\net.properties

	対象行 ：jdk.http.auth.tunneling.disabledSchemes=Basic

__※コメントアウトする場合、以下の通りとなります。__

+ 変更前 ：jdk.http.auth.tunneling.disabledSchemes=Basic

+ 変更後 ：#jdk.http.auth.tunneling.disabledSchemes=Basic

Contribution
----------------------------------
当リポジトリに対するPull Requestは現在受け付けておりません。バグや機能改善のご要望は当社の楽天ペイ（オンライン決済 LITE版）の[ヘルプページ](https://checkout.faq.rakuten.ne.jp/app/ask) よりお問い合わせ下さい。

ライセンス
----------------------------------
MITライセンスで配布しております。
