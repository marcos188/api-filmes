package com.api.filmes.repository;

import com.api.filmes.models.Filme;

import org.springframework.data.jpa.repository.JpaRepository;

    public interface FilmeRepository extends JpaRepository<Filme, Long>{

        Filme findById(long id);

    }
