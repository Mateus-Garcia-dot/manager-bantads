package com.bantads.manager.controller;

import com.bantads.manager.model.ManagerModel;
import com.bantads.manager.repository.ManagerRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController()
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerRepository managerRepository;

    @GetMapping
    public ResponseEntity<List<ManagerModel>> getAllmanagers() {
        List<ManagerModel> managerModelList = this.managerRepository.findAll();
        managerModelList.sort((a, b) -> a.getId().compareTo(b.getId()));
        return ResponseEntity.ok(managerModelList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ManagerModel> getManagerById(@PathVariable Long id) {
        ManagerModel managerModel = this.managerRepository.findById(id).orElseThrow();
        return ResponseEntity.ok(managerModel);
    }

    @PostMapping
    public ResponseEntity<ManagerModel> createManager(@RequestBody ManagerModel managerModel) {
        ManagerModel addedManagerModel = this.managerRepository.save(managerModel);
        return ResponseEntity.ok(addedManagerModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManagerModel> updateManager(@PathVariable Long id, @RequestBody ManagerModel managerModel) {
        ManagerModel manager = this.managerRepository.findById(id).orElseThrow();
        manager.setName(managerModel.getName());
        manager.setCpf(managerModel.getCpf());
        manager.setTelephone(managerModel.getTelephone());
        return ResponseEntity.ok(this.managerRepository.save(manager));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteManager(@PathVariable Long id) {
        this.managerRepository.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }

}
