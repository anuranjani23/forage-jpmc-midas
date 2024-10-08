package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Balance;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balance")
public class BalanceController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Balance getBalance(@RequestParam long userId) {
        UserRecord userRecord = userRepository.findById(userId);

        if (userRecord == null) return new Balance(0);

        return new Balance(userRecord.getBalance());
    }
}
