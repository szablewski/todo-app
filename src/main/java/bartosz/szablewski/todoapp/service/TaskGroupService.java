package bartosz.szablewski.todoapp.service;

import bartosz.szablewski.todoapp.model.TaskGroups;
import bartosz.szablewski.todoapp.model.dto.GroupReadDTO;
import bartosz.szablewski.todoapp.model.dto.GroupWriteDTO;
import bartosz.szablewski.todoapp.repository.TaskGroupsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskGroupService {

    private TaskGroupsRepository repository;

    TaskGroupService(final TaskGroupsRepository repository){
        this.repository = repository;
    }

    public GroupReadDTO createGroups(GroupWriteDTO dto){
        TaskGroups result = repository.save(dto.toGroup());
        return new GroupReadDTO(result);
    }

    public List<GroupReadDTO> readAll(){
        return repository.findAll().stream()
                .map(GroupReadDTO::new)
                .collect(Collectors.toList());
    }
}
