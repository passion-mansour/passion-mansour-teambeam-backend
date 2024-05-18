package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passionmansour.teambeam.repository.BoardRepository;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;

    public void createBoard(){

    }
}
