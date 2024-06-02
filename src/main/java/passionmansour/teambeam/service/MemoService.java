package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.memo.request.PatchMemoRequest;
import passionmansour.teambeam.model.dto.memo.request.PostMemoRequest;
import passionmansour.teambeam.model.dto.memo.response.MemoListResponse;
import passionmansour.teambeam.model.dto.memo.response.MemoResponse;
import passionmansour.teambeam.model.entity.Memo;
import passionmansour.teambeam.repository.MemoRepository;
import passionmansour.teambeam.service.security.JwtTokenService;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemoService {
    private final JwtTokenService jwtTokenService;
    private final MemoRepository memoRepository;

    @Transactional
    public MemoResponse createMemo(String token, PostMemoRequest postMemoRequest){
        Memo memo = Memo.builder()
                .memoTile(postMemoRequest.getMemoTitle())
                .memoContent(postMemoRequest.getMemoContent())
                .createDate(LocalDateTime.now())
                .member(jwtTokenService.getMemberByToken(token))
                .build();

        return new MemoResponse().form(memoRepository.save(memo));
    }

    @Transactional
    public MemoResponse updateMemo(PatchMemoRequest patchMemoRequest){
        Memo memo = getById(patchMemoRequest.getMemoId());

        memo.setMemoTile(patchMemoRequest.getMemoTitle());
        memo.setMemoContent(patchMemoRequest.getMemoContent());
        memo.setUpdateDate(LocalDateTime.now());

        return new MemoResponse().form(memoRepository.save(memo));
    }

    @Transactional
    public void deleteMemo(Long memoId){
        Memo memo = getById(memoId);
        memoRepository.delete(memo);
    }

    @Transactional(readOnly = true)
    public Memo getById(Long memoId){
        Memo memo = memoRepository.findById(memoId)
                .orElseThrow(() -> new RuntimeException("Memo not found"));

        return memo;
    }

    @Transactional(readOnly = true)
    public MemoListResponse getAllByToken(String token){
        return new MemoListResponse().entityToForm(memoRepository.findAllByMemberId(jwtTokenService.getMemberByToken(token).getMemberId()));
    }
}
