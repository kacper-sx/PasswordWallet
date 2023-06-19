package com.sawo.PasswordWallet.login_info;

import com.sawo.PasswordWallet.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoginInfoRepository extends JpaRepository<LoginInfo, Long> {

    List<LoginInfo> findAllByUser(User user);

}
