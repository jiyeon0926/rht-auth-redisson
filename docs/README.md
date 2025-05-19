# 🛠️ 기술 스택
- Java 21
- Spring Boot 3.4.5 Version
- JPA
- Spring Security
- JWT
- Redis
- MySQL

# 💡 기능
- 일반 사용자 회원가입
- 관리자 회원가입
- 로그인
- 로그아웃
- Token 재발급

# 🧩 설계

## 1️⃣ ERD
![rht-auth-redis](https://github.com/user-attachments/assets/3da24304-c514-41a6-90ee-58471b7b018f)

## 2️⃣ API 명세서
👉 [Notion API 명세서](https://www.notion.so/JWT-1f2e22e7e413805ab06ac854faa19a8b)

<details>
  <summary>인증/인가 확인 테스트용 API</summary>
  <br>

|테스트|Method|URL|상태코드|응답 성공 메시지|
  |---|---|---|---|---|
|인증|GET|/auth/check|200 OK <br> 401 Unauthorized|인증된 사용자입니다.|
|사용자 권한|GET|/users/check|200 OK <br> 401 Unauthorized <br> 403 Forbidden|사용자와 관리자 모두 접근할 수 있습니다.|
|관리자 권한|GET|/admins/check|200 OK <br> 401 Unauthorized <br> 403 Forbidden|관리자만 접근할 수 있습니다.|

</details>