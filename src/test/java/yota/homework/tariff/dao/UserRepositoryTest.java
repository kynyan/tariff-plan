package yota.homework.tariff.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

//@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
//@ContextConfiguration(classes = PersistenceConfig.class)
public class UserRepositoryTest {

//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    public void mustSaveUserToDb() {
//        User user = new User(UserDto.random());
//        User added = userRepository.save(user);
//        flushAndClear();
//        User fromDb = userRepository.findById(added.getId()).get();
//        assertEquals(user.getUsername(), fromDb.getUsername());
//        assertEquals(user.getPassword(), fromDb.getPassword());
//    }
//
//    @Test
//    public void mustFindUserByUsername() {
//        User user = new User(UserDto.random());
//        String username = user.getUsername();
//        User added = userRepository.save(user);
//        flushAndClear();
//        User fromDb = userRepository.findByUsername(username);
//        assertEquals(user.getUsername(), fromDb.getUsername());
//        assertEquals(user.getPassword(), fromDb.getPassword());
//    }
//
//    private void flushAndClear() {
//        entityManager.flush();
//        entityManager.clear();
//    }

}
