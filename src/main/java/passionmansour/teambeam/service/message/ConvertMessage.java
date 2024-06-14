package passionmansour.teambeam.service.message;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import passionmansour.teambeam.model.dto.message.MessageCommentDTO;
import passionmansour.teambeam.model.dto.message.MessageDTO;
import passionmansour.teambeam.model.dto.message.MessageMemberDTO;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Message;
import passionmansour.teambeam.model.entity.MessageComment;
import passionmansour.teambeam.repository.MemberRepository;
import passionmansour.teambeam.service.MemberService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConvertMessage {
    private final MemberRepository memberRepository;
    private final MemberService memberService;

    public MessageDTO convertToMessageDto(Message message) {
        List<MessageCommentDTO> comments = new ArrayList<>();

        if(message.getMessageComments() != null){
            comments = message.getMessageComments().stream()
                    .map(this::convertToMessageCommentDto)
                    .collect(Collectors.toList());

            comments.sort(Comparator.comparing(MessageCommentDTO::getMessageCommentId));
        }

        return new MessageDTO(message.getMessageId(),
                message.getMessageContent(),
                message.getCreateDate(),
                message.getUpdateDate(),
                new MessageMemberDTO(message.getMember().getMemberId(),
                        message.getMember().getMemberName(),
                        memberService.getImageAsBase64(message.getMember().getProfileImage())),
                message.getProject().getProjectId(),
                comments.size(),
                comments);
    }

    public MessageCommentDTO convertToMessageCommentDto(MessageComment comment) {
        Member member = memberRepository.findById(comment.getMember().getMemberId()).orElseThrow(()
                ->new RuntimeException("Member not found"));

        return new MessageCommentDTO(
                comment.getMessageCommentId(),
                comment.getMessage().getMessageId(),
                comment.getMessageCommentContent(),
                comment.getCreateDate(),
                comment.getUpdateDate(),
                new MessageMemberDTO(member.getMemberId(), member.getMemberName(), memberService.getImageAsBase64(member.getProfileImage()))
        );
    }
}
