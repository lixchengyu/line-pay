# Credential

## Generate keystore

```shell
keytool -genkey -alias linepay -keyalg RSA -keystore keystore.jks \
    -validity 3650 -storetype JKS \
    -dname "CN=Lance Li, OU=Lance, O=Lance, L=Taipei, ST=Taiwan, C=TW" \
    -keypass keypass1234 -storepass storepass1234
```

## Encrypt store password

1. Encrypt key and keystore passwd
    ```shell
    java src/main/java/com/lance/util/AES.java keypass1234
    java src/main/java/com/lance/util/AES.java storepass1234
    ```
2. Put the text to .txt respective file under credential directory

## LINE Pay

1. Create sandbox account to get channel id and channel secret key
    - https://pay.line.me/jp/developers/techsupport/sandbox/testflow?locale=en_US
2. Put channel id and channel secret key to application.yaml

# Start application

```shell
./mvnw spring-boot:run
```

# Make Request

1. Create request
    - Post https://localhost:8443/line-pay/request
    - Body
        ```json
        {
            "amount": 40,
            "currency": "TWD",
            "orderId": "test_lance_10",
            "packages":
            [
                {
                    "id": "1",
                    "amount": 40,
                    "name": "TECH SHOP",
                    "products":
                    [
                        {
                            "id": "product_1",
                            "name": "USB 1T",
                            "quantity": 1,
                            "price": 40
                        }
                    ]
                }
            ],
            "redirectUrls":
            {
                "confirmUrl": "https://lance.requestcatcher.com/",
                "cancelUrl": "https://lance.requestcatcher.com/"
            }
        }
        ```
2. Pay the request by the response body paymentUrl
3. Confirm request
    - Post https://localhost:8443/line-pay/confirm
    - Body
        ```json
         {
             "amount": 40,
             "currency": "TWD",
             "transactionId": "2022082500725250810"
         }
        ```
4. Refund request
    - Post https://localhost:8443/line-pay/refund
    - Body
        ```json
         {
             "amount": 5,
             "transactionId": "2022082500725250810"
         }
        ```
5. Get request detail
    - Get https://localhost:8443/line-pay/payments?transactionId=2022082500725250810
