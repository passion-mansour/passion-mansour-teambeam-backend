package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passionmansour.teambeam.repository.BookmarkRepository;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
}
