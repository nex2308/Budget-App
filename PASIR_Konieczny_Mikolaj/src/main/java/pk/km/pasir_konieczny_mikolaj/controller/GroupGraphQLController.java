package pk.km.pasir_konieczny_mikolaj.controller;

import jakarta.validation.Valid;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import pk.km.pasir_konieczny_mikolaj.dto.GroupDto;
import pk.km.pasir_konieczny_mikolaj.model.Group;
import pk.km.pasir_konieczny_mikolaj.service.GroupService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Controller
public class GroupGraphQLController {

    private final GroupService groupService;

    public GroupGraphQLController(GroupService groupService) {
        this.groupService = groupService;
    }

    @QueryMapping
    public List<Group> groups() throws AccessDeniedException { return groupService.getAllGroups(); }

    @MutationMapping
    public Group createGroup(@Valid @Argument GroupDto groupDTO) throws AccessDeniedException {
        return groupService.createGroup(groupDTO);
    }

    @MutationMapping
    public Boolean deleteGroup(@Argument Long id) throws AccessDeniedException {
        groupService.deleteGroup(id);
        return true;
    }
}
