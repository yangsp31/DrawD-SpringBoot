# DrawD - OLED 다이어그램 작성 웹 서비스 (Back-end application)

### 사이트 방문 : https://do.drawd.store

* 해당 사이트는 사용자의 그 어떠한 정보도 서비스 이외의 목적으로 사용하지 않으며, 사용자의 결과물을 이용하거나 타인에게 공개하지 않습니다.
<br>

* 2025.02.03. - 진행중.
* 1인 프로젝트로 진행.
<br><br>

* ### 업데이트 예정
  * 실시간 협업 기능 (서버 부하정도에 따라서 WebSocket과 WebRTC중 선택하여 활용)
  * 난잡한 코드 구조 개선
<br><br><br>

# 개요

* 디스플레이 분야, 특히 OLED를 연구하는 대학원생의 연구 및 문서 작업 효율성을 높이기 위한 웹 서비스
* 사람이 손으로 그린 도식보다 정밀하고 객관적인 다이어그램을 작성할 수 있도록 지원하며, 실험 설계 단계나 연구 내용을 설명할 때 시각적 근거 자료로 활용될 수 있도록 개발
* AWS EC2에 Docker/Docker Compose를 사용하여 배포 (도메인을 구매하여 적용)
<br><br>

# Architecture
![ddddd drawio](https://github.com/user-attachments/assets/d7373aa8-0f06-4e1e-9b74-4604d887add6)
<br><br>

![image](https://github.com/user-attachments/assets/d4ca1065-9e6c-4885-b1c3-380c436316be)
<br><br>

# ERD
![image](https://github.com/user-attachments/assets/6cfd9a04-540f-4461-8e42-e33f2729c50c)
<br><br>

# 사용기술

* ### SpringBoot
  * 복잡한 개발 환경 설정이 필요없어, 빠른 개발이 가능하다고 판단하여 사용.
  * 배포와 실행이 간단한 환경을 제공하기 떄문에 사용.
  * 추후 기능/구조 수정과 확장을 포함한 유지보수가 간단하다고 판단하여 사용.
 
* ### OAuth2.0 (구글 로그인)
  * 간단한 인증과정을 사용자에게 제공하기 위해 사용.
 
* ### Spring Security
  * 인증/인가 과정을 간편하게 설정하기 위해 사용.
  * OAuth2.0을 사용한 소셜로그인 과정을 모두 서버에서 진행하기 위해 사용.
 
* ### JWT
  * 세션을 사용하는 대신, 인증과 인가를 진행하기 위해 사용.
 
* ### JSON Schema
  * 다이어그램 데이터의 구조를 표준화하고 검증하기 위해 JSON Schema를 사용.
 
* ### JPA
  * 추후 DB 접근이 필요한 로직을 쉽게 유지보수하고 확장할 수 있다고 판단하여 사용.
 
* ### MySql
  * 사용자와 다이어그램 데이터를 연관 지어 관리하기 위해 사용.
<br><br>

# 주요 개발내역

* ### 소셜로그인(구글 로그인) 구현 ([코드위치](https://github.com/yangsp31/DrawD-SpringBoot/blob/main/src/main/java/SJ_Project/DrawD/config/securityConfig.java))
  * 구글 계정은 기본적으로 신뢰성을 확보하고 있어, 이를 기반으로 로그인을 진행함으로써 보안을 강화하기 위해 구글 로그인을 선택.
  * 많은 사용자가 작업용 또는 개인용 PC에서 이미 구글 로그인을 한 상태이므로, 사용자 편의성을 위해 구글 로그인을 선택.
  * 클라이언트에서 반환된 인가 코드를 서버로 전달하는 방식에서 발생할 수 있는 탈취 및 공개 문제를 방지하고, 보안성을 강화하기 위해 소셜 로그인 전 과정을 서버에서 처리하도록 구현.
  <br>
  
  ![구글 로그인](https://github.com/user-attachments/assets/d6e4f5a7-0c53-411a-845d-48c8e152dbd6)
  
----

* ### JWT를 활용한 발급/갱신/인증 구현 ([코드위치](https://github.com/yangsp31/DrawD-SpringBoot/tree/main/src/main/java/SJ_Project/DrawD/service/auth))
  * 사용자의 요청에 인증을 진행하고, 자동로그인을 구현하기 위해 사용.
  * 유효기간이 짧고 요청시 매번 함께 전달되어 빈번하게 사용되는 AccessToken과, AccessToken의 유효기간이 만료 되었을때 AccessToken을 재발급 하기 위한 RefreshToken, 2개의 JWT를 발급하고 사용하도록 구축.
  * JWT 탈취 등으로 인한 보안위험에 대응하기 위해, 서버 무상태라는 JWT의 장점을 포기하고, RefreshToken은 발급시 DB에 저장하여 유저별로 관리하도록 구현.
  * 2개의 JWT를 각각의 쿠키에 담아 클라이언트로 반환하도록 구현.
  * JWT발급과 검증을 통한 인증은 Spring Security에 커스텀 핸들러를 등록하여 진행하도록 구현.
  <br>
  
  ![DrawD JWT](https://github.com/user-attachments/assets/a042e835-7a45-4c84-874e-36971dc2d5d5)
  
  ----

* ### JSON Schema를 활용하여 다이어그램 데이터 표준화 및 검증 구현 ([코드위치](https://github.com/yangsp31/DrawD-SpringBoot/blob/main/src/main/java/SJ_Project/DrawD/service/data/diagramUtil.java#L32))
  * 다이어그램 데이터는 사용자가 그린대로 그릴수 있도록 잘 저장하고 전달해야 할 중요한 데이터라 판단.
  * 다이어그램 데이터는 각각 다양한 상태 데이터를 담고 있기 때문에 복잡하기 때문에 표준화가 필요하다 판단.
  * 구성된 다이어그램 데이터 JSON Schema을 유지보수/기능확장에 사용하도록 프로젝트에 포함.
  * Json으로 구조화된 다이어그램 데이터의 유효성 검증과 표준화를 위해 JSON Schema를 사용하여 검증을 진행 하도록 구현.
  * [JSON Schema](https://github.com/yangsp31/DrawD_Next.js/blob/main/src/app/(page)/newD/%5Buuid%5D/page.tsx#L679)
  <br>
  
  ![Json val](https://github.com/user-attachments/assets/a311f906-8600-4199-a6df-39266720532e)
  
-----

* ### JPA를 사용하기 위한 연결테이블 매핑 시 관계 설정
  * 사용자 테이블과 다이어그램 데이터 테이블간의 다대다 관계를 해결하는 목적보단 연결하기 위한 테이블로 다이어그램 메타 테이블을 구축.
  * 다이어그램 메타 테이블 매핑 시 외래키 기반 복합키의 연관 조건을 설정할 때, FetchType.LAZY를 사용하여 연관된 엔티티가 실제로 필요한 시점에만 로딩되도록 구현.
<br><br>

# 회고 & 개선 필요사항/방법 (회고 원문 : [Velog](https://velog.io/@yang_seongp31/DrawD-SpringBoot-Backend-WAS))

* ### JWT 발급
  * AccessToken과 RefreshToken, 2개의 JWT를 발급할때 사용하는 유저 데이터가 완전히 동일함.
  * 이러한 발급 방식은 JWT를 2가지나 사용할 이유가 없으며 보안적 측면에서도 굉장히 치명적이라 판단.
  * 2개의 JWT에 암호화된 데이터의 내용이 동일하다면 그저 사용될 타이밍과 유효기간만 다를뿐인 JWT이며, 동일한 데이터이기 때문에 하나의 JWT만 탈취 되어도 모든 유저 데이터가 탈취 되기 때문.
  * AccessToken은 최소한의 유저를 판단할 수 있고, 탈취 되더라도 크게 치명적이지 않는 유저 데이터 (ex: Email)만을 사용하여 발급하도록 개선필요.
  * RefreshToken은 확실하게 해당 유저임을 판단할 수 있어야 하며, 탈취 되더라도 비교적 덜 치명적인 데이터 (ex : 사용자 명, 임의로 발급한 유저코드)를 사용하여 발급하도록 개선 필요.
<br><br>

* ### Cookie 경로 설정 및 사용방식
  * 발급된 JWT는 Cookie에 담아 반환됨.
  * 클라이언트는 전달 받은 쿠키중 AccessToken에 해당하는 쿠키를 직접 접근하여 LocalStorage에 저장.
  * 쿠키를 만들때 사용가능 경로를 모든 프로잭트의 URL로 설정하였기 때문에 모든 페이지에서 서버로 요청시 AccessToken과 RefreshToken에 해당하는 2개의 쿠키가 모두 전달되었기 때문.
  * 이 문제에서 선택한 방식은 AccessToken의 쿠키를 클라이언트에서 읽어 LocalStorage에 저장 후 서버로 요청시 AccessToken을 LocalStorage에서 읽어 요청 헤더의 Authorization에 넣어 보내는 방법을 선택.
  * 클라이언트에서 쿠키를 JavaScript로 접근한다는 것은 쿠키가 HttpOnly 설정이 false이므로 누구든지 쿠키를 탈취할 수 있다는 의미이므로 보안적으로 굉장히 위험한 방법이라 판단.
  * 또한 쿠키로 전달된 JWT를 쿠키로서 사용하지 않고 일일이 헤더에 넣어 요청한다는 것은 굉장히 소모적인 작업이라 판단.
  * Cookie의 사용 가능 페이지를 세분화 하여, Cookie를 Cookie답게 사용 하도록 개선 필요.
<br><br>
 
* ### 연결테이블 매핑 시 관계 설정
  * 사용자 테이블과 다이어그램 데이터 테이블간의 다대다 관계를 해결하는 목적보단 연결하기 위한 테이블로 다이어그램 메타 테이블을 구축함.
  * 이때 다이어그램 메타 테이블 매핑 시 외래키 기반 복합키의 연관 조건을 설정할 때, FetchType.LAZY를 사용하여 연관된 엔티티가 실제로 필요한 시점에만 로딩되도록 구현.
  * FetchType.LAZY설정은 연관된 데이터가 필요할떄 추가 쿼리를 발생시키기 떄문에, N+1 문제가 발생할 수 있다는 부분을 확인.
  * 그럼에도, 다이어그램 메타 테이블은 사용자가 작성하고 저장한 다이어그램 목록을 보여주는 용도로 사용되며, 외래키가 참조하는 다이어그램 데이터 테이블보다 사용 빈도가 많다고 판단함.
  * 따라서 N+1 문제를 감안 하더라도 FetchType.LAZY 설정은 합리적이라고 판단.
<br><br>

* ### 개선 필요사항/방법
  * AccessToken은 최소한의 유저를 판단할 수 있고, 탈취 되더라도 크게 치명적이지 않는 유저 데이터 (ex: Email)만을 사용하여 발급하도록 구축.
  * RefreshToken은 확실하게 해당 유저임을 판단할 수 있어야 하며, 탈취 되더라도 비교적 덜 치명적인 데이터 (ex : 사용자 명, 임의로 발급한 유저코드)를 사용하여 발급하도록 구축.
  * Cookie의 사용 가능 페이지를 세분화 하여, Front에서 직접 쿠키를 조작하지 못하게 구축.
