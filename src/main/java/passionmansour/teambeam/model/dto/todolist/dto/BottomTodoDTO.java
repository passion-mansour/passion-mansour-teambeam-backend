package passionmansour.teambeam.model.dto.todolist.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import passionmansour.teambeam.model.entity.Member;

import java.util.Date;

@Data
public class BottomTodoDTO {
    private Long topTodoId;
    private Long middleTodoId;
    private Long bottomTodoId;
    private String title;
    private boolean status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;
    private String memo;
    private BottomMember member;

    @Data
    public class BottomMember{
        private Long memberId;
        private String memberName;
    }

    public void setBottomMember(Long memberId, String memberName){
        this.member= new BottomMember();
        this.member.setMemberId(memberId);
        this.member.setMemberName(memberName);
    }
}


