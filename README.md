# spring-level3
회원가입, 로그인, 댓글 작성/조회/수정/삭제 기능이 추가된 나만의 블로그 백엔드 서버 만들기

## Convention
### code
- setter 사용을 제한한다.
- 변수명은 CamelCase로 한다.
- 변수명이나 메서드명에 줄임말을 사용하지 않는다.
- 한 줄에 점을 2~3개 이내로 찍는다.
- 메서드와 메서드 사이에 공백 라인을 한 라인 둔다.
- DTO는 협의 후에 추가 및 수정한다.

### commit message
[커밋 메세지 컨벤션](https://velog.io/@archivvonjang/Git-Commit-Message-Convention)을 참고하자

## ERD
<img width="737" alt="스크린샷 2023-07-04 오후 11 27 45" src="https://github.com/thing-zoo/spring-level3/assets/62596783/aca3d645-64ef-464c-b691-817f0da7f1f1">

## API 명세
[GitBook API 명세서](https://thing-zoo-world.gitbook.io/my-blog/)

## 코드리뷰
1. 처음 설계한 API 명세서에 변경사항이 있었나요? 
변경 되었다면 어떤 점 때문 일까요? 첫 설계의 중요성에 대해 작성해 주세요!
    
    ```
    /api/posts/{id}/comments/{id} -> /api/posts/{postId}/comments/{id}
    변수이름이 중복되어서 변경하였다.
    
    개발진행상황이 많아질수록 수정에 필요한 비용이 크게 증가하기 때문
    작은 변경점이 크게 구조를 바꾸는 경우가 생길 수 있다.
    ```
    
2. ERD를 먼저 설계한 후 Entity를 개발했을 때 어떤 점이 도움이 되셨나요?
    
    ```
    종속성 
    엔티티를 작성하기 쉽다.
    엔티티를 작성할때 누락이 발생하지 않는다.
    협업을 할 때 소통하기 편하다.
    ```
    
3. JWT를 사용하여 인증/인가를 구현 했을 때의 장점은 무엇일까요?
    
    ```
    서버 내에서 Security코드를 구현해 바로 해체하고 제작할 수 있었다는 장점이 있었습니다.
    ```
    
4. 반대로 JWT를 사용한 인증/인가의 한계점은 무엇일까요?
    
    ```
    해당 토큰을 다른 사람이 도용했을 때, 바로 허용된다는 단점이 있고,
    코드 구현하기 귀찮은 점도 있습니다.
    ```
    
5. 댓글이 달려있는 게시글을 삭제하려고 할 때 무슨 문제가 발생할까요? JPA가 아닌 Database 테이블 관점에서 해결방법이 무엇일까요?
    
    ```
    해당 내용을 관점으로 ERD내에서 종속성을 두었으며, 
    Entity내에서 ManyToOne과 OneToMany를 이용하여,
    해결하였습니다.
    ```
    
6. 5번과 같은 문제가 발생했을 때 JPA에서는 어떻게 해결할 수 있을까요?
    
    ```
    해당 내용을 관점으로 ERD내에서 종속성을 두었으며, 
    Entity내에서 ManyToOne과 OneToMany를 이용하여,
    해결하였습니다.
    ```
    
7. IoC / DI 에 대해 간략하게 설명해 주세요! 
    
    ```
    컨트롤러 객체마다 서비스와 레포를 생성하는 경우에는 결국 컨트롤러가 매번 새로 생길 때 마다 그 서비스와 레포가 새로 생성 되어 메모리적인 측면이나 해당 컨트롤러마다 새로운 서비스를 생성해줘야 하는 불편함이 있는데
    서비스와 레포를 외부에서 생성하여, 스스로 만들어지고 그 내용을 가져와서 사용하는 것이 메모리 적인 측면이나 객체 지향적 언어의 특징인 캡슐화가 잘 되어,
    특정 정보를 가져올 때 해당 레포를 이용한다던지 하는 의존성이 역전 되는 것을
    DI(각 객체당 의존성 주입)을 통한 IoC방식으로 코드를 짠다고 설명할 수 있습니다.
    ```
