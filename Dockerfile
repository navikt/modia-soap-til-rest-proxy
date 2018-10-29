FROM docker.adeo.no:5000/fo/maven as builder

ARG app_name

ADD / /source
WORKDIR /source
RUN mvn package -DskipTests

FROM docker.adeo.no:5000/pus/nais-java-app
COPY --from=builder /source/target/modia-soap-til-rest-proxy /app
