package com.hana4.keywordhanaro.model.entity.transaction;

/**
 * 거래 status 관리
 */
public enum TransactionStatus {
    SUCCESS,
    FAILURE,	// 잔액 부족 또는 계좌 오류 및 서버 장애로 인해 거래 처리 불가.
    PENDING    // 네트워크 지연이나 사용자가 OTP 인증 후 거래를 진행할 수 있을 때까지 대기.
}
