package ua.foxminded.university.util.mappers;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.foxminded.university.domain.Group;
import ua.foxminded.university.dto.group.GroupNestedDTO;
import ua.foxminded.university.dto.group.GroupResponseDTO;
import ua.foxminded.university.dto.group.GroupRequestDTO;
import ua.foxminded.university.service.interfaces.GroupService;
import ua.foxminded.university.service.interfaces.LectureService;
import ua.foxminded.university.service.interfaces.SubjectService;

import java.util.List;
import java.util.Objects;

@Component
public class GroupMapper {

    private final GroupService groupService;
    private final SubjectService subjectService;
    private final LectureService lectureService;

    @Autowired
    public GroupMapper(GroupService groupService,
                       SubjectService subjectService,
                       LectureService lectureService) {
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.lectureService = lectureService;
    }

    public Group convertToGroup(GroupRequestDTO groupDTO) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        PropertyMap<GroupRequestDTO, Group> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getDepartment().getName());
                skip(destination.getStudents());
            }
        };

        Group group = modelMapper.addMappings(propertyMap).map(groupDTO);
        group.setSubjects(group.getSubjects()
            .stream()
            .map(subject -> subjectService.find(subject.getId()))
            .toList());

        if (Objects.nonNull(group.getLectures())) {
            group.setLectures(group.getLectures()
                .stream()
                .map(lecture -> lectureService.find(lecture.getId()))
                .toList());
        }

        return group;
    }

    public GroupRequestDTO convertToGroupRequestDTO(Group group) {
        return new ModelMapper().map(group, GroupRequestDTO.class);
    }

    public GroupResponseDTO convertToGroupResponseDTO(Group group) {
        return new ModelMapper().map(group, GroupResponseDTO.class);
    }

    public GroupResponseDTO convertToGroupResponseDTOWhenFindAll(Group group) {
        PropertyMap<Group, GroupResponseDTO> propertyMap = new PropertyMap<>() {
            @Override
            protected void configure() {
                skip(destination.getStudents());
                skip(destination.getSubjects());
                skip(destination.getLectures());
            }
        };
        return new ModelMapper().addMappings(propertyMap).map(group);
    }

    public GroupNestedDTO convertToGroupNestedDTO(Group group) {
        return new ModelMapper().map(group, GroupNestedDTO.class);
    }

    public List<GroupNestedDTO> getGroupsDTO() {
        return groupService.findAll().stream()
            .map(this::convertToGroupNestedDTO)
            .toList();
    }
}
