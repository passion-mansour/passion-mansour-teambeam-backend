package passionmansour.teambeam.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import passionmansour.teambeam.model.dto.board.request.PostBoardRequest;
import passionmansour.teambeam.model.dto.board.request.PostPostCommentRequest;
import passionmansour.teambeam.model.dto.board.request.PostPostRequest;
import passionmansour.teambeam.model.dto.board.response.BoardResponse;
import passionmansour.teambeam.model.dto.board.response.PostCommentResponse;
import passionmansour.teambeam.model.dto.board.response.PostResponse;
import passionmansour.teambeam.model.dto.member.request.RegisterRequest;
import passionmansour.teambeam.model.dto.project.ProjectDto;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.model.enums.PostType;
import passionmansour.teambeam.model.enums.ProjectStatus;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TotalServiceTest {
    @Mock
    MemberService memberService;
    @Mock
    ProjectService projectService;
    @Mock
    BoardService boardService;
    @Mock
    PostService postService;
    @Mock
    PostCommentService postCommentService;

    @BeforeEach
    void setup() {
        RegisterRequest member = new RegisterRequest();
        member.setMemberName("홍길동");
        member.setPassword("1234");
        member.setMail("1234@gmail.com");
        member.setToken("qwer@!!123");

        ProjectDto project = new ProjectDto();
        project.setProjectId(1L);
        project.setProjectName("project1");
        project.setProjectStatus(ProjectStatus.PROGRESS);

        memberService.saveMember(member);
        projectService.createProject("qwer@!!123", project);
    }

    @Test
    @DisplayName("게시판 생성 테스트")
    public void createBoard(){
        // given
        PostBoardRequest board1 = new PostBoardRequest();
        board1.setName("board1");

        // when
        BoardResponse save = boardService.createBoard(board1);
        if (save != null) {
            System.out.println(save.getBoardId());

            // then
            assertThat(boardService.getBoardById(save.getBoardId()).getName()).isEqualTo(board1.getName());
        } else {
            System.out.println("createBoard 메소드가 null을 반환했습니다.");
        }
    }


    @Test
    @DisplayName("게시물 생성 테스트")
    public void createPost(){
        // given
        PostPostRequest post1 = new PostPostRequest();
        post1.setTitle("제목");
        post1.setContent("내용");
        post1.setPostType(PostType.text);
        post1.setBoardId(1L);

        // when
        PostResponse save = postService.createPost("qwer@!!123", post1);

        if (save != null) {
            System.out.println(save.getPostId());

            // then
            assertThat(postService.getById(save.getPostId()).getPostTitle()).isEqualTo(post1.getTitle());
        } else {
            System.out.println("createPost 메소드가 null을 반환했습니다.");
        }
    }

    @Test
    @DisplayName("게시물 댓글 생성 테스트")
    public void createPostComment(){
        // given
        PostPostCommentRequest postComment1 = new PostPostCommentRequest();
        postComment1.setPostId(1L);
        postComment1.setContent("댓글");

        // when
        PostCommentResponse save = postCommentService.createPostComment("qwer@!!123", postComment1);

        if (save != null) {
            System.out.println(save.getPostCommentId());

            // then
            assertThat(postCommentService.getById(save.getPostCommentId()).getPostCommentContent()).isEqualTo(postComment1.getContent());
        } else {
            System.out.println("createPostComment 메소드가 null을 반환했습니다.");
        }
    }
}