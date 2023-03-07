FROM ibm-semeru-runtimes:open-17-jdk-focal

WORKDIR /usr/app
COPY build/libs/Supportbot-0.1-alpha-all.jar ./

CMD ["java", "-jar", "Supportbot-0.1-alpha-all.jar"]