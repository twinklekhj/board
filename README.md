# 게시판 프로젝트

## 0. 들어가며

가장 기본 중에 기본인 게시판 프로젝트를 진행한 이유는 다음과 같은 기술 스택을 프로젝트를 직접 만들어가며 익히기 위함입니다.
* 최신 버전인 `Java 17`과 `Spring Boot 3.x` 버전 기반 스택
* `Spring + React` Integration

<br>

## 1. 개발 환경

### Backend

* Language: ` Java 17`
* Framework: `Spring Boot 3.x`

* Build Tool: `Gradle`
* DBMS: `PostgreSQL 17` (PgAdmin 4)
* Hibernate: `Spring Data JPA` (+ `QueryDSL`)

<br>

### Frontend

* Language: `Typescript`
* Runtime: `Node 20.15.1`
* Framework: `React`
* Location: `board/src/main/frontend`

<br>



## 2. 주요 사항

### 공통

* **JWT(JSON Web Token)** 로 회원 인증
* CRUD로 알아볼 수 있는 REST API 설계

<br>

### Backend

* 기존 스택 Java 8, Spring 2.7 => **Java 17, Spring Boot 3.x (Migration)**
* 스택 Migration으로 인한 **Spring Security 6**의 **Configurer 패턴**을 이용하여 설정 적용
* **JWT 로직 처리**: 로그인 시 토큰 발생, 요청 별 JWT 인증 처리
* 보안 처리: CORS 처리, 권한 인증, 회원 인증
* Entity 생성자, 생성일시, 수정자, 수정일시 자동 업데이트

<br>

### Frontend

* **웹팩(Webpack)** 이용한 리소스 번들링, 네이밍(Alias) 등 세팅
* **Redux Toolkit**를 이용한 전역변수 관리. JWT 토큰 관리, 로그인 User 정보 둥
* **Custom Hook**을 이용한 **선언적 프로그래밍** (View와 Action 로직 분리)
* **JWT로 요청**: API 요청시 Header에 JWT 토큰 추가




