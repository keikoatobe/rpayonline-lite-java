Java bindings for Rpay Online LITE
===================================

[楽天ペイ（オンライン決済 LITE版）のAPI](https://lite.checkout.rakuten.co.jp/manual)をJavaで利用するためのSDKです。

動作環境
----------------------------------
Java 8 以上

インストール
----------------------------------

Gradle
----------------------------------
TBD

Maven
----------------------------------
TBD

使い方
==================================

お支払いボタンの作成方法
-----------------------------------

```java
public static void main(String args[]) {
    try{
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

```
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
        Rlite.setApiKey("***");
        // 注文番号を指定してChargeオブジェクトを取得
        Charge ch = Charge.retrieve("1250000255-20150623-0000168715");
        System.out.println("ID:" + ch.getId() + "\n"
                       + "Amount:" + ch.getAmount() + "\n"
                       + "Point:" + ch.getPoint()
                       );
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```

結果例

```
ID:1250000255-20150623-0000168715
Amount:5000
Point:1000
```

### 決済の確定

```java
public static void main(String args[]) {
    try {
        // 秘密鍵を設定
        Rlite.setApiKey("***");
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
        Rltie.setApiKey("***");
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
        Rltie.setApiKey("***");
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
        Rlite.setApiKey("***");
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
        Rlite.setApiKey("***");
        Map<String, Object> params = new HashMap<String, Object>();
        // メールのCharge番号を指定して内容を入力
        params.put("charge", "1250000255-20150623-0000168715");
        params.put("subject", "件名");
        params.put("body", "商品を発送しました。\\n#CUSTOMER_NAME# 様のまたのご利用をお待ちしております。");
        // メールを送信する
        Notification n = Notification.send(params);
        System.out.println("ID:" + n.getCharge() + "\n"
                       + "Subject:" + n.getSubject() + "\n"
                       + "Body:" + n.getBody()
                       );
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```

結果例

```
ID:1250000255-20150623-0000168715
Subject:件名
Body:商品を発送しました。\n#CUSTOMER_NAME# 様のまたのご利用をお待ちしております。
```

### メール送信済み情報の取得

```java
public static void main(String args[]) {
    try{
        // 秘密鍵を設定
        Rlite.setApiKey("***");
        // Charge オブジェクトのIDに紐づく、特定のIDを持ったメールの情報を取得。
        Notification n = Notification.retrieve("1250000255-20150623-0000168715", "ntfn_77cc7ad01dee4772a2f4ed4c9778da89");
        for (Notification n : nc.getData()) {
            System.out.println("Charge:" + n.getCharge() + "\n"
                           + Subject:" + n.getSubject() + "\n"
                           + Body:" + n.getBody());
        }
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```
結果例

```
ID:1250000255-20150623-0000168715
Subject:件名
Body:商品を発送しました。\n#CUSTOMER_NAME# 様のまたのご利用をお待ちしております。
```

### メール送信済リストの取得

```java
public static void main(String args[]) {
    try{
　　　　　　　// 秘密鍵を設定
        Rlite.setApiKey("***");
　　　　　　　// Charge オブジェクトのIDに紐づく全てのメールの情報を取得
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("charge", "1250000255-20150623-0000168715");
        NotificationCollection nc = Notification.list(params);
        for (Notification n : nc.getData()) {
            System.out.println("Charge:" + n.getCharge() + "\n"
                           + Subject:" + n.getSubject() + "\n"
                           + Body:" + n.getBody());
        }
    } catch (RpayLiteException e) {
        e.printStackTrace();
    }
}
```
結果例

```
ID:1250000255-20150623-0000168715
Subject:通知件名3
Body:通知本文3
ID:1250000255-20150623-0000168715
Subject:通知件名2
Body:通知本文2
ID:1250000255-20150623-0000168715
Subject:通知件名1
Body:通知本文1
```

Webhook
-----------------------------------

```java
// 事前にDashboardより、WebhookのSignatureを入手し、設定して下さい。
Rlite.setWebhookSignature("webhook_xxxxxxxxxx");
 
// Httpサーバで、Webhookのリクエストヘッダを受け取って下さい。
String sigHeader = headers.get(Rlite.SIGNATURE_HEADER);
// Httpサーバで、WebhookのリクエストボディをJSON形式で受け取って下さい
String payload = "{\"object\":\"event\", \"id\":\"evt_xxxxxxxxx\", \"type\":\"charge.check\", \"data\": \"...\"}";
 
// Event オブジェクトを生成
Event ev = Webhook.constructEvent(payload, sigHeader, Rlite.getWebhookSignature());
//OR
Event ev = Webhook.constructEvent(payload, SigHeader, null);
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
