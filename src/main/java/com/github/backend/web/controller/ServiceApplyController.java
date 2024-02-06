package com.github.backend.web.controller;


import com.github.backend.service.ServiceApplyService;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.apply.ServiceApplyDto;
import com.github.backend.web.dto.apply.UserDto;
import com.github.backend.web.dto.apply.UserMyPageDto;
import com.github.backend.web.dto.apply.UserProceedingDto;
import com.github.backend.web.entity.custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
@Tag(name = "유저 API", description = "유저 서비스 신청 및 조회 API입니다.")
public class ServiceApplyController {
    private final ServiceApplyService serviceApplyService;
    @PostMapping("/service-apply")
    public ResponseEntity<CommonResponseDto> serviceApply(@RequestBody ServiceApplyDto serviceApplyDto,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("serviceApplyDto: " + serviceApplyDto);
        log.info("customUserDetails: " + customUserDetails);
        CommonResponseDto registerService = serviceApplyService.applyService(serviceApplyDto, customUserDetails);

        return ResponseEntity.ok().body(registerService);


    }

    @GetMapping("/status")
    public ResponseEntity<List<?>> getServicesByStatus(
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            @RequestParam String status) {
        if (status.equalsIgnoreCase("waiting")) {
            List<UserDto> waitingServices = serviceApplyService.findByServiceStatus(customUserDetails, status);
            return ResponseEntity.ok(waitingServices);
        } else if (status.equalsIgnoreCase("cancel")) {
            List<UserDto> cancelledServices = serviceApplyService.findByServiceStatus(customUserDetails, status);
            return ResponseEntity.ok(cancelledServices);
        } else if(status.equalsIgnoreCase("proceeding")){
            List<UserProceedingDto> proceedingServices = serviceApplyService.findByServiceStatus2(customUserDetails, status);
            return ResponseEntity.ok(proceedingServices);
        } else if (status.equalsIgnoreCase("completed")) {
            List<UserProceedingDto> completedServices = serviceApplyService.findByServiceStatus2(customUserDetails, status);
            return ResponseEntity.ok(completedServices);
        } else return ResponseEntity.badRequest().build();
    }
    @GetMapping("/mypage")
    public ResponseEntity<UserMyPageDto> getMyPage(@AuthenticationPrincipal CustomUserDetails customUserDetails){return ResponseEntity.ok().body(serviceApplyService.findByMyPage(customUserDetails));}

    @PutMapping("/cancel-service/{careCid}")
    public CommonResponseDto cancelWaitingService(@PathVariable Long careCid){return serviceApplyService.cancelByService(careCid);}

    @PutMapping("/rating-mate/{careCid}")
    public CommonResponseDto ratingMateService(@PathVariable Long careCid, Double starCount){return serviceApplyService.updateByMateStarCount(careCid, starCount);}

}
