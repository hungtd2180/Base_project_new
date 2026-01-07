# Base Project Java Spring Boot – Hướng dẫn sử dụng làm nền tảng phát triển

Dự án này cung cấp một khung (baseline) Spring Boot đã tích hợp sẵn các thành phần cốt lõi: xác thực JWT/AccessToken, quản lý phiên, RSQL search cho JPA, tầng CRUD mẫu, cache trong bộ nhớ, cấu hình bảo mật, hạ tầng repository/converter, cùng các tiện ích chung. Mục tiêu là giúp bạn khởi tạo nhanh dự án mới, tập trung vào nghiệp vụ mà không phải lặp lại phần nền tảng.

## Tính năng chính
- Bảo mật và xác thực
  - JWT và AccessToken: phát hành, xác thực, gia hạn token
  - Lọc yêu cầu bằng JwtFilter, tích hợp SecurityConfig
  - Phân quyền theo Role/Privilege, seed dữ liệu ban đầu (InitialDataLoader)
- Quản lý phiên (Session)
  - Tạo, duy trì, và hủy phiên thông qua SessionService và SessionEndpoint
- Tìm kiếm động với RSQL cho JPA
  - Hỗ trợ RSQL query qua GenericRsqlSpecification, CustomRsqlVisitor, RsqlSearchOperation
- CRUD mẫu theo best practice
  - CrudService/CrudEndpoint mẫu để mở rộng nhanh cho entity mới
- Tầng Repository mở rộng
  - CustomJpaRepository cho các tiện ích JPA chung
- Caching trong bộ nhớ
  - MemoryCacheService, UserCacheService, TokenCacheService
- Cấu hình Spring tiện dụng
  - BeansConfig, SecurityConfig, SpringContext, cấu hình YAML theo profile
- Tiện ích dùng chung
  - CommonUtils, StringUtils, EncryptUtils, SecurityUtil
- Converter cho JPA
  - List/String converters (ListStringConverter, SetStringConverter, ListToStringConverter)
- Mẫu domain sẵn có
  - User, Role, Privilege, Token/RefreshToken, UserAttempt
- Endpoint mẫu
  - AuthEndpoint, SessionEndpoint, UserEndpoint, CrudEndpoint
- Kiểm thử cơ bản
  - ApplicationTests, JwtTokenServiceTest

## Cấu trúc thư mục (tóm tắt)
```
src/main/java/org/example/base/
  configurations/    # Spring configs, Security, Beans, InitialDataLoader
  constants/         # Hằng số hệ thống, mã lỗi
  converters/        # JPA AttributeConverters
  endpoints/         # REST endpoints mẫu (Auth/Session/User/Crud)
  filters/           # JwtFilter
  models/            # DTOs, Entities, Error info
  repositories/      # JPA repositories + CustomJpaRepository
  rsql/              # RSQL integration cho JPA
  services/          # Business services (token, user, cache, crud, session)
  utils/             # Tiện ích chung (encrypt, string, security)
Application.java      # Điểm khởi chạy Spring Boot
```

## Bắt đầu nhanh
1) Yêu cầu
- JDK 17+ (khuyến nghị)
- Maven 3.8+

2) Cấu hình ứng dụng
- Sửa cấu hình trong `src/main/resources/application.yml` và/hoặc `application-local.yml` (database, security, secrets...)
- Bật profile phù hợp (ví dụ: `spring.profiles.active=local`)

3) Chạy dự án
- Dòng lệnh: `./mvnw spring-boot:run` (Linux/Mac) hoặc `mvnw.cmd spring-boot:run` (Windows)
- IDE: chạy class `org.example.base.Application`

4) Kiểm thử nhanh
- `./mvnw test` hoặc `mvnw.cmd test`

## Sử dụng dự án làm nền tảng cho dự án mới
### 1. Đổi thông tin nhóm/package
- Tìm và thay thế `org.example.base` bằng namespace của bạn (ví dụ `com.mycompany.project`)
- Cập nhật `pom.xml` (groupId, artifactId, name, description)

### 2. Cấu hình bảo mật và khởi tạo dữ liệu
- Mở `configurations/SecurityConfig.java` để điều chỉnh:
  - Quy tắc bảo vệ endpoint, CORS, session policy
  - Cơ chế xác thực (Bearer JWT/AccessToken)
- Kiểm tra `InitialDataLoader.java` để seed Role/Privilege/User mặc định theo nhu cầu

### 3. Tạo entity và repository mới
- Thêm entity trong `models/entity/<domain>/YourEntity.java`
- Tạo repository tương ứng trong `repositories/<domain>/YourEntityRepository.java`
  - Kế thừa `CustomJpaRepository` nếu cần mở rộng tiện ích

### 4. Tạo service nghiệp vụ
- Tạo service trong `services/<domain>/YourEntityService.java`
  - Có thể kế thừa/compose `CrudService` để có CRUD nhanh
  - Tận dụng `MemoryCacheService`/cache chuyên biệt nếu cần

### 5. Tạo endpoint REST
- Thêm controller trong `endpoints/<domain>/YourEntityEndpoint.java`
  - Dựa trên `CrudEndpoint` để có sẵn CRUD chuẩn
  - Bổ sung endpoint tùy chỉnh cho nghiệp vụ đặc thù

### 6. Tìm kiếm động với RSQL (tùy chọn)
- Ở endpoint hoặc service, nhận tham số RSQL (ví dụ `search`), chuyển vào `GenericRsqlSpecification`
- Áp dụng cho repository JPA để lọc/sắp xếp dữ liệu linh hoạt mà không phải viết nhiều query

### 7. Tích hợp xác thực/token
- Sử dụng `JwtTokenService`, `AccessTokenService` để phát hành/giải mã token
- Lọc request qua `JwtFilter` (đã gắn trong `SecurityConfig`)
- Quản lý phiên qua `SessionService` và `SessionEndpoint` nếu cần theo dõi phiên người dùng

### 8. Quản lý lỗi và mã lỗi
- Chuẩn hóa phản hồi lỗi bằng `models/error/ErrorInfo.java`
- Định nghĩa mã lỗi/bộ khóa trong `constants/ErrorKey.java` và `constants/Constant.java`

### 9. Tiện ích và converter
- Dùng `EncryptUtils` cho băm/mã hóa liên quan đến mật khẩu/token
- `StringUtils`/`CommonUtils` cho xử lý chuỗi và tiện ích chung
- Các converter giúp map list/set <-> string trong JPA khi cần lưu trữ đơn giản

## Ví dụ luồng cơ bản
- Đăng nhập/nhận token: sử dụng `AuthEndpoint` (ví dụ `POST /auth/token` với thông tin đăng nhập)
- Dùng token Bearer truy cập các endpoint đã bảo vệ
- Tìm kiếm user với RSQL: `GET /users?search=username==alice;enabled==true` (tùy thuộc mapping thực tế)
- Quản lý phiên: `SessionEndpoint` cho việc liệt kê/hủy phiên

Lưu ý: URL thực tế phụ thuộc vào `@RequestMapping` trong các Endpoint cụ thể của bạn.

## Nguyên tắc mở rộng nhanh
- Dùng các lớp mẫu (CrudService, CrudEndpoint) để tránh lặp lại CRUD
- Tách rõ domain (entity, repo, service, endpoint) theo module con
- Viết test sớm cho token/service quan trọng (xem `JwtTokenServiceTest`)
- Sử dụng RSQL khi muốn bộ lọc động thay vì viết nhiều phương thức repository

## Bảo mật và cấu hình production
- Sinh khóa/secret mạnh cho JWT/AccessToken, đặt trong biến môi trường hoặc vault
- Bật HTTPS và cấu hình CORS phù hợp
- Thiết lập TTL token, refresh token policy, lockout theo UserAttempt nếu cần
- Giới hạn thông tin lỗi trả về tránh lộ nội dung nhạy cảm

## Cấu hình hồ sơ (profiles)
- `application.yml`: cấu hình chung
- `application-local.yml`: dành cho môi trường local (DB, logging, security debug...)
- Sử dụng `-Dspring.profiles.active=<profile>` khi chạy Maven hoặc cấu hình trong IDE

## Giấy phép và ghi công
- Tùy chỉnh theo dự án của bạn. Nếu sử dụng lại, vui lòng giữ phần ghi công phù hợp trong README hoặc pom.xml.

---
Dùng dự án này như một nền tảng để tăng tốc độ phát triển, chuẩn hóa kiến trúc và bảo mật ngay từ đầu, đồng thời linh hoạt mở rộng theo yêu cầu nghiệp vụ.