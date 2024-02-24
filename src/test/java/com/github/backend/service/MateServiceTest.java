package com.github.backend.service;

import com.github.backend.repository.MateCareHistoryRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MateServiceTest {

    @Autowired
    private MateCareHistoryRepository mateCareHistoryRepository;

    @Autowired
    private MateService mateService;

    @Test
    void applyService() throws InterruptedException {
        System.out.println("테스트 실행");

        for(int i = 1; i<= 2; i++) {
            long finalI = i;
            new Thread(()-> {
                System.out.println(finalI+"번째 쓰레드가 실행되고있습니다.");
                mateService.applyCaring(5L, finalI);
            }).start();
        }

        int count = mateCareHistoryRepository.countByCare_CareCid(5l);

        System.out.println(count);
    }
}
