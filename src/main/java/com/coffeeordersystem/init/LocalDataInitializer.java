package com.coffeeordersystem.init;

import com.coffeeordersystem.menu.domain.Menu;
import com.coffeeordersystem.menu.repository.MenuRepository;
import com.coffeeordersystem.point.domain.UserPoint;
import com.coffeeordersystem.point.repository.UserPointRepository;
import com.coffeeordersystem.user.domain.User;
import com.coffeeordersystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Profile("local")
@RequiredArgsConstructor
public class LocalDataInitializer implements ApplicationRunner {

    private final MenuRepository menuRepository;
    private final UserRepository userRepository;
    private final UserPointRepository userPointRepository;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (menuRepository.count() == 0) {
            menuRepository.saveAll(List.of(
                    Menu.create("아메리카노", 4_500),
                    Menu.create("카페라뗴", 5_000),
                    Menu.create("바닐라라떼", 5_500),
                    Menu.create("콜드브루", 5_000),
                    Menu.create("카푸치노", 5_000)));
        }
        if (userRepository.count() == 0) {
            for (int i = 1; i <= 3; i++) {
                User user = userRepository.save(User.create("user" + i));
                userPointRepository.save(UserPoint.create(user.getId()));
            }
        }
    }
}
