package com.sqx.modules.file.controller;



import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.qcloud.cos.region.Region;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.file.utils.FileUploadUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;



/**
 * 阿里云文件上传
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "阿里云文件上传", tags = {"阿里云文件上传"})
@RequestMapping(value = "/alioss")
@Slf4j
public class AliFileUploadController {


    private final CommonInfoService commonRepository;
    private AmazonS3 amazonS3;

    @Autowired
    public AliFileUploadController(CommonInfoService commonRepository, AmazonS3 amazonS3) {
        this.commonRepository = commonRepository;
        this.amazonS3 = amazonS3;
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ApiOperation("文件上传")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file){
        String value = commonRepository.findOne(234).getValue();
        if("1".equals(value)){
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream =new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath=getPath(suffix);
            ossClient.putObject(commonRepository.findOne(71).getValue(), completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //        String src = commonRepository.findOne(72).getValue()+"/"+completePath;
            String src = commonRepository.findOne(19).getValue()+"/img/"+completePath;
            return Result.success().put("data",src);
        }else if("2".equals(value)){
            String accessKey=commonRepository.findOne(800).getValue();
            String secretKey=commonRepository.findOne(801).getValue();
            String bucket=commonRepository.findOne(802).getValue();
            // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String path=commonRepository.findOne(804).getValue();
            String bucketName=commonRepository.findOne(803).getValue();
            String oldFileName = file.getOriginalFilename();
            String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID()+eName;
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DATE);
            // 1 初始化用户身份信息(secretId, secretKey)
            COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            ClientConfig clientConfig = new ClientConfig(new Region(bucket));
            // 3 生成cos客户端
            COSClient cosclient = new COSClient(cred, clientConfig);


            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
            // 大文件上传请参照 API 文档高级 API 上传
            File localFile = null;
            try {
                localFile = File.createTempFile("temp",null);
                file.transferTo(localFile);
                // 指定要上传到 COS 上的路径
                //String key = "/duanju/"+year+"/"+month+"/"+day+"/"+newFileName;
                String key = "/uploadPath/"+year+"/"+month+"/"+day+"/"+newFileName;
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
                PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
                //return Result.success().put("data",path + putObjectRequest.getKey());
                return Result.success().put("data","https://static.fanhuavideo.com" + putObjectRequest.getKey());
            } catch (IOException e) {
                return Result.error(-100,"文件上传失败！");
            }finally {
                // 关闭客户端(关闭后台线程)
                cosclient.shutdown();
            }
        }else if("3".equals(value)){
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream =new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath=getPath(suffix);
            String bucket=commonRepository.findOne(810).getValue();
            com.amazonaws.services.s3.model.PutObjectRequest putObjectRequest = new com.amazonaws.services.s3.model.PutObjectRequest(bucket, completePath, inputStream, new ObjectMetadata());

            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

            com.amazonaws.services.s3.model.PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);

            IOUtils.closeQuietly(inputStream);
            return Result.success().put("data",commonRepository.findOne(811).getValue()+"/"+completePath);
        }else {
            try
            {
                String http = commonRepository.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath ="/www/wwwroot/"+split[1]+"/file/uploadPath";
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = http +fileName;
                return Result.success().put("data",url);
            }
            catch (Exception e)
            {
                log.error("本地上传失败："+e.getMessage(),e);
                return Result.error(-100,"文件上传失败！");
            }
        }

    }

    @RequestMapping(value = "/uploadUniApp", method = RequestMethod.POST)
    @ApiOperation("文件上传")
    @ResponseBody
    public String uploadUniApp(@RequestParam("file") MultipartFile file){
        String value = commonRepository.findOne(234).getValue();
        if("1".equals(value)){
            // 创建OSSClient实例。
            OSS ossClient = new OSSClientBuilder().build(commonRepository.findOne(68).getValue(), commonRepository.findOne(69).getValue(), commonRepository.findOne(70).getValue());
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream =new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath=getPath(suffix);
            ossClient.putObject(commonRepository.findOne(71).getValue(), completePath, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            return commonRepository.findOne(19).getValue()+"/img/"+completePath;
        }else if("2".equals(value)){
            String accessKey=commonRepository.findOne(800).getValue();
            String secretKey=commonRepository.findOne(801).getValue();
            String bucket=commonRepository.findOne(802).getValue();
            // bucket的命名规则为{name}-{appid} ，此处填写的存储桶名称必须为此格式
            String path=commonRepository.findOne(804).getValue();
            String bucketName=commonRepository.findOne(803).getValue();
            String oldFileName = file.getOriginalFilename();
            String eName = oldFileName.substring(oldFileName.lastIndexOf("."));
            String newFileName = UUID.randomUUID()+eName;
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month=cal.get(Calendar.MONTH);
            int day=cal.get(Calendar.DATE);
            // 1 初始化用户身份信息(secretId, secretKey)
            COSCredentials cred = new BasicCOSCredentials(accessKey, secretKey);
            // 2 设置bucket的区域, COS地域的简称请参照 https://cloud.tencent.com/document/product/436/6224
            ClientConfig clientConfig = new ClientConfig(new Region(bucket));
            // 3 生成cos客户端
            COSClient cosclient = new COSClient(cred, clientConfig);


            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20 M 以下的文件使用该接口
            // 大文件上传请参照 API 文档高级 API 上传
            File localFile = null;
            try {
                localFile = File.createTempFile("temp",null);
                file.transferTo(localFile);
                // 指定要上传到 COS 上的路径
                String key = "/duanju/"+year+"/"+month+"/"+day+"/"+newFileName;
                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, localFile);
                PutObjectResult putObjectResult = cosclient.putObject(putObjectRequest);
                return path + putObjectRequest.getKey();
            } catch (IOException e) {
                return null;
            }finally {
                // 关闭客户端(关闭后台线程)
                cosclient.shutdown();
            }
        }else if("3".equals(value)){
            String suffix = file.getOriginalFilename().substring(Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf("."));
            // 上传文件流。
            InputStream inputStream = null;
            try {
                inputStream =new ByteArrayInputStream(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            String completePath=getPath(suffix);
            String bucket=commonRepository.findOne(810).getValue();
            com.amazonaws.services.s3.model.PutObjectRequest putObjectRequest = new com.amazonaws.services.s3.model.PutObjectRequest(bucket, completePath, inputStream, new ObjectMetadata());

            putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);

            com.amazonaws.services.s3.model.PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);

            IOUtils.closeQuietly(inputStream);
            return commonRepository.findOne(811).getValue()+"/"+completePath;
        }else{
            try
            {
                String http = commonRepository.findOne(19).getValue();
                String[] split = http.split("://");
                // 上传文件路径
                String filePath ="/www/wwwroot/"+split[1]+"/file/uploadPath";
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = http +fileName;
                return url;
            }
            catch (Exception e)
            {
                log.error("本地上传失败："+e.getMessage(),e);
                return null;
            }
        }

    }



    private String getPath(String suffix) {
        //生成uuid
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //文件路径
        String path =format(new Date()) + "/" + uuid;
        return path + suffix;
    }


    private String format(Date date) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            return df.format(date);
        }
        return null;
    }


}
