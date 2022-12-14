# 아나바다

<img src="https://s3.us-west-2.amazonaws.com/secure.notion-static.com/f1b18afe-b4c6-47bb-806e-d2064cedca1c/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220915%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220915T094305Z&X-Amz-Expires=86400&X-Amz-Signature=0d9969b61e305356a536970fc77ea3a2f01b36a48fba703bf9a3d82639993f87&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject" ></img>

<br/>
<br/>
<br/>

## 📢프로젝트 소개

> 서핑에 관심이 있는데 막상 처음하시긴 두려우신가요?
>
> 아님 함께 서핑을 즐길 사람들을 찾고 싶으신가요?
>
> 혹시 자신만 아는 서핑 장소들을 사람들과 공유하고 싶은 생각이
> 있으신가요?
>
> 그런 분들을 위한 모두의 서핑 커뮤니티 **<span style="background-color:rgba(0,0,0,0.2);padding:0.2rem;font-size:1rem;border-radius:5px">아나바다</span>** 입니다!
>
> 아나바다를 통해 함께 서핑을 즐길 친구들을 찾아보세요!
>
> 아나바다와 함께라면, 많은 서핑 🍯팁을 공유할 수도 있고,
>
> 전국 서핑명소들을 쉽게 찾을수도 있어요!!
> 이제 서핑 혼자할까봐 걱정하지 말아요!!

[프로젝트 링크](https://ohanabada.com)

<br/>
<br/>
<br/>

## 🎬시연 영상

[유튜브 시연 링크]()

<br/>
<br/>
<br/>

## 📅프로젝트 기간

2022년 8월 5일 ~ 2022년 9월 15일

<br/>
<br/>
<br/>

## **🔨사용 기술 및 라이브러리**
<br/>
<br/>
<br/>
<br/>

## **👨‍👩‍👧‍👦팀원**

### 김송이

|     기능     |                        비고                        |
| :----------: | :------------------------------------------------: |


### 방기웅

|        기능        |                                      비고                                       |
| :----------------: | :-----------------------------------------------------------------------------: |


### 사상원

|      기능      |                       비고                        |
| :------------: | :-----------------------------------------------: |


<br/>

<br/>
<br/>
<br/>

## 🎨 와이어프레임

![Untitled](https://s3.us-west-2.amazonaws.com/secure.notion-static.com/31d47c25-37fa-48da-85fa-1510ea38f352/Untitled.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Content-Sha256=UNSIGNED-PAYLOAD&X-Amz-Credential=AKIAT73L2G45EIPT3X45%2F20220915%2Fus-west-2%2Fs3%2Faws4_request&X-Amz-Date=20220915T094510Z&X-Amz-Expires=86400&X-Amz-Signature=2149927d2cad96b172e34d4f55f0ef5b4e50b280648e9feae4a2f0f139719e8d&X-Amz-SignedHeaders=host&response-content-disposition=filename%20%3D%22Untitled.png%22&x-id=GetObject)

<br/>
<br/>
<br/>

## 💡핵심 기능

### 🗺️ **서핑 스팟**

- 카카오맵을 통한 해수욕장 날씨 정보를 확인할 수 있어요!
- 검색을 통해 서핑이 가능한 해수욕장을 쉽고 빠르게 찾아볼 수 있어요!

### 📝 **포스팅**

- 게시물을 통해 이미지를 올리고 서핑 관련 정보를 소개할 수 있어요 !
- 좋아요 기능을 통해 게시물을 모아볼 수 있어요!

### 🧑‍🤝‍🧑 **오픈 모임**

- 오픈 모임 탭을 이용해 모임을 개설하고 참여할 수 있어요!
- 특정날짜에 모임을 시행할 날짜를 지정할 수 있어요!
- 모임에 참여할 인원을 설정할 수 있고, 모임을 모집하는 기간 또한 지정할 수 있어요!
- 모임에 참여한 목록과 인원수를 확인할 수 있어요!
- 조회수를 기준으로 선정된 인기 모임을 모아서 확인할 수 있어요!

### 💬 **일대일 채팅**

- 상대방의 닉네임을 클릭 해 바로 1:1 채팅을 보낼 수 있어요!
- 채팅방에 입장 시 실시간으로 상대방과 대화를 주고받을 수 있어요!

### 🔔 **실시간 알림**

- 내가 쓴 글에 좋아요와 댓글이 추가 될 경우 실시간 알림으로 확인할 수 있어요!
- 읽은 알림과 안 읽은 알림을 구분할 수 있어요!

<br/>
<br/>
<br/>

## 💫Trouble Shooting

### 기상청 Open API 로딩 방식


### ❓문제 상황

클라이언트의 요청마다 기상청 Open API 요청을 통해 해수욕장의 날씨정보를 받아오는 방식을 사용시 요청→응답까지 5초~10초 가량 걸리는 현상이 발생함.

### ✏️문제 해결

-@Scheduled 어노테이션을 이용해서 서버 실행 시 날씨 정보를 데이터베이스에 저장하고 클라이언트에는 DB에 저장된 값을 응답해주는 방식으로 변경함.
-클라이언트에서 해변 정보 요청시 응답 시간이 5~20ms 정도로 개선됨.

---

## **기타**

[노션링크](https://www.notion.so/503e00648f9d4e148496fb244b05be26)
