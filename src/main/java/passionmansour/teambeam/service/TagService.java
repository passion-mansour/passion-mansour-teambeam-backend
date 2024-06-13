package passionmansour.teambeam.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import passionmansour.teambeam.model.dto.tag.request.PostTagRequest;
import passionmansour.teambeam.model.dto.tag.response.TagListResponse;
import passionmansour.teambeam.model.dto.tag.response.TagResponse;
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
    public void deletePostTag(PostTag postTag){
        postTagRepository.delete(postTag);
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
    public void deleteScheduleTag(ScheduleTag scheduleTag){
        scheduleTagRepository.delete(scheduleTag);
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
    public void deleteTodoTag(TodoTag todoTag){
        todoTagRepository.delete(todoTag);
    }

    @Transactional
    public void deleteTag(Long tagId) {
        Tag tag = getById(tagId);
        tagRepository.delete(tag);
    }

    @Transactional(readOnly = true)
    public Tag getById(Long tagId){
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        return tag;
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
