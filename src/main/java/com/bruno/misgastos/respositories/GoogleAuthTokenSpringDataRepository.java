package com.bruno.misgastos.respositories;

import com.bruno.misgastos.entities.GoogleAuthToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleAuthTokenSpringDataRepository extends JpaRepository<GoogleAuthToken, Integer> {}
