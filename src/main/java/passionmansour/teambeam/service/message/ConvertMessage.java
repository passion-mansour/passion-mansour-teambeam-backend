package passionmansour.teambeam.service.message;

import org.springframework.stereotype.Service;
import passionmansour.teambeam.model.dto.message.MessageCommentDTO;
import passionmansour.teambeam.model.dto.message.MessageDTO;
import passionmansour.teambeam.model.dto.message.MessageMemberDTO;
import passionmansour.teambeam.model.entity.Member;
import passionmansour.teambeam.model.entity.Message;
import passionmansour.teambeam.model.entity.MessageComment;
import passionmansour.teambeam.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ConvertMessage {
    private final MemberRepository memberRepository;

    public ConvertMessage(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MessageDTO convertToMessageDto(Message message) {
        List<MessageCommentDTO> comments = new ArrayList<>();

        if(message.getMessageComments() != null){
            comments = message.getMessageComments().stream()
                    .map(this::convertToMessageCommentDto)
                    .collect(Collectors.toList());
        }


        return new MessageDTO(message.getMessageId(),
                message.getMessageContent(),
                message.getCreateDate(),
                message.getUpdateDate(),
                new MessageMemberDTO(message.getMember().getMemberId(),
                        message.getMember().getMemberName(),
                        "temp"),
                message.getProject().getProjectId(),
                comments.size(),
                comments);
    }

    public MessageCommentDTO convertToMessageCommentDto(MessageComment comment) {
        Member member = memberRepository.findById(comment.getMember().getMemberId()).orElseThrow(()
                ->new RuntimeException("Member not found"));

        return new MessageCommentDTO(
                comment.getMessageCommentId(),
                comment.getMessageCommentContent(),
                comment.getCreateDate(),
                comment.getUpdateDate(),
                new MessageMemberDTO(member.getMemberId(), member.getMemberName(), member.getProfileImage())
        );
    }
}
