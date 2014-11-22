Cytus模拟器
============
Cytus是台湾雷亚公司(`Rayark Inc.`)出品的音乐游戏  
官网: [http://rayark.com/g/cytus/](http://rayark.com/g/cytus/)   

本程序使用Java语言,在Windows环境下编写  

使用的外部程序库
------------
- [JMF(Java Media Framework,Oracle Technology Network,`v2.1.1e`)](http://www.oracle.com/technetwork/java/javase/download-142937.html)
- [JLayer(`v1.0.1`)](http://www.javazoom.net/javalayer/javalayer.html)
- [FastJSON](https://github.com/alibaba/fastjson)

运行
------------
确保JRE版本在`1.8.0`或以上  
主程序入口: cytus.Main  
> java cytus.Main &lt;songtitle&gt;

需要在相同文件夹下加入assets子文件夹作为游戏数据才能正常运行  
(出于版权原因,不提供Cytus Lambda assets相关文件的下载)  

### assets目录结构
- assets  
	- /chapters  
	- /common  
	- /fonts
		- combo.fnt  
		- ComboSmall.fnt  
		- BoltonBold.fnt  
		- combo_0.png  
		- ComboSmall_0.png  
	- /notes  
	- /songs
		- /aquaticposeidon
			- aquaticposeidon.mp3
			- aquaticposeidon.easy.txt
			- aquaticposeidon.hard.txt
			- aquaticposeidon_bg.png
			- ...
		- /area184
		- /...
		- /zauberkugel
	- /sounds
		- beat1.wav
		- ...
	- /ui
		- /GamePlay
			- animation_1.json
			- animation_2.json
			- common.json
			- common_2.json
			- common_add.json
			- animation_1.png
			- animation_2.png
			- common.png
			- common_2.png
			- common_add.png
			- arrow_explode.anim.json
			- arrow_flash.anim.json
			- beat_flash.anim.json
			- beat_vanish.anim.json
			- critical_explosion.anim.json
			- drag_light.anim.json
			- explosion.anim.json
			- hold_pressing_1.anim.json
			- hold_pressing_2.anim.json
			- judge_bad.anim.json
			- judge_good.anim.json
			- judge_miss.anim.json
			- judge_perfect.anim.json
			- light_add_2.anim.json
			- node_explode.anim.json
			- node_flash..anim.json
			- ...
		- /...


### Preferences.txt参数说明
- convertmp3
	- 描述:是否将mp3转换成wav格式再播放(转换后的文件为test.wav)  
	- 值: 0,1  
	- 默认值: 1

----------

- bg
	- 描述:是否开启背景
		- 开启背景时,程序会寻找歌曲文件夹中的背景文件  
		- 格式: <songtitle>.png 或 <songtitle>_bg.png 或 bg.png  
	- 值: 0,1  
	- 默认值: 1

----------

- click_fx
	- 描述:是否开启打击音效 
	- 值: 0,1  
	- 默认值: 1

----------

- popupmode
	- 描述:设置note的显示方式,0为None,1为Default,2为Grouped  
	- 值: 0,1,2  
	- 默认值: 2

----------

- quality
	- 描述:设置绘画品质(1为低,2为中,3为高)  
	- 值: 1,2,3  
	- 默认值: 1

----------

- width&height
    - 描述:设置画面宽度和高度
    - 默认值: 720,480

----------

- showid
    - 描述:显示Note序号
    - 值: 0,1
    - 默认值: 0

----------

- inputmode
    - 描述:切换输入模式,0为Autoplay,1为类似osu的输入方式(测试)
    - 值: 0,1
    - 默认值: 0

----------

- nospecialpaint
    - 描述:当fps较低时,关闭部分效果
    - 值: 0,1
    - 默认值: 0


其他事项
-------------

### 运行截图
![Screenshot](https://github.com/Dewott/cytus/blob/master/ss.jpg) 

### ext/Kamcord 
实验性质的录像工具 

### ext/osu & ext/deemo
cytus谱面和osu!,Deemo谱面的互相转换(未测试)  

### cover/SelectCover
模拟歌曲封面动画 

### Editor
谱面编辑器(制作中,请给予帮助!)  

### 关于作者
程序作者: [Dewott(@GitHub)](https://github.com/Dewott/cytus)   
新浪微博: [@双贝君](http://weibo.com/Dewott502/)  
开始时间: 2014-01-27

Long Live the Rayark
------------
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM;``````````````````OMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMM8```````````````MM;``````````````NMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMM``````````````````MMMM``````````````````MMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMM`````````````````````8MMMMM;```````````````````'MMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMM``````````````````````MMMMMMMM``````````````````````MMMMMMMMMMMMMM
    MMMMMMMMMMMM```````````````````````;MMMMMMMMM'``````````````````````MMMMMMMMMMMM
    MMMMMMMMMM````````````````````````MMMMMMMMMMMM````````````````````````MMMMMMMMMM
    MMMMMMMM'````````````````````````OMMMMMMMM`'MMM;```````````````````````OMMMMMMMM
    MMMMMMM8````````````````````````8MMMMMMMM```;MMM````````````````````````MMMMMMMM
    MMMMMM;````````````````````````'MMMMMMMM``````MMM'MMMMMMMMMN`````````````8MMMMMM
    MMMMMM````````````````````````NMMMMMMM`NO;`````NMM`OMMMMMMMMMMM```````````MMMMMM
    MMMMM8``````````````'8MMO;````MMMMMMM```````````MMM'```OMMMMMMM```````````8MMMMM
    MMMMM'``````````````````````OMMMMMMM``````````````MM```'MMMMM8````````````;MMMMM
    MMMMM'`````````````````````'MMMMMM`````````````````MM'MMMMM8``````````````;MMMMM
    MMMMM8````````````````````'MMMMMM```````````````````MMMMM;````````````````8MMMMM
    MMMMMM````````````````````MMMMMN`````````````````8MMMM`;``````````````````MMMMMM
    MMMMMMO`````````````````;MMMMM````````````````MMMMM````M`````````````````NMMMMMM
    MMMMMMM;````````````````MMMMM``````````````MMMMN````````M'``````````````8MMMMMMM
    MMMMMMMM;`````````````'MMMMO```````````MMMM8`````````````8`````````````8MMMMMMMM
    MMMMMMMMMM````````````MMMM``````````````````MMMN``````````;'``````````MMMMMMMMMM
    MMMMMMMMMMMM`````````MMMM```````````````````````;MM'````````````````MMMMMMMMMMMM
    MMMMMMMMMMMMMM``````MMM;``````````````````````````````M;`````'````MMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMM;``MMM`````````````````````````````````````````;MMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMN```````````````````````````````````````OMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMM````````````````````````````````MMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM8'````````````````;NMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
    MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM