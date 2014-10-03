Cytus模拟器
============
Cytus是台湾雷亚公司(`Rayark Inc.`)出品的音乐游戏  
官网: [http://rayark.com/g/cytus/](http://rayark.com/g/cytus/)   

本程序使用Java语言,在Windows环境下编写  

使用的外部程序库
------------
- [JMF(Java Media Framework,Oracle Technology Network,`v2.1.1e`)](http://www.oracle.com/technetwork/java/javase/download-142937.html)
- [JLayer(`v1.0.1`)](http://www.javazoom.net/javalayer/javalayer.html)
- [Apache Commons Collections(`v3.2.1`)](http://commons.apache.org/proper/commons-collections/)
- [Apache Commons BeanUtils(`v1.9.1`)](http://commons.apache.org/proper/commons-beanutils/)
- [Apache Commons Lang(`v2.6`)](http://commons.apache.org/proper/commons-lang/)
- [Apache Commons Logging(`v1.1.3`)](http://commons.apache.org/proper/commons-logging/)
- [EZMorph(`v1.0.6`)](http://sourceforge.net/projects/ezmorph/files/ezmorph/)
- [Json-lib(`v2.4`)](http://sourceforge.net/projects/json-lib/)

运行
------------
确保JRE版本在`1.8.0`或以上  
主程序入口: cytus.Main  
> java cytus.Main &lt;songtitle&gt;

需要在相同文件夹下加入assets子文件夹作为游戏数据才能正常运行  
(出于版权原因,不提供assets相关文件的下载)  

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

- note_effect
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
    - 默认值: 960,640

----------

- showid
    - 描述:显示Note序号
    - 值: 0,1
    - 默认值: 0

其他事项
-------------

### 运行截图
![Screenshot](https://github.com/Dewott/cytus/blob/master/ss.jpg) 

### ext/Kamcord 
实验性质的录像工具 

### ext/osu & ext/deemo
cytus谱面和osu!,Deemo谱面的互相转换(测试中) 

### Editor
谱面编辑器(制作中) 

### 关于作者
程序作者: [Dewott(@GitHub)](https://github.com/Dewott/cytus)   
新浪微博: [@双贝君](http://weibo.com/Dewott502/)  
开始时间: 2014-01-27