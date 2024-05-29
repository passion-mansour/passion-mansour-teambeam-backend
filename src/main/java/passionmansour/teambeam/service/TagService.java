package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.Tag.request.PostTagRequest;
import passionmansour.teambeam.model.dto.Tag.response.TagListResponse;
import passionmansour.teambeam.model.dto.Tag.response.TagResponse;
import passionmansour.teambeam.model.entity.*;
import passionmansour.teambeam.model.enums.TagCategory;
import passionmansour.teambeam.repository.*;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository tagRepository;
    private final ProjectRepository projectRepository;
    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final ScheduleRepository scheduleRepository;
    private final ScheduleTagRepository scheduleTagRepository;
    private final BottomTodoRepository bottomTodoRepository;
    private final TodoTagRepository todoTagRepository;

    @Transactional
    public TagResponse createTags(PostTagRequest postTagRequest){
        Optional<Project> project = projectRepository.findById(postTagRequest.getProjectId());
        if(project.isEmpty()){
            //TODO: 예외처리
        }

        Tag tag = Tag.builder()
                .tagName(postTagRequest.getTagName())
                .tagCategory(postTagRequest.getTagCategory())
                .project(project.get())
                .build();

        return new TagResponse().form(tagRepository.save(tag));
    }

    @Transactional
    public PostTag addPostTag(Long tagId, Long postId){
        PostTag postTag = PostTag.builder()
                .post(postRepository.findById(postId).get())
                .tag(tagRepository.findById(tagId).get())
                .build();

        return postTagRepository.save(postTag);
    }

    @Transactional
    public ScheduleTag addScheduleTag(Long tagId, Long scheduleId){
        ScheduleTag scheduleTag = ScheduleTag.builder()
                .schedule(scheduleRepository.findById(scheduleId).get())
                .tag(tagRepository.findById(tagId).get())
                .build();

        return scheduleTagRepository.save(scheduleTag);
    }

    @Transactional
    public TodoTag addTodoTag(Long tagId, Long todoId){
        TodoTag todoTag = TodoTag.builder()
                .todo(bottomTodoRepository.findById(todoId).get())
                .tag(tagRepository.findById(tagId).get())
                .build();

        return todoTagRepository.save(todoTag);
    }

    @Transactional
    public void deleteTag(Long tagId) {

    }

    @Transactional(readOnly = true)
    public Tag getById(Long tagId){
        Optional<Tag> tag = tagRepository.findById(tagId);
        if(tag.isEmpty()){
            // TODO: 예외처리
        }

        return tag.get();
    }

    @Transactional(readOnly = true)
    public TagListResponse getAllByTagCategory(Long projectId, TagCategory tagCategory){
        return new TagListResponse().entityToForm(tagRepository.findByTagCategory(projectId, tagCategory));
    }

    @Transactional(readOnly = true)
    public TagListResponse getAllByProjectId(Long projectId){
        return new TagListResponse().entityToForm(tagRepository.findByProject_ProjectId(projectId));
    }
}
