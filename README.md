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

### 🔧 기능 구현
- [x] `DevPulseAgent`의 `premain()`으로 agent 진입점 설정
- [x] `ClassTransformer`로 클래스 로딩 시 메서드 바이트코드 변형
- [x] ASM 기반 `AdviceAdapter`로 메서드 진입/종료 타이밍 삽입
- [x] `System.nanoTime()`으로 실행 시간 측정 (`MethodProfiler`)
- [x] 메서드 호출 추적 스택(`CallContext`) 구현
- [x] 로그 엔트리(`LogEntry`) JSON 객체 구조로 구성
- [x] 로그 저장 유틸리티(`LogWriter`)로 JSON 파일 저장
- [x] `include`, `exclude` 패키지 필터링 기능 구현
- [x] `-javaagent:...=logPath=...,include=...,exclude=...` 파라미터 파싱 완료

### ⚙️ 빌드 및 테스트
- [x] Spring Boot 모듈과 Agent 모듈 분리 완료
- [x] `shadowJar`로 fat jar 빌드 구성
- [x] Agent jar와 App jar의 충돌 문제 해결
- [x] 샘플 앱으로 로컬 실행 및 로그 출력 테스트 완료

---

## 🔜 해야 할 작업 (TODO)

### 📄 로그 기능 고도화
- [ ] 로그 파일 날짜별 자동 분리 (예: `devpulse-20250419.json`)
- [ ] 로그 포맷 선택 기능 (TEXT vs JSON)
- [ ] 로그 레벨 필드(`INFO`, `DEBUG` 등) 추가
- [ ] `logs/` 폴더 유무에 따라 자동 생성 처리
- [ ] 비동기 로깅 처리 (BlockingQueue + 쓰레드)

### 📦 설정 및 확장성 개선
- [ ] YML 기반 config 파일 로딩(`devpulse.yml`)
- [ ] agentArgs 없이도 설정 가능한 구조 구성
- [ ] 메서드 이름/어노테이션 기반 필터링 추가
- [ ] 로그에 환경 정보(OS, JVM 등) 추가

### 📊 분석/시각화
- [ ] Flame Graph 로그 포맷 변환기 제공
- [ ] 로그를 Loki, Elasticsearch로 전송 기능
- [ ] 호출 통계 요약 자동화 (`Top 10 느린 메서드` 등)

### 🧪 테스트 및 구조 개선
- [ ] `testapp` 샘플 프로젝트 정리
- [ ] agent/app 멀티 모듈 정리 및 문서화
- [ ] 단위 테스트 기반 설계로 리팩토링

---


## 🚀 실행 예시

```bash
java -javaagent:build/libs/devpulse-1.0.0-agent.jar -jar build/libs/devpulse-app.jar
