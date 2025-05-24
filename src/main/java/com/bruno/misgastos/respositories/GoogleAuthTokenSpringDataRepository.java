package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.GoogleAuthToken;
import java.time.OffsetDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GoogleAuthTokenSpringDataRepository
    extends JpaRepository<GoogleAuthToken, Integer> {

  @Query(
      value =
          """
    SELECT *
    FROM google_auth_token t
    WHERE DATE_ADD(t.created_at, INTERVAL t.expires_in SECOND) > :now
    ORDER BY t.created_at DESC
    LIMIT 1
    """,
      nativeQuery = true)
  Optional<GoogleAuthToken> getLastActiveToken(@Param("now") OffsetDateTime now);

  default Optional<GoogleAuthToken> getLastActiveToken() {
    return getLastActiveToken(OffsetDateTime.now());
  }
}
