package cn.cnyaoshun.oauth.dao;

import cn.cnyaoshun.oauth.entity.User;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by fyh on 2020-6-9.
 */
public interface UserRepository extends PagingAndSortingRepository<User,Long>{
    void deleteAllByIdIn(Set<Long> ids);
    User findByPhone(String phone);
    boolean existsByPhone(String phone);
}
