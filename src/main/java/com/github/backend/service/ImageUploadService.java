package com.github.backend.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.internal.Mimetypes;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.github.backend.repository.ProfileImageRepository;
import com.github.backend.service.exception.ImageUploadException;
import com.github.backend.web.entity.ProfileImageEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageUploadService {
    private final ProfileImageRepository profileImageRepository;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    // 이미지 중복 방지를 위한 랜덤 이름 생성 ( s3에 올라가는데만 사용 )
    private String changedImageName(String ext){
      String random = UUID.randomUUID().toString();
      return random + ext;
    }

    //실제 S3에 저장되는 로직
    private String profileImageToS3(MultipartFile profileImage, String uuid){

      String changedName = "myProfileImage/" + uuid;
      ObjectMetadata metadata = new ObjectMetadata();
      metadata.setContentType(Mimetypes.getInstance().getMimetype(changedName));

      try{
        byte[] bytes = IOUtils.toByteArray(profileImage.getInputStream());
        metadata.setContentLength(bytes.length);
        ByteArrayInputStream byteArrayIs = new ByteArrayInputStream(bytes);

        PutObjectResult putObjectResult = amazonS3.putObject(new PutObjectRequest(
                bucketName, changedName, byteArrayIs, metadata
        ).withCannedAcl(CannedAccessControlList.PublicRead));
        log.info("[myProfileImageToS3] s3에 이미지가 업로드 되었습니다.");
      } catch (IOException e){
        throw new ImageUploadException(e.getMessage());
      }

      return amazonS3.getUrl(bucketName, changedName).toString();
    }

    //프로필 이미지 업로드 로직 ->  S3에 저장
    public ProfileImageEntity profileUploadImage(MultipartFile profileImage){

      String originName = profileImage.getOriginalFilename();
      String ext = originName.substring(originName.lastIndexOf("."));
      String uuid = changedImageName(ext);

      String imagePath = profileImageToS3(profileImage, uuid);

      log.info("[uploadImage] 이미지가 s3업데이트 메서드로 넘어갈 예정입니다. originName = " + originName);

      ProfileImageEntity newProfileImage = ProfileImageEntity.builder()
              .UUID(uuid)
              .fileExt(ext)
              .fileUrl(imagePath)
              .build();

      profileImageRepository.save(newProfileImage);

      return newProfileImage;
    }
    public void deleteImage(String s3ImagePath) {
        String key = extractKey(s3ImagePath);
        DeleteObjectRequest deleteRequest = new DeleteObjectRequest(bucketName, key);

        amazonS3.deleteObject(deleteRequest);
    }

    private String extractKey(String s3ImagePath){
        String splitStr = ".com/";
        return s3ImagePath.substring(s3ImagePath.lastIndexOf(splitStr) + splitStr.length());
    }
}
