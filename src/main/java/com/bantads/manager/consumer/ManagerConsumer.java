package com.bantads.manager.consumer;

import com.bantads.manager.config.ManagerConfiguration;
import com.bantads.manager.model.ManagerModel;
import com.bantads.manager.repository.ManagerRepository;
import lombok.Data;
import org.springframework.amqp.core.Message;
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
    public void createManager(ManagerModel managerModel) {
       this.managerRepository.save(managerModel);
       rabbitTemplate.convertAndSend(ManagerConfiguration.sortRequestQueueName, 1);
    }

    @RabbitListener(queues = ManagerConfiguration.updateQueueName)
    public void updateManager(String id, @RequestBody ManagerModel managerModel) {
        ManagerModel manager = this.managerRepository.findById(id).orElseThrow();
        manager.setName(managerModel.getName());
        manager.setCpf(managerModel.getCpf());
        manager.setTelephone(managerModel.getTelephone());
    }

    @RabbitListener(queues = ManagerConfiguration.deleteQueueName)
    public void deleteManager(String id) {
        this.managerRepository.deleteById(id);
        rabbitTemplate.convertAndSend(ManagerConfiguration.sortRequestQueueName, 1);
    }

    @RabbitListener(queues = ManagerConfiguration.sortRequestQueueName)
    public void sortManager(Object obj, Message message) {
        List<ManagerModel> managerModel = this.managerRepository.findAll();
        System.out.println(managerModel);
        if (managerModel.isEmpty()) {
            this.rabbitTemplate.convertAndSend(ManagerConfiguration.delayedSortRequestQueueName, 1);
             return;
        }
        this.rabbitTemplate.convertAndSend(ManagerConfiguration.sortResponseQueueName, managerModel);
    }

    @RabbitListener(queues = ManagerConfiguration.patchQueueName)
    public void patchManager(ManagerModel managerModel) {
        ManagerModel manager = this.managerRepository.findById(managerModel.getUuid()).orElse(null);
        if (manager == null) {
            return;
        }
        if (managerModel.getName() != null) {
            manager.setName(managerModel.getName());
        }
        if (managerModel.getCpf() != null) {
            manager.setCpf(managerModel.getCpf());
        }
        if (managerModel.getTelephone() != null) {
            manager.setTelephone(managerModel.getTelephone());
        }
        this.managerRepository.save(manager);
    }

}

