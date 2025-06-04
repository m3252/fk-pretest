# 문승찬

## Overview
상품과 옵션을 관리하는 사전 과제입니다.

---

## 기술 스택
- Java 23, Spring Boot 3.x
- Spring Data JPA
- H2 (개발/테스트 DB)
- Spring Security + JWT 인증
- Gradle

---

### 실행 방법
```
./gradlew bootRun
```

---
## 설계 고려사항
- 도메인 계층 중심의 설계: Product, ProductOption은 Aggregate Root로 동작
- 양방향 연관 관계 관리: Product와 ProductOption 간 관계를 명확하게 하기 위해 연관 설정 메서드 구성
- Cascade 및 orphanRemoval 사용: 옵션 수정/삭제 시 하위 엔티티 자동 정리
- 에러 처리 일관화: CustomException + ErrorType 기반 전역 에러 처리 적용
- 단위 테스트/통합 테스트 구성: JPA 연동 테스트 포함

---


## Features

### 1. 사용자 (User)
- 회원가입, 로그인 (JWT 기반), 사용자 정보 조회, 수정, 탈퇴 처리
- Spring Security 기반 커스텀 인증 처리 구현
### 2. 상품 (Product)
- 상품 생성, 단건 조회, 전체 조회 (페이지네이션), 수정 기능 지원
- SKU, 배송비, 설명 등의 속성 포함
### 3. 옵션 (ProductOption)
- 상품별 옵션 등록, 조회, 수정, 삭제 가능
- 옵션 타입은 SELECT, INPUT 중 선택 가능
- 옵션 값(ProductOptionValue)을 포함하여 SELECT 타입일 경우 사용자가 선택 가능한 목록 제공
- 최대 3개의 옵션까지 등록 가능하도록 도메인 제약 구현

### API 목록
#### Product

| 메서드 | 경로               | 설명                   |
|--------|--------------------|------------------------|
| POST   | /products          | 상품 생성              |
| GET    | /products          | 상품 목록 조회 (페이지 지원) |
| GET    | /products/{id}     | 단일 상품 조회         |
| PUT    | /products/{id}     | 상품 정보 수정         |
#### ProductOption
| 메서드 | 경로                        | 설명                   |
|--------|-----------------------------|------------------------|
| POST   | /products/{productId}/options | 옵션 등록              |
| GET    | /products/{productId}/options | 옵션 목록 조회         |
| PUT    | /products/{productId}/options/{optionId} | 옵션 수정         |
| DELETE | /products/{productId}/options/{optionId} | 옵션 삭제         |
#### User
| 메서드 | 경로          | 설명                   |
|--------|---------------|------------------------|
| POST   | /users/signup | 회원가입 및 토큰 발급   |
| POST   | /users/login  | 로그인 및 토큰 발급     |
| GET    | /users/me     | 현재 사용자 정보 조회   |
| PUT    | /users/me     | 사용자 정보 수정       |
| DELETE | /users/me     | 회원 탈퇴 처리         |


### 그 외 남길 말
이번 과제를 통해 단순 CRUD 구현을 넘어서, 실제 서비스 확장성과 유지보수성을 고려한 구조를 고민했습니다.

도메인-애플리케이션-UI 구조로 계층 분리를 시도했고, 네이밍과 책임 분리를 의식적으로 적용했습니다.

Product와 Option의 관계는 단방향보다 양방향이 도메인 표현에 더 직관적이라 판단했고, 해당 관계에 대한 연관 설정과 cascade 옵션에도 신경을 썼습니다.

아쉬운 점은 개인 일정상 과제에 충분한 시간을 들이진 못해,

과제에 부족한 부분이 많은 것 같습니다. 그 외 설계 문서, 시큐리티 테스트, Swagger 문서 자동화 등은 우선순위에서 제외했습니다.

그럼에도 최대한 명확한 의도와 의미 있는 코드 중심으로 구성하려고 노력했습니다.