package io.oigres.ecomm;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse;
import com.amazonaws.services.lambda.runtime.events.SQSBatchResponse.BatchItemFailure;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import io.oigres.ecomm.service.users.api.UsersService;
import io.oigres.ecomm.service.users.api.UsersServiceProxy;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.ArrayList;
import java.lang.reflect.Type;

public class S3EventHandler implements RequestHandler<SQSEvent, SQSBatchResponse> {
    private Logger log = LoggerFactory.getLogger(getClass());

    private UsersService usersService;
    private WebClient webClient;
    private Gson gson;
    private static final String BUCKET_PREFIX = "users/";

    public S3EventHandler() {
        log.info("**************************** S3EventHandler ****************************");
        String serviceUri = System.getenv("USERS_SERVICE_URI");
        log.info("Service URI: {}", serviceUri);
        webClient = WebClient.builder().build();
        webClient = WebClient.builder()
                .baseUrl(serviceUri)
                .clientConnector(
                        new ReactorClientHttpConnector(
                                HttpClient.create().responseTimeout(Duration.ofMillis(15000))
                        )
                )
                .build();
        usersService = new UsersServiceProxy(webClient, () -> "");
        gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .serializeNulls()
                .registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>() {
                    @Override
                    public JsonElement serialize(DateTime json, Type typeOfSrc, JsonSerializationContext context) {
                        return new JsonPrimitive(ISODateTimeFormat.dateTime().print(json));
                    }
                })
                .registerTypeAdapter(DateTime.class, new JsonDeserializer<DateTime>() {
                    @Override
                    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        DateTime dt = ISODateTimeFormat.dateTime().parseDateTime(json.getAsString());
                        return dt;
                    }
                })
                .setPrettyPrinting()
                .setVersion(1.0)
                .create();
    }

    @Override
    public SQSBatchResponse handleRequest(SQSEvent event, Context context) {
        log.info("**************************** handleRequest ****************************");
        log.debug("SQS Event: " + gson.toJson(event));
        log.debug("-----------------------------------------------------------------------");
        SQSBatchResponse response = new SQSBatchResponse();
        response.setBatchItemFailures(new ArrayList<>());
        if (event.getRecords() != null) {
            for (SQSMessage msg : event.getRecords()) {
                S3Event s3Event = null;
                String msgId = msg.getMessageId();
                String body = msg.getBody();
                log.info("Processing SQS message: {}", msgId);
                try {
                    body = body.replace("Records", "records");
                    s3Event = gson.fromJson(body, S3Event.class);
                } catch (JsonSyntaxException e) {
                    log.warn("Invalid S3Event format: ", e);
                    log.warn(body);
                    continue;
                }
                try {
                    doWork(s3Event, context);
                } catch (Exception e) {
                    log.warn("SQS message will be retry because of: ", e);
                    response.getBatchItemFailures().add(new BatchItemFailure(msgId));
                }
            }
        }
        log.info("***********************************************************************");
        return response;
    }

    public void doWork(S3Event s3Event, Context context) {
        log.debug("S3 Event: " + gson.toJson(s3Event));
        for(S3EventNotification.S3EventNotificationRecord record: s3Event.getRecords()) {
            String objectKey = record.getS3().getObject().getKey();
            log.info("S3 key {} was created", objectKey);
            String resourceName = objectKey.substring(objectKey.lastIndexOf("/") + 1);
            String folderPath = objectKey.replace(resourceName, "").replace(BUCKET_PREFIX, "");
            log.info("Resource Name: " + resourceName);
            log.info("Folder path: : " + folderPath);
            if(BlobType.PROFILE_IMAGE.getFolder().equals(folderPath)) {
                log.info("Changing status for profile image {}", objectKey);
                usersService.changeProfileImageStatus(objectKey);
            } else if(BlobType.MMJ_CARD_IMAGE.getFolder().equals(folderPath)) {
                log.info("Changing status for mmj card image {}", objectKey);
                usersService.updateCardImageStatus(objectKey);
            }
        }
    }

}