# solarpower-surve
DIY太陽光発電システムから電力量を取得してグラフ化します。  
(Get Power Information form PV Controller and graphed in "Ambient")

![SpringBoot](https://img.shields.io/badge/SpringBoot-2.4.3-green.svg)
![Lombok](https://img.shields.io/badge/Lombok-1.18.18-green.svg) 
![Jackson](https://img.shields.io/badge/Jackson-2.11.4-green.svg) 
![j2mod](https://img.shields.io/badge/j2mod-2.7.0-green.svg) 

[PVController (LS1024B)](https://www.epsolarpv.com/product/10.html)  
[Ambient](https://ambidata.io/)

1. DIY太陽光発電システム試作～PC接続 (Make prototype system, and PC connect)  
[![Video1](https://img.youtube.com/vi/6Da_AODUvFU/0.jpg)](https://www.youtube.com/watch?v=6Da_AODUvFU)

2. プログラム作成～Ambientでグラフ化 (Make program, and graphed by "Ambient")  
[![Video2](https://img.youtube.com/vi/0v6mcdFbdKQ/0.jpg)](https://www.youtube.com/watch?v=0v6mcdFbdKQ)

3. バッテリーの容量測定と復活の儀式 (Battery capacity measurement and Sulfation removal)  
[![Video3](https://img.youtube.com/vi/w3DU3fAgzpg/0.jpg)](https://www.youtube.com/watch?v=w3DU3fAgzpg)

4. 復活の儀式 約1ヶ月後 (a month after, sulfation removal)  
[![Video4](https://img.youtube.com/vi/q_ZRtQihjoY/0.jpg)](https://www.youtube.com/watch?v=q_ZRtQihjoY)

## 必要要件 (Requirement)
- Java 8 以降 (Java 8 or higher)
- Maven

## 使い方 (Usage)
1. PCとPVコントローラーをRS485 USBアダプターで接続してください。  
(Connect PC and PV controller with RS485 USB Adapter)

3. application.ymlを編集して、PVコントローラーやWEBサービスの接続情報を記入してください。  
(Edit application.yml and fills connect information of PV Controller and WEB service)  

3. JARモジュール生成 (Create JAR)
```command
mvn clean package
```
4. 実行 (Execute)
```command
java -jar solarpower-surve-1.0.jar
```
