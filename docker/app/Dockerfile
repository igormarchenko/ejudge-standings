FROM tomcat:8-jre8  
MAINTAINER Igor Marchenko

COPY startup.sh /home/
RUN ["/bin/bash", "/home/startup.sh"]
CMD ["catalina.sh", "run"]  