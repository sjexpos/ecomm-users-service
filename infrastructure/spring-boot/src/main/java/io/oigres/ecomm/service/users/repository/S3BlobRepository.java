package io.oigres.ecomm.service.users.repository;

import io.oigres.ecomm.service.users.config.UsersAssetsConfig;
import io.oigres.ecomm.service.users.domain.StoreLocation;
import io.oigres.ecomm.service.users.enums.BlobType;
import io.oigres.ecomm.service.users.repository.BlobRepository;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import software.amazon.awssdk.awscore.exception.AwsServiceException;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectsRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectsResponse;
import software.amazon.awssdk.services.s3.model.ObjectIdentifier;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.DeletedObject;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class S3BlobRepository implements BlobRepository {

    private final String bucketName;
    private final String rootFolder;
    private final Duration signatureDurationInMinutes;
    private final S3Presigner presigner;
    private final S3Client s3Client;

    public S3BlobRepository(UsersAssetsConfig config, S3Presigner presigner, S3Client s3Client) {
        this.bucketName = config.getBucket().getName();
        this.rootFolder = config.getBucket().getRootFolder();
        this.signatureDurationInMinutes = config.getBucket().getSignatureDuration();
        this.presigner = presigner;
        this.s3Client = s3Client;
    }

    public StoreLocation getStoreLocation(BlobType blobType, String extension) {
        String filename = UUID.randomUUID().toString().replace("-", "");
        String key = String.format("%s/%s/%s.%s", this.rootFolder, blobType.getFolder(), filename, extension);
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(this.bucketName)
                .key(key)
                .build();
        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(this.signatureDurationInMinutes)
                .putObjectRequest(putObjectRequest)
                .build();
        PresignedPutObjectRequest presignedRequest = this.presigner.presignPutObject(putObjectPresignRequest);
        StoreLocation location = StoreLocation.builder()
                .uploadURL(presignedRequest.url())
                .httpMethod(presignedRequest.httpRequest().method().name())
                .key(key)
                .build();
        return location;
    }

    @Override
    public List<String> deleteAllByKey(List<String> keysList) {
        List<String> resultList = Collections.emptyList();
        if (keysList != null && !keysList.isEmpty()) {
            try {
                List<ObjectIdentifier> objectIdentifierList = keysList.stream()
                        .map(key -> {
                            return ObjectIdentifier.builder()
                                    .key(key)
                                    .build();
                        })
                        .collect(Collectors.toList());
                DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                        .bucket(this.bucketName)
                        .delete( delete -> delete.objects(objectIdentifierList))
                        .build();
                DeleteObjectsResponse deletedObjectsResponse = s3Client.deleteObjects(deleteObjectsRequest);
                if (deletedObjectsResponse.hasDeleted() && !deletedObjectsResponse.deleted().isEmpty()) {
                    resultList = deletedObjectsResponse.deleted().stream()
                            .map(DeletedObject::key)
                            .collect(Collectors.toList());
                }
            } catch (AwsServiceException | SdkClientException e) {
                log.error("Error removing keys: ", e);
            }
        }
        return resultList;
    }

}
