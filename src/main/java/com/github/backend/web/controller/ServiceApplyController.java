package com.github.backend.web.controller;


import com.github.backend.service.ServiceApplyService;
import com.github.backend.web.dto.CommonResponseDto;
import com.github.backend.web.dto.apply.ServiceApplyDto;
import com.github.backend.web.dto.apply.UserDto;
import com.github.backend.web.dto.apply.UserMyPageDto;
import com.github.backend.web.dto.apply.UserProceedingDto;
import com.github.backend.web.dto.chatDto.CreatedChatRoomDto;
import com.github.backend.web.entity.custom.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
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

    @Operation(summary = "서비스 신청", description = "서비스를 신청합니다.")
    @PostMapping("/service-apply")
    public ResponseEntity<CreatedChatRoomDto> serviceApply(@RequestBody ServiceApplyDto serviceApplyDto,
                                                          @AuthenticationPrincipal CustomUserDetails customUserDetails){
        log.info("serviceApplyDto: {}", serviceApplyDto.toString());
        CreatedChatRoomDto registerService = serviceApplyService.applyService(serviceApplyDto, customUserDetails);

        return ResponseEntity.ok().body(registerService);
    }

    @Operation(summary = "상태별 조회", description = "상태별로 서비스를 조회합니다.")
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

    @Operation(summary = "유저 마이페이지 조회", description = "유저의 마이페이지를 조회합니다.")
    @GetMapping("/mypage")
    public ResponseEntity<UserMyPageDto> getMyPage(@AuthenticationPrincipal CustomUserDetails customUserDetails){return ResponseEntity.ok().body(serviceApplyService.findByMyPage(customUserDetails));}

    @Operation(summary = "서비스 취소하기", description = "서비스를 취소합니다.")
    @PutMapping("/cancel-service/{careCid}")
    public CommonResponseDto cancelWaitingService(@PathVariable Long careCid){return serviceApplyService.cancelByService(careCid);}


    @Operation(summary = "메이트 평가", description = "도움이 완료된 메이트를 평가합니다.")
    @PutMapping("/rating-mate/{careCid}")
    public CommonResponseDto ratingMateService(@PathVariable Long careCid, Double starCount){return serviceApplyService.updateByMateStarCount(careCid, starCount);}

}
