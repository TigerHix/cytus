Cytus Simulator
============
Cytus is a music rhythm game produced by `Rayark Inc.`   
Official Website: [http://rayark.com/g/cytus/](http://rayark.com/g/cytus/)   

This program is written in Java and tested on Windows.  

Dependencies
------------
- [JMF(Java Media Framework,Oracle Technology Network,`v2.1.1e`)](http://www.oracle.com/technetwork/java/javase/download-142937.html)
- [JLayer(`v1.0.1`)](http://www.javazoom.net/javalayer/javalayer.html)
- [FastJSON](https://github.com/alibaba/fastjson)

To Run
------------
JRE version 1.8.0 or above is required  
Main class: cytus.Main  
> java cytus.Main &lt;songtitle&gt;

Put *assets*  in the same folder as game data  
(Downloads of *assets* are NOT provided due to copyright issues)

### Folder Structure of *assets*
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


### Preferences.txt
- convertmp3
	- Description: Decides whether the .mp3 file should be first converted into .wav file(test.wav)
	- **NOTE:JMF will probably refuse to work if it is 0**
	- Value: 0,1  
	- Default Value: 1

----------

- bg
	- Description: Enable/Disable background
		- If on, the program will search for background files in the song folder  
		-  **&lt;songtitle&gt;.png** | **&lt;songtitle&gt;_bg.png** | **bg.png** 
	- Value: 0,1  
	- Default Value: 1

----------

- clickfx
	- Description: Enable/Disable hitsound 
	- Value: 0,1  
	- Default Value: 1

----------

- popupmode
	- Description: Change the pop-up mode of the notes(0-None,1-Default,2-Grouped)  
	- Value: 0,1,2  
	- Default Value: 2

----------

- quality
	- Description: Set the quality of the graphics(1-Low,2-Medium,3-High)  
	- Value: 1,2,3  
	- Default Value: 1

----------

- width&height
    - Description: Set the size of the window
    - Default Value:960 & 640

----------
- showid
    - Description: Show note id when drawing, for debug use
    - Value: 0,1
    - Default Value:0

----------

- inputmode
    - Description: 0 for Autoplay,1 for playing like osu!(Testing)
    - Value: 0,1
    - Default Value: 0

----------

- nospecialpaint
    - Description: Disable "Add" blending effects to increase fps
    - Value: 0,1
    - Default Value: 0


Others
-------------

### Screenshot
![Screenshot](https://github.com/Dewott/cytus/blob/master/ss.jpg) 

### ext/Kamcord
An experimental tool for recording videos 

### ext/osu & ext/deemo
Conversion between Cytus patterns and osu! beatmaps/Deemo compositions(Untested)  

### cover/SelectCover
Simulate song covers 

### Editor
Under construction(Needs your help!)

### Author
Author: [Dewott(@GitHub)](https://github.com/Dewott/cytus)   
Sina Weibo: [@双贝君](http://weibo.com/Dewott502/)  
Begin time: 2014-01-27

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
