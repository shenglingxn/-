package com.campus.trade.config;

import com.campus.trade.goods.entity.Goods;
import com.campus.trade.goods.repository.GoodsRepository;
import com.campus.trade.user.entity.User;
import com.campus.trade.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final GoodsRepository goodsRepository;
    private final BCryptPasswordEncoder encoder;

    public DataInitializer(UserRepository ur, GoodsRepository gr, BCryptPasswordEncoder e) {
        userRepository = ur; goodsRepository = gr; encoder = e;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByUsername("admin")) {
            userRepository.save(User.builder().username("admin")
                    .passwordHash(encoder.encode("admin123456")).phone("13800000000")
                    .studentId("ADMIN001").realName("管理员").department("信息中心")
                    .role("admin").authStatus(1).creditScore(100).build());
            log.info("Admin created: admin / admin123456");
        }
        if (!userRepository.existsByUsername("alice")) {
            var u = userRepository.save(User.builder().username("alice")
                    .passwordHash(encoder.encode("abc123456")).phone("13812345678")
                    .studentId("2024001").realName("爱丽丝").department("计算机学院")
                    .role("user").authStatus(1).creditScore(95).build());
            goodsRepository.save(Goods.builder().userId(u.getId()).title("高等数学第七版 九成新")
                    .description("考研用书，几乎全新，只有前几页有笔记").category("教材教辅")
                    .price(new BigDecimal("25.00")).contactPhone("13812345678").status("onsale").build());
            goodsRepository.save(Goods.builder().userId(u.getId()).title("惠普计算器 HP-39GS")
                    .description("工科必备，功能完好，带原装保护壳").category("电子数码")
                    .price(new BigDecimal("60.00")).contactPhone("13812345678").status("onsale").build());
            log.info("Test user created: alice / abc123456");
        }
        if (!userRepository.existsByUsername("bob")) {
            var u = userRepository.save(User.builder().username("bob")
                    .passwordHash(encoder.encode("abc123456")).phone("13987654321")
                    .studentId("2024112").realName("鲍勃").department("经济管理学院")
                    .role("user").authStatus(0).creditScore(100).build());
            goodsRepository.save(Goods.builder().userId(u.getId()).title("全新雅思真题集 剑18")
                    .description("全新未做，随书附赠听力光盘").category("教材教辅")
                    .price(new BigDecimal("80.00")).contactPhone("13987654321").status("onsale").build());
            log.info("Test user created: bob / abc123456");
        }
    }
}
