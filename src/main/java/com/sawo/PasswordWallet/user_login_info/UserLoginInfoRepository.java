package com.sawo.PasswordWallet.user_login_info;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserLoginInfoRepository extends JpaRepository<UserLoginInfo, Long> {

    Optional<List<UserLoginInfo>> findAllByIpAddressAndCountDuringLoginAttemptTrue(String ipAddress);

    @Query(value = "select * from user_service_login_informations u where u.ip_address = :ipAddress and u.successful = :successful " +
            "order by u.login_time desc limit 1", nativeQuery = true)
    Optional<UserLoginInfo> findLastFailedOrSuccessfulUserLogin(String ipAddress, boolean successful);
}
