package passionmansour.teambeam.model.dto.calendar.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;

@Data
public class PostScheduleRequest {
  private String string;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date startDate;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  private Date endDate;
  private String location;
  private String content;
  private LocalTime time;
  private String url;
}
