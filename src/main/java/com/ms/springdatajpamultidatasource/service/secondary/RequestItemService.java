package com.ms.springdatajpamultidatasource.service.secondary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ms.springdatajpamultidatasource.bean.secondary.RequestItem;
import com.ms.springdatajpamultidatasource.repository.secondary.RequestItemRepository;

@Service
@Transactional("transactionManagerSecondary")
public class RequestItemService {

    @Autowired
    private RequestItemRepository requestItemRepository;

    public RequestItem save(RequestItem requestItem) {
        return requestItemRepository.save(requestItem);
    }

    public void delete(Long id) {
        requestItemRepository.delete(id);
    }

    public List<RequestItem> findAll() {
        return requestItemRepository.findAll();
    }
}
