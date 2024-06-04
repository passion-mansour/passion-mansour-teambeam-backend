package passionmansour.teambeam.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.request.PostBoardRequest;
import passionmansour.teambeam.model.dto.board.response.BoardListResponse;
import passionmansour.teambeam.model.dto.board.response.BoardResponse;
import passionmansour.teambeam.model.entity.Board;
import passionmansour.teambeam.model.entity.Post;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.repository.BoardRepository;
import passionmansour.teambeam.repository.ProjectRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {
    private final ProjectRepository projectRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public BoardResponse createBoard(PostBoardRequest postBoardRequest){
        Project project = projectRepository.findByProjectId(postBoardRequest.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        Board board = Board.builder()
                .boardName(postBoardRequest.getName())
                .project(project)
                .build();

        return new BoardResponse().form(boardRepository.save(board));
    }

    @Transactional
    public void deleteBoard(Long boardId){
        Board board = getBoardById(boardId);
        boardRepository.delete(board);
    }

    @Transactional(readOnly = true)
    public Board getBoardById(Long boardId){
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new RuntimeException("Board not found"));

        return board;
    }

    @Transactional(readOnly = true)
    public BoardListResponse getAllBoardByProjectId(Long projectId){
        List<BoardResponse> boardResponses = new ArrayList<>();

        for (Board board : boardRepository.getAllBoardByProjectId(projectId)){
            boardResponses.add(new BoardResponse().form(board));
        }

        return new BoardListResponse().form(boardResponses);
    }
}