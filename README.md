## 프로젝트 이름

개발 중 디버깅/실행시간 분석을 위한 로그 유틸리티

## 개요

**DevPulse**는 JVM 애플리케이션의 메서드 실행 시간과 자원 사용 정보를 추적하는  
**초경량 Java Agent 기반 APM 유틸리티**입니다.  
Java Instrumentation API와 ASM을 활용하여 메서드 단위로 바이트코드를 후킹하며,  외부 코드 수정 없이 사용 가능합니다.

## 저장소 주소

URL : https://github.com/ran2yo/devpulse1


## 체크리스트

## ✅ 현재까지 구현한 내용

- [x] `DevPulseAgent` 클래스에 `premain()` 정의하여 JVM 시작 시 Agent 등록
- [x] `ClassTransformer`를 통해 JVM 로딩 중인 클래스의 바이트코드 수정
- [x] ASM(`ClassReader`, `ClassWriter`, `MethodVisitor`) 기반 메서드에 시간 측정 코드 삽입
- [x] `MethodProfiler`를 통한 `System.nanoTime()` 기반 메서드 실행 시간 측정
- [x] 콘솔에 로그 출력: `[DevPulse] 메서드명 - 실행 시간: xx.xx ms`
- [x] `shadowJar` 플러그인으로 fat jar 빌드 구성 완료 (ASM 라이브러리 포함)
- [x] Agent jar (`*-agent.jar`)와 App jar (`*.jar`) 충돌 문제 해결
- [x] JVM 옵션으로 `-javaagent` 실행 성공 테스트 완료

---

## 🔜 해야 할 작업 (TODO)

### 🔹 호출 흐름 추적 기능
- [ ] `CallContext` 구현 (ThreadLocal 기반)
- [ ] 메서드 진입/종료 시 call stack push/pop
- [ ] 호출 흐름 트리 출력 가능하도록 구조화

### 🔹 메모리 사용량 추적
- [ ] `Runtime.getRuntime()` 기반 메모리 사용량 로그 출력
- [ ] `MethodProfiler.end()` 단계에서 메모리 측정 병합

### 🔹 로그 포맷 고도화
- [ ] 실행 로그를 JSON 형태로 출력
- [ ] 로그를 콘솔이 아닌 파일(`logs/devpulse.json`)로 저장
- [ ] 로그 항목에 스레드 ID, 클래스, 호출 깊이(call depth) 포함

### 🔹 후킹 대상 필터링
- [ ] 포함/제외 패키지 필터 기능 (`include`, `exclude` 지원)
- [ ] `-javaagent:xxx.jar=include=com.example.*` 형태의 agentArgs 파싱

### 🔹 테스트 환경 및 샘플 앱
- [ ] `testapp.SampleApp` 모듈 분리 또는 간이 프로젝트 구성
- [ ] 다양한 후킹 대상 클래스 테스트용 샘플 제공

### 🔹 프로젝트 구조 개선
- [ ] `agent`, `app` 멀티모듈 구성으로 분리
- [ ] 모듈 간 의존성 명확화 및 단위 테스트 기반 확장 준비

### 🔹 고급 기능 확장
- [ ] GC 이벤트 추적 (JFR, GC 로그 분석)
- [ ] 외부 트레이싱 시스템 연동 설계 (Jaeger, Zipkin 등)
- [ ] Spring, JDBC 등 프레임워크별 후킹 템플릿 설계

---

## 🚀 실행 예시

```bash
java -javaagent:build/libs/devpulse-1.0.0-agent.jar -jar build/libs/devpulse-app.jar
