FROM amazoncorretto:21-al2-jdk
LABEL AUTHOR = 'Sergio Exposito'
LABEL EMAIL = 'sjexpos@gmail.com'

# ENV JAVA_XMS             <set initial Java heap size>
# ENV JAVA_XMX             <set maximum Java heap size>
# ENV PORT                 <port to run server>
# ENV MONITORING_URL
# ENV DATABASE_HOST        <postgres server host name>
# ENV DATABASE_PORT        <postgres server port>
# ENV DATABASE_SCHEMA      <postgres schema>
# ENV DATABASE_USER        <postgres username>
# ENV DATABASE_PASSWORD    <postgres password>
# ENV REDIS_HOST           <redis server host name>
# ENV REDIS_PORT           <redis server port>
# ENV OPENSEARCH_CONN
# ENV KAFKA_SERVERS            <kafka servers host name and port>
# ENV KAFKA_SECURITY_PROTOCOL
# ENV KAFKA_SASL_MECHANISM
# ENV KAFKA_SASL_JAAS_CONFIG
# ENV KAFKA_EXTRAS
# ENV ASSETS_BUCKET        <Images bucket name>
# ENV AWS_ACCESS_KEY_ID
# ENV AWS_SECRET_ACCESS_KEY
# ENV AWS_REGION
# ENV TRACING_URL

ADD infrastructure/spring-boot/target/*.jar /opt/user-service.jar

RUN bash -c 'touch /opt/user-service.jar'

RUN echo "#!"

RUN echo "#!/usr/bin/env bash" > /opt/entrypoint.sh && \
    echo "" >> /opt/entrypoint.sh && \
    echo "echo \"===============================================\" " >> /opt/entrypoint.sh && \
    echo "echo \"JAVA_XMS: \$JAVA_XMS \" " >> /opt/entrypoint.sh && \
    echo "echo \"JAVA_XMX: \$JAVA_XMX \" " >> /opt/entrypoint.sh && \
    echo "echo \"===============================================\" " >> /opt/entrypoint.sh && \
    echo "echo \"PORT: \$PORT \" " >> /opt/entrypoint.sh && \
    echo "echo \"MONITORING_URL: \$MONITORING_URL\" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_HOST: \$DATABASE_HOST \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_PORT: \$DATABASE_PORT \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_SCHEMA: \$DATABASE_SCHEMA \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_USER: \$DATABASE_USER \" " >> /opt/entrypoint.sh && \
    echo "echo \"DATABASE_PASSWORD: \$DATABASE_PASSWORD \" " >> /opt/entrypoint.sh && \
    echo "echo \"REDIS_HOST: \$REDIS_HOST \" " >> /opt/entrypoint.sh && \
    echo "echo \"REDIS_PORT: \$REDIS_PORT \" " >> /opt/entrypoint.sh && \
    echo "echo \"OPENSEARCH_CONN: \$OPENSEARCH_CONN \" " >> /opt/entrypoint.sh && \
    echo "echo \"KAFKA_SERVERS: \$KAFKA_SERVERS \" " >> /opt/entrypoint.sh && \
    echo "echo \"KAFKA_SECURITY_PROTOCOL: \$KAFKA_SECURITY_PROTOCOL \" " >> /opt/entrypoint.sh && \
    echo "echo \"KAFKA_SASL_MECHANISM: \$KAFKA_SASL_MECHANISM \" " >> /opt/entrypoint.sh && \
    echo "echo \"KAFKA_SASL_JAAS_CONFIG: \$KAFKA_SASL_JAAS_CONFIG \" " >> /opt/entrypoint.sh && \
    echo "echo \"KAFKA_EXTRAS: \$KAFKA_EXTRAS \" " >> /opt/entrypoint.sh && \
    echo "echo \"ASSETS_BUCKET: \$ASSETS_BUCKET \" " >> /opt/entrypoint.sh && \
    echo "echo \"AWS_ACCESS_KEY_ID: \$AWS_ACCESS_KEY_ID \" " >> /opt/entrypoint.sh && \
    echo "echo \"AWS_SECRET_ACCESS_KEY: \$AWS_SECRET_ACCESS_KEY \" " >> /opt/entrypoint.sh && \
    echo "echo \"AWS_REGION: \$AWS_REGION \" " >> /opt/entrypoint.sh && \
    echo "echo \"TRACING_URL: \$TRACING_URL \" " >> /opt/entrypoint.sh && \
    echo "echo \"===============================================\" " >> /opt/entrypoint.sh && \
    echo "" >> /opt/entrypoint.sh && \
    echo "echo \"singleServerConfig:\" > /opt/redisson.yaml " >> /opt/entrypoint.sh && \
    echo "echo \"  address: redis://\$REDIS_HOST:\$REDIS_PORT\" >> /opt/redisson.yaml " >> /opt/entrypoint.sh && \
    echo "" >> /opt/entrypoint.sh && \
    echo "java -Xms\$JAVA_XMS -Xmx\$JAVA_XMX \
        -Dserver.port=\$PORT \
        -Dmanagement.server.port=\$PORT \
        -Dmanagement.otlp.tracing.endpoint=\$TRACING_URL \
        -Dspring.boot.admin.client.url=\$MONITORING_URL \
        -Dspring.datasource.host=\$DATABASE_HOST \
        -Dspring.datasource.port=\$DATABASE_PORT \
        -Dspring.datasource.schemaName=\$DATABASE_SCHEMA \
        -Dspring.datasource.username=\$DATABASE_USER \
        -Dspring.datasource.password=\$DATABASE_PASSWORD \
        -Dspring.data.redis.host=\$REDIS_HOST \
        -Dspring.data.redis.port=\$REDIS_PORT \
        -Dspring.jpa.properties.hibernate.cache.redisson.config=/opt/redisson.yaml \
        \$OPENSEARCH_CONN \
        -Dspring.kafka.bootstrap-servers=\$KAFKA_SERVERS \
        -Dspring.kafka.properties.security.protocol=\$KAFKA_SECURITY_PROTOCOL \
        -Dspring.kafka.properties.sasl.mechanism=\$KAFKA_SASL_MECHANISM \
        -Dspring.kafka.properties.sasl.jaas.config=\"\$KAFKA_SASL_JAAS_CONFIG\" \
        \$KAFKA_EXTRAS \
        -Decomm.service.users.assets.bucket.name=\$ASSETS_BUCKET \
        -jar /opt/user-service.jar" >> /opt/entrypoint.sh

RUN chmod 755 /opt/entrypoint.sh

EXPOSE ${PORT}

ENTRYPOINT [ "/opt/entrypoint.sh" ]
