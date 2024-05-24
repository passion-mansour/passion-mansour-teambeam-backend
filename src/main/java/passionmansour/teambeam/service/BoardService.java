package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.board.request.PostBoardRequest;
import passionmansour.teambeam.model.dto.board.response.BoardListResponse;
import passionmansour.teambeam.model.dto.board.response.BoardResponse;
import passionmansour.teambeam.model.entity.Board;
import passionmansour.teambeam.model.entity.Project;
import passionmansour.teambeam.repository.BoardRepository;
import passionmansour.teambeam.repository.ProjectRepository;

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
        Optional<Project> project = projectRepository.findById(postBoardRequest.getProjectId());
        if(project.isEmpty()){
            //TODO: 예외처리
        }

        Board board = Board.builder()
                .boardName(postBoardRequest.getName())
                .project(project.get())
                .build();

        log.info(board.toString());

        return new BoardResponse().form(board);
    }

    @Transactional(readOnly = true)
    public BoardResponse getBoardById(Long boardId){
        Optional<Board> board = boardRepository.findById(boardId);
        if(board.isEmpty()){
            //TODO: 예외처리
        }

        return new BoardResponse().form(board.get());
    }

    @Transactional(readOnly = true)
    public BoardListResponse getAllBoardByProjectId(Long projectId){
        return boardRepository.getAllBoardByProjectId(projectId);
    }
}
