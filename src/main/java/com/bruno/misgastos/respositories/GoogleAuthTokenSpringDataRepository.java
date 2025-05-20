package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.GoogleAuthToken;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleAuthTokenSpringDataRepository extends JpaRepository<GoogleAuthToken, Integer> {
  List<GoogleAuthToken> findByRevokedOrderByCreatedAtDesc(boolean revoked);
}
