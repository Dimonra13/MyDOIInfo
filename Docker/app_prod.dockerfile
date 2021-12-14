FROM azul/zulu-openjdk:8u272
RUN mkdir /usr/app
COPY /. /usr/app
WORKDIR /usr/app
EXPOSE 80
CMD [ "java", "-jar", "target/MyDOIInfo-0.0.1-SNAPSHOT.jar" ]
