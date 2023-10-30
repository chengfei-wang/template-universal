FROM openjdk:11

RUN chsh -s /bin/bash
ENV SHELL=/bin/bash

RUN adduser --gecos '' --disabled-password template

ADD --chown=template bin /home/template

EXPOSE 20000
USER template
WORKDIR /home/template
ENTRYPOINT [ "java", "-jar", "/home/template/template-universal-1.0.0.jar", "--spring.profiles.active=prod" ]