package com.bantads.manager.consumer;

import com.bantads.manager.config.ManagerConfiguration;
import com.bantads.manager.model.ManagerModel;
import com.bantads.manager.repository.ManagerRepository;
import lombok.Data;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@Service
public class ManagerConsumer {

    private final ManagerRepository managerRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = ManagerConfiguration.createQueueName)
    public void createManager(@RequestBody ManagerModel managerModel) {
       this.managerRepository.save(managerModel);
    }

    @RabbitListener(queues = ManagerConfiguration.updateQueueName)
    public void updateManager(@PathVariable String id, @RequestBody ManagerModel managerModel) {
        ManagerModel manager = this.managerRepository.findById(id).orElseThrow();
        manager.setName(managerModel.getName());
        manager.setCpf(managerModel.getCpf());
        manager.setTelephone(managerModel.getTelephone());
    }

    @RabbitListener(queues = ManagerConfiguration.deleteQueueName)
    public void deleteManager(@PathVariable String id) {
        this.managerRepository.deleteById(id);
    }

    @RabbitListener(queues = ManagerConfiguration.sortRequestQueueName)
    public void sortManager() {
        List<ManagerModel> managerModel = this.managerRepository.findAll();
        this.rabbitTemplate.convertAndSend(ManagerConfiguration.sortResponseQueueName, managerModel);
    }
}
