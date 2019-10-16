#!/bin/bash

echo "export JAVA_OPTS=\"-Dapp.env=staging\"" > /usr/local/tomcat/bin/setenv.sh
apt-get update && apt-get install -y gradle openjdk-8-jdk git vim
cd ~
curl -sL https://deb.nodesource.com/setup_8.x -o nodesource_setup.sh
bash nodesource_setup.sh
apt-get install -y nodejs
npm install -g bower
npm install -g grunt-cli 
apt-get clean
git clone https://github.com/igormarchenko/ejudge-standings.git /home/standings
cd /home/standings
git checkout v2.0

cd /home/standings/src/main/resources
echo "hibernate.driver = org.postgresql.Driver" >> db.properties
echo "hibernate.url = jdbc:postgresql://db:5432/standings" >> db.properties 
echo "hibernate.username =standings" >> db.properties 
echo "hibernate.password =standings" >> db.properties 
echo "hibernate.dialect = org.hibernate.dialect.PostgreSQL95Dialect" >> db.properties 
echo "hibernate.hbm2ddl.auto = update" >> db.properties 
cd /home/standings/
npm install 
bower install --allow-root 
grunt 
gradle build -x test
rm -rf /usr/local/tomcat/webapps/ROOT
cp /home/standings/build/libs/ejudge-standings-1.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war