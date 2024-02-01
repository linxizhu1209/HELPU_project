package com.github.backend.web.controller;


import com.github.backend.service.ServiceApplyService;
import com.github.backend.web.dto.apply.CommonResponseDto;
import com.github.backend.web.dto.apply.ServiceApplyDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@Slf4j
@RequiredArgsConstructor
public class ServiceApplyController {
    private final ServiceApplyService serviceApplyService;

    @PostMapping("/service-apply")
    public ResponseEntity<CommonResponseDto> serviceApply(
            @RequestBody ServiceApplyDto serviceApplyDto){

        log.info("serviceApplyDto: " + serviceApplyDto);

        CommonResponseDto registerService = serviceApplyService.applyService(serviceApplyDto);

        return ResponseEntity.ok().body(registerService);
    }


}
