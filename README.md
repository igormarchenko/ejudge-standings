##Description
Interactive standings table for [Ejudge](http://ejudge.ru). 
##Requirements

###Tomcat 7
sudo apt-get install tomcat7

###Gradle
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt-get install gradle

###NodeJS
sudo apt-get install nodejs

###NPM
sudo apt-get install npm

##Install
*git clone https://github.com/igormarchenko/ejudge-standings.git*<br/>
*cd ejudge-standings*<br /> 
*npm install*<br />
*bower install*<br />
*grunt*<br />
*gradle build*<br />

##Usage

####Settings file: src/main/resources/sources.xml
*login* &ndash; username for unfreeze results.<br />
*password* &ndash; password for unfreeze results.<br />
*tables* &ndash; files external.xml (acepted local files or http links).<br />
**You can specify a lot of files in one contest.**<br/>
*file* &ndash; file description.<br />
&nbsp;&nbsp;*value* &ndash; path to external.xml.<br />
&nbsp;&nbsp;*contest* &ndash; contest id (must be equals for all standings in one contest).<br />
&nbsp;&nbsp;*final* &ndash; when it sets true displayed unfreezed stanings table. When value is false - table is freezed.<br />

*team-university-file* &ndash;	file with mapping from team to university.<br />
*university-type-file* &ndash; file with mapping university to its region.<br />
*baylor-teams-file* &ndash; file which exported from baylor web-site. Using for upload results to baylor servers.<br />
	

###Useful URL
To authorize user go to link */authorize?login=<b>LOGIN</b>&password=<b>PASSWORD</b>* where *<b>LOGIN</b>* and *<b>PASSWORD</b>* - values, specified in file sources.xml.
After that you can unfreeze standings. To start unfreeze process press "Unfreeze" button. To find next frozen submission and unfreeze it press **n** key on your keyboard. <br/>
Use */baylor* URL to create .xml for upload to baylor web-site.
