Cytus Simulator
============
Cytus is a music rhythm game produced by `Rayark Inc.`   
Official Website: [http://rayark.com/g/cytus/](http://rayark.com/g/cytus/)   

This program is written in Java and tested on Windows.  

Dependencies
------------
- [JMF(Java Media Framework,Oracle Technology Network,`v2.1.1e`)](http://www.oracle.com/technetwork/java/javase/download-142937.html)
- [JLayer(`v1.0.1`)](http://www.javazoom.net/javalayer/javalayer.html)
- [Apache Commons Collections(`v3.2.1`)](http://commons.apache.org/proper/commons-collections/)
- [Apache Commons BeanUtils(`v1.9.1`)](http://commons.apache.org/proper/commons-beanutils/)
- [Apache Commons Lang(`v2.6`)](http://commons.apache.org/proper/commons-lang/)
- [Apache Commons Logging(`v1.1.3`)](http://commons.apache.org/proper/commons-logging/)
- [EZMorph(`v1.0.6`)](http://sourceforge.net/projects/ezmorph/files/ezmorph/)
- [Json-lib(`v2.4`)](http://sourceforge.net/projects/json-lib/)

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
	- Description: Determines whether the mp3 file should be first converted into wav file(test.wav)
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

- note_effect
	- Description: Enable/Disable hitsound 
	- Value: 0,1  
	- Default Value: 1

----------

- popupmode
	- Description: Change the display style of the notes(0-None,1-Default,2-Grouped)  
	- Value: 0,1,2  
	- Default Value: 2

----------

- quality
	- Description: Set the quality of graphics(1-Low,2-Medium,3-High)  
	- Value: 1,2,3  
	- Default Value: 1

----------

- width&height
    - Description: Set window size
    - Default Value:960 & 640

Others
-------------

### Screenshot
![Screenshot1](https://github.com/Dewott/cytus/blob/master/ss1.jpg) 

![Screenshot2](https://github.com/Dewott/cytus/blob/master/ss2.jpg) 


### Preview Video
[http://pan.baidu.com/s/1jGmPOou](http://pan.baidu.com/s/1jGmPOou)

### ext/osu
Conversion between Cytus patterns and osu! beatmaps(beta)  

### Editor
Under construction

### About author
Author: [Dewott(@GitHub)](https://github.com/Dewott/cytus)   
Sina Weibo: [@双贝君](http://weibo.com/Dewott502/)  
Begin time: 2014-01-27
