package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passionmansour.teambeam.repository.PostRepository;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
}
