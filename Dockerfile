FROM clojure AS development

WORKDIR /authorizer

COPY . /authorizer

RUN lein deps

FROM development AS application

CMD ["lein", "run"]

FROM development AS application-test

CMD ["lein", "test"]
