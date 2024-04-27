package com.sqx.modules.file.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.sqx.modules.common.service.CommonInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
public class AWSConfiguration {

    @Autowired
    private CommonInfoService commonInfoService;

    @Bean
    public BasicAWSCredentials basicAWSCredentials() {
        return new BasicAWSCredentials(commonInfoService.findOne(807).getValue(), commonInfoService.findOne(808).getValue());
    }

    @Bean
    public AmazonS3 amazonS3Client(AWSCredentials awsCredentials) {
        AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
        builder.withCredentials(new AWSStaticCredentialsProvider(awsCredentials));
        builder.setRegion(commonInfoService.findOne(809).getValue());
        AmazonS3 amazonS3 = builder.build();
        return amazonS3;
    }
}
