package kr.cibusiter.foodplanner.conponent;//package com.shilladfs.pushportal.component;
//
//import com.shilladfs.pushportal.service.CommonService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//
///**
// * Created by whydda on 2018-04-17
// * File Ko Name : 웹 배치성 작업
// */
//public class BatchComp {
//
//    @Autowired
//    CommonService commonService;
//
//    /**
//     * 사용자 패스워드 만료 배치, 사용자 로그인 만료 배치
//     * 매일 1시 구동
//     */
//    @Scheduled(cron="0 0 1 * * *")
//    private void batchUserExpired(){
//        commonService.batchUserExpired();
//    }
//}
