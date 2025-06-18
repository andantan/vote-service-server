package org.zerock.voteservice.controller.query.docs.ballotQueryApiResponses;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.zerock.voteservice.dto.query.BallotQueryResponseDto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "200",
                description = """
                            투표 기록 조회 성공 또는 기록 없음
                             - submitted_at: Date 형식에 따름
                            """,
                content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = BallotQueryResponseDto.class),
                        examples = {
                                @ExampleObject(
                                        name = "성공 응답 예시 (기록 있음)",
                                        summary = "사용자 투표 기록 조회 성공 1",
                                        value = """
                                            {
                                              "success": true,
                                              "message": "조회가 완료되었습니다.",
                                              "status": "OK",
                                              "http_status_code": 200,
                                              "ballots": [
                                                 {
                                                   "voteHash": "eb2c748fea3a68d724ae50f8a98528fd9ff5d1fa6cc686bd2fac8e9b64d9ef35",
                                                   "topic": "친환경 에너지 전환 정책 평가",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:03:28.614Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "703d3f6d269d862a3befc118d6f25a738b593074f621160da91bff8f111ff2f3",
                                                   "topic": "여성 경력 단절 해소 방안",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:08:21.315Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "c2ce4733e1d6f39b9296255e6134eb1a9e9683cde715e4202d5302dd72a92856",
                                                   "topic": "인터넷 실명제 도입 필요성",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:10:51.969Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "7fc98345aade5f0f9bb9828d1f86d71db2e35ed9f52d2a8d894b1293ad22fadf",
                                                   "topic": "초등학생 코딩 교육 의무화",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:10:57.911Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "cfeb60992a303782296088d24515c979d13fbf90a768489151ad75d29873de10",
                                                   "topic": "국가 채무 증가에 대한 우려",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:26:50.959Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "dc77bb8f7ea301f4f2b1e99203336f2267453d3372f3e0abf9b08bc2ccc9436c",
                                                   "topic": "양극화 해소를 위한 정부 역할",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:37:50.582Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "7a5ac0c4bd14744224b0bb3a1dbdc94b1fd93224067e334a2dd4728992295b44",
                                                   "topic": "도시 숲 조성 확대 필요성",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:38:48.667Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "393076c2a6e90b107dc7a25caa928f1af23b79da6dfa63680cf85fd1bcf519f5",
                                                   "topic": "사형제도 존폐 여부 논의",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:39:34.572Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "130b5aac8e2f22ffb9f3182bf6cdbcd85aff93691d8b76198849971364dab4cf",
                                                   "topic": "빈집 활용 도시 재생 사업",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:39:58.519Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "739ffd3fc0628f0537d15509bc1c5c837f1861d33268f7f388b1e5e1400ac1a7",
                                                   "topic": "청년들의 정치 참여율 증진",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:40:16.602Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "5e3b7b1f9cfb620019959a085a9aef17d8f1095031f8759848e7bc79cefa6793",
                                                   "topic": "재택근무 확대에 따른 법적 이슈",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:42:18.459Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "21df6f789d5aec512cf5c3760a13e69d031bc5a636061090693ab2f552e2519f",
                                                   "topic": "공공 부문 인공지능 도입 확대",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:43:00.123Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "b5b417605b8704bd74cbdc4d83c01d8bd0f0ee852990e1973da5ff08b47a9ea3",
                                                   "topic": "사회적 약자 보호를 위한 법적 장치",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:48:03.969Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "c0a7e8a0fe4258c308ec66c07985851a17e02c42a004c72e1a3f69bc56a4bfd0",
                                                   "topic": "기후 난민 발생에 대한 국가적 대응",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T08:55:55.205Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "16a4c43eaff262e42f7423d4bf405e1d392a1c64c4dc7a51cfcb6eb82022bfb1",
                                                   "topic": "온라인 플랫폼 규제 방안",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T09:05:53.986Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "38d667fea8a96c4556d71832f1c72bb03ff31ec93c3bafdca8afc4da34799cbb",
                                                   "topic": "역사 교육 과정 개편 방향",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T09:13:46.224Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "0cb6ac9999e6e04ed4726a058186431465d0dc11eb58529df0c2b9a4e3d50165",
                                                   "topic": "자율형 사립고 존폐 여부 찬반",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T09:16:51.909Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "e46f26da1d2d41d4c4a03d76b875da701625d7a7f65c0a0886f2f8a9eef9fbfa",
                                                   "topic": "범죄 예방을 위한 CCTV 확대",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T09:25:10.115Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "272688343c8fc9305548e0a09caf2c718e91bbed84ee3b505209748f99a40b4b",
                                                   "topic": "초고속 인터넷망 안정성 강화",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T09:31:26.371Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "7f8b823fe9ae4bafede778d80ecd7c0bb28e50dcc6fbead88df97ddd19e47023",
                                                   "topic": "수능 제도 개편 방향 논의",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T09:31:59.724Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "66bcbbd936fb803d386697c5f96d2e3464b815ba2d3e1dc6b7e2e938563efa5f",
                                                   "topic": "반려동물 등록제 의무화 논의",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T12:03:09.502Z"
                                                   }
                                                 },
                                                 {
                                                   "voteHash": "f14545af6d8b7e397b4daff9e648d44259c392566a883f13a7dbababe6a3b366",
                                                   "topic": "미래 산업 기술 선점을 위한 투자",
                                                   "submittedAt": {
                                                     "$date": "2025-06-14T12:08:35.801Z"
                                                   }
                                                 }
                                               ],
                                              "ballot_length": 22
                                            }"""
                                ),
                                @ExampleObject(
                                        name = "성공 응답 예시 (기록 없음)",
                                        summary = "사용자 투표 기록 조회 성공 2",
                                        value = """
                                            {
                                              "success": true,
                                              "message": "조회가 완료되었습니다.",
                                              "status": "OK",
                                              "http_status_code": 200,
                                              "ballots": [],
                                              "ballot_length": 0
                                            }"""
                                )
                        }
                )
        )
})
public @interface QueryBallotOkApiResponses {
}
