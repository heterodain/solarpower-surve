# solarpower-surve
DIY太陽光発電システムから電力量を取得してグラフ化します。  
(Get Power Information form PV Controller and graphed in "Ambient")

![SpringBoot](https://img.shields.io/badge/SpringBoot-2.5.3-green.svg)
![Lombok](https://img.shields.io/badge/Lombok-1.18.18-green.svg) 
![Jackson](https://img.shields.io/badge/Jackson-2.12.4-green.svg) 
![j2mod](https://img.shields.io/badge/j2mod-2.7.0-green.svg) 

[PVController (LS1024B)](https://www.epsolarpv.com/product/10.html)  
[Ambient](https://ambidata.io/)

1. DIY太陽光発電システム試作～PC接続 (Make prototype system, and PC connect)  
[![Video1](https://img.youtube.com/vi/6Da_AODUvFU/0.jpg)](https://www.youtube.com/watch?v=6Da_AODUvFU)

2. プログラム作成～Ambientでグラフ化 (Make program, and graphed by "Ambient")  
[![Video2](https://img.youtube.com/vi/0v6mcdFbdKQ/0.jpg)](https://www.youtube.com/watch?v=0v6mcdFbdKQ)

3. バッテリーの容量測定と復活の儀式 (Battery capacity measurement and Sulfation removal)  
[![Video3](https://img.youtube.com/vi/w3DU3fAgzpg/0.jpg)](https://www.youtube.com/watch?v=w3DU3fAgzpg)

4. 復活の儀式 約1ヶ月後 (A month after, Sulfation removal)  
[![Video4](https://img.youtube.com/vi/q_ZRtQihjoY/0.jpg)](https://www.youtube.com/watch?v=q_ZRtQihjoY)

5. 車に搭載 (Install in the car)  
[![Video4](https://img.youtube.com/vi/cT1pF2nqwzs/0.jpg)](https://www.youtube.com/watch?v=cT1pF2nqwzs)

6. 半年間使用したバッテリーの容量測定と復活の儀式 (About half year used battery, Capacity measurement and Sulfation removal)  
[![Video4](https://img.youtube.com/vi/Qpyq_MSj-ZQ/0.jpg)](https://www.youtube.com/watch?v=Qpyq_MSj-ZQ)

## 必要要件 (Requirement)
- Java 8 以降 (Java 8 or higher)
- Maven

## 使い方 (Usage)
1. PCとPVコントローラーをRS485 USBアダプターで接続してください。  
(Connect PC and PV controller with RS485 USB Adapter)

2. application.ymlを編集して、PVコントローラーやWEBサービスの接続情報を記入してください。  
(Edit application.yml and fills connect information of PV Controller and WEB service)  

3. 実行 (Execute)
    - VS Code上で実行 (Run on VS Code)  
    App.javaファイルを右クリックして実行 (Right-click on the App.java and run)
  
    - ターミナル上で実行 (Run on Terminal)  
        ```command
        mvn clean package
        java -jar solarpower-surve-1.0.jar
        ```
